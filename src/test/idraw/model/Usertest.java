package test.idraw.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

import idraw.model.User;
import idraw.orm.DbUtil;

public class Usertest{

	@Before
	public void setUp() throws ClassNotFoundException, SQLException {
		DbUtil.connect(toMap(m -> {
			m.put("env", "test");
			m.put("password", "s551107t");
			m.put("user", "root");
			m.put("host", "127.0.0.1:3306");
			m.put("db_name", "idraw");
		}));
	}
	// 簡単にMapを作る用メソッド
	public static <K, V> Map<K, V> toMap(Consumer<Map<K, V>> initializer) {
		Map<K, V> map = new LinkedHashMap<>();
		initializer.accept(map);
		return map;
	}
	@Test
	public void setNameでユーザー名が半角英数字20文字以内かどうかの判定() {
		// fail("まだ実装されていません");
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "a";// userNameをaで初期化
			boolean expected = true;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "あ";// userNameを全角ひらがなで初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "aaaaaaaaaaaaaaaaaaaaa";// userNameを20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
