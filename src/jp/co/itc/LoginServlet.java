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
		req.setCharacterEncoding("UTF-8");						//���������΍�
		String input_mail = req.getParameter("mail");			//���͒l�擾(���[��)
		String input_pass = req.getParameter("pass");			//���͒l�擾(�p�X���[�h)
		HttpSession session = req.getSession();
		String from_top = req.getParameter("from_top"); 		//TOP��ʂ��痈�����}
		String jump = "top.jsp";

		//top.jsp���炫������
		if(from_top != null){
			//�Z�b�V�������̔j��
			session.invalidate();
			jump = "login.jsp";
		}else if(input_mail.equals("") || input_pass.equals("")){
			req.setAttribute("ERROR", "�������f�[�^����͂��Ă�������");
			jump = "login.jsp";
		}else{
			//DB�֘A����
			try{
				DbUtil.connectionDb();
				DbDao dao = new DbDao();
				//�e�[�u���w��
				dao.table_name = "user_info";
				ResultSet rs = dao.select(0);
				while(rs.next()){
					User_infoVo userinfo = new User_infoVo();
					userinfo.setUser_id(rs.getInt("user_id"));
					userinfo.setUser_name(rs.getString("user_name"));
					userinfo.setMail(rs.getString("mail"));
					userinfo.setPass(rs.getString("pass"));
					if(input_mail.equals(userinfo.getMail()) && input_pass.equals(userinfo.getPass())){
						//�Z�b�V�������i�[
						session.setAttribute("LOGIN_INFO",userinfo);
					}
				}
				DbUtil.closeDb();
			}catch(ClassNotFoundException | SQLException e){
				e.printStackTrace();
				req.setAttribute("ERROR", "�f�[�^�����݂��܂���");
				jump = "login.jsp";
			}
		}

		//��ʑJ��
		req.getRequestDispatcher(jump).forward(req, res);
	}
}
