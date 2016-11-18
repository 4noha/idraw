package idraw;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 */
@ServerEndpoint("/endpoint")
public class WebsocketEndpoint {
    static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnMessage
    public void onMessage(String message) throws IOException {
    	////
    	//    送信以外の処理はこのへん(Synchronizedの外)で書きます
    	////
    	//JSON（文字列ベースのデータフォーマット）形式の文字列をHashMapインスタンスに＜Command名:値＞にパースする 2016/11/18高田追加
    	HashMap<String, Object> parsedJson = new ObjectMapper().readValue(message, HashMap.class);


		// 送信中はセマフォで他のプロセスにアクセスされないようにする
    	synchronized(sessions) {
    		System.out.println("セッション数(" + sessions.size() + ")" + message);
    		// 接続ごとに送る
	        for(Session s : sessions){
	            s.getBasicRemote().sendText(message);
	    	}
        }
    }

    @OnOpen
    public void open(Session sess){
        sessions.add(sess);
    }
    @OnClose
    public void close(Session sess){
        sessions.remove(sess);
    }
}