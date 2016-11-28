package idraw.orm;

import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DbStaticDao<T> {
	private Class<T> modelClass;
	private String tableName;

	public DbStaticDao(Class<T> modelClass, String tableName) {
		this.modelClass = modelClass;
		this.tableName = tableName;
	}

	// カラムをひとつだけ取得、uniqueで引くときに便利
	public T findBy(String column_name, Object value) throws SQLException, InstantiationException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		String sql = "SELECT * FROM " + tableName + " WHERE " + column_name + " = ? LIMIT 1";
		PreparedStatement stmt = DbUtil.con.prepareStatement(sql);
		setValue(stmt, 1, value);
		System.out.println("SQL_Log: " + stmt.toString().split(":")[1]);

		ResultSet rs = stmt.executeQuery();
		if (!rs.next()) {
			// 検索結果なし
			return null;
		}

		T tInstance = modelClass.newInstance();
		Field modelField = DbInstanceDao.class.getDeclaredField("newFlag");
		modelField.set(tInstance, false);
		for (int i = 1; i <= getColumnNames().size(); i++) {
			modelField = modelClass.getDeclaredField(getColumnNames().get(i - 1));
			modelField.set(tInstance, rs.getObject(i));
		}

		return tInstance;
	}

	// 文字列での部分検索
	public ArrayList<T> findPartial(String column_name, String value) throws SQLException, InstantiationException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		String sql = "SELECT * FROM " + tableName + " WHERE " + column_name + " LIKE ?";
		PreparedStatement stmt = DbUtil.con.prepareStatement(sql);
		stmt.setString(1, "%" + value + "%");
		ResultSet rs = stmt.executeQuery();
		System.out.println("SQL_Log: " + stmt.toString().split(":")[1]);

		ArrayList<T> tInstances = new ArrayList<T>();
		while (rs.next()) {
			T tInstance = modelClass.newInstance();
			Field modelField = DbInstanceDao.class.getDeclaredField("newFlag");
			modelField.set(tInstance, false);

			for (int i = 1; i <= getColumnNames().size(); i++) {
				modelField = modelClass.getDeclaredField(getColumnNames().get(i - 1));
				modelField.set(tInstance, rs.getObject(i));
			}
			tInstances.add(tInstance);
		}

		return tInstances;
	}

	// 条件でたくさん引くときに使う
	public ArrayList<T> find(String column_name, Object value) throws InstantiationException, IllegalAccessException,
			NoSuchFieldException, SecurityException, SQLException {
		return find(toMap(m -> { m.put(column_name, value); }));
	}

	public ArrayList<T> find(Map<String, Object> columnValueMap) throws SQLException, InstantiationException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		String sql = "SELECT * FROM " + tableName + " WHERE ";
		String[] keyArray = columnValueMap.keySet().toArray(new String[columnValueMap.size()]);
		for (int i = 0; i < keyArray.length; i++) {
			sql += keyArray[i] + " = ?" + (i < keyArray.length - 1 ? " AND " : "");
		}
		PreparedStatement stmt = DbUtil.con.prepareStatement(sql);

		for (int i = 0; i < keyArray.length; i++) {
			setValue(stmt, i + 1, columnValueMap.get(keyArray[i]));
		}
		ResultSet rs = stmt.executeQuery();
		System.out.println("SQL_Log: " + stmt.toString().split(":")[1]);

		ArrayList<T> records = new ArrayList<T>();
		while (rs.next()) {
			T record = modelClass.newInstance();
			Field modelField = DbInstanceDao.class.getDeclaredField("newFlag");
			modelField.set(record, false);
			for (int i = 1; i <= getColumnNames().size(); i++) {
				modelField = modelClass.getDeclaredField(getColumnNames().get(i - 1));
				modelField.set(record, rs.getObject(i));
			}
			records.add(record);
		}
		if (records.isEmpty()) {
			return null;
		}

		return records;
	}

	public ArrayList<T> all() throws SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException,
			SecurityException {
		String sql = "SELECT * FROM " + tableName;
		PreparedStatement stmt = DbUtil.con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		System.out.println("SQL_Log: " + stmt.toString().split(":")[1]);

		ArrayList<T> records = new ArrayList<T>();
		while (rs.next()) {
			T record = modelClass.newInstance();
			Field modelField = DbInstanceDao.class.getDeclaredField("newFlag");
			modelField.set(record, false);
			for (int i = 1; i <= getColumnNames().size(); i++) {
				modelField = modelClass.getDeclaredField(getColumnNames().get(i - 1));
				modelField.set(record, rs.getObject(i));
			}
			records.add(record);
		}
		if (records.isEmpty()) {
			return null;
		}

		return records;
	}

	public ArrayList<String> getColumnNames() throws SQLException {
		DatabaseMetaData meta = DbUtil.con.getMetaData();
		// DBのメタ情報を取得
		ResultSet rs = meta.getColumns(null, null, tableName, "");

		ArrayList<String> columnNames = new ArrayList<String>();
		while (rs.next()) {
			// ループごとに4つ目の値がカラム名になっている
			columnNames.add(rs.getString(4));
		}

		return columnNames;
	}

	public static void setValue(PreparedStatement stmt, int index, Object value) throws SQLException {
		// 新しい型を使いたい場合はif文を都度追加(汗)
		if (value instanceof String) {
			stmt.setString(index, (String) value);
		} else if (value instanceof Timestamp) {
			stmt.setTimestamp(index, (Timestamp) value);
		} else if (value == null) {
			stmt.setObject(index, null);
		} else {
			stmt.setInt(index, (int) value);
		}
	}

	// 簡単にMapを作る用メソッド
	public static <K, V> Map<String, V> toMap(Consumer<Map<String, V>> initializer) {
		Map<String, V> map = new LinkedHashMap<>();
		initializer.accept(map);
		return map;
	}
}