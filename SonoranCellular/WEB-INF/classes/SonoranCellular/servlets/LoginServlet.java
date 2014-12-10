package SonoranCellular.servlets;
import java.util.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import SonoranCellular.servlets.*;
import SonoranCellular.utils.*;

import java.sql.*;

public class LoginServlet extends HttpServlet
{
    public LoginServlet()
    {
        super();
    }

    private OracleConnect oc = new OracleConnect();
    private Statement s;
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

			s = c.createStatement();
			
			if (s == null) throw new Exception("Statement creation fialed.");
		
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("HW8: Connection to database successful");
    }
    
    public void destroy() {
    	try {
		c.commit();
		c.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void drawHeader(HttpServletRequest req, PrintWriter out)
    {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sonoran Cellular -- Main Menu</title>");
    	out.println("<link href=\"layout.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\">");
        out.println("</head>");

        out.println("<body>");
        out.println("<div id=\"wrapper\">");
        out.println("<h1>SonoranCellular</h1>");
		out.println("<hr>");
    }

    public void drawFooter(HttpServletRequest req, PrintWriter out)
    {
    	out.println("</div>"); // Close id="wrapper"
        out.println("</body>");
        out.println("</html>");
    }


    private void drawActiveOptions(HttpServletRequest req, PrintWriter out, int accountNumber)
    {
        out.println("<h2>Main Menu</h2>");
        out.println("<p>Welcome, user " + accountNumber + ".</p>");
        
    	out.println("<div class=\"menuButton\">");
        out.println("<form name=\"AddPlan\" action=AddPlan method=get>");
        out.println("<input type=submit name=\"AddPlan\" value=\"Add a Plan\">");
        out.println("</form>");
        out.println("</div>");

    	out.println("<div class=\"menuButton\">");
        out.println("<form name=\"FindBill\" action=FindBill method=get>");
        out.println("<input type=submit name=\"FindBill\" value=\"Print Bill for a billing period\">");
        out.println("</form>");
        out.println("</div>");

    	out.println("<div class=\"menuButton\">");
        out.println("<form name=\"PlanShare\" action=./JSP/SharedAssignment.jsp>");
        out.println("<input type=submit name=\"SharedAssignment\" value=\"Who is assigned to the same plan?\">");
        out.println("</form>");
        out.println("</div>");

    	out.println("<div class=\"menuButton\">");
        out.println("<form name=\"ViewDependents\" action=ViewDependents method=get>");
        out.println("<input type=submit name=\"LogoutServlet\" value=\"View dependent accounts\">");
        out.println("</form>");
        out.println("</div>");

    	out.println("<div class=\"menuButton\">");
        out.println("<form name=\"AddMaster\" action=AddMaster method=get>");
        out.println("<input type=submit name=\"LogoutServlet\" value=\"Make this a dependent account\">");
        out.println("</form>");
        out.println("</div>");

    	out.println("<div class=\"menuButton\">");
        out.println("<form class=\"toBottom\"name=\"LogoutServlet\" action=LogoutServlet method=get>");
        out.println("<input type=submit name=\"LogoutServlet\" value=\"Log out\">");
        out.println("</form>");
        out.println("</div>");
    }

    private void drawFailOptionsNoAccountNumber(HttpServletRequest req, PrintWriter out)
    {
        out.println("<h2>Error: account number does not exist.</h2>");

        out.println("<hr>");
        out.println("<form class=\"toBottom\" name=\"logout\" action=index.html>");
        out.println("<input type=submit name=\"home\" value=\"Return to Main Menu\">");
        out.println("</form>");
    }


    private void drawFailOptionsIncorrectAccountName(HttpServletRequest req, PrintWriter out)
    {
        out.println("<h2>Error: enter the correct account name.</h2>");

        out.println("<hr");
        out.println("<form class=\"toBottom\"name=\"logout\" action=index.html>");
        out.println("<input type=submit name=\"home\" value=\"Return to Main Menu\">");
        out.println("</form>");
    }

    public void drawLoginSuccess(HttpServletRequest req, PrintWriter out, int accountNumber)
    {
        drawHeader(req,out);
        drawActiveOptions(req,out,accountNumber);
        drawFooter(req,out);
    }

    public void drawLoginFailOnNumber(HttpServletRequest req, PrintWriter out)
    {
        drawHeader(req,out);
        drawFailOptionsNoAccountNumber(req,out);
        drawFooter(req,out);
    }
    
    public void drawLoginFailOnName(HttpServletRequest req, PrintWriter out) {
    	drawHeader(req,out);
    	drawFailOptionsIncorrectAccountName(req,out);
    	drawFooter(req,out);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
    	System.out.println("HW8: Doing get for LoginServlet.");
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
     // Is the user already logged in?
    	if(req.getSession().getAttribute("Username") != null) {
    		drawLoginSuccess(req, out, (int)req.getSession().getAttribute("AccountNumber"));
    		return;
    	}
    	
        int accountNumber = 0;
        String ownerName = "";

        try{
            String[] params = req.getParameterValues("account number");
        	accountNumber = Integer.parseInt(params[0]);
        	
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        try{
            String[] params = req.getParameterValues("account");
            ownerName = params[0];
        	
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        try{
        	ResultSet rs = s.executeQuery("SELECT * FROM Account WHERE " +
        			"AccountNumber = " + accountNumber);
        	if(!rs.next()) {
        		drawLoginFailOnNumber(req, out);
        		return;
        	}
        	
        	rs = s.executeQuery("SELECT * FROM Account WHERE " +
        			"AccountNumber = " + accountNumber + " AND " +
        			"Name = \'" + ownerName + "\'");
        	
        	if(rs.next()) {
        		req.getSession().setAttribute("Username", ownerName);
        		req.getSession().setAttribute("AccountNumber", accountNumber);
        		drawLoginSuccess(req, out, (int)req.getSession().getAttribute("AccountNumber"));
        	}
        	else
        		drawLoginFailOnName(req,out);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
}


