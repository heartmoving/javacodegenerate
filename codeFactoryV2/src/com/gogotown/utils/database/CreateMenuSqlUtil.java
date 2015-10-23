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
import com.gogotown.entity.MenuEntity;
import com.gogotown.utils.StringUtil;

/**   
* @Title: MySqlTabComUtil.java 
* @Package com.gogotown.utils 
* @Description: 获取表备注
* @author hezhoujun
* @date 2014-12-22 上午9:16:41 
* @version V1.0   
*/
public class CreateMenuSqlUtil {
	public static int n;

        public static Connection getConnection(DbEntity dbEntity) throws Exception {
                Class.forName(dbEntity.getDriver());
                Connection conn = DriverManager.getConnection(dbEntity.getUrl(),dbEntity.getUser(),dbEntity.getPassword());
                return conn;
        }
        
        
        /**
        * @author hezhoujun
        * @Title: getLastIndex
        * @Description:获取表索引值
        * @param dbEntity
        * @return
        * @throws Exception int
        */
        public static int getLastIndex(DbEntity dbEntity,String menuTableName) throws Exception{
        		int id = 0;
                Connection conn = getConnection(dbEntity);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `"+menuTableName+"` ORDER BY id desc LIMIT 1");
                while(rs.next()) {
                        id = rs.getInt(1);
                }
                rs.close();
                stmt.close();
                conn.close();
                return id;
        }
        
        /**
        * @author hezhoujun
        * @Title: getSencodeMenuSql
        * @Description: 获取二级菜单
        * @param nowIndex
        * @param pid
        * @param listName
        * @param domainName
        * @return
        * @throws Exception Map<String,Object>
        */
        public static Map<String, Object> getSencodeMenuSql(int nowIndex,int pid,String listName,String domainName) throws Exception{
        	StringBuilder sb = new StringBuilder();
        	String insert1 = "INSERT INTO m_menu ( id, NAME, url, is_show, parent_id, remark, createdate, changedate, createby, createbyid )";
        	String insert2 = "INSERT INTO m_menu ( id, NAME, url, is_show, parent_id, remark, createdate, changedate, createby, createbyid )";
        	String value1 = "VALUES ( "+nowIndex+", '"+listName+"', '"+domainName+"/list', 1, "+pid+", '"+listName+"', SYSDATE(), SYSDATE(), 'system', 1 );";
        	String addVal = "VALUES ( "+(nowIndex+1)+",'添加', 'edit', 1, "+nowIndex+", '"+listName+"添加', SYSDATE(), SYSDATE(), 'system', 1 );";
        	String updateVal = "VALUES ( "+(nowIndex+2)+", '修改', 'update', 1, "+nowIndex+", '"+listName+"修改', SYSDATE(), SYSDATE(), 'system', 1 );";
        	String delVal = "VALUES ( "+(nowIndex+3)+", '删除', 'delete', 1, "+nowIndex+", '"+listName+"删除', SYSDATE(), SYSDATE(), 'system', 1 );";
        	sb.append(insert1+"\r\n");
        	sb.append(value1+"\r\n");
        	sb.append(insert2+"\r\n");
        	sb.append(addVal+"\r\n");
        	sb.append(insert2+"\r\n");
        	sb.append(updateVal+"\r\n");
        	sb.append(insert2+"\r\n");
        	sb.append(delVal+"\r\n");
        	Map<String, Object> r_map = new HashMap<String, Object>();
        	r_map.put("end_index", (nowIndex+3));
        	r_map.put("sql", sb.toString());
        	return r_map;
        }
        
        public static Map<String, Object> getFirstMenuSQlStr(String menuName,int nowIndex) throws Exception{
        	nowIndex ++;
        	StringBuilder sb = new StringBuilder();
        	String insert1 = "INSERT INTO m_menu ( id, NAME, url, is_show, parent_id, remark, createdate, changedate, createby, createbyid )";
        	String value1 = "VALUES ( "+nowIndex+", '"+menuName+"', '###', 1, 0, '"+menuName+"', SYSDATE(), SYSDATE(), 'system', 1 );";
        	sb.append(insert1+"\r\n");
        	sb.append(value1+"\r\n");
        	Map<String, Object> r_map = new HashMap<String, Object>();
        	r_map.put("index", nowIndex);
        	r_map.put("sql", sb.toString());
        	return r_map;
        }
        
        /**
        * @author hezhoujun
        * @Title: createAllMenu
        * @Description: 创建菜单方法入口
        * @param topMenus 顶级菜单名称，多个以英文逗号分隔
        * @param dbEntity 数据库配置
        * @param menulist 二级菜单实体
        * @param menuTable 菜单表名称
        * @throws Exception void
        */
        public static String createAllMenu(String topMenus,DbEntity dbEntity,List<MenuEntity> menulist,String menuTable) throws Exception{
        	List<String> firstNameList = StringUtil.stringToList(topMenus, ",");
        	//第一级菜单与id的关联关系
        	Map<String, Object> FirstMenuMap = new HashMap<String, Object>();
        	StringBuilder sb = new StringBuilder();
        	sb.append("-- 一级菜单\r\n");
        	//最后的索引
        	int nowIndex = getLastIndex(dbEntity,menuTable);
        	//一级菜单生成完后返回的索引
        	int lastIndex = 0;
        	for(int i = 0;i < firstNameList.size() ;i++){
        		nowIndex++;
        		Map<String, Object> map = getFirstMenuSQlStr(firstNameList.get(i),nowIndex);
        		sb.append(map.get("sql"));
        		FirstMenuMap.put(firstNameList.get(i),nowIndex+1 );
        		if(i == firstNameList.size()-1){
        			lastIndex = nowIndex+1;
        		}
        	}
        	sb.append("-- 二级菜单\r\n");
        	lastIndex ++;
        	//二级菜单生成完后返回的索引
        	int current_end = 0;
        	current_end = lastIndex;
        	for(int i=0;i<menulist.size();i++){
        		MenuEntity menu = menulist.get(i);
        		sb.append("-- "+menu.getTableDesc()+"\r\n");
				String firstname = menu.getTopName();
				int pid = (int) FirstMenuMap.get(firstname);
				String currentname = menu.getTableDesc();
				String entityname = GenEntityMysqlUtil.initcap(menu.getTableName());
				entityname = StringUtil.firstChar2Little(entityname);
				Map<String, Object> map = getSencodeMenuSql(current_end, pid, currentname, entityname);
				sb.append(map.get("sql"));
				current_end = (int) map.get("end_index");
				current_end ++;
        	}
        	return sb.toString();
        }
        
        public static void main(String[] args) throws Exception{
        	DbEntity dbEntity = new DbEntity("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/lisendb", "root", "root");
        	//顶级菜单名称，以英文逗号分隔
        	String modes = "新闻管理,权限管理,会员管理,分类管理,店铺管理";
        	//二级菜单
        	List<MenuEntity> menulist = new ArrayList<MenuEntity>();
        	menulist.add(new MenuEntity("会员管理","t_admin","管理员列表"));
        	menulist.add(new MenuEntity("会员管理","t_role","角色列表"));
        	menulist.add(new MenuEntity("分类管理","t_type","分类列表"));
        	//执行创建全部菜单方法
        	String sb = createAllMenu(modes, dbEntity,menulist,"menuelement");
        	System.out.println(sb);
        }

}