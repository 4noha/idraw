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
		req.setCharacterEncoding("UTF-8");										//���������΍�
		String jump = "selectResult.jsp";										//��ʑJ�ڐ�w��
		ArrayList<Article_infoVo> list = new ArrayList<Article_infoVo>();		//�󂯓n��p��List�N���X
		try{
			//DB�ڑ�
			DbUtil.connectionDb();
			DbDao dao = new DbDao();
			//�e�[�u���w��
			dao.table_name = "article_info";
			//SQL���s
			ResultSet rs = dao.select(0);
			while(rs.next()){
				Article_infoVo art = new Article_infoVo();
				art.setDate_contributed(rs.getTimestamp("date_contributed"));
				art.setTitle_article(rs.getString("title_article"));
				art.setContent_article(rs.getString("content_article"));
				art.setUser_name(rs.getString("user_name"));
				list.add(art);
			}

			//DB�ؒf
			DbUtil.closeDb();
		}catch(ClassNotFoundException | SQLException e){
			e.printStackTrace();
			jump = "select.jsp";
		}
		//�󂯓n���f�[�^�i�[
		req.setAttribute("SELECT_DATA", list);
		//��ʑJ��
		req.getRequestDispatcher(jump).forward(req, res);

	}
}
