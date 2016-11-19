package idraw;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
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

import idraw.manager.EncryptManager;
import idraw.model.Page;
import idraw.model.User;
import idraw.orm.DbUtil;

/**
 *
 */
@ServerEndpoint("/endpoint")
public class WebsocketEndpoint {
	static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

	@OnMessage //
	public void onMessage(String message) throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, NoSuchFieldException, SecurityException, SQLException, IllegalArgumentException,
			NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException {
		////
		// 送信以外の処理はこのへん(Synchronizedの外)で書きます
		////

		// JSON（文字列ベースのデータフォーマット）形式の文字列をHashMapインスタンスに＜Command名:値＞にパースする
		HashMap<String, Object> parsedJson = new ObjectMapper().readValue(message, HashMap.class);
		String cmd = (String) parsedJson.get("cmd"); // onMessageメソッドを呼び出したコマンドを選択

		switch (cmd) {
		case "pen": // cmd = pen の場合、DBへの保存やpen自体の新規作成はないため何もしない
			break;

		case "save": // cmd = save の場合、状況により新規作成 or 上書きをする
			Page page = Page.findBy("page_num", parsedJson.get("page"));
			if (page == null) { // pageが何も無ければ新規作成する
				page = new Page(toMap(m -> {
					m.put("page", parsedJson.get("page"));
					m.put("joined_image", parsedJson.get("image"));
				}));
			} else { // pageが既にあれば上書保存する
				page.joined_image = (String) parsedJson.get("image");
			}
			page.save();
			break;

		case "login": // cmd = login の場合、ログイン判定を行う
			String userName = (String) parsedJson.get("id");
			boolean cmdNew = (boolean) parsedJson.get("new");
			User user = null;
			if (cmdNew == true) { //ユーザの新規作成をするための処理
				user = new User();
				user.username = userName;
				user.save();
			} else { //ユーザ検索の結果ユーザが存在した場合にPublicKeyを作成し”key”として返却するための処理
				user = User.findBy("username", parsedJson.get("id"));
				if (user == null) {
					message = "{ \"cmd\":\"error\", \"key\":\"IDが見つかりません\" }";
					break;
				}
			}
			String publicKey = EncryptManager.generateKeyPair(user);
			Map<Object, Object> json = toMap(m -> {
				m.put("cmd", "pubkey");
				m.put("key", publicKey);
			});
			ObjectMapper om = new ObjectMapper();
			message = om.writeValueAsString(json);
			break;

		case "image": // cmd = image の場合、Websocket上では何もせずクライアントへ送信
			break;

		case "session": // cmd = session の場合、ユーザを検索し、ユーザが見つかればページを与える
			User sessionUser = User.findBy("secret_key", parsedJson.get("id"));
			if (sessionUser == null) {
				message = "{ \"cmd\":\"error\", \"key\":\"ユーザが見つかりません\" }";
			}else{
				//ページを返す処理を記載予定
			}
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

	@OnOpen // 接続したユーザをセッションに加えるメソッド
	public void open(Session sess) throws ClassNotFoundException, SQLException {
		if (sessions.isEmpty()) { // 誰も接続していない状況ならDBへの接続を開始する
			DbUtil.connect(toMap(m -> {
				m.put("env", "production");
				m.put("host", "127.0.0.1:3306");
				m.put("db_name", "idraw");
			}));
		}
		sessions.add(sess);
	}

	@OnClose // 接続済みのユーザをセッションから除外するメソッド
	public void close(Session sess) throws SQLException {
		sessions.remove(sess);
		if (sessions.isEmpty()) { // 最後のユーザがセッションから外れた時にDBを閉じる
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