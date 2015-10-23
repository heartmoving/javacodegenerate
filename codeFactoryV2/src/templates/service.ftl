package ${base_packge}.${type_service};

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import ${base_packge}.commons.ResultData;
import ${base_packge}.${type_dao}.${table.entityName?cap_first}Dao;
import ${base_packge}.${type_model}.${table.entityName?cap_first};
import ${base_packge}.util.log.util.CustomerLogUtil;
import ${base_packge}.commons.Pager;

 /**   
* @Title: ${table.entityName?cap_first}Service.java 
* @Package ${base_packge}.${type_service}
* @Description: ${table.table_description}
* @author ${author}
* @date ${current_now}
* @version V1.0   
* create by codeFactory
*/
public class ${table.entityName?cap_first}Service {
	private static Logger logger = Logger.getLogger(${table.entityName?cap_first}Service.class);
	/** 
	* @author ${author}
	* @Title: save 
	* @Description: 保存
	* @param t
	* @return
	* @throws Exception 
	*/
	public static ${table.entityName?cap_first} save(${table.entityName?cap_first} t) throws Exception{
		${table.entityName?cap_first} a = null;
		try {
			a = ${table.entityName?cap_first}Dao.save(t);
		} catch (Exception e) {
			CustomerLogUtil.hezhoujunLog(logger, "保存出错，error："+e.getMessage(), e.fillInStackTrace());
			throw e;
		}
		return a;
	}
	
	/** 
	* @author ${author}
	* @Title: list 
	* @Description: 分页查询
	* @param maps
	* @return
	* @throws Exception ResultData
	*/
	public static ResultData list(Map<String, Object> maps) throws Exception{
		ResultData data = new ResultData();
		try {
			int count = ${table.entityName?cap_first}Dao.qryListObjectCount(${table.entityName?cap_first}.class, maps);
			int offset =Pager.getOffest();
			int pagesize =Pager.getPageSize();
			int current = offset / pagesize +1;
			int pages = count%pagesize == 0 ? count / pagesize : count/pagesize +1;
			maps.put("currentPage", Pager.getOffest());
			maps.put("pages", pages);
			maps.put("current", current);
			 List<${table.entityName?cap_first}> list= ${table.entityName?cap_first}Dao.qryListObject(${table.entityName?cap_first}.class, maps, offset, pagesize);
			data.setPageSize(pagesize);
			data.setTotal(count);
			data.setData(list);
			data.setMem(maps);
			data.setParam(maps);
		} catch (Exception e) {
			CustomerLogUtil.hezhoujunLog(logger, "分页查询出错，error："+e.getMessage(), e.fillInStackTrace());
			throw e;
		}
		return data;
	}
	
	/** 
	* @author ${author}
	* @Title: getList 
	* @Description: 获取列表
	* @param maps
	* @return
	* @throws Exception List<${table.entityName?cap_first}>
	*/
	public static List<${table.entityName?cap_first}> getList(Map<String, Object> maps) throws Exception{
		List<${table.entityName?cap_first}> list = null;
		try {
			list = ${table.entityName?cap_first}Dao.qryList(${table.entityName?cap_first}.class, maps);
		} catch (Exception e) {
			CustomerLogUtil.hezhoujunLog(logger, "查询列表出错，error："+e.getMessage(), e.fillInStackTrace());
			throw e;
		}
		return list;
	}
	
	/** 
	* @author ${author}
	* @Title: getById 
	* @Description: 根据id查询
	* @param id
	* @return
	* @throws Exception ${table.entityName?cap_first}
	*/
	public static ${table.entityName?cap_first} getById(Long ${table.primary_colmun?uncap_first}) throws Exception{
		${table.entityName?cap_first} t = null;
		try {
			t = ${table.entityName?cap_first}Dao.getById(${table.entityName?cap_first}.class, ${table.primary_colmun?uncap_first});
		} catch (Exception e) {
			CustomerLogUtil.hezhoujunLog(logger, "根据id查询出错，error："+e.getMessage(), e.fillInStackTrace());
			throw e;
		}
		return t;
	}
	
	/** 
	* @author ${author}
	* @Title: delete 
	* @Description: 根据id删除
	* @param id
	* @return
	* @throws Exception int
	*/
	public static int delete(Long ${table.primary_colmun?uncap_first}) throws Exception{
		return ${table.entityName?cap_first}Dao.delete(${table.entityName?cap_first}.class, ${table.primary_colmun?uncap_first});
	}
	
	/** 
	* @author ${author}
	* @Title: update 
	* @Description: 修改
	* @param t
	* @throws Exception void
	*/
	public static void update(${table.entityName?cap_first} t) throws Exception{
		${table.entityName?cap_first}Dao.update(t);
	}
	}