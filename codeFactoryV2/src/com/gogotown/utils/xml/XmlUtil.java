package com.gogotown.utils.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.gogotown.commons.Constans;
import com.gogotown.entity.FileEntity;
import com.gogotown.entity.FlagEntity;
import com.gogotown.entity.TableEntity;
import com.gogotown.utils.StringUtil;

public class XmlUtil {
	
	/** 
	* @author hezhoujun
	* @Title: writeXml 
	* @Description: 写xml
	* @param doc
	* @param xmlPath
	* @throws IOException void
	*/
	private static void writeXml(Document doc,String xmlPath) throws IOException{
//			OutputFormat format = new OutputFormat();
			OutputFormat format = OutputFormat.createPrettyPrint();// 创建文件输出的时候，自动缩进的格式
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new FileWriter(xmlPath), format);
			writer.write(doc);
			writer.flush();
			writer.close();
	}
	
	@SuppressWarnings("unchecked")
	private static Document readXml(String xmlPath,String servletName,String fullClassPath,String urlPattern) throws Exception{
		SAXReader reader = new SAXReader(); 
		   Document doc = null;
			doc = reader.read(xmlPath);
			Element rootElement = doc.getRootElement(); 
			List<Element> nodes = rootElement.elements("servlet");
			//是否已经创建该节点
			boolean createFlag = true;
			for (Element element : nodes) {
				String s_name = element.element("servlet-name").getText();
				if(servletName.equals(s_name)){
					createFlag = false;
				}
			}
			if(createFlag){
				Element s_e_ele = rootElement.addElement("servlet");
				Element s_n_ele_1 = s_e_ele.addElement("servlet-name");
				s_n_ele_1.setText(servletName);
				Element s_class_ele = s_e_ele.addElement("servlet-class");
				s_class_ele.setText(fullClassPath);
				Element s_m_ele = rootElement.addElement("servlet-mapping");
				Element s_n_ele_2 = s_m_ele.addElement("servlet-name");
				s_n_ele_2.setText(servletName);
				Element u_p_ele = s_m_ele.addElement("url-pattern");
				u_p_ele.setText("/"+urlPattern+"/*");
			}else{
				return null;
			}
		return doc;
	}
	
	/** 
	* @author hezhoujun
	* @Title: createWebXml 
	* @Description: 创建web.xml
	* @param fileEntity
	* @param servletName
	* @param fullClassPath
	* @param urlPattern
	* @throws Exception void
	*/
	public static void createWebXml(TableEntity table,FileEntity fileEntity,FlagEntity flagEntity) throws Exception{
		if(fileEntity != null && flagEntity.isCreateWebXml()){
			String servletName = StringUtil.firstChar2Up(table.getEntityName());
			String urlPattern = StringUtil.firstChar2Little(table.getEntityName());
			String fullClassPath = fileEntity.getBasePackage()+ "." +Constans.TYPE_ACTION+"."+ servletName +"Action";
			String xmlPath =  StringUtil.getFilePath(fileEntity.getProjectPath(), Constans.WEB_XML_PATH, "web.xml");
			File xmlFile = new File(xmlPath);
			if(xmlFile.exists()){
				if(xmlPath.endsWith("/"))  xmlPath = xmlPath.substring(0, xmlPath.length()-1);
				Document document = readXml(xmlPath, servletName, fullClassPath, urlPattern);
				if(document != null){
					writeXml(document, xmlPath);
					System.out.println("生成web.xml "+xmlPath+" 成功");
				}else{
					System.err.println("对应["+servletName+"]的值，已经在"+xmlPath+" 中配置了..");
				}
			}else{
				System.err.println("web.xml:"+xmlPath+"不存在");
			}
		}
	}
	
}
