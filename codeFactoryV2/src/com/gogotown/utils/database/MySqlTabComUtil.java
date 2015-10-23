package com.gogotown.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gogotown.entity.DbEntity;

/**   
* @Title: MySqlTabComUtil.java 
* @Package com.gogotown.utils 
* @Description: 获取表备注
* @author hezhoujun
* @date 2014-12-22 上午9:16:41 
* @version V1.0   
*/
public class MySqlTabComUtil {

        public Connection getMySQLConnection(DbEntity dbConstans) throws Exception {
//        	Properties prop = new Properties();
//        	InputStream  in = Object.class.getResourceAsStream("/db.properties");
//        	prop.load(in);
                Class.forName(dbConstans.getDriver());
                Connection conn = DriverManager.getConnection(dbConstans.getUrl(),dbConstans.getUser(),dbConstans.getPassword());
                return conn;
        }
        
        public Map<String, Object> getCommentByTableName(List<String> tableName,DbEntity dbConstans) throws Exception {
                Map<String, Object> map = new HashMap<String, Object>();
                Connection conn = getMySQLConnection(dbConstans);
                Statement stmt = conn.createStatement();
                for(int i = 0; i < tableName.size(); i++) {
                        String table = (String)tableName.get(i);
                        ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + table);
                        if(rs != null && rs.next()) {
                                String create = rs.getString(2);
                                String comment = parse(create);
                                map.put(table,comment);
                        }
                        rs.close();
                }
                stmt.close();
                conn.close();
                return map;
        }
        
        public List<String> getAllTableName(DbEntity dbConstans) throws Exception{
                List<String> tables = new ArrayList<String>();
                Connection conn = getMySQLConnection(dbConstans);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SHOW TABLES ");
                while(rs.next()) {
                        String tableName = rs.getString(1);
                        tables.add(tableName);
                }
                rs.close();
                stmt.close();
                conn.close();
                return tables;
        }
        
        public String parse(String all) {
                String comment = null;
                int index = all.indexOf("COMMENT='");
                if(index < 0) {
                        return "";
                }
                comment = all.substring(index+9);
                comment = comment.substring(0,comment.length() - 1);
                return comment;
        }
        
        /**
         * @throws Exception  
        * @author hezhoujun
        * @Title: getTableCommentByName 
        * @Description: 根据表名获取表注释 
        * @param @param table    设定文件 
        * @return void    返回类型 
        */
        public static String getTableCommentByName(String table,DbEntity dbConstans) throws Exception{
        	MySqlTabComUtil tableComment = new MySqlTabComUtil();
        	List<String> tables = tableComment.getAllTableName(dbConstans);
        	Map<String, Object> tablesComment = tableComment.getCommentByTableName(tables,dbConstans);
        	return (String)tablesComment.get(table);
        }
        
        /** 
        * @author hezhoujun
        * @Title: dbTables 
        * @Description:查询数据库所有表
        * @param @return
        * @param @throws Exception    设定文件 
        * @return List<String>    返回类型 
        */
        public static List<String> getDbTables(DbEntity dbConstans) throws Exception{
        	MySqlTabComUtil tableComment = new MySqlTabComUtil();
        	return tableComment.getAllTableName(dbConstans);
        }
        
        public static void main(String[] args) throws Exception{
                /*List<String> tables = getAllTableName();
                Map<String, Object> tablesComment = getCommentByTableName(tables);
               for (int i = 0; i < tables.size(); i++) {
				System.out.println(tables.get(i)+" "+tablesComment.get(tables.get(i)));
			}*/
//        	System.out.println(getTableCommentByName("g_file"));;
        }

}