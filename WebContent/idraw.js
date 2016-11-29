idraw = {}
// まず実行、mockの場合はこれを実行せず、socketのmockを作成する
idraw.websocketInit = function() {
	// 本番のIP
    hosts=[
    	"ws://126.15.139.167:8080/idraw/endpoint",
    	"ws://192.168.1.21:8080/idraw/endpoint",
    	"ws://localhost:8080/idraw/endpoint",
    	];
    hostNum = -1;

    onmessage = function(msg){
        var json = $.parseJSON(msg.data);
    	console.log(json);
    	for(var i=0; i<commands.length; i++){
    		commands[i](json);
    	}
    }

    // 接続先を変えながら最初のsocketを作る
    createSocket = function(){
    	if (hostNum+1 < hosts.length) {
    		hostNum += 1;
    		try {
    			socket = new WebSocket(hosts[hostNum]);
    		} catch (e) {}
    	    socket.onerror = function(){
    	    	createSocket();
    	    };
    	    socket.onopen = function(){
    	    	socket.oneror = function() {
					reConnectSocket();
				}
    	    };
    	    socket.onmessage = onmessage;
    	}
    }
    createSocket();
        
    // 再接続機能付きsocketにする
    reConnectSocket = function(){
        socket = new WebSocket(host);
    	socket.oneror = function() {
			reConnectSocket();
		}
	    socket.onopen = function(){
	    };
	    socket.onmessage = onmessage;
    }
}
// 次にこちらを実行
idraw.loadSessionId = function() {
    // Cookieが使えるかの処理
    if (window.navigator.cookieEnabled) {
    	sessionId = $.cookie("JSESSIONID");
    } else {
    	alert("ブラウザでCookieを有効化してください");
    }
}
// 次にこちらを実行
idraw.eventDefine = function(isMock) {
    var offset = 0;
    var fromX;
    var fromY;
    var drawFlag = false;
    var drawFlip = false;
    var context = $("canvas").get(0).getContext('2d');
    var GCO=context.globalCompositeOperation;
    var penStyle="#000000";
    var isEraser=false;
    imageBuffer = {}
    currentPage = "-1";
    cmdAccept = true;

    // Websocket受信時の処理
    commands = [
    	function(json){
	    	switch (json.cmd){
	    	case "pen":
	    		if (currentPage == json.page) {
	    			var globalCompositeOperation = context.globalCompositeOperation;
	    			var strokeStyle = context.strokeStyle;
	    			var lineWidth = isEraser ? 10 : 2;
	    			if (json.erase) {
	    		    	context.globalCompositeOperation = "destination-out";
	    		    	context.strokeStyle = "rgba(0,0,0,1)";
			            context.lineWidth = 10;
	    		    } else {
	    		    	context.globalCompositeOperation = GCO;
	    		    	context.strokeStyle = penStyle;
			            context.lineWidth = 2;
	    		    }
		            context.strokeStyle = json.color;
		            context.beginPath();
		            context.moveTo(json.fx, json.fy);
		            context.lineTo(json.tx, json.ty);
		            context.stroke();
		            context.closePath();
		            context.strokeStyle = strokeStyle;
		            context.lineWidth = lineWidth;
		            context.globalCompositeOperation = globalCompositeOperation;
	    		} else {
	    			var ctx = pagerJson[json.page]["image"].getContext('2d');
	    			if (json.erase) {
	    		    	ctx.globalCompositeOperation = "destination-out";
	    		    	ctx.strokeStyle = "rgba(0,0,0,1)";
			            ctx.lineWidth = 10;
	    		    } else {
	    		    	ctx.globalCompositeOperation = GCO;
	    		    	ctx.strokeStyle = penStyle;
			            ctx.lineWidth = 2;
	    		    }
		            ctx.strokeStyle = json.color;
		            ctx.beginPath();
		            ctx.moveTo(json.fx, json.fy);
		            ctx.lineTo(json.tx, json.ty);
		            ctx.stroke();
		            ctx.closePath();
	    		}
	    		break;
	    	}
	    }
    ];
    socket.onmessage = onmessage;

    // キー入力時の処理
    keyHookers = [
    	function (e){
	    	switch (e.key){
	    	case "ArrowUp":
	    	case "w":
	    		$("#panel_console").animate({top: '495px'},500);
	    		break;
	    	case "ArrowDown":
	    	case "s":
	    		$("#panel_console").animate({top: '600px'},500);
	    		break;
			}
	    }
    ];
    window.onkeydown = function(e){
    	if (cmdAccept) {
        	for(var i=0; i<keyHookers.length; i++){
        		keyHookers[i](e);
        	}
    	}
    }

    // textFieldフォーカス中にコマンドが効かないようにする
    $('.text_field').focusin(  function(e) { cmdAccept = false; });
    $('.text_field').focusout( function(e) { cmdAccept = true;  });

    $('canvas').mousedown(function(e) {
        drawFlag = true;
        fromX = e.pageX - $(this).offset().left - offset;
        fromY = e.pageY - $(this).offset().top - offset;
        return false;  // for chrome
    });

    $('canvas').mousemove(function(e) {
        if (drawFlag) {
        	if (drawFlip) {
                draw(e);
        	}
        	drawFlip = !drawFlip;
        }
    });

    $('canvas').on('mouseup', function() {
        drawFlag = false;
    });

    $('canvas').on('mouseleave', function() {
        drawFlag = false;
    });

    $('.palette_cell').click(function() {
    	context.globalCompositeOperation = GCO;
        context.strokeStyle = $(this).css('background-color');
        penStyle=context.strokeStyle;
        isEraser=false;
        canvas.style.cursor = "url('"+(isMock ?"../":"")+"images/pencil.png'), auto";
    });

    $('#tool_eraser').click(function() {
    	context.globalCompositeOperation = "destination-out";
    	context.strokeStyle = "rgba(0,0,0,1)";
    	isEraser=true;
    	canvas.style.cursor = "url('"+(isMock ?"../":"")+"images/eraser_cur.png'), auto";
    });

    $('#tool_pen').click(function() {
    	context.globalCompositeOperation = GCO;
    	context.strokeStyle=penStyle;
    	isEraser=false;
    	canvas.style.cursor = "url('"+(isMock ?"../":"")+"images/pencil.png'), auto";
    });

    $('#tool_clear').click(function(e) {
        e.preventDefault();
        context.clearRect(0, 0, $('canvas').width(), $('canvas').height());
    });

    $('select#select_theme').change(function() {
    	var theme=$("option:selected", theme).text();
    	var string1='tool_button ';
    	$('#panel_console').attr('class', theme);
    	$('#tool_pen').attr('class', string1.concat(theme));
    	$('#tool_text').attr('class', string1.concat(theme));
    	$('#tool_image').attr('class', string1.concat(theme));
    	$('#tool_save').attr('class', string1.concat(theme));
    	$('#tool_clear').attr('class', string1.concat(theme));
    	$('#tool_newp').attr('class', string1.concat(theme));
    	$('#tool_delp').attr('class', string1.concat(theme));
    	$('#tool_eraser').attr('class', string1.concat(theme));
    	$('#panel_timer').attr('class', theme);
    	if(theme=='Dark'){
    		$('#select_theme').attr('style','background-color:grey;');
    		$('.options').attr('style','background-color:grey;');
    		$('#timer_text').attr('style','background-color:grey;');
    		$('#timer_button').attr('style','background-color:grey;');
    		$('#theme_text').attr('style','color:grey;');
    	}else{
    		$('#select_theme').attr('style','background-color:auto;');
    		$('.options').attr('style','background-color:auto;');
    		$('#timer_text').attr('style','background-color:auto;');
    		$('#timer_button').attr('style','background-color:auto;');
    		$('#theme_text').attr('style','color:auto;');
    	}
    });

    function draw(e) {
        var toX = e.pageX - $('canvas').offset().left - offset;
        var toY = e.pageY - $('canvas').offset().top - offset;
        if(isEraser){
        	context.lineWidth = 10;
        } else {
            context.lineWidth = 2;
        }
        context.beginPath();
        context.moveTo(fromX, fromY);
        context.lineTo(toX, toY);
        context.stroke();
        context.closePath();

        // サーバへメッセージ送信
        socket.send(JSON.stringify({ cmd:"pen", page: currentPage, fx:fromX, fy:fromY, tx:toX, ty:toY, color:context.strokeStyle, erase: (isEraser ? true:false) }));
        fromX = toX;
        fromY = toY;
    }

    pasteBase64 = function(canvas, x, y, base64Image) {
    	var img = new Image();
    	img.onload = function() {
    	  //Imageをキャンバスに描画
    	  var context = canvas.getContext("2d");
    	  context.drawImage(img, x, y);
    	};
    	img.src = base64Image;
    }

    slicePushImage = function(cmd, page, text, byte) {
    	var count = Math.ceil(text.length/byte)-1;
    	var uuid = getUuid();
    	for (var i=(count)*byte; i > -1; i-=byte){
	        socket.send(JSON.stringify({cmd: cmd, page_num: page, uuid: uuid, count: count, image: text.slice(i, i+byte)}));
    		count--;
    	}
    }

    getUuid = function() {
    	var uuid = "", i, random;
    	for (i = 0; i < 32; i++) {
    		random = Math.random() * 16 | 0;
    		if (i == 8 || i == 12 || i == 16 || i == 20) {
    			uuid += "-"
    		}
    		uuid += (i == 12 ? 4 : (i == 16 ? (random & 3 | 8) : random)).toString(16);
    	}
    	return uuid;
	}

    idraw.newPage = function() {
    	socket.send(JSON.stringify({cmd: "new_page", page_num: currentPage+1}));
    }
    idraw.delPage = function() {
    	if (currentPage > 0) {
        	if(window.confirm('ページを削除しますか？(削除はすぐに他のユーザに影響します)')){
        		socket.send(JSON.stringify({cmd: "del_page", page_num: currentPage}));
        	}
    	}
    }
    
    idraw.changePage = function(pageNum) {
    	// 絵を保存
		var ctx = $("#canvas")[0].getContext("2d");
		var ctx2 = pagerJson[currentPage]["image"].getContext("2d");
		ctx2.putImageData(ctx.getImageData(0, 0, 800, 600), 0, 0);
		// タイマー保存
		$("#timer_text").val(pagerJson[pageNum]["timerSec"] != null ? pagerJson[pageNum]["timerSec"] : "タイマー");
		// 絵を読み込み
		if (pagerJson[pageNum]["image"] != null) {
			var ctx2 = pagerJson[pageNum]["image"].getContext("2d");
			ctx.putImageData(ctx2.getImageData(0, 0, 800, 600), 0, 0);
		}else{
			var ctx = $("#canvas")[0].getContext("2d");
			ctx.clearRect(0, 0, 800, 600);
		}
		// 背景を読み込み
		if (pagerJson[pageNum]["bg_image"] != null) {
			$("#panel_canvas").css("background-image", "url('" + pagerJson[pageNum]["bg_image"] + "')");
		}else{
			$("#panel_canvas").css("background-image", "");
		}
		currentPage = pageNum;
		if (currentPage > 0) {
			$("#page_num").text(currentPage);
		} else {
			$("#page_num").text("");
		}
    }

    clearText = function(str){
    	$("#"+str).val("");
    }
    
    writeTimer = function(str){
    	if($("#"+str).val() == ""){
    		$("#"+str).val("タイマー");
    	}
    }
}