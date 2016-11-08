package jp.co.itc;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.itc.db.Article_infoVo;
import jp.co.itc.db.DbDao;
import jp.co.itc.db.DbUtil;

public class SelectServlet extends HttpServlet{
	protected void service(HttpServletRequest req,HttpServletResponse res)
			throws ServletException,IOException{
		req.setCharacterEncoding("UTF-8");										//文字化け対策
		String jump = "selectResult.jsp";										//画面遷移先指定
		ArrayList<Article_infoVo> list = new ArrayList<Article_infoVo>();		//受け渡り用のListクラス
		try{
			//DB接続
			DbUtil.connectionDb();
			DbDao dao = new DbDao();
			//テーブル指定
			dao.table_name = "article_info";
			//SQL発行
			ResultSet rs = dao.select(0);
			while(rs.next()){
				Article_infoVo art = new Article_infoVo();
				art.setDate_contributed(rs.getTimestamp("date_contributed"));
				art.setTitle_article(rs.getString("title_article"));
				art.setContent_article(rs.getString("content_article"));
				art.setUser_name(rs.getString("user_name"));
				list.add(art);
			}

			//DB切断
			DbUtil.closeDb();
		}catch(ClassNotFoundException | SQLException e){
			e.printStackTrace();
			jump = "select.jsp";
		}
		//受け渡しデータ格納
		req.setAttribute("SELECT_DATA", list);
		//画面遷移
		req.getRequestDispatcher(jump).forward(req, res);

	}
}
