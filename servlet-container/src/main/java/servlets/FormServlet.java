package servlets;

import javax.servlet.annotation.WebServlet;

import com.github.pohtml.GetServlet;

@WebServlet({"/dbac/ui", "/common/intranet/dep/explotacion/arqudesa/dbac/ui.html"})
public class FormServlet extends GetServlet<Form> {

	private static final long serialVersionUID = 1L;

	public FormServlet() {
		super(serialVersionUID);
	}
	
	@Override
	public Form get() {
		return new Form();
	}

}