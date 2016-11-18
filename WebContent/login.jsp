<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8"> 
	<title>Login</title>
	<link rel="stylesheet" type="text/css" href="./idraw.css">
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
	<script src="./cryptico.min.js"></script>
	<script src="./idraw.js"></script>
</head>
<body>
<div>
<% // インフォメーション用文字  %>
<h1 id="title" style="position:absolute;margin-left: 160px;margin-top: 160px;font-size: 140px;">WeDraw</h1>
<h1 id="infoTitle" style="position:absolute;margin-left: 340px;margin-top: 240px;font-size: 24px;display:none;">ログイン</h1>
<h1 id="subInfo" style="position:absolute;margin-left: 280px;margin-top: 280px;font-size: 20px;display:none;">ログインIDを入力</h1>
<% // 絵を書くキャンバス  %>
<canvas width="800" height="600" id="canvas" style="position: absolute;"></canvas>
<% // ブラッシュの画像  %>
<img id="brush" src="images/brush.jpg" style="height:130px;width:0px;position:absolute">
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

<table id="palette">
  <tr>
    <td class="palette_cell" style="background-color:hsl(0, 100%, 90%)"></td>
    <td class="palette_cell" style="background-color:hsl(30, 100%, 90%)"></td> 
    <td class="palette_cell" style="background-color:hsl(60, 100%, 90%)"></td>
    <td class="palette_cell" style="background-color:hsl(90, 100%, 90%)"></td>
    <td class="palette_cell" style="background-color:hsl(120, 100%, 90%)"></td>
    <td class="palette_cell" style="background-color:hsl(150, 100%, 90%)"></td>
    <td class="palette_cell" style="background-color:hsl(180, 100%, 90%)"></td>
    <td class="palette_cell" style="background-color:hsl(210, 100%, 90%)"></td>
    <td class="palette_cell" style="background-color:hsl(240, 100%, 90%)"></td>
    <td class="palette_cell" style="background-color:hsl(270, 100%, 90%)"></td>
    <td class="palette_cell" style="background-color:hsl(300, 100%, 90%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 100%, 90%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 0%, 100%)"></td>
  </tr>
  <tr>
    <td class="palette_cell" style="background-color:hsl(0, 100%, 80%)"></td>
    <td class="palette_cell" style="background-color:hsl(30, 100%, 80%)"></td> 
    <td class="palette_cell" style="background-color:hsl(60, 100%, 80%)"></td>
    <td class="palette_cell" style="background-color:hsl(90, 100%, 80%)"></td>
    <td class="palette_cell" style="background-color:hsl(120, 100%, 80%)"></td>
    <td class="palette_cell" style="background-color:hsl(150, 100%, 80%)"></td>
    <td class="palette_cell" style="background-color:hsl(180, 100%, 80%)"></td>
    <td class="palette_cell" style="background-color:hsl(210, 100%, 80%)"></td>
    <td class="palette_cell" style="background-color:hsl(240, 100%, 80%)"></td>
    <td class="palette_cell" style="background-color:hsl(270, 100%, 80%)"></td>
    <td class="palette_cell" style="background-color:hsl(300, 100%, 80%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 100%, 80%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 0%, 80%)"></td>
  </tr>
  <tr>
    <td class="palette_cell" style="background-color:hsl(0, 100%, 70%)"></td>
    <td class="palette_cell" style="background-color:hsl(30, 100%, 70%)"></td> 
    <td class="palette_cell" style="background-color:hsl(60, 100%, 70%)"></td>
    <td class="palette_cell" style="background-color:hsl(90, 100%, 70%)"></td>
    <td class="palette_cell" style="background-color:hsl(120, 100%, 70%)"></td>
    <td class="palette_cell" style="background-color:hsl(150, 100%, 70%)"></td>
    <td class="palette_cell" style="background-color:hsl(180, 100%, 70%)"></td>
    <td class="palette_cell" style="background-color:hsl(210, 100%, 70%)"></td>
    <td class="palette_cell" style="background-color:hsl(240, 100%, 70%)"></td>
    <td class="palette_cell" style="background-color:hsl(270, 100%, 70%)"></td>
    <td class="palette_cell" style="background-color:hsl(300, 100%, 70%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 100%, 70%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 0%, 70%)"></td>
  </tr>
  <tr>
    <td class="palette_cell" style="background-color:hsl(0, 100%, 60%)"></td>
    <td class="palette_cell" style="background-color:hsl(30, 100%, 60%)"></td> 
    <td class="palette_cell" style="background-color:hsl(60, 100%, 60%)"></td>
    <td class="palette_cell" style="background-color:hsl(90, 100%, 60%)"></td>
    <td class="palette_cell" style="background-color:hsl(120, 100%, 60%)"></td>
    <td class="palette_cell" style="background-color:hsl(150, 100%, 60%)"></td>
    <td class="palette_cell" style="background-color:hsl(180, 100%, 60%)"></td>
    <td class="palette_cell" style="background-color:hsl(210, 100%, 60%)"></td>
    <td class="palette_cell" style="background-color:hsl(240, 100%, 60%)"></td>
    <td class="palette_cell" style="background-color:hsl(270, 100%, 60%)"></td>
    <td class="palette_cell" style="background-color:hsl(300, 100%, 60%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 100%, 60%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 0%, 60%)"></td>
  </tr>
  <tr>
    <td class="palette_cell" style="background-color:hsl(0, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(30, 100%, 50%)"></td> 
    <td class="palette_cell" style="background-color:hsl(60, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(90, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(120, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(150, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(180, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(210, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(240, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(270, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(300, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 0%, 50%)"></td>
  </tr>
  <tr>
    <td class="palette_cell" style="background-color:hsl(0, 100%, 50%)"></td>
    <td class="palette_cell" style="background-color:hsl(30, 100%, 40%)"></td> 
    <td class="palette_cell" style="background-color:hsl(60, 100%, 40%)"></td>
    <td class="palette_cell" style="background-color:hsl(90, 100%, 40%)"></td>
    <td class="palette_cell" style="background-color:hsl(120, 100%, 40%)"></td>
    <td class="palette_cell" style="background-color:hsl(150, 100%, 40%)"></td>
    <td class="palette_cell" style="background-color:hsl(180, 100%, 40%)"></td>
    <td class="palette_cell" style="background-color:hsl(210, 100%, 40%)"></td>
    <td class="palette_cell" style="background-color:hsl(240, 100%, 40%)"></td>
    <td class="palette_cell" style="background-color:hsl(270, 100%, 40%)"></td>
    <td class="palette_cell" style="background-color:hsl(300, 100%, 40%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 100%, 40%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 0%, 40%)"></td>
  </tr>
  <tr>
    <td class="palette_cell" style="background-color:hsl(0, 100%, 30%)"></td>
    <td class="palette_cell" style="background-color:hsl(30, 100%, 30%)"></td> 
    <td class="palette_cell" style="background-color:hsl(60, 100%, 30%)"></td>
    <td class="palette_cell" style="background-color:hsl(90, 100%, 30%)"></td>
    <td class="palette_cell" style="background-color:hsl(120, 100%, 30%)"></td>
    <td class="palette_cell" style="background-color:hsl(150, 100%, 30%)"></td>
    <td class="palette_cell" style="background-color:hsl(180, 100%, 30%)"></td>
    <td class="palette_cell" style="background-color:hsl(210, 100%, 30%)"></td>
    <td class="palette_cell" style="background-color:hsl(240, 100%, 30%)"></td>
    <td class="palette_cell" style="background-color:hsl(270, 100%, 30%)"></td>
    <td class="palette_cell" style="background-color:hsl(300, 100%, 30%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 100%, 30%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 0%, 30%)"></td>
  </tr>
  <tr>
    <td class="palette_cell" style="background-color:hsl(0, 100%, 20%)"></td>
    <td class="palette_cell" style="background-color:hsl(30, 100%, 20%)"></td> 
    <td class="palette_cell" style="background-color:hsl(60, 100%, 20%)"></td>
    <td class="palette_cell" style="background-color:hsl(90, 100%, 20%)"></td>
    <td class="palette_cell" style="background-color:hsl(120, 100%, 20%)"></td>
    <td class="palette_cell" style="background-color:hsl(150, 100%, 20%)"></td>
    <td class="palette_cell" style="background-color:hsl(180, 100%, 20%)"></td>
    <td class="palette_cell" style="background-color:hsl(210, 100%, 20%)"></td>
    <td class="palette_cell" style="background-color:hsl(240, 100%, 20%)"></td>
    <td class="palette_cell" style="background-color:hsl(270, 100%, 20%)"></td>
    <td class="palette_cell" style="background-color:hsl(300, 100%, 20%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 100%, 20%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 0%, 20%)"></td>
  </tr>
  <tr>
    <td class="palette_cell" style="background-color:hsl(0, 100%, 10%)"></td>
    <td class="palette_cell" style="background-color:hsl(30, 100%, 10%)"></td> 
    <td class="palette_cell" style="background-color:hsl(60, 100%, 10%)"></td>
    <td class="palette_cell" style="background-color:hsl(90, 100%, 10%)"></td>
    <td class="palette_cell" style="background-color:hsl(120, 100%, 10%)"></td>
    <td class="palette_cell" style="background-color:hsl(150, 100%, 10%)"></td>
    <td class="palette_cell" style="background-color:hsl(180, 100%, 10%)"></td>
    <td class="palette_cell" style="background-color:hsl(210, 100%, 10%)"></td>
    <td class="palette_cell" style="background-color:hsl(240, 100%, 10%)"></td>
    <td class="palette_cell" style="background-color:hsl(270, 100%, 10%)"></td>
    <td class="palette_cell" style="background-color:hsl(300, 100%, 10%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 100%, 10%)"></td>
    <td class="palette_cell" style="background-color:hsl(330, 0%, 0%)"></td>
  </tr>
