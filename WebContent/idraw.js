$(function() {
    var offset = 5;
    var fromX;
    var fromY;
    var drawFlag = false;
    var drawFlip = false;
    var context = $("canvas").get(0).getContext('2d');
    var host="ws://localhost:8080/idraw/endpoint";
    socket = new WebSocket(host);
 
    // Websocket受信時の処理
    socket.onmessage = function(msg){
        var json = $.parseJSON(msg.data);
    	console.log(json);
    	switch (json.cmd){
    	case "pen":
            context.strokeStyle = json.color;
            context.lineWidth = 2;
            context.beginPath();
            context.moveTo(json.fx, json.fy);
            context.lineTo(json.tx, json.ty);
            context.stroke();
            context.closePath(); 
    		break;
    	case "save":
    		break;
    	}
    }
    
    // キー入力時の処理
    document.onkeydown = function (e){
    	switch (e.key){
    	case "ArrowUp":
    	case "w":
    		// 上に移動
    		setPositionById("canvas");
    		break;
    	case "ArrowDown":
    	case "s":
    		// 下に移動
    		setPositionById("console");
    		break;
    	}
    };
    
    /*
    socket.on('clear user', function () {
        context.clearRect(0, 0, $('canvas').width(), $('canvas').height());
    });*/
 
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
 
    $('li').click(function() {
        context.strokeStyle = $(this).css('background-color');
    });
 
    $('#clear').click(function(e) {
        //socket.emit('clear send');
        e.preventDefault();
        context.clearRect(0, 0, $('canvas').width(), $('canvas').height());
    });
 
    $('#save').click(function() {
        socket.send(JSON.stringify({ cmd:"save", page:1, image: canvasToMinimizeBase64($("#canvas")[0])}));
    });

    function draw(e) {
        var toX = e.pageX - $('canvas').offset().left - offset;
        var toY = e.pageY - $('canvas').offset().top - offset;
        context.lineWidth = 2;
        context.beginPath();
        context.moveTo(fromX, fromY);
        context.lineTo(toX, toY);
        context.stroke();
        context.closePath();
 
        // サーバへメッセージ送信
        socket.send(JSON.stringify({ cmd:"pen", page: 1, fx:fromX, fy:fromY, tx:toX, ty:toY, color:context.strokeStyle }));
        fromX = toX;
        fromY = toY;
    }
    
    // IDのタグの位置に移動
    function setPositionById(id) {
		var element = document.getElementById(id);
		var rect = element.getBoundingClientRect();
		var positionX = rect.left + window.pageXOffset;	// 要素のX座標
		var positionY = rect.top + window.pageYOffset;	// 要素のY座標

		// 要素の位置にスクロールさせる
		window.scrollTo( positionX, positionY );
    }

    canvasToMinimizeBase64 = function(canvas) {
        var ctx = canvas.getContext('2d');
        // 小さいキャンバスを作成
        // 800x600の解像度だとJavascriptのWebsocketで送れるデータ量上限に引っかかって送れない
		// 苦肉の策として同じ4:3の576x432にした
        var canvas2 = document.createElement('canvas');
        canvas2.width = 576;
        canvas2.height = 432;
        var ctx2 = canvas2.getContext('2d');
        // 元のキャンバスを縮小コピー
        ctx2.drawImage(canvas, 0, 0, canvas2.width, canvas2.height);
        return canvas2.toDataURL("image/png");
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
});