package idraw;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

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