</table>	

    <div id="button">
		<input type="button" id="save" value="保存" />
		<input type="button" id="clear" value="消去" />
	</div>
</div>
</body>
<script>
	var ani_speed = 500;
	$("#loginButton").click(function() {
		idNew = false;
		$("#brush").animate({top: '300px',opacity: '1'},0);
		$("#title").animate({top: '-100px', opacity: '0.2'},ani_speed);
		$("#loginButton").animate({opacity: '0'},ani_speed);
		$("#signUpButton").animate({opacity: '0'},ani_speed);
		$("#brush").animate({opacity: '0'},ani_speed);
		
		$("#infoTitle").show(ani_speed);
		$("#subInfo").show(ani_speed);
		$("#idField").show(ani_speed);
		$("#idSubmitButton").show(ani_speed);
		$("#brush").hide(0);
	});
	
	$("#signUpButton").click(function() {
		idNew = true;
		$("#title").animate({top: '-100px', opacity: '0.2'},ani_speed);
		$("#loginButton").animate({opacity: '0'},ani_speed);
		$("#signUpButton").animate({opacity: '0'},ani_speed);
		$("#brush").animate({opacity: '0'},ani_speed);
		
		$("#infoTitle").text("サインアップ");
		
		$("#infoTitle").show(ani_speed);
		$("#subInfo").show(ani_speed);
		$("#idField").show(ani_speed);
		$("#idSubmitButton").show(ani_speed);
	});

	$("#idSubmitButton").click(function() {
		// id送信
		socket.send(JSON.stringify({ cmd:"login", id:$("#idField").val(), "new":idNew}));
		$("#idField").animate({opacity: '0'},ani_speed);
		$("#idSubmitButton").animate({opacity: '0'},ani_speed);
		// subinfoをしまい終わってから全部出す
		$("#subInfo").animate({opacity: '0'},ani_speed, function() {
			$("#subInfo").text("パスワードを入力");
			
			$("#subInfo").animate({opacity: '1'},ani_speed);
			$("#pwField").show(ani_speed);
			$("#pwSubmitButton").show(ani_speed);
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
	
$("#loginButton").mouseover(function() {
	$("#brush").animate({top: '300px', left: '220px'},0);
	$("#brush").animate({width: '200px'},200);
	});
$("#loginButton").mouseout(function() {
	$("#brush").animate({width: '0px'},200);
	});
$("#signUpButton").mouseover(function() {
	$("#brush").animate({top: '300px', left: '390px'},0);
	$("#brush").animate({width: '200px'},200);
	});
$("#signUpButton").mouseout(function() {
	$("#brush").animate({width: '0px'},200);
	});
</script>
</html>