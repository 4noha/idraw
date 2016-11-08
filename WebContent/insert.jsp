<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String error = (String)request.getAttribute("ERROR");
if(error == null){
	error ="";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>記事投稿</title>
</head>
<body>
	<font color="red"><%=error %></font>
	<form action="insert" method="post">
		<table>
			<tr>
				<td>タイトル</td>
				<td><textarea cols="50" rows="1" name = "title"></textarea></td>
			</tr>
			<tr>
				<td>投稿内容</td>
				<td><textarea cols="50" rows="10" name = "content"></textarea></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value = "投稿"></td>
			</tr>
		</table>
		<br>
		<a href="/sample_servlet_system/top.jsp">戻る</a><br>
	</form>
</body>
</html>