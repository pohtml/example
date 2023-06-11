package wlpl;

import com.github.pohtml.Get;
import com.github.pohtml.Json;
import com.github.pohtml.annotations.Resource;

@Resource(servlet = "/dbac/ui", context = "/intranet/dep/explotacion/arqudesa")
public class Form extends Get {

	@Override
	public void init(Json response) throws Exception {
		response.value("greeting", "Hello, World!");
	}

}