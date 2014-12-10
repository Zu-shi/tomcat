package SonoranCellular.servlets;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import SonoranCellular.servlets.*;
import SonoranCellular.utils.*;

public class AddPlan extends HttpServlet
{
    public AddPlan()
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
 	   out.println("<title>Sonoran Cellular -- Add Plan</title>");
 	   out.println("<link href=\"layout.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\">");
 	   out.println("</head>");
 	   out.println("<body>");
 	   out.println("<div id=\"wrapper\">");
 	   out.println("<h1>Sonoran Cellular</h1>");
 	   out.println("<hr>");
    }
    
    
    public void drawFooter(HttpServletRequest req, PrintWriter out)
    {
        out.println("<hr>");
        out.println("<form name=\"MainMenu\" action=LoginServlet>");
        out.println("<input type=submit name=\"MainMenu\" value=\"Return to Main Menu\">");
        out.println("</form>");
        
        out.println("<form name=\"logout\" action=LogoutServlet>");
        out.println("<input type=submit name=\"logoutSonoranCellular\" value=\"Logout\">");
        
        out.println("</form>");
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
        	out.println("<div id=\"loginContainer\">");
            out.println("<form name=\"AddPlan\" action=AddPlan method=get>");
            out.println("<label>Plan Name:</label>");
            out.println("<select name=\"planname\">");
            for(String s: plans){
                out.println("<option value = \"" + s + "\">" + s + "</option>");
            }
            out.println("</select>");
            
            out.println("<label>IMEI: </label>");
            out.println("<input type=text name=\"imei\">");
            
            out.println("<label>Mobile Number: </label>");
            out.println("<input type=text name=\"mobilenumber\">");
            
            out.println("<label>Model: </label>");
            out.println("<input type=text name=\"model\">");
            
            out.println("<input type=submit name=\"Submit\" value=\"Insert\">&nbsp&nbsp");
            
            out.println("</form>");
            
            out.println("<form name=\"Cancel\" action=AddPlan method=get>");
            out.println("<input type=submit name=\"Cancel\" value=\"Cancel\">&nbsp&nbsp");
            out.println("</form>");
            out.println("</div>");
        }else{
            out.println("<h2>There are no availible plans at the moment, please contact your Database Administrator.<h2>");
        }
    }
    
    public void drawCannotFindPlan(HttpServletRequest req, PrintWriter out){
        drawHeader(req,out);
        out.println("<h2>Error: cannot find corresponding plan.</h2>");
        
        drawAddPlanPage(req,out);
        drawFooter(req,out);
    }
    
    public void drawCannotFindPhone(HttpServletRequest req, PrintWriter out){
        drawHeader(req,out);
        out.println("<h2>Error: no matching phone found on record.</h2>");
        drawAddPlanPage(req,out);
        drawFooter(req,out);
    }
    
    public void drawPhoneAlreadySubscribed(HttpServletRequest req, PrintWriter out){
        drawHeader(req,out);
        out.println("<h2>Error: the indicated phone already has a subscription.</h2>");
        drawAddPlanPage(req,out);
        drawFooter(req,out);
    }
    
    public void drawParseError(HttpServletRequest req, PrintWriter out, String err){
        drawHeader(req,out);
        out.println("<h2> " + err + "</h2>");
        
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