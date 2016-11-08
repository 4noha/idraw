package jp.co.itc;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.co.itc.db.Article_infoVo;
import jp.co.itc.db.DbDao;
import jp.co.itc.db.DbUtil;
import jp.co.itc.db.User_infoVo;

public class InsertServlet extends HttpServlet{
	protected void service(HttpServletRequest req,HttpServletResponse res)
			throws ServletException,IOException{
		req.setCharacterEncoding("UTF-8");				//���������΍�
		String jump = "insertResult.jsp";
		String input_title = req.getParameter("title");
		String input_content = req.getParameter("content");
		HttpSession session = req.getSession();
		User_infoVo user = (User_infoVo)session.getAttribute("LOGIN_INFO");

		if(input_title.equals("") || input_content.equals("")){
			//���͒l�������͂̏ꍇ
			req.setAttribute("ERROR", "���͂��Ă�������");
			jump = "insert.jsp";
		}else if(user == null){
			req.setAttribute("ERROR", "�ēx���O�C�����Ă�������");
			jump = "login.jsp";
		}else{
			try{
				DbUtil.connectionDb();
				Timestamp now = new Timestamp(System.currentTimeMillis());
				DbDao dao = new DbDao();
				Article_infoVo sql_article = new Article_infoVo();
				sql_article.setDate_contributed(now);
				sql_article.setTitle_article(input_title);
				sql_article.setContent_article(input_content);
				sql_article.setUser_id(user.getUser_id());
				sql_article.setDel_flg(true);
				dao.table_name = "article_info";
				dao.insert(sql_article);

			}catch(ClassNotFoundException | SQLException e){
				e.printStackTrace();
				req.setAttribute("ERROR", "�������f�[�^����͂��Ă�������");
				jump = "insert.jsp";
			}

			//�f�[�^����ʂ�
			req.setAttribute("INPUT_TITLE", input_title);
			req.setAttribute("INPUT_CONTENT", input_content);

		}

		//��ʑJ��
		req.getRequestDispatcher(jump).forward(req, res);
	}
}
