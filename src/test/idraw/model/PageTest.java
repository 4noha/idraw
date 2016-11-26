package test.idraw.model;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

import idraw.model.Page;
import idraw.orm.DbUtil;

public class PageTest {
	@Before
	public void setUp() throws Exception {
		DbUtil.connect(toMap(m -> {
			m.put("env", "test");
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
	public void ORMの書式が正しいかチェック_テーブル設定() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field pageField = Page.class.getDeclaredField("TABLE_NAME");
		pageField.setAccessible(true);
		assertEquals(pageField.get(null), "page");
	}

	@Test
	public void ORMの書式が正しいかチェック_クラス指定() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field pageField = Page.class.getDeclaredField("THIS_CLASS");
		pageField.setAccessible(true);
		assertEquals(pageField.get(null), Page.class);
	}

	@Test
	public void テーブルのカラム名一覧の取得() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
		Method pageMethod = Page.class.getDeclaredMethod("getColumnNames", null);
		ArrayList<String> answerColumnNames = new ArrayList<String>(Arrays.asList("page_num", "joined_image", "background_image", "timer"));
		ArrayList<String> columnNames = (ArrayList<String>) pageMethod.invoke(new Page(), null);
		for (int i=0; i < columnNames.size(); i++){
			assertEquals(answerColumnNames.get(i), columnNames.get(i));
		}
	}
}
