package idraw;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	static HashMap<String, String[]> imageBuffer = new HashMap<String, String[]>();

	@OnMessage // クライアントから来たJSON文字列から処理を認識、実行しJSON文字列を返却するメソッド
	public void onMessage(String message) throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, NoSuchFieldException, SecurityException, SQLException, IllegalArgumentException,
			NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		System.out.println(message);
		// JSON（文字列ベースのデータフォーマット）形式の文字列をHashMapインスタンスに＜Command名:値＞にパースする
		HashMap<String, Object> parsedJson = new ObjectMapper().readValue(message, HashMap.class);
		String cmd = (String) parsedJson.get("cmd"); // onMessageメソッドを呼び出したコマンドを選択

		switch (cmd) {

		/* ■■■■■■■■■■【コマンドが（pen）、（image）の場合】■■■■■■■■■■ */
		case "pen": // cmd = pen の場合、DBへの保存やpen自体の新規作成はないため何もしない
		case "image": // cmd = image の場合、Websocket上では何もせずクライアントへ送信
			break;

		/* ■■■■■■■■■■【コマンドが（save）の場合】■■■■■■■■■■ */
		case "save":{ // cmd = save の場合、状況により新規作成 or 上書きをする
			int pageNum = (int) parsedJson.get("page_num");
			if (parsedJson.containsKey("timer")){
				Page page = Page.findBy("page_num", pageNum);
				page.timer = (String) parsedJson.get("timer");
				page.save();
			} else if (parsedJson.containsKey("count")){
				int count     = (int) parsedJson.get("count");
				String image  = (String) parsedJson.get("image");
				String uuid   = (String) parsedJson.get("uuid");

				// 画像は分割されて届くのでバッファにためる
				if (!imageBuffer.containsKey(uuid)){
					imageBuffer.put(uuid, new String[count+1]);
				}
				String[] splittedImages = imageBuffer.get(uuid);
				splittedImages[count] = image;
	
				// バッファがたまったら保存
				if (!Arrays.asList(splittedImages).contains(null)){
					Page page = Page.findBy("page_num", pageNum);
					page.joined_image = String.join("", splittedImages);
					page.save();
					imageBuffer.remove(uuid);
				}
			}
			break;
		}
		/* ■■■■■■■■■■【コマンドが（login）の場合】■■■■■■■■■■ */
		case "login": // cmd = login の場合、ログイン判定を行う

			// 来たJSONから情報を読み取る
			String userName = (String) parsedJson.get("id");
			String pwd = (String) parsedJson.get("pwd");
			String session_id = (String) parsedJson.get("session_id");
			Boolean cmdNew = (Boolean) parsedJson.get("new");

			// ハッシュ化パスワード保存用
			String hashPwd;

			// 来たJSONがcmd=login且つ情報内にpwdとsession_idがあればusernameとpwdに合致するユーザにsession_idを付与
			if (userName != null && pwd != null && session_id != null) {
				User user = User.findBy("username", parsedJson.get("id"));

				if (user == null){
					message = "{ \"cmd\":\"error\", \"key\":\"ユーザが見つかりません\" }";
					break;
				}
				// pwdが空なら新規ユーザーなのでpwdを入れる
				if (user.pwd == null){
					// 復号化
					String tDecryptPwd = EncryptManager.getDecryptPwd(user, pwd);

					// ソルトを自動生成し、復号化したパスワードをハッシュ化
					byte[] hashSalt = EncryptManager.createSalt();
					hashPwd = EncryptManager.getHashPwd(tDecryptPwd, hashSalt);

					user.pwd = hashPwd;
					user.salt = EncryptManager.toHexString(hashSalt);
				} else { // 既存ユーザの場合ハッシュ化に使用するソルトはDB内のものを使用する
					// 復号化
					String tDecryptPwd = EncryptManager.getDecryptPwd(user, pwd);

					// 復号化したパスワードをハッシュ化
					byte[] hashSalt = DatatypeConverter.parseHexBinary(user.salt);
					hashPwd = EncryptManager.getHashPwd(tDecryptPwd, hashSalt);
				}
				if (!user.pwd.equals(hashPwd)){
					message = "{ \"cmd\":\"error\", \"key\":\"ユーザIDとPWの組み合わせが間違っています\" }";
					break;
				}

				user.session_id = (String) parsedJson.get("session_id");
				user.save();
				message = null;

			} else { // 来たJSON内にidはあるがpwd,session_id情報が無ければpublicKeyを返す
				User user = User.findBy("username", parsedJson.get("id"));
				if (user == null) {
					// ユーザが無く、newフラグも無ければエラー
					if (cmdNew != true) {
						message = "{ \"cmd\":\"error\", \"key\":\"ユーザIDが見つかりません\" }";
						break;
					}

					// ユーザの新規作成をするための処理
					user = new User();
					user.username = userName;
					user.save();
				}

				// newでもnewじゃなくてもPublicKeyを作成し”key”として返却するための処理
				String publicKey = EncryptManager.generateKeyPair(user);
				message = mapToJsonString(m -> {
					m.put("cmd", "pubkey");
					m.put("id",  userName);
					m.put("key", publicKey);
				});
				break;
			}
			break;

		/* ■■■■■■■■■■【コマンドが（bgsave）の場合】■■■■■■■■■■ */
		case "bgsave":{
			int pageNum = (int) parsedJson.get("page_num");
			int count     = (int) parsedJson.get("count");
			String image  = (String) parsedJson.get("image");
			String uuid   = (String) parsedJson.get("uuid");
			if (image == null) { // 値が正しくない際はエラーメッセージを表示
				message = "{ \"cmd\":\"error\", \"key\":\"BGイメージがnullです\" }";
			} else { // 値が適切であればBGイメージを保存
				// 画像は分割されて届くのでバッファにためる
				if (!imageBuffer.containsKey(uuid)){
					imageBuffer.put(uuid, new String[count+1]);
				}
				String[] splittedImages = imageBuffer.get(uuid);
				splittedImages[count] = image;

				// バッファがたまったら保存
				if (!Arrays.asList(splittedImages).contains(null)){
					Page bg = Page.findBy("page_num", pageNum);
					bg.background_image = String.join("", splittedImages);
					bg.save();
					imageBuffer.remove(uuid);
				}
			}
			break;
		}
		/* ■■■■■■■■■■【コマンドが（chat）の場合】■■■■■■■■■■ */
		case "chat":
			String chatName = "匿名";
			String sessionId = (String) parsedJson.get("session");
			String chatMessage = (String) parsedJson.get("message");
			User user = User.findBy("session_id", sessionId);
			if (user != null) {
				chatName = ""+user.username;
			}
			String chatText = "　"+chatName +"さん：" + chatMessage;
			message = mapToJsonString(m -> {
				m.put("cmd", "chat");
				m.put("text", chatText);
			});
			break;
		/* ■■■■■■■■■■【コマンドが（new_page）の場合】■■■■■■■■■■ */
		case "new_page":{
			int pageNum = (int) parsedJson.get("page_num");
			Page page = Page.findBy("page_num", pageNum);
			if (page != null){
				ArrayList<Page> pages = Page.all();
				HashMap<Integer, Page> pageMap = new HashMap<Integer, Page>();
				int maxPageNum = -255;
				for(Page page_: pages){
					if (maxPageNum < page_.page_num){
						maxPageNum = page_.page_num;
					}
					pageMap.put(page_.page_num, page_);
				}
				Page lastPage = pageMap.get(maxPageNum);
				lastPage.page_num = maxPageNum+1;
				lastPage.newFlag = true;
				lastPage.save();
				for(int i = maxPageNum-1; pageMap.get(i) != null; i--){
					Page page_ = pageMap.get(i);
					if (page.page_num <= page_.page_num){
						page_.page_num += 1;
						page_.save();
					}
				}
				page.background_image = null;
				page.joined_image = null;
			}else{
				page = new Page();
				page.page_num = pageNum;
			}
			page.save();
			break;
		}
		/* ■■■■■■■■■■【コマンドが（del_page）の場合】■■■■■■■■■■ */
		case "del_page":{
			int pageNum = (int) parsedJson.get("page_num");
			Page page = Page.findBy("page_num", pageNum);
			if (page != null){
				ArrayList<Page> pages = Page.all();
				HashMap<Integer, Page> pageMap = new HashMap<Integer, Page>();
				int maxPageNum = -255;
				for(Page page_: pages){
					if (maxPageNum < page_.page_num){
						maxPageNum = page_.page_num;
					}
					pageMap.put(page_.page_num, page_);
				}
				for(int i = pageNum+1; pageMap.get(i) != null; i++){
					Page page_ = pageMap.get(i);
					if (page.page_num <= page_.page_num){
						page_.page_num -= 1;
						page_.save();
					}
				}
				page.page_num = maxPageNum;
				page.destroy();
			}
			break;
		}
		}

		// messageがnullならセッションを無駄に消費するだけなのでreturnで返す
		if (message == null) {
			return;
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
		synchronized (sessions) {
			if (sessions.isEmpty()) { // 誰も接続していない状況ならDBへの接続を開始する
				DbUtil.connect(toMap(m -> {
					m.put("env", "production");
					m.put("host", "127.0.0.1:3306");
					m.put("db_name", "idraw");
				}));
			}
			sessions.add(sess);
		}
	}

	@OnClose // 接続済みのユーザをセッションから除外するメソッド
	public void close(Session sess) throws SQLException {
		synchronized (sessions) {
			sessions.remove(sess);
			if (sessions.isEmpty()) { // 最後のユーザがセッションから外れた時にDBを閉じる
				DbUtil.close();
			}
		}
	}

	// 簡単にMapを作る用メソッド
	public static <K, V> Map<K, V> toMap(Consumer<Map<K, V>> initializer) {
		Map<K, V> map = new LinkedHashMap<>();
		initializer.accept(map);
		return map;
	}

	// JSON(map)を与えることでJSON(String)を返却するメソッド
	public static <K, V> String mapToJsonString(Consumer<Map<K, V>> initializer) throws JsonProcessingException {
		Map<K, V> map = new LinkedHashMap<>();
		initializer.accept(map);

		ObjectMapper om = new ObjectMapper();
		return om.writeValueAsString(map);
	}
}