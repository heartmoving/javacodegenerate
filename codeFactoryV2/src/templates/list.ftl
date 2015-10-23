<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../commons/tag.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title>${table.table_description}列表</title>
<jsp:include page="../commons/head.jsp"/>
</head>
<body>
<%@include file="../commons/tip_info.jsp" %>
<div class="pd30">
<form id="searchForm" action="list" method="post" class="form-inline" >
<div class="row">
<div class="col-md-9">
 <a class="btn btn-success" href="edit">添加</a>
</div>
<div class="col-md-3">
<input type="text" class="form-control lg-width" placeholder="输入关键字" name="" value="" />
<button type="submit" class="btn btn-info">查询</button>
</div>
</div>
</form>
<table class="table table-striped table-bordered text-center mgt10">
<thead>
        <tr>
		<#list table.fieldNames as i>
		<th>${table.remaks[i_index]}</th>
        </#list>
      	<th>操作</th>
        </tr>
    </thead>
    <c:forEach items="${r"$"}{resultData.data}" var="obj" varStatus="i">
      <tr>
 		 <#list table.fieldNames as i>
 		 <td>
 		 <#if table.filedTypes[i_index] == "Date">
 		 <fmt:formatDate value="${r"$"}{obj.${table.fieldNames[i_index]}}" pattern="yyyy-MM-dd HH:mm:ss"/>
 		 </#if>
 		 <#if table.filedTypes[i_index] != "Date">
 		 ${r"$"}{obj.${table.fieldNames[i_index]}}
 		 </#if>
 		 </td>
      </#list>
      <td >
      	<div class="btn-group" role="group" >				      		      		 
      	    <a class="btn btn-link"  href="javascript:del('${r"$"}{obj.${table.primary_colmun} }');" role="button" ><span class="glyphicon glyphicon-trash"></span></a>		         	   
      	    <a class="btn btn-link" href="edit?id=${r"$"}{obj.${table.primary_colmun?if_exists}}&operation=edit" role="button"><span class="glyphicon glyphicon-edit"></span></a>
  			</div>
      </td>
    </tr>
     </c:forEach>
</table>
    <div class="pdl20 pdr20">
     <!-- 分页信息展示 -->
     <%@include file="../commons/pageinfo.jsp" %>
    <div class="col-sm-6">
    <ul class="pagination">
		  <!-- 分页 -->
		  <pg:pager url="list.do" maxPageItems="${r"$"}{resultData.pageSize }" items="${r"$"}{resultData.total}" export="currentPageNumber=pageNumber" scope="request" >
		  <pg:param name="pager.pagesize" value="${r"$"}{resultData.pageSize}" />
		  <jsp:include page="../commons/pager.jsp"/>
    	  </pg:pager>
	  </ul>
	  </div>
    </div>
    </div>
</body>
</html>
