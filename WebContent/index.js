// $();にコードが入っているのでページが読み込まれてから呼ばれる
$(function(){
	idraw.websocketInit();
	idraw.loadSessionId();
	idraw.eventDefine();
    if (Object.keys(pagerJson).length == 0){
    	pagerJson = {0:{bg_image: null, image: null, timerSec: "タイマー"}};
    }
    currentPage = 0;
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
    
	// ページ追加
    $('#tool_newp').click(function(){
    	idraw.newPage();
    });

 	// キー入力時の処理
    document.onkeydown = function (e){
    	switch (e.key){
    	case "ArrowUp":
    	case "w":
    		$("#panel_console").animate({top: '500px'},500);
    		break;
    	case "ArrowDown":
    	case "s":
    		$("#panel_console").animate({top: '600px'},500);
    		break;
    	case "ArrowLeft":
    	case "a":
    		if (!(pagerJson === undefined || currentPage === undefined || pagerJson[currentPage+1] === undefined)){
    			idraw.changePage(currentPage+1);
    		}
    		break;
		case "ArrowRight":
		case "d":
    		if (!(pagerJson === undefined || currentPage === undefined || pagerJson[currentPage-1] === undefined)){
    			idraw.changePage(currentPage-1);
    		}
			break;
		}
    };

	//タイマー数値入力後フォーカスが外れるとpagerJsonにタイマー数値を保存するための関数
    $("#timer_text").change(function() {
    	pagerJson[currentPage]["timerSec"] = $("#timer_text").val();
    });

	//設定ボタンが押された時にタイマーを作動させる処理
	onClickTimer = function(nowValue) {
		$(".point").remove(); //前回このfunctionで生成したしたHTML,CSSを削除する

		if (/\D/.test($("#timer_text").val()) || $("#timer_text").val() == 0) return;
		var sum = 0; //全ページのタイマー値合計を保存する変数
		for(var pageNum in pagerJson){ //拡張for文 各ページのタイマー値をsumに入れていく
    		x = parseFloat(pagerJson[pageNum]["timerSec"]);
    		if(x === x && /^[0-9]+$/.test(x) ){ //数値以外（NaN）の場合falseが返ってくるので弾く
				sum += x;
    		}
    	}
		console.log(sum);

		var tmpSum = 0;
		for(var i = 0; i < Object.keys(pagerJson).length; i++){
			if(/^[0-9]+$/.test(pagerJson[i]["timerSec"]) && pagerJson[i]["timerSec"] != 0){
				var autoPoint = $("<div></div>");
				tmpSum += pagerJson[i]["timerSec"];
				autoPoint.attr("id","point" + i);
				autoPoint.attr("class","point");
				autoPoint.text("■");
				var pointSet = 800 * tmpSum / sum;
				$("body").append(autoPoint);
				$("#point" + i).attr("style", "left:" + pointSet + "px");
			}
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
