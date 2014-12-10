package SonoranCellular.servlets;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.*;
import javax.servlet.http.*;

import SonoranCellular.servlets.*;
import SonoranCellular.utils.*;

public class LogoutServlet extends HttpServlet {

	public LogoutServlet()
    {
        super();
    }
	
	private OracleConnect oc = new OracleConnect();
    private Connection c;
    
    /**
     * Connect to the database and setup instance variables.
     */
    public void init() {
    	String user_name = OracleConnect.user_name;
		String password = OracleConnect.password;
		String connect_string = OracleConnect.connect_string;
		
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			c = DriverManager.getConnection(connect_string,user_name,password);
		
			if (c == null) throw new Exception("Connection to database failed.");
		
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("HW8: Connection to database successful -- LogoutServlet");
    }
    
    public void destroy() {
    	try {
		c.commit();
		c.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
	 public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		 
		 System.out.println("HW8: Doing LogoutServlet doGet.");
		 req.getSession().setAttribute("Username", null);
		 req.getSession().setAttribute("AccountNumber", null);
		 
		 PrintWriter out = res.getWriter();
		 
		 // Header
		 out.println("<html>");
		 out.println("<head>");
	     out.println("<title>Sonoran Cellular -- Logged Out</title>");
	     out.println("<link href=\"layout.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\">");
		 out.println("</head>");
		 out.println("<body>");
		 out.println("<div id=\"wrapper\">");
		 out.println("<h1>Sonoran Cellular</h1>");
		 out.println("<hr>");
		 
		 // Body
		 out.println("<h2>Logout Successful!</h2>");
		 out.println("<div id=\"space\"></div>");
		 out.println("<hr>");
		 out.println("<form class=\"menuButton\" name=\"logout\" action=index.html>");
		 out.println("<input type=submit name=\"home\" value=\"Return to Main Menu\">");
		 out.println("</form>");
		 
		 // Footer
		 out.println("</div>");
		 out.println("</body>");
		 out.println("</html>");
	}
}
