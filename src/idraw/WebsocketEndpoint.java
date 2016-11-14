package idraw;

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
    public void onMessage(String message) {
        for(Session s : sessions){
        	// おまじない
        	synchronized(s) {
                s.getAsyncRemote().sendText(message);
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