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
<div>
<% // インフォメーション用文字  %>
<h1 id="title" style="position:absolute;margin-left: 230px;margin-top: 160px;font-size: 140px;">iDraw</h1>
<h1 id="infoTitle" style="position:absolute;margin-left: 340px;margin-top: 240px;font-size: 24px;display:none;">ログイン</h1>
<h1 id="subInfo" style="position:absolute;margin-left: 280px;margin-top: 280px;font-size: 20px;display:none;">ログインIDを入力</h1>
<% // 絵を書くキャンバス  %>
<canvas width="800" height="600" id="canvas" style="position: absolute;"></canvas>
<% // タイトルのオブジェクト  %>
<input id="loginButton" type="submit" value="ログイン" class="button" style="position:absolute;margin-left: 280px;margin-top: 350px;">
<input id="signUpButton" type="submit" value="サインアップ" class="button" style="position:absolute;margin-left: 440px;margin-top: 350px;">
<% // ID入力のオブジェクト  %>
<input id="idField" type="text" value="" class="button" style="position:absolute;margin-left: 280px;margin-top: 330px;display:none;">
<input id="idSubmitButton" type="submit" value="Submit" class="button" style="position:absolute;margin-left: 440px;margin-top: 330px;display:none;">
<input id="pwField" type="text" value="" class="button" style="position:absolute;margin-left: 280px;margin-top: 330px;display:none;">
<input id="pwSubmitButton" type="submit" value="Submit" class="button" style="position:absolute;margin-left: 440px;margin-top: 330px;display:none;">
<% // 背景のキャンバス  %>
<canvas width="800" height="600" id="base_canvas" style="background-color:#fff;"></canvas>
</div>
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
<script>
	$("#loginButton").click(function() {
		$("#title").hide(300);
		$("#loginButton").hide(300);
		$("#signUpButton").hide(300);
		
		$("#infoTitle").show(300);
		$("#subInfo").show(300);
		$("#idField").show(300);
		$("#idSubmitButton").show(300);
	});
	
	$("#signUpButton").click(function() {
		$("#title").hide(300);
		$("#loginButton").hide(300);
		$("#signUpButton").hide(300);

		$("#infoTitle").text("サインアップ");
		
		$("#infoTitle").show(300);
		$("#subInfo").show(300);
		$("#idField").show(300);
		$("#idSubmitButton").show(300);
	});

	$("#idSubmitButton").click(function() {
		$("#idField").hide(300);
		$("#idSubmitButton").hide(300);
		// subinfoをしまい終わってから全部出す
		$("#subInfo").hide(300, function() {
			$("#subInfo").text("パスワードを入力");
			
			$("#subInfo").show(300);
			$("#pwField").show(300);
			$("#pwSubmitButton").show(300);
		});
	});

	$("#pwSubmitButton").click(function() {
		var redirect_url = "index.jsp" + location.search;
		if (document.referrer) {
			var referrer = "referrer=" + encodeURIComponent(document.referrer);
			redirect_url = redirect_url + (location.search ? '&' : '?') + referrer;
		}
		location.href = redirect_url;
	});
</script>
</html>