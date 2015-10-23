<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../commons/tag.jsp" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../commons/head.jsp">
<jsp:param value="Y" name="isSencond"/>
<jsp:param value="saveObjForm" name="formId"/>
<jsp:param value="Y" name="needXhEdit"/>
</jsp:include>
<title>${table.table_description}编辑</title>
</head>
<body>
<div class="pd30">
<div class="row">
<div class="col-md-10">
<a href="javascript:history.go(-1);" role="button" class="btn btn-success">&lt;&lt;返回</a>
<button type="button" class="btn btn-success"  onclick="saveObj('${r"$"}{${table.entityName?uncap_first} == null ?'save':'update'}')">保存</button> 
</div>
</div>
  <form data="editForm" class="form-horizontal" id="saveObjForm">
	<#--主键隐藏域-->
 <input type="hidden"  name="${table.primary_colmun?if_exists}" value="${r"${"}${table.entityName?uncap_first}.${table.primary_colmun?if_exists}}">
    <table class="table table-bordered mgt10" >
     <#list table.fieldNames as i>
 	<#--排除主键生成form字段-->
     <#if table.primary_colmun != table.fieldNames[i_index]>
        <tr>
            <td  class="text-r" style="width: 15%">${table.remaks[i_index]}</td>
            <#if table.fieldNames[i_index] != 'createdate' || table.fieldNames[i_index] != 'changedate'>
            <td>
        		<#if table.filedTypes[i_index] != "Date" && table.colSizes[i_index] &lt; 10000>
		     	<input type="text" style="width:20%" name="${table.fieldNames[i_index]}"  placeholder="请输入${table.fieldNames[i_index]}" value="${r"${"}${table.entityName?uncap_first}.${table.fieldNames[i_index]}}" class="form-control"/>
		     	</#if>
		     	<#if table.filedTypes[i_index] == "Date">
		     	<input type="text" style="width:20%;height:33px;" onClick="WdatePicker()"  name="${table.fieldNames[i_index]}"  placeholder="日期" value="<fmt:formatDate value="${r"${"}${table.entityName?uncap_first}.${table.fieldNames[i_index]}}" pattern="yyyy-MM-dd HH:mm:ss"/>" class="form-control Wdate"/>
		     	</#if>
		     	<#if table.colSizes[i_index] &gt; 10000>
		     	<textarea id="xh_editor" name="${table.fieldNames[i_index]}" cols="100" rows="8" style="width:98%;height:300px;visibility:hidden;">${r"${"}${table.entityName?uncap_first}.${table.fieldNames[i_index]}}</textarea>
		     	</#if>
            </td>
            </#if>
        </tr>
      </#if>
	</#list>
    </table>
   </form>
</div>
</body>
</html>