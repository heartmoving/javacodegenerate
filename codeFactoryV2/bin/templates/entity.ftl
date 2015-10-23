package ${base_packge}.${type_model};

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import ${base_packge}.commons.BaseBean;
<#if table.f_util == true>
import java.util.*;
</#if>
<#if table.f_sql == true>
import java.sql.*;
</#if>
<#if table.f_math == true>
import import java.math.*;
</#if>

/**
* @TableName: ${table.tablename} 
* @Package: ${base_packge}.${type_model}
* @Title:${table.entityName}.java 
* @Description: ${table.table_description} 
* @author: ${author}
* @date: ${current_now}
* @version V1.0    
* create by codeFactory
*/
@Entity
@Table(name="${table.tablename}")
public class ${table.entityName} extends BaseBean{
	<#list table.fieldNames as field>
	/**
	*@Fields ${field} :${table.remaks[field_index]}
	*/
	<#if field == table.primary_colmun>
	@Id
	</#if>
	@Column(name="${table.table_filelds[field_index]}")
	private ${table.filedTypes[field_index]} ${field};
	</#list>
	<#list table.fieldNames as field>
	public void set${field?cap_first}(${table.filedTypes[field_index]} ${field?uncap_first}){
		this.${field?uncap_first}=${field?uncap_first};
	}

	public ${table.filedTypes[field_index]} get${field?cap_first}(){
		return ${field?uncap_first};
	}
	</#list>

	public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("GAdmin[");
	<#list table.fieldNames as field>
	if(null != ${field?uncap_first} && !"".equals(${field?uncap_first}) ){
		sb.append("${field?uncap_first}= "+${field?uncap_first}+",");
	}
	</#list>
	sb.append("]");
	String toStr =sb.toString();
	return toStr.substring(0,toStr.indexOf(",]"))+"]";
	}

}

