package ${base_packge}.${type_action};

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import ${base_packge}.commons.ResultData;
import ${base_packge}.constant.SysConstants;
import ${base_packge}.${type_model}.${table.entityName?cap_first};
import ${base_packge}.${type_service}.${table.entityName?cap_first}Service;
import ${base_packge}.util.ObjectMapUtil;
import ${base_packge}.util.ReflectionModelUtil;
import ${base_packge}.util.StringUtils;
import ${base_packge}.util.json.JsonUtil;
import ${base_packge}.util.log.util.CustomerLogUtil;

/**   
* @Title: ${table.entityName?cap_first}Action.java 
* @Package ${base_packge}.${type_action}
* @Description: ${table.table_description}
* @author ${author}
* @date ${current_now}
* @version V1.0   
* create by codeFactory
*/
public class ${table.entityName?cap_first}Action extends BaseAction{
	Logger logger = Logger.getLogger(${table.entityName?cap_first}Action.class);
	
	 /**@Fields serialVersionUID : TODO*/ 
	private static final long serialVersionUID = 1L;
	String actionFilePath = "${table.entityName?uncap_first}";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		if(uri.endsWith("list")){
			list(req,resp);
		}else if(uri.endsWith("edit")){
			edit(req, resp);
		}else if(uri.endsWith("delete")){
			delete(req, resp);
		}else if(uri.endsWith("save")){
			save(req, resp);
		}else{
			resp.sendError(400, "未找到对应页面");
		}
	}

	/** 
	* @author hezhoujun
	* @Title: list 
	* @Description: 查询列表
	* @param req
	* @param resp void
	*/
	void list(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		try {
			${table.entityName?cap_first} ${table.entityName?uncap_first} = ReflectionModelUtil.getObjectModelRequest(${table.entityName?cap_first}.class, req);
			Map<String, Object> maps = ObjectMapUtil.obj2Map(${table.entityName?uncap_first});
			ResultData data = ${table.entityName?cap_first}Service.list(maps);
			req.setAttribute("resultData", data);
			//获取处理结果信息
			//save/update/delete
			req.setAttribute("operation", req.getParameter("operation"));
			//处理结果 1成功 0失败
			req.setAttribute("operation_result", req.getParameter("result"));
			req.getRequestDispatcher(SysConstants.PAGE_BASE_PATH+actionFilePath+"/list.jsp").forward(req, resp);
		} catch (Exception e) {
			CustomerLogUtil.${author}Log(logger, "查询列表出错，error："+e.getMessage(), e.fillInStackTrace());
			print(JsonUtil.returnJsonInfo(500, e.fillInStackTrace().toString()), resp);
		}
	}
	
	/** 
	* @author hezhoujun
	* @Title: edit 
	* @Description: 编辑、新增页面
	* @param req
	* @param resp void
	*/
	void edit(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		try {
			String id = req.getParameter("id");
			if(StringUtils.isNotBlank(id) && StringUtils.isNumber(id)){
				${table.entityName?cap_first} ${table.entityName?uncap_first} = ${table.entityName?cap_first}Service.getById(Long.parseLong(id));
				req.setAttribute("${table.entityName?uncap_first}", ${table.entityName?uncap_first});
			}
			req.getRequestDispatcher(SysConstants.PAGE_BASE_PATH+actionFilePath+"/edit.jsp").forward(req, resp);
		} catch (Exception e) {
			CustomerLogUtil.${author}Log(logger, "到编辑页面出错，error："+e.getMessage(), e.fillInStackTrace());
			print(JsonUtil.returnJsonInfo(500, e.fillInStackTrace().toString()), resp);
		}
	}
	
	/** 
	* @author hezhoujun
	* @Title: save 
	* @Description: 保存
	* @param request
	* @param response void
	*/
	void save(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		int code = 0;
		String result = null;
		try {
			${table.entityName?cap_first} ${table.entityName?uncap_first} = ReflectionModelUtil.getObjectModelRequest(${table.entityName?cap_first}.class, req);
			<#if table.primary_colmun?exists>
			//修改
			if(null != ${table.entityName?uncap_first}.get${table.primary_colmun?cap_first}() && ${table.entityName?uncap_first}.get${table.primary_colmun?cap_first}() > 0){
				${table.entityName?cap_first}Service.update(${table.entityName?uncap_first});
			}else{
			//保存
				${table.entityName?uncap_first} = ${table.entityName?cap_first}Service.save(${table.entityName?uncap_first});
			}
			</#if>
			code = 1;
			result = JsonUtil.returnJsonInfo(code, "");
		} catch (Exception e) {
			result = JsonUtil.returnJsonInfo(500, e.fillInStackTrace().toString());
			CustomerLogUtil.${author}Log(logger, "保存出错，error："+e.getMessage(), e.fillInStackTrace());
		}
		print(result, resp);
	}
	
	/** 
	* @author hezhoujun
	* @Title: delete 
	* @Description: 删除
	* @param request
	* @param response void
	*/
	void delete(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		String id = req.getParameter("id");
		String result = null;
		int code = 0;
		if(StringUtils.isNotBlank(id) && StringUtils.isNumber(id)){
			try {
				int n = ${table.entityName?cap_first}Service.delete(Long.parseLong(id));
				code = n > 0 ? 1 : 0;
				result = JsonUtil.returnJsonInfo(code, "");
			}  catch (Exception e) {
				result = JsonUtil.returnJsonInfo(500, e.fillInStackTrace().toString());
				CustomerLogUtil.${author}Log(logger, "删除出错，error："+e.getMessage(), e.fillInStackTrace());
			}
		}
		print(result, resp);
	}
}
