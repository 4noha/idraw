package test.idraw.orm;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import idraw.orm.DbStaticDao;
import idraw.orm.DbUtil;

public class DbStaticDaoTest {
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
	public void レコードの生成削除() throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		Model page = new Model();
		page.page_num = 1;
		page.save();
		page = Model.findBy("page_num", 1);
		assertEquals(page.page_num, 1);
		page.destroy();
		
		page = Model.findBy("page_num", 1);
		assertEquals(page, null);
	}
	
	@Test
	public void 全レコード出力() throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		Model page = new Model();
		page.page_num = 1;
		page.save();
		page = new Model();
		page.page_num = 2;
		page.save();
		
		assertEquals(Model.all().size(), 2);
		page.destroy();
		page.page_num = 1;
		page.destroy();
	}
	
	@Test
	public void テーブルのカラム名一覧の取得() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
		Method pageMethod = Model.class.getDeclaredMethod("getColumnNames", null);
		ArrayList<String> answerColumnNames = new ArrayList<String>(Arrays.asList("page_num", "joined_image", "background_image", "timer"));
		ArrayList<String> columnNames = (ArrayList<String>) pageMethod.invoke(new Model(), null);
		for (int i=0; i < columnNames.size(); i++){
			assertEquals(answerColumnNames.get(i), columnNames.get(i));
		}
	}

	public void 一カラムを検索するのときのfind() throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		Model page = new Model();
		page.page_num = 1;
		page.joined_image = "aaa";
		page.save();
		page = new Model();
		page.page_num = 2;
		page.joined_image = "aaa";
		page.save();
		
		assertEquals(Model.find("joined_image", "aaa").size(), 2);
		page.destroy();
		page.page_num = 1;
		page.destroy();
	}
	
	public void 複数カラムを検索するのときのfind() throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		Model page = new Model();
		page.page_num = 1;
		page.joined_image = "aaa";
		page.background_image = "eee";
		page.save();
		page = new Model();
		page.page_num = 2;
		page.joined_image = "aaa";
		page.background_image = "eee";
		page.save();
		
		assertEquals(
			Model.find(DbStaticDao.toMap(m -> {
						m.put("joined_image", "aaa");
						m.put("background_image", "eee");
					})).size(), 2);
		page.destroy();
		page.page_num = 1;
		page.destroy();
	}
	
	public void 部分一致検索() throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		Model page = new Model();
		page.page_num = 1;
		page.joined_image = "aaa";
		page.save();
		page = new Model();
		page.page_num = 2;
		page.joined_image = "abc";
		page.save();
		
		assertEquals(Model.findPartial("joined_image", "a").size(), 2);
		page.destroy();
		page.page_num = 1;
		page.destroy();
	}
	
	@Test
	public void valueのセットを行うsetValue() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
		String sql = "?";
		PreparedStatement stmt = DbUtil.con.prepareStatement(sql);
		// String型
		DbStaticDao.setValue(stmt, 1, "aaa");
		// int型
		DbStaticDao.setValue(stmt, 1, 1);
		// null
		DbStaticDao.setValue(stmt, 1, null);
		// TimeStamp型
		DbStaticDao.setValue(stmt, 1, new Timestamp(System.currentTimeMillis()));
	}
}
