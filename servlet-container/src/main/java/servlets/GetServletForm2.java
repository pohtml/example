package servlets;

import javax.servlet.annotation.WebServlet;

import com.github.pohtml.GetServlet;

@WebServlet({"/dbac/ui2", "/common/intranet/dep/explotacion/arqudesa/dbac/ui2.html"})
public class GetServletForm2 extends GetServlet<Form> {

	private static final long serialVersionUID = 1L;

	public GetServletForm2() {
		super(serialVersionUID);
	}
	
	@Override
	public Form get() {
		return new Form();
	}

}