package jp.co.itc.db;

/* Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Tue Jan 19 16:10:18 JST 2016
 */
import java.io.Serializable;

/**
 * Article_infoVo.
 * @author t-sugai
 * @version 1.0
 * history
 * Symbol	Date		Person		Note
 * [1]		2016/01/19	t-sugai		Generated.
 */
public class Article_infoVo implements Serializable{

	public static final String TABLE = "ARTICLE_INFO";

	/**
	 * id_article:int(10) <Primary Key>
	 */
	private int id_article;

	/**
	 * date_contributed:datetime(0)
	 */
	private java.sql.Timestamp date_contributed;

	/**
	 * title_article:varchar(100)
	 */
	private String title_article;

	/**
	 * content_article:varchar(1024)
	 */
	private String content_article;

	/**
	 * user_id
	 */
	private int user_id;

	/**
	 * user_name
	 */
	private String user_name;

	/**
	 * del_flg
	 */
	private boolean del_flg;

	/**
	* Constractor
	*/
	public Article_infoVo(){}

	/**
	* Constractor
	* @param <code>id_article</code>
	*/
	public Article_infoVo(int id_article){
		this.id_article = id_article;
	}

	public int getId_article(){ return this.id_article; }

	public void setId_article(int id_article){ this.id_article = id_article; }

	public java.sql.Timestamp getDate_contributed(){ return this.date_contributed; }

	public void setDate_contributed(java.sql.Timestamp date_contributed){ this.date_contributed = date_contributed; }

	public String getTitle_article(){ return this.title_article; }

	public void setTitle_article(String title_article){ this.title_article = title_article; }

	public String getContent_article(){ return this.content_article; }

	public void setContent_article(String content_article){ this.content_article = content_article; }

	public String getUser_name() { return user_name; }

	public void setUser_name(String user_name) { this.user_name = user_name; }

	public int getUser_id() { return user_id; }

	public void setUser_id(int user_id) { this.user_id = user_id; }

	public boolean isDel_flg() { return del_flg; }

	public void setDel_flg(boolean del_flg) { this.del_flg = del_flg; }

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("[Article_infoVo:");
		buffer.append(" id_article: ");
		buffer.append(id_article);
		buffer.append(" date_contributed: ");
		buffer.append(date_contributed);
		buffer.append(" title_article: ");
		buffer.append(title_article);
		buffer.append(" content_article: ");
		buffer.append(content_article);
		buffer.append("]");
		return buffer.toString();
	}

}
