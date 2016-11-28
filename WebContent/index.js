idraw.index = function(){
    if (Object.keys(pagerJson).length == 0){
    	pagerJson = {0:{bg_image: null, image: null, timerSec: null, modified: false}};
    }
    currentPage = 0;
    presenMode = false;
	socket.onopen = function(){
        socket.send(JSON.stringify({cmd:"session", id: sessionId}));
	}
	// タイマーとイメージをロード
	$("#timer_text").val(pagerJson[currentPage]["timerSec"] != null ? pagerJson[currentPage]["timerSec"] : "タイマー");
	if (pagerJson[currentPage]["image"] != null) {
		var image = new Image();
		image.src = pagerJson[currentPage]["image"];
		image.onload = function(){
			// 画像の読み込みが終わったら、Canvasに画像を反映する。
			var ctx = $("#canvas")[0].getContext("2d");
			ctx.drawImage(image, 0, 0);
		}
	}
	if (pagerJson[currentPage]["bg_image"] != null) {
		$("#panel_canvas").css("background-image", "url('" + pagerJson[currentPage]["bg_image"] + "')");
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
    
	// ページ追加、削除
    $('#tool_newp').click(function() { idraw.newPage(); });
    $('#tool_delp').click(function() { idraw.delPage(); });

    // 絵のセーブ機能
    $('#tool_save').click(function() {
        socket.send(JSON.stringify({cmd:"save", page_num: currentPage, timer: pagerJson[currentPage]["timerSec"]}));
        pagerJson[currentPage]["modified"] = false;
    	slicePushImage("save", currentPage, $("#canvas")[0].toDataURL("image/png"), 8000);
    });

    // チャット
    $('#tool_text').click(function() {
    	var chatMessage = window.prompt("チャット入力（40文字まで）　\n\"#del\",\"#削除\"で現在のチャット内容を削除します","");
    	if(chatMessage!=null && chatMessage.length>0){
    		if(chatMessage.length > 40){
    			alert("入力できる文字は40文字までです！");
    		}else{
    			if (chatMessage == "#del" || chatMessage == "#削除") {
    	    		var select = confirm("チャット内容を全て削除します。よろしいですか？");
    	    		if(select == true){
    	    			$("#chat_window").empty();
    	        		alert("削除しました");
    	    		}else{
    	    			alert("キャンセルしました");
    	    		}
    	    		return;
    			}
    			socket.send(JSON.stringify({ cmd:"chat", session: sessionId, message:chatMessage }));
    		}
    	}
    });

 	// キー入力時の処理
    keyHookers.push(function (e){
    	switch (e.key){
    	case "ArrowLeft":
    	case "a":
    		if (pagerJson[currentPage+1] !== undefined){
    			idraw.changePage(currentPage+1);
    			socket.send(JSON.stringify({ cmd:"page_shift", page_num: currentPage }));
    		}
    		break;
		case "ArrowRight":
		case "d":
    		if (pagerJson[currentPage-1] !== undefined){
    			idraw.changePage(currentPage-1);
    			socket.send(JSON.stringify({ cmd:"page_shift", page_num: currentPage }));
    		}
			break;
    	case "p":
    		if (e.altKey) {
    			presenMode = !presenMode;
    			if (presenMode) {
    				$("#modified").css("visibility", "hidden");
    				$("#chat_window").css("visibility", "hidden");
    			} else {
    				$("#modified").css("visibility", "visible");
    				$("#chat_window").css("visibility", "visible");
    			}
    		}
    		break;
		}
    });

	//タイマー数値入力後フォーカスが外れるとpagerJsonにタイマー数値を保存するための関数
    $("#timer_text").change(function() {
    	pagerJson[currentPage]["timerSec"] = $("#timer_text").val() != "タイマー" ? $("#timer_text").val() : null;

		$(".point").remove(); //前回このfunctionで生成したしたHTML,CSSを削除する

		if (/\D/.test($("#timer_text").val()) || $("#timer_text").val() == 0) return;
		sum = 0; //全ページのタイマー値合計を保存する変数
		for(var pageNum in pagerJson){ //拡張for文 各ページのタイマー値をsumに入れていく
    		x = parseFloat(pagerJson[pageNum]["timerSec"]);
    		if(x === x && /^[0-9]+$/.test(x) ){ //数値以外（NaN）の場合falseが返ってくるので弾く
				sum += x;
    		}
    	}

		var tmpSum = 0;
		for(var i = 0; i < Object.keys(pagerJson).length; i++){
			if(/^[1-9]+$/.test(pagerJson[i]["timerSec"])){
				var autoPoint = $("<div></div>");
				tmpSum += parseFloat(pagerJson[i]["timerSec"]);
				autoPoint.attr("id","point" + i);
				autoPoint.attr("class","point");
				autoPoint.text("■");
				var pointSet = 800 * tmpSum / sum;
				$("body").append(autoPoint);
				$("#point" + i).attr("style", "left:" + pointSet + "px");
			}
		}
    });

	//設定ボタンが押された時にタイマーを作動させる処理
	onClickTimer = function(nowValue) {
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
};
