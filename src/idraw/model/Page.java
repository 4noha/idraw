package idraw.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import idraw.orm.DbInstanceDao;
import idraw.orm.DbStaticDao;

public class Page extends DbInstanceDao<Page>{
	//ORMはTABLE_NAME, THIS_CLASSの入力とカラムと同名のフィールドを定義すれば後は大体コピペで使える
	
	// ORMでクラスをテーブルにマッピングするのに必要
	private final static String TABLE_NAME = "page";
	private final static Class<Page> THIS_CLASS = Page.class;
	private static ArrayList<String> columnNames;
	private static DbStaticDao<Page> dbStaticAdapter = null;
	private static DbInstanceDao<Page> dbAdapter = null;
	
	// 同名のカラムと対応
	public int page_num;
	public String joined_image;
	public String background_image;
	
	//newしてsaveするとcreateが走る
	public Page() throws SQLException {
		super(THIS_CLASS, TABLE_NAME);
		adapterStandby();
	}
	public Page(Map<String, Object> params) throws SQLException {
		super(THIS_CLASS, TABLE_NAME);
		adapterStandby();
		this.page_num   = (int) params.get("page_num");
		this.joined_image   = (String) params.get("joined_image");
		this.background_image   = (String) params.get("background_image");
		newFlag = true;
	}
	
	// 検索関数を定義
	public static Page findBy(String column_name, Object value) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException{
		adapterStandby();
		return dbStaticAdapter.findBy(column_name, value);
	}
	public static ArrayList<Page> findPartial(String column_name, String value) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException{
		adapterStandby();
		return dbStaticAdapter.findPartial(column_name, value);
	}
	
	// 全部出す
	public static ArrayList<Page> all() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException{
		adapterStandby();
		return dbStaticAdapter.all();
	}
	
	// クラスの設定が初期化されていないときに呼ばれる
	private static void adapterStandby() throws SQLException{
		if (dbStaticAdapter == null || dbAdapter == null){
			// クラスメソッドを使うための設定
			dbStaticAdapter = new DbStaticDao<Page>(THIS_CLASS, TABLE_NAME);
			// インスタンスメソッドを使うための設定
			dbAdapter = new DbInstanceDao<Page>(THIS_CLASS, TABLE_NAME);
			// クラスのカラム名一覧を設定
			columnNames = dbStaticAdapter.getColumnNames();
		}
	}
	
	// クラスのモデルのカラム名一覧を取得
	public static ArrayList<String> getColumnNames(){
		return columnNames;
	}
}