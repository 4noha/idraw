<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String error = (String)request.getAttribute("ERROR");
	if(error == null){
		error = "";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LOGIN</title>
</head>
<body>
	<font color="red"> <%=error %> </font>
	<form action="login" method="post">
		<table>
			<tr>
				<td>mail</td>
				<td><input type="text" name = "mail"></td>
			</tr>
			<tr>
				<td>pass</td>
				<td><input type="password" name = "pass"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value = "ログイン"></td>
			</tr>
		</table>
	</form>
</body>
</html>