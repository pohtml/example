package redirect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/common/*")
public class StaticFilesServer extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext context = getServletContext();
		String path = System.getProperty("wlp.user.dir");
		int index = path.indexOf("/static-files-server/");
		path = path.substring(0, index + 13);
		path += req.getRequestURI().substring(context.getContextPath().length());
		File file = new File(path);
		String ims = req.getHeader("If-Modified-Since");
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Boolean modified = null;
		if (ims == null) {
			resp.addHeader("Last-Modified", sdf.format(new Date(file.lastModified())));
		} else {
			try {
				modified = sdf.parse(ims).getTime() < file.lastModified() / 1000;
			} catch (ParseException e) {
				throw new IllegalArgumentException(ims);
			}
		}
		if (modified == null || modified) {
			try (InputStream is = new FileInputStream(file); OutputStream os = resp.getOutputStream()) {
				byte[] buffer = new byte[1024];
				int read = is.read(buffer);
				while (read != -1) {
					os.write(buffer, 0, read);
					read = is.read(buffer);
				}
			}	
		} else {
			resp.setStatus(304);
		}
	}

}