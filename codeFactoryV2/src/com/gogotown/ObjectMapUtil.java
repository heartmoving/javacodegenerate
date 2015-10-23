package com.gogotown;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjectMapUtil {
	
    /** 
     * @author hezhoujun
     * @Title: obj2Map 
     * @Description: 将对象转化为map
     * @param thisObj
     * @return Map<String,Object>
     */
     public static Map<String, Object> obj2Map(Object thisObj){
       Map<String, Object> map = new HashMap<String, Object>();
       Class<?> c;
       try  
       {
         c = Class.forName(thisObj.getClass().getName());  
         Method[] m = c.getMethods();  
         for (int i = 0; i < m.length; i++)  
         {  
           String method = m[i].getName();  
           if (method.startsWith("get"))  
           {  
             try{  
             Object value = m[i].invoke(thisObj);  
             if (value != null)  
             {  
               String key=method.substring(3);  
               key=key.substring(0,1).toUpperCase()+key.substring(1);  
               map.put(getFiledFromGetMethod(method), value);  
             }  
             }catch (Exception e) {  
               System.out.println("error:"+method);  
             }  
           }  
         }  
       }  
       catch (Exception e){  
         e.printStackTrace();  
       }  
       return map;  
     }
     
     /** 
    * @author hezhoujun
    * @Title: getFiledFromGetMethod 
    * @Description: 取出get方法对应的字段名称
    * getName -> name
    * @param getMethodName
    * @return String
    */
    private static String getFiledFromGetMethod(String getMethodName){
 		getMethodName = getMethodName.substring(getMethodName.indexOf("get")+3, getMethodName.length());
 		char[] chars=new char[1];  
         chars[0]=getMethodName.charAt(0);  
         String temp=new String(chars);  
         if(chars[0]>='A'  &&  chars[0]<='Z'){
         	getMethodName  = getMethodName.replaceFirst(temp,temp.toLowerCase());
         }
         return getMethodName;
     }
     
     public static void main(String[] args) {
		String a = "getName";
		System.out.println(getFiledFromGetMethod(a));
	}
}
