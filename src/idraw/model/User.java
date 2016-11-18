package idraw.model;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class User{

	//セッションid格納用変数
	public static String user;

	//ユーザーのセッションIDを返すメソッド
	//仮引数：httpサーブレットのリクエスト・レスポンスのインスタンス
	//戻り値：TRUE/FALSE
	public static boolean findBySessionId(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		HttpSession session = req.getSession();
		//userにセッションid値を代入
		user = (String)req.getAttribute("SESSION-ID");
		if(user.equals(null)){
			return false;
		}
		return true;
	}
}