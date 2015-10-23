package com.gogotown.utils.freemarker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.gogotown.entity.FileEntity;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerUtil {
	
	/** 
	* @author hezhoujun
	* @Title: analysisTemplate 
	* @Description: freemarker加载模版生成文件
	* @param templateName 模版文件名称(action.ftl)
	* @param distinctPath 目标目录
	* @param distinctName 目标文件名称
	* @param datamap 需要编译的数据 (name : zhangsan)
	* @param isCover 是否覆盖 true 覆盖 false 不覆盖
	 * @throws IOException 
	*/
	public static void analysisTemplate(String templateName,String distinctPath,String distinctName,Map<?,?> datamap,FileEntity fileEntity) throws IOException{
		BufferedWriter bw = null;
		//生成文件保存的位置(完整路径:example:e:/xx/xx/xxActino.java)
		File distinctFile = getDistinctFile(distinctPath, distinctName,fileEntity.isIs_cover());
		if(distinctFile == null){
			System.err.println("文件:"+distinctPath+distinctName+" 已经存在！");
		}else{
			try {
				Configuration config=new Configuration();
				if(fileEntity.getTempateReadType() == 2){
					//通过引用项目template路径下读取start
					File file=new File(fileEntity.getTemplatePath());
					//设置要解析的模板所在的目录，并加载模板文件
					config.setDirectoryForTemplateLoading(file);
					//通过引用项目template路径下读取end
				}else{
					//通过生成器路径读取start
					config.setClassForTemplateLoading(FreemarkerUtil.class, "/templates");
					//通过生成器路径读取end
				}
				//设置包装器，并将对象包装为数据模型
				config.setObjectWrapper(new DefaultObjectWrapper());
				//获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
				Template template=config.getTemplate(templateName,"utf-8");
				//合并数据模型与模板
				//StringWriter stringWriter = new StringWriter(); 
				//输出到控制台
				//Writer out = new OutputStreamWriter(System.out);  
				//返回字符串
			  bw = new BufferedWriter(new FileWriter(distinctFile));
			  template.process(datamap, bw);
			  bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (TemplateException e) {
				e.printStackTrace();
			}finally{
				if(bw != null){
					bw.close();
				}
			}
			System.out.println("创建文件:"+distinctFile.getPath()+" 成功！");
		}
	}
	
	/** 
	* @author hezhoujun
	* @Title: getDistinctFile 
	* @Description: 根据文件路径+名称，自动生成文件夹及其文件
	* @param distinctPath
	* @param distinctName
	* @return
	* @throws IOException File
	*/
	private static File getDistinctFile(String distinctPath,String distinctName,boolean isCover) throws IOException{
		File document = new File(distinctPath);
		if(!document.exists()) document.mkdirs();
		if(!distinctPath.endsWith("/") || !distinctPath.endsWith("\\")){
			distinctPath += "/";
		}
		String fullPathFile = distinctPath+distinctName;
		File file = new File(fullPathFile);
		if(!file.exists() || isCover){
			file.createNewFile();	
		}else{
			return null;
		}
		return file;
	}
}
