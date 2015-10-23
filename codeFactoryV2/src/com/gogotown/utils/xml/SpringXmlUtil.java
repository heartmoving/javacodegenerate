package com.gogotown.utils.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.gogotown.commons.Constans;
import com.gogotown.entity.FileEntity;
import com.gogotown.entity.FlagEntity;
import com.gogotown.entity.TableEntity;
import com.gogotown.utils.StringUtil;
import com.gogotown.utils.database.GenEntityMysqlUtil;

public class SpringXmlUtil {
	/** 
	* @author hezhoujun
	* @Title: getClassNameByType 
	* @Description: 获取classesName
	* @param pakage
	* @param entityName
	* @param type
	* @return String
	*/
	private static String getClassNameByType(String pakage,String entityName,String type){
		if("dao".equals(type)){
			return pakage.replaceAll("/", ".") +".dao.impl."+ entityName+"DAOImpl";
		}else{
			return pakage.replaceAll("/", ".") +".service.impl."+ entityName+"ServiceImpl";
		}
	}
	/** 
	* @author hezhoujun
	* @Title: updateDaoServiceXml 
	* @Description: 对dao xml和service xml进行追加
	* @param className 类名
	* @param beanName id名
	* @param xmlPath xml路径
	* @param attrName property的属性的name
	* @param attrRef property的属性的ref
	* @param type 类型 dao or service
	* @return String 处理结果
	*/
	@SuppressWarnings("unchecked")
	public void updateDaoServiceXml(String className,String beanName,String xmlPath,String attrName,String attrRef,String type){
		SAXReader reader = new SAXReader(); 
		   Document doc;
		try {
			File xmlFile = new File(xmlPath);
			if(xmlFile.exists()){
				doc = reader.read(xmlPath);
				Element rootElement = doc.getRootElement(); 
				Element addressElement = rootElement.addElement("bean");
				List<Element> nodes = rootElement.elements("bean");
				boolean createFlag = true;
				for (Element element : nodes) {
					if(beanName.equals(element.attributeValue("id"))){
						createFlag = false;
					}
				}
				if(createFlag){
					addressElement.addAttribute("id", beanName);
					addressElement.addAttribute("class", className);
					Element child = addressElement.addElement("property");
					child.addAttribute("name", attrName);
					child.addAttribute("ref", attrRef);
//					OutputFormat format = new OutputFormat();
					OutputFormat format = OutputFormat.createPrettyPrint();// 创建文件输出的时候，自动缩进的格式
					format.setEncoding("UTF-8");
					XMLWriter writer = new XMLWriter(new FileWriter(xmlPath), format);
					writer.write(doc);
					writer.close();
					System.out.println("处理xml： "+xmlPath+" 成功");
				}else{
					System.err.println("对应["+beanName+"]的值，已经在"+xmlPath+" 中配置了..");
				}
			}else{
				System.err.println("xml： "+xmlPath+" 不存在");
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/** 
	* @author hezhoujun
	* @Title: updateXmlRun 
	* @Description: 对dao xml和service xml进行追加 方法入口
	* @param className 类名
	* @param beanName id名
	* @param xmlPath xml路径
	* @param attrName property的属性的name   type为dao事可为null
	* @param attrRef property的属性的ref   type为dao事可为null
	* @param type 类型 dao or service
	* @return String 处理结果
	*/
	public static void updateXmlRun(String className,String beanName,String xmlPath,String attrName,String attrRef,String type){
		if("dao".equals(type)){
			attrName = attrRef = "sqlSessionFactory";
		}
		if(StringUtil.isNotBlank(className) && StringUtil.isNotBlank(beanName) && StringUtil.isNotBlank(xmlPath) && StringUtil.isNotBlank(attrName) && StringUtil.isNotBlank(attrRef) && StringUtil.isNotBlank(type)){
			new SpringXmlUtil().updateDaoServiceXml(className, beanName, xmlPath, attrName, attrRef, type);
		}else{
			System.err.println("生成spring dao service xml参数不完整..");
		}
	}
	
	/**
	 * 修改sql mapper文件
	 * @param className
	 * @param xmlPath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String updateSqlMapXml(String classFullPath,String xmlPath){
		String info = "";
		String className = classFullPath.substring(classFullPath.lastIndexOf(".")+1, classFullPath.length());
		String alias = GenEntityMysqlUtil.first2little(className);
		SAXReader reader = new SAXReader(); 
		   Document doc;
		try {
			doc = reader.read(xmlPath);
			Element rootElement = doc.getRootElement(); 
			Element typeElement = rootElement.element("typeAliases");
			List<Element> nodes = typeElement.elements("typeAlias");
			boolean createFlag = true;
			for (Element element : nodes) {
				System.out.println(alias+"  "+element.attributeValue("alias"));
				if(alias.equals(element.attributeValue("alias"))){
					System.out.println("this.....");
					createFlag = false;
				}
			}
			if(createFlag){
				Element typeChild = typeElement.addElement("typeAlias");
				typeChild.addAttribute("type", classFullPath);
				typeChild.addAttribute("alias", alias);
				Element mappersElement = rootElement.element("mappers");
				Element mapperChild = mappersElement.addElement("mapper");
				mapperChild.addAttribute("resource", "resource/mapper/"+className+"Mapper.xml");
//				OutputFormat format = new OutputFormat();
				OutputFormat format = OutputFormat.createPrettyPrint();// 创建文件输出的时候，自动缩进的格式
				format.setEncoding("UTF-8");
				XMLWriter writer = new XMLWriter(new FileWriter(xmlPath), format);
				writer.write(doc);
				writer.close();
				info = "success:处理sqlmap xml成功";
			}else{
				info = "success:对应的alias的值已经存在";
			}
		}catch (Exception e) {
			info = "failed:出现异常："+e.getMessage();
			e.printStackTrace();
		} 
		return info;
	}
	
	/** 
	* @author hezhoujun
	* @Title: createDaoServiceXml 
	* @Description: 创建dao service xml
	* @param fileEntity
	* @param flag
	* @param table void
	*/
	public static void createDaoServiceXml(FileEntity fileEntity,FlagEntity flag,TableEntity table){
		if(fileEntity != null && flag.isCreateDaoServiceXml()){
			String bigFirstName = StringUtil.firstChar2Up(table.getEntityName());
			String littleFirstName = StringUtil.firstChar2Little(table.getEntityName());
			String springXmlPath =  StringUtil.getFilePath(fileEntity.getProjectPath(), Constans.XML_SPRING_DAO_PATH, "spring-service.xml");
			String springDaoPath =  StringUtil.getFilePath(fileEntity.getProjectPath(), Constans.XML_SPRING_DAO_PATH, "spring-dao.xml");
			String daoClassName = getClassNameByType(fileEntity.getBasePackage(), bigFirstName, "dao");
			updateXmlRun(daoClassName, bigFirstName+"DAOImpl", springDaoPath, null, null, "dao");
			String serviceClassName = getClassNameByType(fileEntity.getBasePackage(), bigFirstName, "service");
			updateXmlRun(serviceClassName, bigFirstName+"ServiceImpl", springXmlPath, littleFirstName+"DAO", bigFirstName+"DAOImpl", "service");
		}
	}
	
	public static void main(String[] args) {
//		String className = "com.lisen.entity.Hezhoujun";
//		String beanName = "testBean02";
//		String xmlPath = "H:/Projects/tlts/LiSenPro/WebContent/WEB-INF/sqlmap-config.xml";
//		String attrName = "x22";
//		String attrRef = "x33";
//		String info = updateSqlMapXml(className, xmlPath);
	}
}
