package test.idraw.orm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import idraw.orm.DbInstanceDao;
import idraw.orm.DbStaticDao;

public class Model extends DbInstanceDao<Model>{
	// Daoテスト用モデル
	private final static String TABLE_NAME = "page";
	private final static Class<Model> THIS_CLASS = Model.class;
	private static ArrayList<String> columnNames;
	private static DbStaticDao<Model> dbStaticAdapter = null;
	private static DbInstanceDao<Model> dbAdapter = null;
	
	public int	  page_num;
	public String joined_image;
	public String background_image;
	public String timer;
	
	public Model() throws SQLException {
		super(THIS_CLASS, TABLE_NAME);
		adapterStandby();
	}
	public Model(Map<String, Object> params) throws SQLException {
		super(THIS_CLASS, TABLE_NAME);
		adapterStandby();
		this.page_num		  = (int) params.get("page_num");
		this.joined_image	  = (String) params.get("joined_image");
		this.background_image = (String) params.get("background_image");
		this.timer			  = (String) params.get("timer");
		newFlag = true;
	}
	
	public static Model findBy(String column_name, Object value) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException{
		adapterStandby();
		return dbStaticAdapter.findBy(column_name, value);
	}

	public static ArrayList<Model> findPartial(String column_name, String value) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException{
		adapterStandby();
		return dbStaticAdapter.findPartial(column_name, value);
	}
	
	public static ArrayList<Model> find(String column_name, Object value) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException{
		adapterStandby();
		return dbStaticAdapter.find(column_name, value);
	}
	
	public static ArrayList<Model> find(HashMap<String, Object> columnValueMap) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException{
		adapterStandby();
		return dbStaticAdapter.find(columnValueMap);
	}
	
	public static ArrayList<Model> all() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException{
		adapterStandby();
		return dbStaticAdapter.all();
	}
	
	private static void adapterStandby() throws SQLException{
		if (dbStaticAdapter == null || dbAdapter == null){
			dbStaticAdapter = new DbStaticDao<Model>(THIS_CLASS, TABLE_NAME);
			dbAdapter = new DbInstanceDao<Model>(THIS_CLASS, TABLE_NAME);
			columnNames = dbStaticAdapter.getColumnNames();
		}
	}
	
	public static ArrayList<String> getColumnNames(){
		return columnNames;
	}
}