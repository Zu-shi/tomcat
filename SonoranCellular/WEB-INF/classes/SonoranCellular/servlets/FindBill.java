package SonoranCellular.servlets;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import SonoranCellular.servlets.*;
import SonoranCellular.utils.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FindBill extends HttpServlet
{
	ArrayList<Bill> bills;
    
    public FindBill()
    {
        super();
    }
    
    private OracleConnect oc = new OracleConnect();
    private Statement s, s2;
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
			s2 = c.createStatement();
			
			if (s == null) throw new Exception("Statement creation fialed.");
			if (s2 == null) throw new Exception("Statement creation fialed.");
            
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
        out.println("<head><title>Find Bill</title></head>");
        
        out.println("<body>");
        out.println("<p>");
        out.println("<center>");
        out.println("<font face=\"Arial, Helvetica, sans-serif\">");
        out.println("<font color=\"#000066\">");
        out.println("<center>\n<font size=7><strong>SonoranCellular</strong></font></br>");
        out.println("</center>\n<font size=4><hr color=\"#000066\">");
        out.println("<b>Find Bill</b><br><br/></font>");
        out.println("</font>");
    }
    
    public void drawFooter(HttpServletRequest req, PrintWriter out)
    {
        out.println("<br>");
        out.println("<hr>");
        out.println("<br>");
        
        out.println("<p>");
        out.println("<form name=\"MainMenu\" action=LoginServlet method=get>");
        out.println("<input type=submit name=\"MainMenu\" value=\"Main Menu\">");
        out.println("</form>");
        out.println("</p>");
        
        out.println("<p>");
        out.println("<form name=\"logout\" action=index.html>");
        out.println("<input type=submit name=\"logoutSonoranCellular\" value=\"Logout\">");
        out.println("</form>");
        out.println("</p>");
        
        out.println("<br><br>");
        out.println("</center>");
        out.println("</p>");
        out.println("</body>");
        out.println("</html>");
    }
    
    
    public void drawGetBill(HttpServletRequest req, PrintWriter out)
    {
        drawHeader(req,out);
        out.println("<form name=\"billSearch\" action=FindBill method=get>");
        out.println("Enter billing period: ");
        out.println("<input type=text size=30 name=\"billperiod\">");
        out.println("<input type=submit name=\"findBill\" value=\"Find\" >");
        out.println("</form>");
        drawFooter(req,out);
    }
    
    public void drawParseError(HttpServletRequest req, PrintWriter out, String err){
        drawHeader(req,out);
        out.println("<font size=5 face=\"Arial,Helvetica\">");
        out.println("<b> " + err + "</b></br>");
        
        out.println("<form name=\"retry\" action=FindBill>");
        out.println("<input type=submit name=\"home\" value=\"retry\">");
        out.println("</form>");
        drawFooter(req,out);
    }
    
    private void drawCannotFindBill(HttpServletRequest req, PrintWriter out)
    {
        drawHeader(req,out);
        out.println("<font size=5 face=\"Arial,Helvetica\">");
        out.println("<b>There is no bill for the specified billing period.</b></br>");
        out.println("<b>Please enter another billing period and try again.</b></br>");
        
        out.println("<form name=\"billSearch\" action=FindBill method=get>");
        out.println("Enter billing period: ");
        out.println("<input type=text size=30 name=\"billperiod\">");
        out.println("<input type=submit name=\"findBill\" value=\"Find\" >");
        out.println("</form>");
        drawFooter(req,out);
    }
    
    public void drawShowInfo(HttpServletRequest req, PrintWriter out, String duedate, ArrayList<Bill> bills)
    {
        drawHeader(req,out);
        String billperiod = req.getParameter("billperiod");
        
        out.println("<p><b>Bill for billing period " + duedate.substring(0, 10) + ":</b>");
        
        for(Bill b : bills){
            out.println("<p><b>---For bill ending at " + b.endDate.substring(0, 10) + "---</b>");
            for(Item i : b.items){
                out.println("<p><b>Item number: " + i.itemNumber + " Amount: $" + i.amount + "</b>");
            }
        }
        
        out.println("<form name=\"billSearch\" action=FindBill method=get>");
        out.println("Enter billing period: ");
        out.println("<input type=text size=30 name=\"billperiod\">");
        out.println("<input type=submit name=\"findBill\" value=\"Find\" >");
        out.println("</form>");
        drawFooter(req,out);
    }
    
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        System.out.println("HW8: Doing get for FindBill.");
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        String billdate = "";
        int accountNumber = 0;

        try{
            System.out.println("HW8: account number: " + (Integer)req.getSession().getAttribute("AccountNumber"));
            accountNumber = (Integer)req.getSession().getAttribute("AccountNumber");
        }catch (Exception e) {
            System.out.println("HW8: account number pasing error.");
            e.printStackTrace();
        }
        
        if(req.getParameter("findBill") == null)
        {
            drawGetBill(req,out);
        }
        else
        {
            try{
                String[] params = req.getParameterValues("billperiod");
                String err = InputSanitizer.checkDateValidAndReturnErrorString("Billing Period", params[0]);
                billdate = params[0];
                if(err != ""){
                    drawParseError(req, out, err);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try{
                ResultSet rs, rs2;
                String query, query2;
                
                bills = new ArrayList<Bill>();
                query = "SELECT Bill.Startdate, Bill.Enddate FROM Bill WHERE " +
                "AccountNumber = \'" + accountNumber + "\' AND " +
                "Duedate = TO_DATE(" + "\'" + billdate + "\', \'YYYY-MM-DD\')";
                System.out.println("HW8: " + query);
                rs = s.executeQuery(query);
                
                while (rs.next()){
                    //SimpleDateFormat inputFormat = new SimpleDateFormat("DD-MMMM-YY");
                    //SimpleDateFormat outputFormat = new SimpleDateFormat("YYYYMMDD");
                    
                    query2 = "SELECT Item.ItemNumber, Item.Amount FROM Item WHERE Item.Enddate = TO_DATE(\'"
                        + rs.getString(2).substring(0, 10) + "\', \'YYYY-MM-DD\') AND Item.AccountNumber = " + accountNumber;
                    System.out.println("HW8: " + query2);
                    rs2 = s2.executeQuery(query2);
                    
                    ArrayList<Item> items = new ArrayList<Item>();
                    while(rs2.next()){
                        items.add(new Item(rs2.getString(1), rs2.getString(2)));
                    }
                    
                    //if(!items.isEmpty()){
                        bills.add(new Bill(accountNumber, rs.getString(2), rs.getString(1), billdate, items));
                    //}
                }
                
                if(bills.isEmpty()){
                    drawCannotFindBill(req, out);
                }else{
                    drawShowInfo(req, out, billdate, bills);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    class Bill{
		public int accountNumber;
		public String endDate;
		public String startDate;
		public String dueDate;
        public ArrayList<Item> items;
        
		Bill(int n, String e, String s, String d, ArrayList<Item> i){
            accountNumber = n;
            endDate = e;
            startDate = s;
            dueDate = d;
            items = i;
        }
        /*
		@Override
		public String toString(){return Integer.toString(number) + ".    " + name;}
         */
	}
    
	class Item{
		public String itemNumber;
		public String amount;
		Item(String i, String a){itemNumber = i; amount = a;}
	}
}



