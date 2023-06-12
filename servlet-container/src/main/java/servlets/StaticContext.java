package servlets;

import javax.servlet.annotation.WebServlet;

import com.github.pohtml.Static;

@WebServlet(StaticContext.VERSION_TIME_SECONDS)
public class StaticContext extends Static {

	public static final String VERSION_TIME_SECONDS = "1686511385";
	
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis() / 1000); // Set this output as the value of VERSION_TIME_SECONDS just before committing 
	}
	
}
