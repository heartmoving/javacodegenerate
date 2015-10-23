package com.gogotown.utils.database;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.gogotown.commons.Constans;
import com.gogotown.entity.DbEntity;
import com.gogotown.entity.FileEntity;
import com.gogotown.entity.TableEntity;
import com.gogotown.utils.StringUtil;

/**   
* @Title: GenEntityMysqlUtil.java 
* @Package com.gogotown.utils 
* @Description: 通过表生成java类
* @author hezhoujun
* @date 2014-12-19 上午11:31:28 
* @version V1.0   
*/
public class GenEntityMysqlUtil {
	/**
	 * 连接数据库，查询表字段、类型、写到指定目录
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	private static TableEntity entityGenerate(String tablename,DbEntity dbEntity,FileEntity fileEntity) throws Exception{
    	//创建连接
    	Connection conn = null;
		//查要生成实体类的表
    	String sql = "select * from " + tablename;
    	TableEntity table = new TableEntity();
    	PreparedStatement pStemt = null;
    	try {
			Class.forName(dbEntity.getDriver());
    		conn = DriverManager.getConnection(dbEntity.getUrl(),dbEntity.getUser(),dbEntity.getPassword());
			pStemt = conn.prepareStatement(sql);
			ResultSetMetaData rsmd = pStemt.getMetaData();
			int size = rsmd.getColumnCount();	//统计列
			//表备注
			String table_description =  MySqlTabComUtil.getTableCommentByName(tablename,dbEntity);
			//表字段注释
			Map<String, String> columnComments = MySqlColComUtil.getTableColumnComments(tablename,dbEntity);
			String [] colnames = new String[size];
			String [] colTypes = new String[size];
			int [] colSizes = new int[size];
			String [] remaks = new String[size];
			String [] fieldNames = new String[size];
			String [] filedTypes = new String[size];
			boolean f_util = false;
			boolean f_sql = false;
			boolean f_math = false;
			//过滤字段类型
			for (int i = 0; i < size; i++) {
				colnames[i] = rsmd.getColumnName(i + 1);
				colTypes[i] = rsmd.getColumnTypeName(i + 1);
				colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
				remaks[i] = columnComments.get(rsmd.getColumnName(i + 1));
				fieldNames[i] = first2little(initcap(colnames[i]));
				filedTypes[i] = sqlType2JavaType(colTypes[i]);
//				System.out.println(colnames[i] + " "+ colTypes[i]+" "+remaks[i] + " "+colSizes[i]);
				if(colTypes[i].equalsIgnoreCase("datetime")){
					f_util = true;
				}
				if(colTypes[i].equalsIgnoreCase("timestamp")){
					f_util = true;
				}
				if(colTypes[i].equalsIgnoreCase("image") || colTypes[i].equalsIgnoreCase("text")){
					f_sql = true;
				}
				if(filedTypes[i].equalsIgnoreCase("BigDecimal")){
					f_math = true;
				}
				if(colSizes[i] >= 11 && "Integer".equalsIgnoreCase(filedTypes[i])){
					colTypes[i] = "bigint";
				}
			}
			table.setColSizes(colSizes);
			table.setF_math(f_math);
			table.setF_sql(f_sql);
			table.setF_util(f_util);
			table.setRemaks(remaks);
			table.setTable_description(table_description);
			table.setColnames(colnames);
			table.setColTypes(colTypes);
			table.setFieldNames(fieldNames);
			table.setFiledTypes(filedTypes);
			table.setTablename(initcap(tablename));
			
			String content = parse(colnames,colTypes,colSizes,fileEntity,table);
//			System.out.println(content);
			//主键
			table.setPrimary_colmun(columnComments.get(Constans.PRIMARY_COLUMN_TAB));
			String projectPath = fileEntity.getProjectPath();
				projectPath = projectPath + ((projectPath.endsWith("/") || projectPath.endsWith("\\")) ? "java" : "/java");
				String document = StringUtil.getFilePath(projectPath, fileEntity.getBasePackage(), Constans.TYPE_MODEL);
				File d_file = new File(document);
				if(!d_file.exists()) d_file.mkdirs();
				String outputPath = document+initcap(tablename) + ".java";
				File file = new File(outputPath);
				// 默认情况下已经存在的文件不需要生成代码
				//是否覆盖 true：覆盖
				if (fileEntity.isIs_cover() || !file.exists()) {
					FileWriter fw = new FileWriter(file);
					PrintWriter pw = new PrintWriter(fw);
					pw.println(content);
					pw.flush();
					pw.close();
					System.out.println("生成实体类："+file.getPath()+" 成功！");
				}else{
					System.err.println("文件："+outputPath+" 已经存在.");
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(null != conn)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	return table;
	}

	/**
	 * 功能：生成实体类主体代码
	 * @param colnames 字段名称
	 * @param colTypes 字段类型
	 * @param colSizes 字段长度
	 * @return
	 */
	private static String parse(String[] colnames, String[] colTypes, int[] colSizes,FileEntity fileEntity,TableEntity table) {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		sb.append("package " +fileEntity.getBasePackage() +"."+Constans.TYPE_MODEL+ ";\r\n");
		//判断是否导入工具包
		if(table.isF_util()){
			sb.append("import java.util.Date;\r\n");
		}
		if(table.isF_sql()){
			sb.append("import java.sql.*;\r\n");
		}
		if(table.isF_math()){
			sb.append("import java.math.BigDecimal;\r\n");
		}
		sb.append("\r\n");
		//注释部分
		sb.append("/**\r\n");
		sb.append("* @TableName: "+table.getTablename()+" \r\n");
		sb.append("* @Package: "+fileEntity.getBasePackage()+" \r\n");
		sb.append("* @Title:"+initcap(table.getTablename())+".java \r\n");
		sb.append("* @Description: "+table.getTable_description()+" \r\n");
		sb.append("* @author: "+fileEntity.getAuthorName()+" \r\n");
		sb.append("* @date: "+sdf.format(new Date())+" \r\n");
		sb.append("* @version V1.0    \r\n");
		sb.append("*/ \r\n");
		//实体部分
		sb.append("public class " + initcap(table.getTablename()) + "{\r\n");
		processAllAttrs(sb,table);//属性
		processAllMethod(sb,table);//get set方法
		//toString 方法
		@SuppressWarnings("static-access")
		StringBuilder toStringSb = createToString(table.getTablename(), table.getFieldNames(),table);
		sb.append(toStringSb+"\r\n");
		sb.append("}\r\n");
		return sb.toString();
	}
	
