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
        out.println("<title>SonoranCellular logged in</title>");
        out.println("</head>");

        out.println("<body>");


        out.println("<p>");
        out.println("<center>");
        out.println("<font size=7 face=\"Arial, Helvetica, sans-serif\" color=\"#000066\">");
        out.println("<center>\n<strong>SonoranCellular</strong></br>");
        out.println("</center>\n<hr color=\"#000066\">");
        out.println("<br><br>");

    }

    public void drawFooter(HttpServletRequest req, PrintWriter out)
    {
        out.println("</center>");
        out.println("</p>");
        out.println("</body>");
        out.println("</html>");
    }


    private void drawActiveOptions(HttpServletRequest req, PrintWriter out)
    {
        System.out.println("HW8: drawActiveOptions");

        out.println("<br>");

        out.println("<form name=\"AddPlan\" action=AddPlan method=get>");
        out.println("<input type=submit name=\"AddPlan\" value=\"Add a Plan\">");
        out.println("</form>");
        
        out.println("<br>");

        out.println("<form name=\"FindBill\" action=FindBill method=get>");
        out.println("<input type=submit name=\"FindBill\" value=\"Print Bill for a billing period\">");
        out.println("</form>");

        out.println("<br>");

        out.println("<form name=\"PlanShare\" action=./JSP/SharedAssignment.jsp>");
        out.println("<input type=submit name=\"SharedAssignment\" value=\"Who is assigned to the same plan?\">");
        out.println("</form>");

        out.println("<br>");
        
        out.println("<form name=\"PlanShare\" action=./JSP/SharedAssignment.jsp>");
        out.println("<input type=submit name=\"SharedAssignment\" value=\"Who is assigned to the same plan?\">");
        out.println("</form>");
        
        out.println("<br>");
        
        out.println("<form name=\"AddMaster\" action=AddMaster method=get>");
        out.println("<input type=submit name=\"AddMaster\" value=\"Set master account for this account.\">");
        out.println("</form>");
        
        out.println("<br>");
        
        out.println("<form name=\"ViewDependents\" action=ViewDependents method=get>");
        out.println("<input type=submit name=\"ViewDependents\" value=\"Show list of accounts dependent on this account.\">");
        out.println("</form>");
    }

    private void drawFailOptionsNoAccountNumber(HttpServletRequest req, PrintWriter out)
    {
        out.println("<font size=5 face=\"Arial,Helvetica\">");
        out.println("<b>Error: account number does not exist.</b></br>");

        out.println("<hr");
        out.println("<br><br>");

        out.println("<form name=\"logout\" action=index.html>");
        out.println("<input type=submit name=\"home\" value=\"Return to Main Menu\">");
        out.println("</form>");

        out.println("<br>");
    }


    private void drawFailOptionsIncorrectAccountName(HttpServletRequest req, PrintWriter out)
    {
        out.println("<font size=5 face=\"Arial,Helvetica\">");
        out.println("<b>Error: enter the correct account name.</b></br>");

        out.println("<hr");
        out.println("<br><br>");

        out.println("<form name=\"logout\" action=index.html>");
        out.println("<input type=submit name=\"home\" value=\"Return to Main Menu\">");
        out.println("</form>");

        out.println("<br>");
    }

    private void drawFailOptions(HttpServletRequest req, PrintWriter out)
    {
        out.println("<font size=5 face=\"Arial,Helvetica\">");
        out.println("<b>Error: account number does not exist.</b></br>");

        out.println("<hr");
        out.println("<br><br>");

        out.println("<form name=\"logout\" action=index.html>");
        out.println("<input type=submit name=\"home\" value=\"Return to Main Menu\">");
        out.println("</form>");

        out.println("<br>");
    }

    public void drawLoginSuccess(HttpServletRequest req, PrintWriter out)
    {
        drawHeader(req,out);
        drawActiveOptions(req,out);
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
        	// Is the user already logged in?
        	if(req.getSession().getAttribute("Username") != null) {
        		drawLoginSuccess(req, out);
        		return;
        	}
        	
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
        		drawLoginSuccess(req, out);
        	}
        	else
        		drawLoginFailOnName(req,out);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }

        //if fail, call the following function
        //drawLoginFail(req,out);
    }
}


