package test.idraw.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import idraw.model.User;
import idraw.orm.DbStaticDao;
import idraw.orm.DbUtil;

public class UserTest {

	@Before
	public void setUp() throws ClassNotFoundException, SQLException {
		DbUtil.connect(DbStaticDao.toMap(m -> {
			m.put("env", "test");
			m.put("db_name", "idraw");
		}));
	}

	@Test
	public void ORMの書式が正しいかチェック_テーブル設定() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field userField = User.class.getDeclaredField("TABLE_NAME");
		userField.setAccessible(true);
		assertEquals(userField.get(null), "user");
	}

	@Test
	public void ORMの書式が正しいかチェック_クラス指定() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field userField = User.class.getDeclaredField("THIS_CLASS");
		userField.setAccessible(true);
		assertEquals(userField.get(null), User.class);
	}

	@Test
	public void テーブルのカラム名一覧の取得() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
		Method userMethod = User.class.getDeclaredMethod("getColumnNames", null);
		ArrayList<String> answerColumnNames = new ArrayList<String>(Arrays.asList("username", "pwd", "salt", "secret_key", "session_id"));
		ArrayList<String> columnNames = (ArrayList<String>) userMethod.invoke(new User(), null);
		for (int i=0; i < columnNames.size(); i++){
			assertEquals(answerColumnNames.get(i), columnNames.get(i));
		}
	}
	
	// 他の機能はテストされているので生成の際の型変換ができれば十分
	// TODO:キャストの動きを見るコードが必要
	@Test
	public void レコードの生成削除() throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		User user = new User();
		user.username = "aaa";
		user.save();
		user = User.findBy("username", "aaa");
		assertEquals(user.username, "aaa");
		user.destroy();
		
		user = User.findBy("username", "aaa");
		assertEquals(user, null);
	}

	@Test
	public void setNameでユーザー名が半角英数字20文字以内かどうかの判定() {
		// fail("まだ実装されていません");
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "a";// userNameを半角英字小文字1文字で初期化
			boolean expected = true;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "aaaaaaaaaaaaaaaaaaaa";// userNameを半角英字小文字20文字で初期化
			boolean expected = true;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "aaaaaaaaaaaaaaaaaaaaa";// userNameを半角英小文字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "ａ";// userNameを全角英小文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "ａａａａａａａａａａａａａａａａａａａａ";// userNameを全角英小文字20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "ａａａａａａａａａａａａａａａａａａａａａ";// userNameを全角英小文字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "A";// userNameを半角英字大文字1文字で初期化
			boolean expected = true;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "AAAAAAAAAAAAAAAAAAAA";// userNameを半角英字大文字20文字で初期化
			boolean expected = true;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "AAAAAAAAAAAAAAAAAAAAA";// userNameを半角英大文字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "Ａ";// userNameを全角英大文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "ＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡ";// userNameを全角英大文字20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "ＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡＡ";// userNameを全角英大文字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "あ";// userNameを全角ひらがなで初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "ああああああああああああああああああああ";// userNameを全角ひらがな20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "あああああああああああああああああああああ";// userNameを全角ひらがな20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "1";// userNameを半角数字1文字で初期化
			boolean expected = true;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "11111111111111111111";// userNameを半角数字20文字で初期化
			boolean expected = true;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "111111111111111111111";// userNameを半角数字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１";// userNameを全角数字1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１１１１１１１１１１１１１１１１１１１１";// userNameを全角数字20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１１１１１１１１１１１１１１１１１１１１１";// userNameを全角数字20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "ア";// userNameを全角カタカナ1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "アアアアアアアアアアアアアアアアアアアア";// userNameを全角カタカナ20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "アアアアアアアアアアアアアアアアアアアアア";// userNameを全角カタカナ20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "ｱ";// userNameを半角カタカナ1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "ｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱ";// userNameを半角カタカナ20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "ｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱ";// userNameを半角カタカナ20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "亜";// userNameを全角漢字1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "亜亜亜亜亜亜亜亜";// userNameを全角漢字8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "亜亜亜亜亜亜亜亜亜";// userNameを全角漢字8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "aA";// userNameを半角英字小文字大文字1文字ずつで初期化
			boolean expected = true;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "aAaAaAaAaAaAaAaAaAaA";// userNameを半角英字小文字大文字20文字で初期化
			boolean expected = true;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "aAaAaAaAaAaAaAaAaAaAa";//  userNameを半角英字小文字大文字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "aA1";// userNameを半角英字大文字小文字数字1文字ずつで初期化
			boolean expected = true;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "aA1aA1aA1aA1aA1aA1aA";// userNameを半角英字大文字小文字数字20文字で初期化
			boolean expected = true;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "aA1aA1aA1aA1aA1aA1aA1";// userNameを半角英字大文字小文字数字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1";// userNameを全角数字半角数字1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1１1１1１1１1１1１1１1１1１1";// userNameを全角数字半角数字20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1１1１1１1１1１1１1１1１1１1１";// userNameを全角数字半角数字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1a";// userNameを全角数字半角数字英小文字1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1a１1a１1a１1a１1a１1a１1";// userNameを全角数字半角数字英小文字20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1a１1a１1a１1a１1a１1a１1a";// userNameを全角数字半角数字英小文字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aA";// userNameを全角数字半角数字英小文字大文字1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aA１1aA１1aA１1aA１1aA";// userNameを全角数字半角数字英小文字大文字20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aA１1aA１1aA１1aA１1aA１";// userNameを全角数字半角数字英小文字大文字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあ";// userNameを全角数字半角数字英小文字大文字全角ひらがな1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあ１1aAあ１1aAあ１1aAあ";// userNameを全角数字半角数字英小文字大文字全角ひらがな20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあ１1aAあ１1aAあ１1aAあ１";// userNameを全角数字半角数字英小文字大文字全角ひらがな20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱ";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角カタカナ1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱ１1aAあｱ１1aAあｱ１1";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角カタカナ20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱ１1aAあｱ１1aAあｱ１1a";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角カタカナ20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱア";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角全角カタカナ1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱア１1aAあｱア１1aAあｱ";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角全角カタカナ20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱア１1aAあｱア１1aAあｱア";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角全角カタカナ20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱアａ";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角全角カタカナ全角英小文字1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱアａ１1aAあｱアａ１1aA";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角全角カタカナ全角英小文字20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱアａ１1aAあｱアａ１1aAあ";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角全角カタカナ全角英小文字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱアａＡ";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角全角カタカナ全角英小文字大文字1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱアａＡ１1aAあｱアａＡ１1";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角全角カタカナ全角英小文字大文字20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱアａＡ１1aAあｱアａＡ１1a";// userNameを全角数字半角数字英小文字大文字全角ひらがな半角全角カタカナ全角英小文字大文字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱアａＡ亜";// userNameを半角数字英小文字大文字半角全角ひらがなカタカナ数字英小文字大文字漢字1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱアａＡ亜１1aAあｱアａＡ亜";// userNameを半角数字英小文字大文字半角全角ひらがなカタカナ数字英小文字大文字漢字20文字で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User user = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userName = "１1aAあｱアａＡ亜１1aAあｱアａＡ亜１";// userNameを半角数字英小文字大文字半角全角ひらがなカタカナ数字英小文字大文字漢字20文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = user.setName(userName);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setPassでパスワードが英数字混合8字以上かどうかの判定() {
		// fail("まだ実装されていません");
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "a";// userNameを半角英小文字1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aaaaaaaa";// userNameを半角英小文字8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aaaaaaaaa";// userNameを半角英小文字8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "ａ";// userNameを全角英小文字1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "ａａａａａａａａ";// userNameを全角英小文字8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "ａａａａａａａａａ";// userNameを全角英小文字8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "A";// userNameを半角英大文字1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "AAAAAAAA";// userNameを半角英大文字8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "AAAAAAAAA";// userNameを半角英大文字9文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "Ａ";// userNameを全角英大文字1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "ＡＡＡＡＡＡＡＡ";// userNameを全角英大文字8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "ＡＡＡＡＡＡＡＡＡ";// userNameを全角英大文字9文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "あ";// userNameを全角ひらがな1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "ああああああああ";// userNameを全角ひらがな8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "あああああああああ";// userNameを全角ひらがな8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "ｱ";// userNameを半角カタカナ1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "ｱｱｱｱｱｱｱｱ";// userNameを半角カタカナ8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "ｱｱｱｱｱｱｱｱｱ";// userNameを半角カタカナ8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "ア";// userNameを半角カタカナ1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "アアアアアアアア";// userNameを半角カタカナ8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "アアアアアアアアア";// userNameを半角カタカナ8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "1";// userNameを半角数字1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "11111111";// userNameを半角数字8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "111111111";// userNameを半角数字8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "１";// userNameを半角数字1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "１１１１１１１１";// userNameを半角数字8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "１１１１１１１１１";// userNameを半角数字8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "亜";// userNameを全角漢字1文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "亜亜亜亜亜亜亜亜";// userNameを全角漢字8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "亜亜亜亜亜亜亜亜亜";// userNameを全角漢字9文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "1a";// userNameを半角英数字混合1文字ずつで初期化
			boolean expected = true;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "1a1a1a1a";// userNameを半角英数字混合8文字で初期化
			boolean expected = true;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "1a1a1a1a1";// userNameを半角英数字混合9文字で初期化
			boolean expected = true;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "1aA";// userNameを半角英小文字大文字数字混合1文字ずつで初期化
			boolean expected = true;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "1aA1aA1a";// userNameを半角英小文字大文字数字混合8文字で初期化
			boolean expected = true;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "1aA1aA1aA";// userNameを半角英小文字大文字数字混合8文字以上で初期化
			boolean expected = true;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA";// userNameを半角英小文字大文字混合1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aAaAaAaA";// userNameを半角英小文字大文字混合8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aAaAaAaAa";// userNameを半角英小文字大文字混合8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１";// userNameを半角英数字全角数字混合1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１aA1１";// userNameを半角英数字全角数字混合8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１aA1１a";// userNameを半角英数字全角数字混合8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あ";// userNameを半角英数字全角数字全角ひらがな混合1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あaA1";// userNameを半角英数字全角数字全角ひらがな混合8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あaA1１";// userNameを半角英数字全角数字全角ひらがな混合8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あア";// userNameを半角英数字全角数字全角ひらがなカタカナ混合1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あアaA";// userNameを半角英数字全角数字全角ひらがなカタカナ混合8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あアaA1";// userNameを半角英数字全角数字全角ひらがなカタカナ混合9文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あアｱ";// userNameを半角英数字カタカナ全角数字全角ひらがなカタカナ混合1文字ずつで初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あアｱa";// userNameを半角英数字カタカナ全角数字全角ひらがなカタカナ混合8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あアｱaA";// userNameを半角英数字カタカナ全角数字全角ひらがなカタカナ混合8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あアｱ亜";// userNameを半角英数字カタカナ全角数字全角ひらがなカタカナ漢字混合8文字で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あアｱ亜a";// userNameを半角英数字カタカナ全角数字全角ひらがなカタカナ漢字混合8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あアｱ亜ａ";// userNameを半角英数字カタカナ全角数字全角ひらがなカタカナ漢字混合8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			User pwd = new User();// インスタンスuserを作成Userコンストラクタ呼び出し
			String userPass = "aA1１あアｱ亜ａＡ";// userNameを半角英数字カタカナ全角英数字全角ひらがなカタカナ漢字混合8文字以上で初期化
			boolean expected = false;
			boolean actual;
			actual = pwd.setPass(userPass);
			assertThat(actual, is(expected));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