	/**
	 * 功能：生成所有属性
	 * @param sb
	 */
	@SuppressWarnings("static-access")
	private static void processAllAttrs(StringBuffer sb,TableEntity tableEntity) {
		String remaks[] = tableEntity.getRemaks();
		String colnames[] = tableEntity.getColnames();
		String colTypes[] = tableEntity.getColTypes();
		for (int i = 0; i < colnames.length; i++) {
			if( null != remaks[i] && !"".equals(remaks[i])){
				sb.append("\t/**\r\n");
				sb.append("\t*@Fields "+first2little(initcap(colnames[i]))+" :"+remaks[i]+"\r\n");
				sb.append("\t*/\r\n");
			}
			sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + first2little(initcap(colnames[i])) + ";\r\n");
		}
		
	}

	/**
	 * 功能：生成所有方法
	 * @param sb
	 */
	@SuppressWarnings("static-access")
	private static void processAllMethod(StringBuffer sb,TableEntity table) {
		String colnames[] = table.getColnames();
		String colTypes[] = table.getColTypes();
		for (int i = 0; i < colnames.length; i++) {
			sb.append("\tpublic void set" + initcap(colnames[i]) + "(" + sqlType2JavaType(colTypes[i]) + " " + 
					first2little(initcap(colnames[i]))+ "){\r\n");
			sb.append("\t\t\tthis." + first2little(initcap(colnames[i]))+ "=" + first2little(initcap(colnames[i])) + ";\r\n");
			sb.append("\t}\r\n");
			sb.append("\r\n");
			sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + initcap(colnames[i]) + "(){\r\n");
			sb.append("\t\t\treturn " + first2little(initcap(colnames[i])) + ";\r\n");
			sb.append("\t}\r\n");
			sb.append("\r\n");
		}
		
	}
	
	/**
	 * 功能：将输入字符串的首字母改成大写
	 * @param str 表字段
	 * @return
	 */
	public static String initcap(String str) {
		StringBuilder sb = new StringBuilder();
		if(str.indexOf("_") > 0){
			String strArr[] = str.split("_");
			for (String string : strArr) {
				char[] ch = string.toCharArray();
				if(ch[0] >= 'a' && ch[0] <= 'z'){
					ch[0] = (char)(ch[0] - 32);
				}
				sb.append(new String(ch));
			}
		}else{
			char[] ch = str.toCharArray();
			if(ch[0] >= 'a' && ch[0] <= 'z'){
				ch[0] = (char)(ch[0] - 32);
			}
			sb.append(new String(ch));
		}
		return sb.toString();
	}
	
	/**
	 * 当首字母大写的时候，将首字母转化为小写
	 * @param str关键字
	 * @return
	 */
	public static String first2little(String str){
		char ch[]  = str.toCharArray();
		if(ch[0] >= 'A' && ch[0] <= 'Z'){
			ch[0] = (char)(ch[0] + 32);
			return new String(ch);
		}else{
			return str;
		}
	}

	/**
	 * 功能：获得列的数据类型
	 * @param sqlType 数据库中字段的类型
	 * @return
	 */
	private static String sqlType2JavaType(String sqlType) {
		if(sqlType.equalsIgnoreCase("bit")){
			return "boolean";
		}else if(sqlType.equalsIgnoreCase("tinyint")){
			return "Integer";
		}else if(sqlType.equalsIgnoreCase("smallint")){
			return "Integer";
		}else if(sqlType.equalsIgnoreCase("int")){
			return "Integer";
		}else if(sqlType.equalsIgnoreCase("bigint")){
			return "Long";
		}else if(sqlType.equalsIgnoreCase("float")){
			return "Float";
		}else if(sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric") 
				|| sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money") 
				|| sqlType.equalsIgnoreCase("smallmoney")){
			return "Double";
		}else if(sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") 
				|| sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar") 
				|| sqlType.equalsIgnoreCase("text")){
			return "String";
		}else if(sqlType.equalsIgnoreCase("datetime")){
			return "Date";
		}else if(sqlType.equalsIgnoreCase("image")){
			return "Blod";
		}else if(sqlType.equalsIgnoreCase("timestamp")){
			return "Date";
		}else if(sqlType.equalsIgnoreCase("int unsigned")){
			return "Integer";
		}else if(sqlType.equalsIgnoreCase("tinyint unsigned")){
			return "Integer";
		}else if(sqlType.equalsIgnoreCase("mediumint unsigned")){
			return "Integer";
		}else if(sqlType.equalsIgnoreCase("date")){
			return "Date";
		}else if(sqlType.equalsIgnoreCase("smallint unsigned")){
			return "Integer";
		}else if(sqlType.equalsIgnoreCase("mediumint")){
			return "Integer";
		}else if(sqlType.equalsIgnoreCase("DECIMAL UNSIGNED")){
			return "BigDecimal";
		}
		return null;
	}
	
	/**
	 * public String toString() {
		return "GMembers []";
		//id=" + id + ", mobile=" + mobile + ", email=" + email
	}
	 * */
	@SuppressWarnings("static-access")
	public static StringBuilder createToString(String tablename,String[] fieldNames,TableEntity table){
		StringBuilder sb = new StringBuilder();
		sb.append("\tpublic String toString() {\r\n");
		sb.append("\tStringBuilder sb = new StringBuilder();\r\n");
		sb.append("\tsb.append(\""+initcap(tablename)+"[\");\r\n");
		String [] filedTypes = table.getFiledTypes();
		for (int i = 0; i < fieldNames.length; i++) {
			if("String".equals(filedTypes[i])){
				sb.append("\tif(null != "+fieldNames[i]+" && !\"\".equals("+fieldNames[i]+") ){\r\n");
			}else{
				sb.append("\tif(null != "+fieldNames[i]+"){\r\n");
			}
			sb.append("\t\tsb.append(\""+fieldNames[i]+"= \"+" +fieldNames[i]+"+\",\");\r\n");
			sb.append("\t}\r\n");
		}
		sb.append("\tsb.append(\"]\");\r\n");
//		sb.append("\treturn sb.toString();\r\n");
		sb.append("\tString toStr =sb.toString();\r\n");
		sb.append("\treturn toStr.substring(0,toStr.indexOf(\",]\"))"+"+\"]\";\r\n");
		sb.append("\t}\r\n");
		return sb;
	}
	
	/** 
	* @author hezhoujun
	* @Title: generateEntityRun 
	* @Description: 通过表生成实体类方法入口
	* @param projectPath 代码生成到的位置 com的上一层
    * @param packagePath 指定实体生成所在包的路径
    * @param authorName 作者名字
    * @param tableName 表名
    * @param isRecover 是否覆盖已经生成的代码 true 覆盖   false 不覆盖
	* @return void    返回类型 
	*/
	public static TableEntity generateEntityRun(String tablename,DbEntity dbEntity,FileEntity file){
		TableEntity tableEntity = null;
		try {
			tableEntity = entityGenerate(tablename, dbEntity, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return tableEntity;
	}
	
	public static void main(String[] args) {
		DbEntity dbConstans = new DbEntity("com.mysql.jdbc.Driver", "jdbc:mysql://192.168.0.155:3306/lisendb", "root", "root");
		String tableName = "admin";
		FileEntity file = new FileEntity("E:/testpage/","com.lisen.entity","hezhoujun",false);
		GenEntityMysqlUtil.generateEntityRun(tableName,dbConstans,file);
		/*if(table != null){
			String[] filedArr = table.getFieldNames();
			StringBuilder sb = createToString(tableName, filedArr,table);
			System.out.println(sb);
		}*/
	}
}
