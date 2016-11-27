package test.idraw.orm;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.Test;

import idraw.orm.DbUtil;

public class DbUtilTest {
	@Test
	public void 正常にTestのDBに接続切断出来るかの確認() throws ClassNotFoundException, SQLException {
		DbUtil.connect(toMap(m -> {
			m.put("env", "test");
			m.put("db_name", "idraw");
		}));
		DbUtil.close();
		// 正常終了すればOK
		assertEquals(true, true);
	}

	@Test
	public void 正常にProductionのDBに接続切断出来るかの確認() throws ClassNotFoundException, SQLException {
		DbUtil.connect(toMap(m -> {
			m.put("env", "production");
			m.put("db_name", "idraw");
		}));
		DbUtil.close();
		// 正常終了すればOK
		assertEquals(true, true);
	}
	
	// 簡単にMapを作る用メソッド
	public static <K, V> Map<K, V> toMap(Consumer<Map<K, V>> initializer) {
		Map<K, V> map = new LinkedHashMap<>();
		initializer.accept(map);
		return map;
	}
}
