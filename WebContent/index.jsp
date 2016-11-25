<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="idraw.model.User"%>
<%@ page import="idraw.orm.DbUtil"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.function.Consumer"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Canvas</title>
<link rel="stylesheet" type="text/css" href="./idraw.css">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="./jquery.cookie-1.4.1.min.js"></script>
<script src="./idraw.js"></script>
<%
	Cookie[] coockies = request.getCookies();
	for (Cookie cookie : coockies) {
		if (cookie.getName().equals("JSESSIONID")) {
			Map<String, String> dbConfig = new HashMap<String, String>();
			dbConfig.put("env", "production");
			dbConfig.put("host", "127.0.0.1:3306");
			dbConfig.put("db_name", "idraw");
			DbUtil.connect(dbConfig);
			//現在のクッキーの値がDBに格納されていないかチェック
			if (User.findBy("session_id", cookie.getValue()) == null) {
				// True:格納されていないので、ログインページに強制リダイレクト
				response.sendRedirect("./login.jsp");
				return;
			} else {
				cookie.setHttpOnly(false);
				response.addCookie(cookie);
			}
		}
	}
%>
</head>
<body>
	<div id="chat_window" style="width=100;height=800; position: absolute;"></div>
	<div id="panel_canvas">
		<canvas width="800" height="600" id="canvas"
			style="position: absolute;"></canvas>
	</div>
	<div id="panel_console" class="Paper">
		<div id="panel_theme">
			<span id="theme_text">テーマ：</span> <select id="select_theme">
				<option class="options">Paper</option>
				<option class="options">Modern</option>
				<option class="options">Dark</option>
			</select>
		</div>
		<table id="palette">
			<tr>
				<td class="palette_cell" style="background-color: hsl(0, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(30, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(60, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(90, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(120, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(150, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(180, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(210, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(240, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(270, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(300, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(330, 100%, 90%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(330, 0%, 100%)"></td>
			</tr>
			<tr>
				<td class="palette_cell" style="background-color: hsl(0, 100%, 80%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(30, 100%, 80%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(60, 100%, 80%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(90, 100%, 80%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(120, 100%, 80%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(150, 100%, 80%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(180, 100%, 80%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(210, 100%, 80%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(240, 100%, 80%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(270, 100%, 80%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(300, 100%, 80%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(330, 100%, 80%)"></td>
				<td class="palette_cell" style="background-color: hsl(330, 0%, 80%)"></td>
			</tr>
			<tr>
				<td class="palette_cell" style="background-color: hsl(0, 100%, 70%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(30, 100%, 70%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(60, 100%, 70%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(90, 100%, 70%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(120, 100%, 70%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(150, 100%, 70%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(180, 100%, 70%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(210, 100%, 70%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(240, 100%, 70%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(270, 100%, 70%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(300, 100%, 70%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(330, 100%, 70%)"></td>
				<td class="palette_cell" style="background-color: hsl(330, 0%, 70%)"></td>
			</tr>
			<tr>
				<td class="palette_cell" style="background-color: hsl(0, 100%, 60%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(30, 100%, 60%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(60, 100%, 60%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(90, 100%, 60%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(120, 100%, 60%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(150, 100%, 60%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(180, 100%, 60%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(210, 100%, 60%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(240, 100%, 60%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(270, 100%, 60%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(300, 100%, 60%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(330, 100%, 60%)"></td>
				<td class="palette_cell" style="background-color: hsl(330, 0%, 60%)"></td>
			</tr>
			<tr>
				<td class="palette_cell" style="background-color: hsl(0, 100%, 50%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(30, 100%, 50%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(60, 100%, 50%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(90, 100%, 50%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(120, 100%, 50%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(150, 100%, 50%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(180, 100%, 50%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(210, 100%, 50%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(240, 100%, 50%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(270, 100%, 50%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(300, 100%, 50%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(330, 100%, 50%)"></td>
				<td class="palette_cell" style="background-color: hsl(330, 0%, 50%)"></td>
			</tr>
			<tr>
				<td class="palette_cell" style="background-color: hsl(0, 100%, 40%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(30, 100%, 40%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(60, 100%, 40%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(90, 100%, 40%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(120, 100%, 40%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(150, 100%, 40%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(180, 100%, 40%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(210, 100%, 40%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(240, 100%, 40%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(270, 100%, 40%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(300, 100%, 40%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(330, 100%, 40%)"></td>
				<td class="palette_cell" style="background-color: hsl(330, 0%, 40%)"></td>
			</tr>
			<tr>
				<td class="palette_cell" style="background-color: hsl(0, 100%, 30%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(30, 100%, 30%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(60, 100%, 30%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(90, 100%, 30%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(120, 100%, 30%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(150, 100%, 30%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(180, 100%, 30%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(210, 100%, 30%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(240, 100%, 30%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(270, 100%, 30%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(300, 100%, 30%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(330, 100%, 30%)"></td>
				<td class="palette_cell" style="background-color: hsl(330, 0%, 30%)"></td>
			</tr>
			<tr>
				<td class="palette_cell" style="background-color: hsl(0, 100%, 20%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(30, 100%, 20%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(60, 100%, 20%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(90, 100%, 20%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(120, 100%, 20%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(150, 100%, 20%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(180, 100%, 20%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(210, 100%, 20%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(240, 100%, 20%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(270, 100%, 20%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(300, 100%, 20%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(330, 100%, 20%)"></td>
				<td class="palette_cell" style="background-color: hsl(330, 0%, 20%)"></td>
			</tr>
			<tr>
				<td class="palette_cell" style="background-color: hsl(0, 100%, 10%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(30, 100%, 10%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(60, 100%, 10%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(90, 100%, 10%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(120, 100%, 10%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(150, 100%, 10%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(180, 100%, 10%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(210, 100%, 10%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(240, 100%, 10%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(270, 100%, 10%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(300, 100%, 10%)"></td>
				<td class="palette_cell"
					style="background-color: hsl(330, 100%, 10%)"></td>
				<td class="palette_cell" style="background-color: hsl(330, 0%, 0%)"></td>
			</tr>
		</table>

		<div id="panel_button">
			<input type="button" id="tool_pen" class="tool_button Paper" /> <input
				type="button" id="tool_text" class="tool_button Paper" /> <input
				type="button" id="tool_image" class="tool_button Paper" /> <input
				type="button" id="tool_newp" class="tool_button Paper" /> <input
				type="button" id="tool_delp" class="tool_button Paper" /> <input
				type="button" id="tool_save" class="tool_button Paper" /> <input
				type="button" id="tool_clear" class="tool_button Paper" />
				<input type="button" id="tool_eraser" class="tool_button Paper" />
		</div>

			<div id="panel_timer" class="Paper">
				<table style="width: 50px;">
					<tr style="height: 25px;">
						<td><input type="button" id="timer_button" value="設定"
							onClick="onClickTimer(progress.value);"></td>
					</tr>
					<tr style="height: 25px;">
						<td><input type="text" id="timer_text" size="1"
							value="タイマー" onClick="Clear_text(this.id);" onblur="write_timer(this.id);"/></td>
					</tr>
					<tr style="height: 50px;">
						<td></td>
					</tr>
				</table>
			</div>

	</div>
	<progress id="progress" value=0 max=0></progress>

	<div id="point"></div>

	<div id="panel_mask"></div>
	<input type="file" id="image_uploader" style="opacity:0;" />
