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
    private ArrayList<String> plans;
    
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
    
    public void drawUpdateMessage(HttpServletRequest req, PrintWriter out, String plan_name, int accountNumber, int imei, String model)
    {
        drawHeader(req,out);
        out.println("<p><b>Plan Name:</b>  " + plan_name + "</p>");
        out.println("<p><b>Mobile Number:</b>  " + accountNumber + "</p>");
        out.println("<p><b>IMEI:</b>  " + imei + "</p>");
        out.println("<p><b>Model:</b>  " + model + "</p>");
        
        out.println("<br>");
        
        out.println("<form name=\"MainMenu\" action=LoginServlet>");
        out.println("<input type=submit name=\"MainMenu\" value=\"MainMenu\">");
        out.println("</form>");
        
        out.println("<br>");
        
        out.println("<form name=\"logout\" action=index.html>");
        out.println("<input type=submit name=\"logoutSonoranCellular\" value=\"Logout\">");
        out.println("</form>");
        drawFooter(req,out);
    }
    
    
    public void drawHeader(HttpServletRequest req, PrintWriter out) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Plan Addition</title>");
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
        out.println("<form name=\"MainMenu\" action=LoginServlet>");
        out.println("<input type=submit name=\"MainMenu\" value=\"Return to Main Menu\">");
        out.println("</form>");
        out.println("</td>");
        out.println("</tr>");
        
        out.println("<tr>");
        out.println("<td>");
        out.println("<form name=\"logout\" action=index.html>");
        out.println("<input type=submit name=\"logoutSonoranCellular\" value=\"Logout\">");
        
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
    
    
    public void drawAddPlanInformationMenu(HttpServletRequest req, PrintWriter out)
    {
        drawHeader(req,out);
        drawAddPlanPage(req,out);
        drawFooter(req,out);
    }
    
    public void drawAddPlanPage(HttpServletRequest req, PrintWriter out){
        
        if(!plans.isEmpty()){
            out.println("<form name=\"AddPlan\" action=AddPlan method=get>");
            out.println("<font size=3 face=\"Arial, Helvetica, sans-serif\" color=\"#000066\">");
            out.println("<p>");
            out.println("<b>Plan Name:</b>");
            out.println("<select name=\"planname\">");
            for(String s: plans){
                out.println("<option value = \"" + s + "\">" + s + "</option>");
            }
            out.println("</select>");
            out.println("<br>");
            out.println("</p>");
            
            out.println("<p>");
            out.println("<b>IMEI: </b>");
            out.println("<input type=text name=\"imei\">");
            out.println("<br>");
            out.println("</p>");
            
            out.println("<p>");
            out.println("<b>Mobile Number: </b>");
            out.println("<input type=text name=\"mobilenumber\">");
            out.println("<br>");
            out.println("</p>");
            
            out.println("<p>");
            out.println("<b>Model: </b>");
            out.println("<input type=text name=\"model\">");
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
            out.println("<form name=\"Cancel\" action=AddPlan method=get>");
            out.println("<input type=submit name=\"Cancel\" value=\"Cancel\">&nbsp&nbsp");
            out.println("</form>");
            out.println("</td>");
            out.println("</tr>");
        }else{
            out.println("<font size=5 face=\"Arial,Helvetica\">");
            out.println("<b>There are no availible plans at the moment, please contact your Database Administrator.</b></br>");
        }
    }
    
    public void drawCannotFindPlan(HttpServletRequest req, PrintWriter out){
        drawHeader(req,out);
        out.println("<font size=5 face=\"Arial,Helvetica\">");
        out.println("<b>Error: cannot find corresponding plan.</b></br>");
        
        out.println("<br>");
        drawAddPlanPage(req,out);
        drawFooter(req,out);
    }
    
    public void drawCannotFindPhone(HttpServletRequest req, PrintWriter out){
        drawHeader(req,out);
        out.println("<font size=5 face=\"Arial,Helvetica\">");
        out.println("<b>Error: no matching phone found on record.</b></br>");
        drawAddPlanPage(req,out);
        drawFooter(req,out);
    }
    
    public void drawPhoneAlreadySubscribed(HttpServletRequest req, PrintWriter out){
        drawHeader(req,out);
        out.println("<font size=5 face=\"Arial,Helvetica\">");
        out.println("<b>Error: the indicated phone already has a subscription.</b></br>");
        drawAddPlanPage(req,out);
        drawFooter(req,out);
    }
    
    public void drawParseError(HttpServletRequest req, PrintWriter out, String err){
        drawHeader(req,out);
        out.println("<font size=5 face=\"Arial,Helvetica\">");
        out.println("<b> " + err + "</b></br>");
        
        drawAddPlanPage(req,out);
        drawFooter(req,out);
    }
    
    private void initializePlanList(){
        ResultSet rs;
        String query;
        plans = new ArrayList<String>();
        try{
            query = "SELECT * FROM Plan";
            rs = s.executeQuery(query);
            while(rs.next()){
                plans.add(rs.getString(1));
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
        
        if(req.getParameter("Submit") == null)
        {
            initializePlanList();
            drawAddPlanInformationMenu(req, out);
        }else{
            initializePlanList();
            
            try{
                String[] params = req.getParameterValues("planname");
                String err = InputSanitizer.checkStringAlphanumericAndReturnErrorString("Plan Name",params[0], 1, 40);
                planName = params[0];
                if(err != ""){
                    drawParseError(req, out, err);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try{
                String[] params = req.getParameterValues("imei");
                String err = InputSanitizer.checkStringNumericAndReturnErrorString("IMEI", params[0], 1, 8);
                if(err != ""){
                    drawParseError(req, out, err);
                    return;
                }
                imei = Integer.parseInt(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try{
                String[] params = req.getParameterValues("mobilenumber");
                String err = InputSanitizer.checkPhoneNumAndReturnErrorString("Mobile Number", params[0]);
                mobilenumber = params[0];
                if(err != ""){
                    drawParseError(req, out, err);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try{
                String[] params = req.getParameterValues("model");
                String err = InputSanitizer.checkStringAlphanumericAndReturnErrorString("Model" ,params[0], 1, 10);
                model = params[0];
                if(err != ""){
                    drawParseError(req, out, err);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try{
                //System.out.println("HW8: TEST");
                ResultSet rs;
                String query;
                
                //Check plan exists
                query = "SELECT * FROM Plan WHERE " +
                "PlanName = \'" + planName + "\'";
                System.out.println("HW8: " + query);
                rs = s.executeQuery(query);
                
                if(rs.next()){
                    //Check phone exists
                    query ="SELECT * FROM Phone WHERE " +
                    "MobileNumber = \'" + mobilenumber + "\' AND " +
                    "IMEI = " + imei + " AND " +
                    "Model = " + "\'" + model + "\'";
                    System.out.println("HW8: " + query);
                    rs = s.executeQuery(query);
                    
                    if(rs.next()){
                        //Check subscription does not exist
                        query = "SELECT * FROM Subscribe WHERE " +
                        "IMEI = " + imei;
                        System.out.println("HW8: " + query);
                        rs = s.executeQuery(query);
                        
                        if(!rs.next()){
                            query = "INSERT INTO Subscribe (IMEI, AccountNumber, PlanName) VALUES(" +    imei + ", " + accountNumber + ", '" + planName + "')";
                            System.out.println("HW8: " + query);
                            s.executeUpdate(query);
                            drawUpdateMessage(req, out, planName, accountNumber, imei, model);
                            
                        }else{
                            drawPhoneAlreadySubscribed(req, out);
                        }                    }else{
                            drawCannotFindPhone(req, out);
                        }
                }else{
                    drawCannotFindPlan(req, out);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}