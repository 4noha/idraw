<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String insert_title = (String)request.getAttribute("INPUT_TITLE");
String insert_content = (String)request.getAttribute("INPUT_CONTENT");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>投稿結果</title>
</head>
<body>
以下内容で投稿しました。
		<table>
			<tr>
				<td>タイトル</td>
				<td><textarea cols="50" rows="1" name = "title" disabled="disabled"><%=insert_title%></textarea></td>
			</tr>
			<tr>
				<td>投稿内容</td>
				<td><textarea cols="50" rows="10" name = "content" disabled="disabled"><%=insert_content%></textarea></td>
			</tr>
			<tr>
				<td colspan="2"><a href="/sample_servlet_system/top.jsp">戻る</a><br></td>
			</tr>
		</table>

</body>
</html>