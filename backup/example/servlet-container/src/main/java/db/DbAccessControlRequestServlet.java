//package db;
//
//import javax.servlet.annotation.WebServlet;
//
//import com.github.pohtml.annotations.Get;
//import com.github.pohtml.apt.ServletAdapter;
//
//@WebServlet("/DbAccessControlRequest")
//public abstract class DbAccessControlRequestServlet extends ServletAdapter<DbAccessControlRequest, Void> {
//
//	private static final long serialVersionUID = 1L;
//	
//	public DbAccessControlRequestServlet() {
//		super(serialVersionUID, DbAccessControlRequest.class, Get.class, Void.class);
//	}
//
//	@Override
//	public DbAccessControlRequest get() {
//		return new DbAccessControlRequest();
//	}
//
//}