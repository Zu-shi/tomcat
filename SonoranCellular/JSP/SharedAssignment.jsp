<%@ page language="java" contentType="text/html"%>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="SonoranCellular.utils.OracleConnect" %>

<html>

<head>
<title>SonoranCellular</title>
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
	<p>
	<center>
		<font size=7 face="Arial, Helvetica, sans-serif" color="#000066">
			<b>SonoranCellular</b><br>
		</font>
		<hr>
		<br> <b>Accounts with the same plan:</b> <br>
		<%
			init();
			ResultSet rs1 = s1.executeQuery("SELECT PlanName FROM Plan");
			/*
			String name1 = "Jean-Pierre";
			String name2 = "Rick Snodgrass";
			String plan1 = "normal";
			String name3 = "Bill Gates";
			String name4 = "Steve Jobs";
			String plan2 = "unlimited";
			*/
		%>

		<table border="1">
			<tr>
				<td><b> Plan Name </b></td>
				<td><b> Customer </b></td>
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
							out.println("<td> </td>");
							out.println("<td> " + rs2.getString(1) + " </td>");
						}
					}
				}
				/*
		  // Card 1
		  out.println("<tr>");
		  out.println("<td> " + plan1 + "</td>");
		  out.println("<td> " + name1 + " </td>");

		  out.println("<tr>");
		  out.println("<td> </td>");
		  out.println("<td> " + name2 + " </td>");

		  // Card 2
		  out.println("<tr>");
		  out.println("<td> " + plan2 + "</td>");
		  out.println("<td> " + name3 + " </td>");

		  out.println("<tr>");
		  out.println("<td> </td>");
		  out.println("<td> " + name4 + " </td>");
		  */
		%>
			
		</table>

		<hr>
		<br>
		<br>

		<table>
			<tr>
				<td>
					<form name="mainmenu" action=../LoginServlet method=get>
						<input type=submit name="MainMenu" value="Main Menu">
					</form>
				</td>
			</tr>
			<tr>
				<td>
					<form name="logout" action=../index.html>
						<input type=submit name="logoutSonoranCellular" value="Logout">
					</form>
				</td>
			</tr>
		</table>


	</center>
	</p>
</body>
</html>


