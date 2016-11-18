package idraw.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DbUtil {
	// Default値
    public static final String ENV = "test";
    public static final String HOST = "127.0.0.1:3306";
    public static final String USER = "rezouser";
    public static final String PASSWORD = "Rezo_0000";
    public static final String DB_NAME = "rezodb";
    public static Connection con;

    public static void connect() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://" + HOST + "/" + ENV + "_" + DB_NAME + "?characterEncoding=utf8";
    	con = DriverManager.getConnection(url, USER, PASSWORD);
    }
    public static void connect(Map<String, String> params) throws SQLException, ClassNotFoundException {
		// JDBCクラスの読み込み
		Class.forName("com.mysql.jdbc.Driver");
		// Mapで値が与えられているところだけ、デフォルト以外を使う
		String url = "jdbc:mysql://" +
				(params != null && params.containsKey("host") ? params.get("host") : HOST) + "/" +
				(params != null && params.containsKey("env") ? params.get("env") : ENV) + "_" +
				(params != null && params.containsKey("db_name") ? params.get("db_name") : DB_NAME) +
				"?characterEncoding=utf8";

    	con = DriverManager.getConnection(
    	    		url,
    				params != null && params.containsKey("user") ? params.get("user") : USER,
    				params != null && params.containsKey("password") ? params.get("password") : PASSWORD
    			);
    }

    public static void close() throws SQLException {
    	con.close();
    }
}
