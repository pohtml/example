package servlets;

import com.github.pohtml.Get;
import com.github.pohtml.Json;
import com.github.pohtml.annotations.DynamicHtml;

@DynamicHtml(model = "/dbac/ui", view = "/common/intranet/dep/explotacion/arqudesa")
public class Form extends Get {
	@Override
	public void run(Json response) {
		response.value("Hello, ", "World!");
	}
}