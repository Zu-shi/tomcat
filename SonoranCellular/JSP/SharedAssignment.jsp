<%@ page language="java" contentType="text/html"%>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="SonoranCellular.utils.OracleConnect" %>

<html>

<head>
	<title>Sonoran Cellular -- Shared Assignments</title>
	<link href="../layout.css" rel="stylesheet" type="text/css" media="all">
</head>
<body link=#f0f0ff alink vlink=#f0f0ff>
	<%! Statement s1; %>
	<%! Statement s2; %>
	<%! Connection c; %>
	
	<%!
	public void init() {
   		String user_name = OracleConnect.user_name;
		String password = OracleConnect.password;
		String connect_string = OracleConnect.connect_string;
		
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			c = DriverManager.getConnection(connect_string,user_name,password);
		
			if (c == null) throw new Exception("Connection to database failed.");

			s1 = c.createStatement();
			s2 = c.createStatement();
			
			if (s1 == null || s2 == null) throw new Exception("Statement creation fialed.");
		
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("HW8: Connection to database successful");
	}
	%>
	
	<div id="wrapper">
	
	<h1>SonoranCellular</h1>
	<hr>
	<h2>Accounts with the same plan</h2>
	<%
		init();
		ResultSet rs1 = s1.executeQuery("SELECT PlanName FROM Plan");
	%>
	
	<div id="tableContainer">
	<table>
		<tr>
			<th>Plan Name</th>
			<th>Customer</th>
			<%
			while (rs1.next()){
				ResultSet rs2 = s2.executeQuery("SELECT Name FROM Account, Subscribe " +
												"WHERE Account.AccountNumber = Subscribe.AccountNumber AND " +
												"Subscribe.PlanName = \'" + rs1.getString(1) + "\'");
				if(rs2.next()) {
					out.println("<tr>");
					out.println("<td> " + rs1.getString(1) + "</td>");
					out.println("<td> " + rs2.getString(1) + "</td>");	
					while(rs2.next()) {
						out.println("<tr>");
						out.println("<td class=\"noTop\"> </td>");
						out.println("<td> " + rs2.getString(1) + " </td>");
					}
				}
			}
			%>
			
	</table>
	</div>
	
	<hr>
	<form name="mainmenu" action=../LoginServlet method=get>
		<input type=submit name="MainMenu" value="Main Menu">
	</form>
	
	<form name="logout" action=../LogoutServlet method=get>
		<input type=submit name="logoutSonoranCellular" value="Logout">
	</form>

	</div>
</body>
</html>


