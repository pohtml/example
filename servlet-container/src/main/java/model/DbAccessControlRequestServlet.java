package model;

import javax.servlet.annotation.WebServlet;

import com.github.pohtml.annotations.Get;

import util.WebAdapter;

@WebServlet({"/DbAccessControlRequest"})
public class DbAccessControlRequestServlet extends WebAdapter<DbAccessControlRequest, Void> {

	private static final long serialVersionUID = 1L;

	public DbAccessControlRequestServlet() {
		super(serialVersionUID, DbAccessControlRequest.class, Get.class, Void.class);
	}

	@Override
	public DbAccessControlRequest get() {
		return new DbAccessControlRequest();
	}

}