</body>
<script>
$(function(){
	idraw.websocketInit();
	idraw.loadSessionId();
	idraw.eventDefine();
    pagerJson = {
    		1: {bg_image: null, image: $("#canvas")[0].toDataURL("image/png"), timerSec: 1},
    		2: {bg_image: null, image: $("#canvas")[0].toDataURL("image/png"), timerSec: 1}
    }
    currentPage = 1;
	socket.onopen = function(){
        socket.send(JSON.stringify({cmd:"session", id: sessionId}));
	}

	// 背景アップロードはindexだけの機能
    $('#tool_image').click(function(){
    	$('#image_uploader').click();
    });
    $('#image_uploader').change(function(){
		var preview = new Image();
		file = this.files[0];
		var reader  = new FileReader();
		reader.readAsDataURL(file);

		reader.onloadend = function () {
				slicePushImage("bgsave", currentPage, reader.result, 8000);
		}
   	});

	//タイマー数値入力後フォーカスが外れるとpagerJsonにタイマー数値を保存するための関数
    $("#timer_text").change(function() {
    	pagerJson[currentPage]["timerSec"] = $("#timer_text").val();
    });

	//設定ボタンが押された時にタイマーを作動させる処理
	onClickTimer = function(nowValue) {
		if ($("#timer_text").val() == "" ||$("#timer_text").val() == 0) return;
		var sum = 0; //全ページのタイマー値合計を保存する変数
		for(var pageNum in pagerJson){ //拡張for文 各ページのタイマー値をsumに入れていく
    		console.log(pagerJson[pageNum]["timerSec"]);
    		x = parseFloat(pagerJson[pageNum]["timerSec"]);
    		if(x === x){ //数値以外（NaN）の場合falseが返ってくるので弾く
				sum += x;
    		}
    	}

		var tmpSum = 0;
		for(var i = 1; i < Object.keys(pagerJson).length + 1; i++){
			var autoPoint = $("<div></div>");
			tmpSum += pagerJson[i]["timerSec"];
			autoPoint.attr("id","point" + i);
			autoPoint.attr("class","point");
			autoPoint.text("■");
			var pointSet = 800 * tmpSum / sum;
			$("body").append(autoPoint);
			$("#point" + i).attr("style", "left:" + pointSet + "px");
		}


		progress.value = 0;
		progress.max = sum; //最大値を設定

		if(nowValue == 0){
			timer = setInterval(() => {
				progress.value += 0.01;
				if(progress.value == progress.max){
					clearInterval(timer);
					progress.value = 0;
				}
			}, 10);
		}else if(nowValue != 0 && nowValue != progress.max){
			clearInterval(timer);
			timer = setInterval(() => {
			progress.value += 0.01;
			if(progress.value == progress.max){
				clearInterval(timer);
				progress.value = 0;
			}
			}, 10);
		}else{
			//setIntervalは重複して起動させないためにelse文は空
		}
	}

});

</script>
</html>