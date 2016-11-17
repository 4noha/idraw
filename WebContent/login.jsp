<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8"> 
	<title>Login</title>
	<link rel="stylesheet" type="text/css" href="./idraw.css">
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
	<script src="./idraw.js"></script>
</head>
<body>
<canvas width="800" height="600" id="canvas" style="position: absolute;"></canvas>
<canvas width="800" height="600" id="base_canvas" style="background-color:#fff;"></canvas>
<div id="console">
	<ul>
	<li style="background-color:#000"></li>
	<li style="background-color:#f00"></li>
	<li style="background-color:#0f0"></li>
	<li style="background-color:#00f"></li>
	<li style="background-color:#ff0"></li>
	<li style="background-color:#fff"></li>
	</ul>
	<div id="button">
		<input type="button" id="save" value="保存" />
		<input type="button" id="clear" value="消去" />
	</div>
</div>
</body>
</html>