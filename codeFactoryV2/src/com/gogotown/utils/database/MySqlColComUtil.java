package com.gogotown.utils.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.gogotown.entity.DbEntity;

/**   
* @Title: MySqlColComUtil.java 
* @Package com.gogotown.utils 
* @Description: 获取表字段备注
* @author hezhoujun
* @date 2014-12-22 上午9:16:18 
* @version V1.0   
*/
public class MySqlColComUtil {
     
    private   String driver= null;
    private   String user= null;
    private   String pwd= null;
    private   String url = null;
    private  Connection getConnection=null;
    private static Map<String, String> column_infoMap = null;
 


	public MySqlColComUtil(String driver, String user, String pwd, String url) {
		super();
		this.driver = driver;
		this.user = user;
		this.pwd = pwd;
		this.url = url;
	}

	public static void main(String[] args) {
		DbEntity dbConstans = new DbEntity("com.mysql.jdbc.Driver", "jdbc:mysql://192.168.0.61:3366/gogocenter", "gogodb", "`12`12");
		getTableColumnComments("user_order_goods",dbConstans);
    }
	
	public static Map<String, String> getTableColumnComments(String table,DbEntity dbConstans){
			MySqlColComUtil dbHelpInfo = new MySqlColComUtil(dbConstans.getDriver(), dbConstans.getUser(),dbConstans.getPassword(), dbConstans.getUrl());
				column_infoMap = dbHelpInfo.getComments(table);
		return column_infoMap;
	}
    
    /**
     * 根据表查询表字段的注释
     * @param table
     * @return
     */
    public  Map<String, String> getComments(String table){
    	Map<String, String> column_infoMap = new HashMap<String, String>();
        getConnection=getConnections();
        try {
            DatabaseMetaData dbmd=getConnection.getMetaData();
            ResultSet resultSet = dbmd.getTables(null, "%", "%", new String[] { "TABLE" });
            while (resultSet.next()) {
                String tableName=resultSet.getString("TABLE_NAME");
                if(tableName.equals(table)){
                    ResultSet rs = dbmd.getColumns(null, "%", tableName, "%");
                    while(rs.next()){
                    column_infoMap.put(rs.getString("COLUMN_NAME"), rs.getString("REMARKS"));
//                    System.out.println(rs.getString("COLUMN_NAME")+" "+ rs.getString("REMARKS"));
                    }
                    rs.close();
                }
            }
            resultSet.close();
            //获取主键
            ResultSet keyRS =   dbmd.getPrimaryKeys(null, null, table);
            while (keyRS.next()) {
            	//keyRS.getString(1) 数据库名 2 null 3 表名  4 字段名 5 主键的序列 6 主键的标识PRIMARY
            	if(keyRS.getString(5).equals("1")){
//            		System.out.println(keyRS.getString(1) + " "+keyRS.getString(2) + " "+ keyRS.getString(3)+ " "+keyRS.getString(4)+ " "+keyRS.getString(5) +" "+keyRS.getString(6));
            		//取出第一个主键
            		column_infoMap.put("primary_column_tab", keyRS.getString(4));
            	}
            }
            keyRS.close();            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return column_infoMap;
    }
    
    public Connection getConnections(){
        try {
            Class.forName(driver);
            url = url+"?user="+user+"&password="+pwd+"&useUnicode=true&characterEncoding=UTF-8";
            getConnection=DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getConnection;
    }
 
}