package test.idraw.orm;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import idraw.model.Page;
import idraw.orm.DbUtil;

public class DbStaticDaoTest {
	@Before
	public void setUp() throws Exception {
		DbUtil.connect(toMap(m -> {
			m.put("env", "test");
			m.put("host", "127.0.0.1:3306");
			m.put("db_name", "idraw");
		}));
	}
	@After
	public void close() throws Exception {
		DbUtil.close();
	}
	// 簡単にMapを作る用メソッド
	public static <K, V> Map<K, V> toMap(Consumer<Map<K, V>> initializer) {
		Map<K, V> map = new LinkedHashMap<>();
		initializer.accept(map);
		return map;
	}
	
	@Test
	public void レコードの生成削除() throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		Page page = new Page();
		page.page_num = 1;
		page.save();
		page = Page.findBy("page_num", 1);
		assertEquals(page.page_num, 1);
		page.destroy();
		
		page = Page.findBy("page_num", 1);
		assertEquals(page, null);
	}
	
	@Test
	public void 全レコード出力() throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		Page page = new Page();
		page.page_num = 1;
		page.save();
		page = new Page();
		page.page_num = 2;
		page.save();
		
		assertEquals(Page.all().size(), 2);
		page.destroy();
		page.page_num = 1;
		page.destroy();
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

	// TODO:FindParsialとかをテストできる独自クラスをつくること
	// TODO:主キー判定とかも見る
}
