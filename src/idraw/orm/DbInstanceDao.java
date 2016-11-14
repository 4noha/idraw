package idraw.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DbInstanceDao<T> {
	private String tableName;
	private String primaryKey;
	public boolean newFlag = true;
	
	public DbInstanceDao(Class<T> modelClass, String tableName) {
		this.tableName = tableName;
	}
	
	// インスタンスに紐づいたレコードを削除
	public boolean destroy() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, SQLException{
		// 主キーを取得
		Field idField = this.getClass().getDeclaredField(getPrimaryKey());
		Object id = idField.get(this);
		
		// 主キーに紐づいたレコードを削除
		String sql = "DELETE FROM " + tableName + " WHERE " + getPrimaryKey() + " = ?";
		PreparedStatement stmt = DbUtil.con.prepareStatement(sql);
		DbStaticDao.setValue(stmt, 1, id);
		System.out.println("SQL_Log: " + stmt.toString().split(":")[1]);
		stmt.executeUpdate();
		return true;
	}
	
	public boolean save() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, SQLException, NoSuchMethodException, InvocationTargetException{
		if (newFlag){
			create();
			return true;
		}
		return update();
	}

	// インスタンスに紐づいたレコードを更新
	private boolean update() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, SQLException, NoSuchMethodException, InvocationTargetException{
		Object id = null;
		
		// 子クラスのカラムネーム取得メソッドを無理やり実行
		Method method = this.getClass().getMethod("getColumnNames");
		ArrayList<String> columnNames = (ArrayList<String>) method.invoke(null, null);

		// 主キーで引いたレコードに値を全更新するSQLを作成
		String sql = "UPDATE " + tableName + " SET ";
		for(int i = 0; i < columnNames.size(); i++){
			if(!columnNames.get(i).equals(getPrimaryKey())){
				sql += columnNames.get(i) + " = ?" + (i < columnNames.size()-1 ? ", " : "");
			}
		}
		sql += " WHERE " + getPrimaryKey() + " = ?";
		PreparedStatement stmt = DbUtil.con.prepareStatement(sql);
		
		// 主キー以外の値をセット
		boolean idEmerge = false;
		for(int i = 1; i <= columnNames.size(); i++){
			Field field = this.getClass().getDeclaredField(columnNames.get(i - 1));
			Object value = field.get(this);
			
			if(!columnNames.get(i - 1).equals(getPrimaryKey())){
				//主キーが出現していたらひとつずれるので-1
				DbStaticDao.setValue(stmt, idEmerge ? i-1 : i, value);
			} else {
				idEmerge = true;
				id = value;
			}
		}
		
		//最後に主キーを設定
		DbStaticDao.setValue(stmt, columnNames.size(), id);
		System.out.println("SQL_Log: " + stmt.toString().split(":")[1]);
		stmt.executeUpdate();
		
		return true;
	}

	private boolean create() throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		// インスタンスに紐づいたレコードを作成
		Method method = this.getClass().getMethod("getColumnNames");
		ArrayList<String> columnNames = (ArrayList<String>) method.invoke(null, null);
		String sql = "INSERT INTO " + tableName + " VALUES(";
		
		for(int i = 0; i < columnNames.size(); i++){
				sql += "?" + (i < columnNames.size()-1 ? ", " : "");
		}
		sql += ");";
		PreparedStatement stmt = DbUtil.con.prepareStatement(sql);

		for(int i = 1; i <= columnNames.size(); i++){
			Field field = this.getClass().getDeclaredField(columnNames.get(i - 1));
			Object value = field.get(this);
			DbStaticDao.setValue(stmt, i, value);
		}
		System.out.println("SQL_Log: " + stmt.toString().split(":")[1]);
		stmt.executeUpdate();
		this.newFlag = false;
		
		return true;
	}
	
	// 主キーを取得
	private String getPrimaryKey() throws SQLException{
		if(primaryKey == null){
			DatabaseMetaData meta = DbUtil.con.getMetaData();
			// �J�������擾�̏�����ݒ�
			ResultSet rs = meta.getPrimaryKeys(null, null, tableName);
			rs.next();
			primaryKey = rs.getString(4);
		}
		return primaryKey;
	}
}