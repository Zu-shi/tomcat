package SonoranCellular.servlets;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import SonoranCellular.servlets.*;
import SonoranCellular.utils.*;

public class ViewDependents extends HttpServlet
{
    public ViewDependents()
    {
        super();
    }
    
    private ArrayList<Dependent> dependents;
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
    
    public void drawHeader(HttpServletRequest req, PrintWriter out) {
        out.println("<html>");
        out.println("<head>");
    	out.println("<link href=\"layout.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\">");
        out.println("<title>View Dependents</title>");
        out.println("</head>");
        
        out.println("<body>");
        out.println("<p>");
        out.println("<center>");
        out.println("<font face=\"Arial, Helvetica, sans-serif\" >");
        out.println("<font color=\"#000066\">");
        out.println("<center>\n<font size=7><strong>SonoranCellular</strong></font></br>");
        out.println("</center>\n<font size=4><hr color=\"#000066\">");
        out.println("Add new plan </b><br></font>");
        out.println("</font>");
        
        out.println("<hr>");
    }
    
    
    public void drawFooter(HttpServletRequest req, PrintWriter out)
    {
        //out.println("<hr>");
        out.println("<tr>");
        out.println("<td>");
    	out.println("<div class=\"menuButton\">");
        out.println("<form name=\"MainMenu\" action=LoginServlet>");
        out.println("<input type=submit name=\"MainMenu\" value=\"Return to Main Menu\">");
        out.println("</form>");
        out.println("</div>");
        out.println("</td>");
        out.println("</tr>");
        
        out.println("<tr>");
        out.println("<td>");
    	out.println("<div class=\"menuButton\">");
        out.println("<form name=\"logout\" action=index.html>");
        out.println("<input type=submit name=\"logoutSonoranCellular\" value=\"Logout\">");
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
    
    public void drawDependentsList(HttpServletRequest req, PrintWriter out){
        drawHeader(req,out);
        if(dependents.isEmpty()){
            out.println("<font size=5 face=\"Arial,Helvetica\">");
            out.println("<h2>There are no accounts dependent on this account.</h2></br>");
        }else{
            out.println("<table border=\"1\">");
            out.println("<tr>");
            out.println("<td><b>Account Number</b></td>");
            out.println("<td><b>Name</b></td>");
            out.println("<tr>");
            for(Dependent d: dependents){
                out.println("<td>" + d.accountNumber + "</td>");
                out.println("<td>" + d.userName + "</td>");
                out.println("<tr>");
            }
        }
        out.println("</table>");
        drawFooter(req,out);
    }
    
    private void initializeDependentsList(int accountNumber){
        ResultSet rs;
        String query;
        dependents = new ArrayList<Dependent>();
        try{
            query = "SELECT Owns.DependentAccountNumber, Account.Name FROM Owns, Account WHERE Owns.MasterAccountNumber = " + accountNumber + " AND Account.AccountNumber = Owns.DependentAccountNumber";
            rs = s.executeQuery(query);
            while(rs.next()){
                dependents.add(new Dependent(rs.getString(1), rs.getString(2)));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        System.out.println("HW8: Doing get for AddPlan.");
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        String planName = "";
        int imei = 0;
        String mobilenumber = "";
        String model = "";
        int accountNumber = 0;
        
        try{
            System.out.println("HW8: account number: " + (Integer)req.getSession().getAttribute("AccountNumber"));
            accountNumber = (Integer)req.getSession().getAttribute("AccountNumber");
        }catch (Exception e) {
            System.out.println("HW8: account number pasing error.");
            e.printStackTrace();
        }
        
        initializeDependentsList(accountNumber);
        drawDependentsList(req, out);
    }
    
    class Dependent{
		public String accountNumber;
		public String userName;
		Dependent(String a, String u){accountNumber = a; userName = u;}
	}
}