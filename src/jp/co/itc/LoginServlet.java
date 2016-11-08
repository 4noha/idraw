package jp.co.itc;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.co.itc.db.DbDao;
import jp.co.itc.db.DbUtil;
import jp.co.itc.db.User_infoVo;

public class LoginServlet extends HttpServlet{
	protected void service(HttpServletRequest req,HttpServletResponse res)
			throws ServletException,IOException{
		req.setCharacterEncoding("UTF-8");						//文字化け対策
		String input_mail = req.getParameter("mail");			//入力値取得(メール)
		String input_pass = req.getParameter("pass");			//入力値取得(パスワード)
		HttpSession session = req.getSession();
		String from_top = req.getParameter("from_top"); 		//TOP画面から来た合図
		String jump = "top.jsp";

		//top.jspからきた処理
		if(from_top != null){
			//セッション情報の破棄
			session.invalidate();
			jump = "login.jsp";
		}else if(input_mail.equals("") || input_pass.equals("")){
			req.setAttribute("ERROR", "正しいデータを入力してください");
			jump = "login.jsp";
		}else{
			//DB関連処理
			try{
				DbUtil.connectionDb();
				DbDao dao = new DbDao();
				//テーブル指定
				dao.table_name = "user_info";
				ResultSet rs = dao.select(0);
				while(rs.next()){
					User_infoVo userinfo = new User_infoVo();
					userinfo.setUser_id(rs.getInt("user_id"));
					userinfo.setUser_name(rs.getString("user_name"));
					userinfo.setMail(rs.getString("mail"));
					userinfo.setPass(rs.getString("pass"));
					if(input_mail.equals(userinfo.getMail()) && input_pass.equals(userinfo.getPass())){
						//セッション情報格納
						session.setAttribute("LOGIN_INFO",userinfo);
					}
				}
				DbUtil.closeDb();
			}catch(ClassNotFoundException | SQLException e){
				e.printStackTrace();
				req.setAttribute("ERROR", "データが存在しません");
				jump = "login.jsp";
			}
		}

		//画面遷移
		req.getRequestDispatcher(jump).forward(req, res);
	}
}
