package jp.co.itc.db;

/* Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Tue Jan 19 10:23:51 JST 2016
 */
import java.io.Serializable;

/**
 * User_infoVo.
 * @author t-sugai
 * @version 1.0 
 * history 
 * Symbol	Date		Person		Note
 * [1]		2016/01/19	t-sugai		Generated.
 */
public class User_infoVo implements Serializable{

	public static final String TABLE = "USER_INFO";

	/**
	 * user_id:int(10) <Primary Key>
	 */
	private int user_id;

	/**
	 * user_name:varchar(50)
	 */
	private String user_name;

	/**
	 * mail:varchar(100)
	 */
	private String mail;

	/**
	 * pass:varchar(10)
	 */
	private String pass;

	/**
	* Constractor
	*/
	public User_infoVo(){}

	/**
	* Constractor
	* @param <code>user_id</code>
	*/
	public User_infoVo(int user_id){
		this.user_id = user_id;
	}

	public int getUser_id(){ return this.user_id; }

	public void setUser_id(int user_id){ this.user_id = user_id; }

	public String getUser_name(){ return this.user_name; }

	public void setUser_name(String user_name){ this.user_name = user_name; }

	public String getMail(){ return this.mail; }

	public void setMail(String mail){ this.mail = mail; }

	public String getPass(){ return this.pass; }

	public void setPass(String pass){ this.pass = pass; }

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("[User_infoVo:");
		buffer.append(" user_id: ");
		buffer.append(user_id);
		buffer.append(" user_name: ");
		buffer.append(user_name);
		buffer.append(" mail: ");
		buffer.append(mail);
		buffer.append(" pass: ");
		buffer.append(pass);
		buffer.append("]");
		return buffer.toString();
	}

}
