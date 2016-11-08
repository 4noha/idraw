package jp.co.itc.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbDao {
	public String table_name;
	public static final String ALL_SELECT = "SELECT * FROM ";
	public static final String JOIN = "article_info a,user_info u WHERE a.user_id = u.user_id AND a.del_flg = true ";
	public static final String SORT = "ORDER BY a.date_contributed DESC";

	public static final String INSERT = "INSERT INTO ";

	//検索メソッド
	public ResultSet select(int type) throws SQLException{
		ResultSet rs = null;
		if(table_name == null){
			table_name = "";
		}
		//SQL作成
		StringBuilder sql = new StringBuilder(ALL_SELECT);
		if(table_name.equals("article_info")){
			sql.append(JOIN);
			sql.append(SORT);
		}else{
			sql.append(table_name);
		}

		//DBにSQL発行
		DbUtil.p_stmt = DbUtil.con.prepareStatement(sql.toString());
		if(type == 0){
			//0は全検索
			rs = DbUtil.p_stmt.executeQuery();
		}
		return rs;
	}

	//挿入メソッド
	public void insert(Object obj) throws SQLException{
		//SQL作成
		StringBuilder sql = new StringBuilder(INSERT);
		if(table_name.equals("article_info")){
			Article_infoVo al = (Article_infoVo)obj;
			sql.append(table_name);
			sql.append("(date_contributed,title_article,content_article,user_id,del_flg)");
			sql.append(" VALUES(?,?,?,?,?)");
			DbUtil.p_stmt = DbUtil.con.prepareStatement(sql.toString());
			DbUtil.p_stmt.setTimestamp(1, al.getDate_contributed());
			DbUtil.p_stmt.setString(2, al.getTitle_article());
			DbUtil.p_stmt.setString(3, al.getContent_article());
			DbUtil.p_stmt.setInt(4, al.getUser_id());
			DbUtil.p_stmt.setBoolean(5,al.isDel_flg());
		}

		//DBにSQL発行
		DbUtil.p_stmt.executeUpdate();
	}

}
