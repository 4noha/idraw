package jp.co.itc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.PreparedStatement;

public class DbUtil {
	public static Connection con;
	public static Statement stmt;
	public static PreparedStatement p_stmt;

	//接続メソッド
	public static void connectionDb() throws ClassNotFoundException,SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(
				"jdbc:mysql://127.0.0.1:3306/rezodb"
				,"rezouser"
				,"rezo");
		stmt = con.createStatement();
	}

	//切断メソッド
	public static void closeDb() throws ClassNotFoundException,SQLException{
		stmt.close();
		con.close();
	}
}
