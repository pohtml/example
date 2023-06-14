package util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pohtml.annotations.Get;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

abstract class WebAdapter<T, I> extends HttpServlet implements Supplier<T> {

	private static final long serialVersionUID = 1L;
	
	private final Class<I> input;
	private final long lastModified;
	private final Action action;
	private final String html;

	private ServletContext context;
	private String uri;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = getServletContext();
		String path = context.getContextPath(); 
		uri = path + (action.uri.isEmpty()? computeUri() : action.uri);
		config.getInitParameter("com.github.pohtml");
	}
	
	private String computeUri() {
		throw new IllegalStateException("not implemented yet");
	}
	
	static class Action {
		final String uri;
		final String context;
		Action(Get get) {
			this(get.uri(), get.context());
		}
		Action(String uri, String context) {
			this.uri = uri;
			this.context = context;
		}
	}
	protected WebAdapter(long lastModified, Class<T> annotated, Action annotation, Class<I> input) {
		this.lastModified = lastModified;
		this.input = input;
		this.action = annotation;			
		String[] patterns = getClass().getAnnotation(WebServlet.class).value();
		this.html = patterns.length == 1? patterns[0] + ".html" : patterns[1];
	}
	
	protected WebAdapter(long lastModified, Class<T> annotated, Class<Get> annotation, Class<I> input) {
		this(lastModified, annotated, new Action(annotated.getDeclaredAnnotation(annotation)), input);
	}
	
	private static final String MODEL_COOKIE_NAME = "model";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(MODEL_COOKIE_NAME)) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					response.setStatus(500);
					new IllegalStateException().printStackTrace();
					return;
				}
			}	
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		T t = get();
		if (t instanceof Consumer) {
			I consumable;
			if (input == Void.class) {
				consumable = null;
			} else if (input == HttpServletRequest.class) {
				@SuppressWarnings("unchecked")
				I i = (I)request;
				consumable = i;
			} else {
				JsonObject jsonObject = new JsonObject();
				Enumeration<String> parameters = request.getParameterNames();
				while(parameters.hasMoreElements()) {
					String key = parameters.nextElement();
					String[] values = request.getParameterValues(key);
					if (values == null) {
						continue;
					} else if (values.length == 1) {
						jsonObject.addProperty(key, values[0]);
					} else {
						JsonArray array = new JsonArray(values.length);
						for (String value : values) {
							array.add(value);	
						}
						jsonObject.add(key, array);
					}
				}
				consumable = gson.fromJson(jsonObject, input);	
			}
			@SuppressWarnings("unchecked")
			Consumer<I> consumer = ((Consumer<I>)t); 
			consumer.accept(consumable);
		}
		JsonElement representation = gson.toJsonTree(t);
		if (t instanceof Supplier) {
			Object gotten = ((Supplier<?>)t).get();
			representation.getAsJsonObject().add("com.github.pohtml.form", toJsonObject(gson, gotten));
		}
		JsonObject result = toJsonObject(gson, representation);
		String json = gson.toJson(result);
		if (request.getHeader("accept").equals("application/json")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(json);
		} else {
			byte[] encoded = Base64.getEncoder().encode(json.getBytes(UTF_8));
			Cookie cookie = new Cookie(MODEL_COOKIE_NAME, new String(encoded));
			cookie.setPath(uri);
			cookie.setMaxAge(-1);
			response.addCookie(cookie);
			RequestDispatcher dispatcher = context.getRequestDispatcher(uri);
			dispatcher.forward(request, response);
		}
	}
	
	private JsonObject toJsonObject(Gson gson, Object source) {
		return gson.toJsonTree(source).getAsJsonObject();
	}

}