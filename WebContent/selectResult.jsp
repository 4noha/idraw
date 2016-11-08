<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="jp.co.itc.db.Article_infoVo" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ログイン</title>
</head>
<body>
	<table border="1">
		<tr>
			<td>投稿日時</td><td>タイトル</td><td>投稿内容</td><td>投稿者</td>
		</tr>
	<%
		ArrayList<Article_infoVo> select_data = (ArrayList<Article_infoVo>)request.getAttribute("SELECT_DATA");
		for(Article_infoVo getdata : select_data){
			out.print("<tr><td>");
			out.print(getdata.getDate_contributed());
			out.print("</td><td>");
			out.print(getdata.getTitle_article());
			out.print("</td><td>");
			out.print(getdata.getContent_article());
			out.print("</td><td>");
			out.print(getdata.getUser_name());
			out.print("</td></tr>");
		}
	%>
	</table>
	<br>
	<a href="/sample_servlet_system/top.jsp">戻る</a><br>
</body>
</html>