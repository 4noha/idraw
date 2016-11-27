package test.idraw.orm;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

import idraw.orm.DbUtil;

public class DbUtilTest {

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
	public void 正常にDBを切断出来るかの確認() {
		try {
			DbUtil.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
