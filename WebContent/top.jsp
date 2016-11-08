<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="jp.co.itc.db.User_infoVo" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ログイン</title>
</head>
<body>
	<%
		User_infoVo user_info = (User_infoVo)session.getAttribute("LOGIN_INFO");
		if(user_info == null){
			out.print("\"<meta http-equiv=\"refresh\" content=\"0;URL=/sample_servlet_system/login\">");
		}else{
			out.print(user_info.getUser_name());
		}
	%>
	さん、こんにちは！<br>


	<br>
	<br>
	<a href="/sample_servlet_system/select.jsp">投稿検索</a><br>
	<a href="/sample_servlet_system/insert.jsp">記事投稿</a><br>
	<br>

	<form action="login" method="post">
		<input type="submit" value="ログアウト"/>
		<input type="hidden" name="from_top" value = "999">
	</form>
</body>
</html>