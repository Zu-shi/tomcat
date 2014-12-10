package SonoranCellular.servlets;
import java.util.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import SonoranCellular.servlets.*;
import SonoranCellular.utils.InputSanitizer;
import SonoranCellular.utils.OracleConnect;


public class AddAccountInformation extends HttpServlet
{
   public AddAccountInformation()
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

   public void updateMessage(HttpServletRequest req, PrintWriter out) {
	   String accountname = "";
	   int accountnum = 0;
	   
	   try{
		   accountname = req.getParameterValues("accountname")[0];
		   if(accountname == null || accountname.length() == 0)
			   throw new Exception("No account name supplied.");
		   
		   if(!InputSanitizer.checkStringAlpha(accountname, 1, 40))
			   throw new Exception("Insane name entered.");
	   }
	   catch (Exception e) {
		   drawUpdateFailOnName(out);
		   return;
	   }
	   
	   try{
		   String input = req.getParameterValues("accountnum")[0];
		   
		   if(!InputSanitizer.checkStringNumeric(input, 4, 8))
			   throw new Exception("Insane account number entered.");
		   
		   accountnum = Integer.parseInt(input);
	   }
	   catch (Exception e) {
		   drawUpdateFailOnNumber(out);
		   return;
	   }
	   
	   try{
		   ResultSet rs = s.executeQuery("SELECT * FROM Account WHERE "
		   		+ "AccountNumber = " + accountnum);
		   if(rs.next())
			   drawUpdateFailOnNumberExists(out, accountnum);
		   else
			   drawUpdateMessage(accountname, accountnum, out);
		   
		   return;
	   } catch (Exception e) {
		   e.printStackTrace();
		   drawUpdateFailOnSQLError(out);
	   }

   }
   
   public void drawUpdateFailOnSQLError(PrintWriter out) {
	   out.println("<font size=5 face=\"Arial,Helvetica\">");
       out.println("<b>Internal Error: Please try again.</b></br>");

       out.println("<hr");
       out.println("<br><br>");

       out.println("<form name=\"logout\" action=index.html>");
       out.println("<input type=submit name=\"home\" value=\"Return to Main Menu\">");
       out.println("</form>");

       out.println("<br>");
   }
   
   public void drawUpdateFailOnNumberExists(PrintWriter out, int account) {
	   out.println("<font size=5 face=\"Arial,Helvetica\">");
       out.println("<b>Error: Account number \'" + account + "\' already exists.</b></br>");

       out.println("<hr");
       out.println("<br><br>");

       out.println("<form name=\"logout\" action=index.html>");
       out.println("<input type=submit name=\"home\" value=\"Return to Main Menu\">");
       out.println("</form>");

       out.println("<br>");
   }
   
   public void drawUpdateFailOnNumber(PrintWriter out) {
	   out.println("<font size=5 face=\"Arial,Helvetica\">");
       out.println("<b>Error: You must enter a new account number to register.</b></br>");

       out.println("<hr");
       out.println("<br><br>");

       out.println("<form name=\"logout\" action=index.html>");
       out.println("<input type=submit name=\"home\" value=\"Return to Main Menu\">");
       out.println("</form>");

       out.println("<br>");
   }
   
   public void drawUpdateFailOnName(PrintWriter out) {
	   out.println("<font size=5 face=\"Arial,Helvetica\">");
       out.println("<b>Error: You must enter a valid name to register.</b></br>");

       out.println("<hr");
       out.println("<br><br>");

       out.println("<form name=\"logout\" action=index.html>");
       out.println("<input type=submit name=\"home\" value=\"Return to Main Menu\">");
       out.println("</form>");

       out.println("<br>");
   }

   public void drawUpdateMessage(String accountname, int accountnum, PrintWriter out)
   {
      out.println("<p><b>Account Name:</b>  " + accountname + "</p>");
      out.println("<p><b>Account Number:</b>  " + accountnum + "</p>");

      out.println("<br>");

      out.println("<form name=\"MainMenu\" action=LoginServlet>");
      out.println("<input type=submit name=\"MainMenu\" value=\"MainMenu\">");
      out.println("</form>");

      out.println("<br>");

      out.println("<form name=\"logout\" action=LogoutServlet method=get>");
      out.println("<input type=submit name=\"logoutSonoranCellular\" value=\"Logout\">");
      out.println("</form>");
   }


   public void drawHeader(HttpServletRequest req, PrintWriter out) {
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Account Addition</title>");
      out.println("</head>");

      out.println("<body>");
      out.println("<p>");
      out.println("<center>");
      out.println("<font size=7 face=\"Arial, Helvetica, sans-serif\" color=\"#000066\">");
      out.println("<center>\n<strong>SonoranCellular</strong></br></font>");
      out.println("</center>\n<hr color=\"#000066\">");
      out.println("Add new account </b><br></font>");

      out.println("<hr>");
   }


   public void drawFooter(HttpServletRequest req, PrintWriter out)
   {
      out.println("</center>");
      out.println("</p>");
      out.println("</body>");
      out.println("</html>");
   }


   public void drawAddAccountInformationMenu(HttpServletRequest req, PrintWriter out)
   {
      out.println("<form name=\"AddAccountInformation\" action=AddAccountInformation method=get>");
      out.println("<font size=3 face=\"Arial, Helvetica, sans-serif\" color=\"#000066\">");

      out.println("<p>");
      out.println("<b>Account Name: </b>");
      out.println("<input type=text name=\"accountname\">");
      out.println("<br>");
      out.println("</p>");

      out.println("<p>");
      out.println("<b>Account Number: </b>");
      out.println("<input type=text name=\"accountnum\">");
      out.println("<br>");
      out.println("</p>");

      out.println("<table>");
      out.println("<tr>");
      out.println("<td>");
      out.println("<input type=submit name=\"Submit\" value=\"Insert\">&nbsp&nbsp");
      out.println("</td>");
      out.println("</tr>");

      out.println("</form>");

      out.println("<tr>");
      out.println("<td>");
      out.println("<form name=\"Cancel\" action=index.html method=get>");
      out.println("<input type=submit name=\"Cancel\" value=\"Cancel\">&nbsp&nbsp");
      out.println("</form>");
      out.println("</td>");
      out.println("</tr>");

      out.println("</table>");
      out.println("<br><br><br>");
   }


   public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
      res.setContentType("text/html");
      PrintWriter out = res.getWriter();

      drawHeader(req,out);

      if(req.getParameter("Submit") == null)
      {
         drawAddAccountInformationMenu(req,out);
      }
      else
      {
         updateMessage(req,out);
      }

      drawFooter(req,out);
   }
}
