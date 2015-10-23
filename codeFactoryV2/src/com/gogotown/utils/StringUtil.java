package com.gogotown.utils;

import java.util.ArrayList;
import java.util.List;

/**   
* @Title: GoGoStringUtil.java 
* @Package com.gogotown.utils 
* @Description: 简单的字符串处理
* @author hezhoujun
* @date 2014-12-22 上午10:19:23 
* @version V1.0   
*/
public class StringUtil {
	
	public static boolean isNotBlank(String str){
		if(null != str && "" != str){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean IsBlank(String str){
		if(null == str || str == ""){
			return true;
		}else{
			return false;
		}
	}
	
	/** 
	* @author hezhoujun
	* @Title: getFilePath 
	* @Description: 根据项目路径、包路径、当前模块类型，拼接文件夹路径
	* @param projectPath
	* @param packagePath
	* @param modelType
	* @return String
	*/
	public static String getFilePath(String projectPath,String packagePath,String modelType){
		if(!projectPath.endsWith("/") || !projectPath.endsWith("\\")){
			projectPath += "/";
		}
		String document = projectPath + packagePath.replace(".", "/")+"/"+modelType+"/";
		return document;
	}
	
	/** 
	* @author hezhoujun
	* @Title: firstChar2Up 
	* @Description: 首字母大写
	* @param str
	* @return String
	*/
	public static String firstChar2Up(String str){
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	/** 
	* @author hezhoujun
	* @Title: firstChar2Little 
	* @Description: 首字母小写
	* @param str
	* @return String
	*/
	public static String firstChar2Little(String str){
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	public static void main(String[] args) {
		String str = getFilePath("e:/project/abc/", "com.gogotown.manage", "model");
		System.out.println(str);
	}
	
	/** 
	* @author hezhoujun
	* @Title: stringToList 
	* @Description: 将字符串根据分隔符转化为list
	* @param specialString
	* @param prefix 分隔符
	* @return List<Integer>
	*/
	public static List<String> stringToList(String specialString,String prefix){
		List<String> list = null;
		if(isNotBlank(specialString) && isNotBlank(prefix)){
			list = new ArrayList<String>();
			if(specialString.indexOf(prefix) > 0){
				String[] strArr = specialString.split(prefix);
				if(null != strArr && strArr.length > 0 ){
					for (String string : strArr) {
						list.add(string);
					}
				}
			}else{
				list.add(specialString);
			}
		}
		return list;
	}
}
