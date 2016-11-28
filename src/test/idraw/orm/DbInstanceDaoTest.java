package test.idraw.orm;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import idraw.model.Page;
import idraw.orm.DbInstanceDao;
import idraw.orm.DbStaticDao;
import idraw.orm.DbUtil;

public class DbInstanceDaoTest {
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
	public void レコードの更新処理() throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		Page page = new Page();
		page.page_num = 1;
		page.save();
		page = Page.findBy("page_num", 1);
		page.joined_image = "aaa";
		page.save();
		page = Page.findBy("page_num", 1);
		assertEquals(page.joined_image, "aaa");
		page.destroy();
	}
	
	@Test
	public void テーブルの主キーの取得() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
		Method modelMethod = DbInstanceDao.class.getDeclaredMethod("getPrimaryKey", null);
		modelMethod.setAccessible(true);
		assertEquals((String) modelMethod.invoke(new Model(), null), "page_num");
	}
}
