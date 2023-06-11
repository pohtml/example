package servlets;

import com.github.pohtml.Get2;
import com.github.pohtml.Json;
import com.github.pohtml.annotations.Resource;

@Resource(uri = "/dbac/ui", context = "/intranet/dep/explotacion/arqudesa")
public class Form extends Get2 {
	
	@Override
	public void run(Json response) {
		response.value("greeting", "Hello, World!");
	}
	
}