package com.github.pohtml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/*")
public class StaticFilesServer extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final String base = System.getProperty("com.github.pohtml.fs.base");
	private final String alternative = System.getProperty("com.github.pohtml.fs.alternative");
	
	private File directory;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (base != null) {
			directory = new File(System.getProperty("wlp.install.dir")).getParentFile();
			if (directory.getName().equals("liberty")) {
				directory = directory.getParentFile();
			}
		}
	}
	
	private InputStream getInputStream(HttpURLConnection connection) {
		try {
			return connection.getInputStream();
		} catch (IOException e) {
			return connection.getErrorStream();
		}
	}
	
	private void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String uri = req.getRequestURI();
		int index = uri.lastIndexOf('.');
		String extension = null;
		if (index != -1) {
			extension = uri.substring(index);
		}
		if (extension != null && !extension.equals("html")) {
			uri = uri.substring(0, index + 1) + "html";
		}
		index = getServletContext().getContextPath().length();
		URL url = new URL(alternative + req.getRequestURI());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			Enumeration<String> headerValues = req.getHeaders(headerName);
			while (headerValues.hasMoreElements()) {
				String headerValue = headerValues.nextElement();
				connection.setRequestProperty(headerName, headerValue);
			}
		}
		int responseCode = connection.getResponseCode();
		resp.setStatus(responseCode);
		for (Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
			String key = entry.getKey();
			if (key != null) {
				for (String value : entry.getValue()) {
					resp.addHeader(key, value);
				}
			}
		}
		try (InputStream reader = getInputStream(connection); OutputStream writer = resp.getOutputStream()) {
			int read = reader.read();
			if (read == -1) {
				return;
			}
			byte[] buffer = new byte[1024];
			buffer[0] = (byte) read;
			read = reader.read(buffer, 1, buffer.length - 1) + 1;
			while (read != -1) {
				writer.write(buffer, 0, read);
				read = reader.read(buffer);
			}
		}	
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getRequestURI();
		File file;
		if (base == null && directory != null) {
			File candidate = new File(directory, path);
			while (!candidate.exists()) {
				directory = directory.getParentFile();
				if (directory == null) {
					throw new IllegalStateException();
				} else {
					candidate = new File(directory, path);
				}
			}
			file = candidate;
		} else if (base == null && directory == null) {
			throw new IllegalStateException();
		} else {
			file = new File(new File(base), path);
			if (!file.exists() && alternative != null) {
				redirect(request, response);
			}
		}
		String ims = request.getHeader("If-Modified-Since");
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Boolean modified = null;
		if (ims == null) {
			response.addHeader("Last-Modified", sdf.format(new Date(file.lastModified())));
		} else {
			try {
				modified = sdf.parse(ims).getTime() < file.lastModified() / 1000;
			} catch (ParseException e) {
				throw new IllegalArgumentException(ims);
			}
		}
		if (modified == null || modified) {
			try (InputStream is = new FileInputStream(file); OutputStream os = response.getOutputStream()) {
				byte[] buffer = new byte[1024];
				int read = is.read(buffer);
				while (read != -1) {
					os.write(buffer, 0, read);
					read = is.read(buffer);
				}
			}	
		} else {
			response.setStatus(304);
		}
	}

}