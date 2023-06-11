package servlets;

import javax.servlet.annotation.WebServlet;

import com.github.pohtml.HtmlGet;

@WebServlet({"/dbac/ui", "/intranet/dep/explotacion/arqudesa/dbac/ui.html"})
public class HtmlGetForm extends HtmlGet<Form> {

	private static final long serialVersionUID = 1L;

	public HtmlGetForm() {
		super(serialVersionUID);
	}
	
	@Override
	public Form get() {
		return new Form();
	}

}
