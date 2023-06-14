package servlets;

import com.github.pohtml.Get;
import com.github.pohtml.Json;
import com.github.pohtml.annotations.DynamicHtml;

//@DynamicHtml(model = "/dbac2/ui", view = "/common/intranet/dep/explotacion/arqudesa")
public class Form2 extends Get {
	@Override
	public void run(Json response) {
		response.value("Hello, ", "World!");
	}
}