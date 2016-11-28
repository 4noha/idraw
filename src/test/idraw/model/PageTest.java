package test.idraw.model;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import idraw.model.Page;
import idraw.orm.DbStaticDao;
import idraw.orm.DbUtil;

public class PageTest {
	@Before
	public void setUp() throws Exception {
		DbUtil.connect(DbStaticDao.toMap(m -> {
			m.put("env", "test");
			m.put("host", "127.0.0.1:3306");
			m.put("db_name", "idraw");
		}));
	}
	@After
	public void close() throws Exception {
		DbUtil.close();
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
	
	// 他の機能はテストされているので生成の際の型変換ができれば十分
	// TODO:キャストの動きを見るコードが必要
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
}
