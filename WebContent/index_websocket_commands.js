idraw.indexWebsocketCommands = function(){
    // indexしか使わないコマンド
    commands.push(function (json){
    	switch (json.cmd){
    	case "pen":
    		pagerJson[currentPage]["modified"] = true;
    		if (currentPage == json.page) {
        		$("#modified").css("opacity", 0.2);
    		}
    		break;
    	case "chat":
    		$("#chat_window").html($("#chat_window").html()+"<br>"+json.text);
    		break;
    	case "page_shift":
    		if (presenMode) {
    			idraw.changePage(json.page_num);
    		}
    		break;
    	case "bgsave":
    		if (imageBuffer[json.uuid] === undefined){
    			imageBuffer[json.uuid] = new Array(json.count);
    		}
    		imageBuffer[json.uuid][json.count] = json.image;

    		// バッファがたまったら保存
			if (!imageBuffer[json.uuid].includes(undefined)){
				var url=imageBuffer[json.uuid].join("");
				pagerJson[currentPage]["bg_image"] = url.replace(/(\r\n|\n|\r)/gm, "")
				$("#panel_canvas").css("background-image", "url('" + pagerJson[currentPage]["bg_image"] + "')");
				delete imageBuffer[json.uuid];
			}
    		break;
    	case "save":
    		pagerJson[json.page_num]["modified"] = false;
    		if (currentPage == json.page_num) {
    			$("#modified").css("opacity", 0);
    		}
    		break;
    	case "new_page":{
        	var newPager = {};
        	for (var pageNum in pagerJson){
        		if (pageNum > json.page_num-1){
        			newPager[Number(pageNum)+1] = pagerJson[pageNum];
        		}else{
        			newPager[pageNum] = pagerJson[pageNum];
        		}
        	}
            var canvas = $("<canvas/>")[0];
            canvas.width = 800;
            canvas.height = 600;
        	newPager[json.page_num] = {
        			bg_image: canvas.toDataURL("image/png"),
        			image: canvas.toDataURL("image/png"),
        			timerSec: null, modified: false
        	}
        	if (json.page_num <= currentPage){
    			idraw.changePage(currentPage+1);
        	}
        	pagerJson = newPager;
        	break;
    	}
    	case "del_page":{
        	var newPager = {};
        	for (var pageNum in pagerJson){
        		if (pageNum > json.page_num){
        			newPager[Number(pageNum)-1] = pagerJson[pageNum];
        		}else{
        			if(pageNum != json.page_num){
        				newPager[pageNum] = pagerJson[pageNum];
        			}
        		}
        	}
        	if (currentPage == json.page_num) {
    			idraw.changePage(currentPage-1);
        	}
        	pagerJson = newPager;
    		break;
    	}
    	}
    });
};
