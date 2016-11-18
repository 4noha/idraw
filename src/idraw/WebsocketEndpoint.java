package idraw;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import idraw.model.Page;
import idraw.orm.DbUtil;

/**
 *
 */
@ServerEndpoint("/endpoint")
public class WebsocketEndpoint {
	static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

	@OnMessage
	public void onMessage(String message) throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, NoSuchFieldException, SecurityException, SQLException, IllegalArgumentException,
			NoSuchMethodException, InvocationTargetException {
		////
		// 送信以外の処理はこのへん(Synchronizedの外)で書きます
		////
		// JSON（文字列ベースのデータフォーマット）形式の文字列をHashMapインスタンスに＜Command名:値＞にパースする
		// 2016/11/18高田追加
		HashMap<String, Object> parsedJson = new ObjectMapper().readValue(message, HashMap.class);
		String cmd = (String) parsedJson.get("cmd"); // onMessageメソッドを呼び出したコマンドを選択

		switch (cmd) {
		case "pen": //cmd = pen なら何も保存する必要はない

			break;

		case "save":
			Page page = Page.findBy("page_num", parsedJson.get("page"));
			if (page == null) {
				page = new Page(toMap(m -> {
					m.put("page", parsedJson.get("page"));
					m.put("joined_image", parsedJson.get("image"));
				}));
			} else {
				page.joined_image = (String) parsedJson.get("image");
			}
			page.save();
			break;

		default:
			break;
		}

		// 送信中はセマフォで他のプロセスにアクセスされないようにする
		synchronized (sessions) {
			System.out.println("セッション数(" + sessions.size() + ")" + message);
			// 接続ごとに送る
			for (Session s : sessions) {
				s.getBasicRemote().sendText(message);
			}
		}
	}

	@OnOpen
	public void open(Session sess) throws ClassNotFoundException, SQLException {
		if (sessions.isEmpty()) {
			DbUtil.connect(toMap(m -> {
				m.put("env", "production");
				m.put("host", "127.0.0.1:3306");
			}));
		}
		sessions.add(sess);
	}

	@OnClose
	public void close(Session sess) throws SQLException {
		sessions.remove(sess);
		if (sessions.isEmpty()) {
			DbUtil.close();
		}
	}

	// 簡単にMapを作る用メソッド
	public static <K, V> Map<K, V> toMap(Consumer<Map<K, V>> initializer) {
		Map<K, V> map = new LinkedHashMap<>();
		initializer.accept(map);
		return map;
	}
}