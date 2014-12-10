package SonoranCellular.servlets;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import SonoranCellular.servlets.*;
import SonoranCellular.utils.*;

public class AddMaster extends HttpServlet
{
    public AddMaster()
    {
        super();
    }
    
    private OracleConnect oc = new OracleConnect();
    private Statement s;
    private Connection c;
    private String owner;
    
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
    
    public void drawHeader(HttpServletRequest req, PrintWriter out) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Set Master Account</title>");
        out.println("</head>");
        
        out.println("<body>");
        out.println("<p>");
        out.println("<center>");
        out.println("<font face=\"Arial, Helvetica, sans-serif\" >");
        out.println("<font color=\"#000066\">");
        out.println("<center>\n<font size=7><strong>SonoranCellular</strong></font></br>");
        out.println("</center>\n<font size=4><hr color=\"#000066\">");
        out.println("Set Master Account </b><br></font>");
        out.println("</font>");
        
        out.println("<hr>");
    }
    
    public void drawAlreadyDependent(HttpServletRequest req, PrintWriter out){
        drawHeader(req,out);
        out.println("<font size=5 face=\"Arial,Helvetica\">");
        out.println("<b>Error: this account is already dependent on account "+ owner +".</b></br>");
        drawFooter(req,out);
    }
    
    public void drawFooter(HttpServletRequest req, PrintWriter out)
    {
        //out.println("<hr>");
        out.println("<tr>");
        out.println("<td>");
        out.println("<form name=\"MainMenu\" action=LoginServlet>");
        out.println("<input type=submit name=\"MainMenu\" value=\"Return to Main Menu\">");
        out.println("</form>");
        out.println("</td>");
        out.println("</tr>");
        
        out.println("<tr>");
        out.println("<td>");
    	out.println("<div class=\"menuButton\">");
        out.println("<form class=\"toBottom\"name=\"LogoutServlet\" action=LogoutServlet method=get>");
        out.println("<input type=submit name=\"LogoutServlet\" value=\"Log out\">");
        out.println("</form>");
        out.println("</div>");
        
        out.println("</form>");
        out.println("</p>");
        out.println("</td>");
        out.println("</tr>");
        
        out.println("</table>");
        out.println("<br><br><br>");
        
        out.println("</center>");
        out.println("</p>");
        out.println("</body>");
        out.println("</html>");
    }
    
    public void drawAddMasterPage(HttpServletRequest req, PrintWriter out){
        out.println("<form name=\"AddMaster\" action=AddMaster method=get>");
        out.println("<font size=3 face=\"Arial, Helvetica, sans-serif\" color=\"#000066\">");
        
        out.println("<p>");
        out.println("<b>Owner Account Number: </b>");
        out.println("<input type=text name=\"owneraccountnumber\">");
        out.println("<br>");
        out.println("</p>");
        
        out.println("<p>");
        out.println("<b>Owner Name: </b>");
        out.println("<input type=text name=\"ownername\">");
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
        out.println("<form name=\"Cancel\" action=AddMaster method=get>");
        out.println("<input type=submit name=\"Cancel\" value=\"Cancel\">&nbsp&nbsp");
        out.println("</form>");
        out.println("</td>");
        out.println("</tr>");
    }
    
    public void drawMainForm(HttpServletRequest req, PrintWriter out){
        drawHeader(req,out);
        drawAddMasterPage(req,out);
        drawFooter(req,out);
    }
    
    private boolean checkNotDependent(int accountNumber){
        ResultSet rs;
        String query;
        try{
            query = "SELECT * FROM Owns WHERE DependentAccountNumber = " + accountNumber;
            rs = s.executeQuery(query);
            if(rs.next()){owner = rs.getString(1); return false;}
            else{return true;}
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public void drawParseError(HttpServletRequest req, PrintWriter out, String err){
        drawHeader(req,out);
        out.println("<font size=5 face=\"Arial,Helvetica\">");
        out.println("<b> " + err + "</b></br>");
        
        drawAddMasterPage(req,out);
        drawFooter(req,out);
    }
    
    public void drawUpdateMessage(HttpServletRequest req, PrintWriter out, int ownerAccountNumber, int accountNumber)
    {
        drawHeader(req,out);
        out.println("<p><b>Owner Account Number:</b>  " + ownerAccountNumber + "</p>");
        out.println("<p><b>Account Number:</b>  " + accountNumber + "</p>");
        
        out.println("<br>");
        drawFooter(req,out);
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        System.out.println("HW8: Doing get for AddPlan.");
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        int ownerAccountNumber = 0;
        int accountNumber = 0;
        String ownerName = "";
        
        
        try{
            System.out.println("HW8: account number: " + (Integer)req.getSession().getAttribute("AccountNumber"));
            accountNumber = (Integer)req.getSession().getAttribute("AccountNumber");
        }catch (Exception e) {
            System.out.println("HW8: account number pasing error.");
            e.printStackTrace();
        }
        
        if(!checkNotDependent(accountNumber)){
            drawAlreadyDependent(req, out);
            return;
        }else{
            if(req.getParameter("Submit") == null){drawMainForm(req, out);}
            else{
                try{
                    String[] params = req.getParameterValues("ownername");
                    String err = InputSanitizer.checkStringAlphaAndReturnErrorString("Owner Name",params[0], 1, 40);
                    ownerName = params[0];
                    if(err != ""){
                        drawParseError(req, out, err);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                try{
                    String[] params = req.getParameterValues("owneraccountnumber");
                    String err = InputSanitizer.checkStringNumericAndReturnErrorString("Owner Account Number", params[0], 1, 8);
                    if(err != ""){
                        drawParseError(req, out, err);
                        return;
                    }
                    ownerAccountNumber = Integer.parseInt(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                try{
                    //System.out.println("HW8: TEST");
                    ResultSet rs;
                    String query;
                
                    //Check owner exists
                    query = "SELECT * FROM Account WHERE " +
                    "Name = \'" + ownerName + "\' AND AccountNumber = " + ownerAccountNumber;
                    System.out.println("HW8: " + query);
                    rs = s.executeQuery(query);
                    
                    if(rs.next()){
                        
                        query = "SELECT * FROM Owns WHERE" +
                            " MasterAccountNumber = " + ownerAccountNumber +
                            " AND DependentAccountNumber = " + accountNumber;
                        System.out.println("HW8: " + query);
                        rs = s.executeQuery(query);
                            
                        if(!rs.next()){
                            query = "INSERT INTO Owns VALUES (" + ownerAccountNumber + ", " + accountNumber + ")";
                            System.out.println("HW8: " + query);
                            s.executeUpdate(query);
                            drawUpdateMessage(req, out, ownerAccountNumber, accountNumber);
                        }else{
                            drawParseError(req, out, "Master account already owns this account.");
                        }
                    }else{
                        drawParseError(req, out, "Master account does not exist.");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}