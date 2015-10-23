package com.gogotown.entity;

public class TableEntity {
		 /** 
		* @Fields tablename : 表名
		*/ 
		private String tablename = null;
		 /** 
		* @Fields table_description :表描述
		*/ 
		private String table_description = null;
		 /** 
		* @Fields colnames :列名类型数组
		*/ 
		private String[] colnames; 
		 /** 
		* @Fields colTypes : 列名类型数组
		*/ 
		private String[] colTypes; 
		 /** 
		* @Fields filedTypes : 对应实体类的类型
		*/ 
		private String[] filedTypes;
		 /** 
		* @Fields remaks :列注释
		*/ 
		private String[] remaks;
		 /** 
		* @Fields colSizes : 列名大小数组
		*/ 
		private int[] colSizes; 
		 /**@Fields primary_colmun : 主键*/ 
		private String primary_colmun;
		 /** 
		* @Fields f_util : 是否需要导入包java.util.*
		*/ 
		private boolean f_util = false; 
		 /** 
		* @Fields f_sql : 是否需要导入包java.sql.*
		*/ 
		private boolean f_sql = false; 
		private boolean f_math = false;
		 /** 
		* @Fields fieldNames : 实体类字段数组
		*/ 
		private String[] fieldNames;
		 /**@Fields table_filelds : 表字段数组*/ 
		private String[] table_filelds;
		 /**@Fields entityName : 实体类名称*/ 
		private String entityName;
		
		public String getTablename() {
			return tablename;
		}
		public void setTablename(String tablename) {
			this.tablename = tablename;
		}
		public String getTable_description() {
			return table_description;
		}
		public void setTable_description(String table_description) {
			this.table_description = table_description;
		}
		public String[] getRemaks() {
			return remaks;
		}
		public void setRemaks(String[] remaks) {
			this.remaks = remaks;
		}
		public int[] getColSizes() {
			return colSizes;
		}
		public void setColSizes(int[] colSizes) {
			this.colSizes = colSizes;
		}
		public boolean isF_util() {
			return f_util;
		}
		public void setF_util(boolean f_util) {
			this.f_util = f_util;
		}
		public boolean isF_sql() {
			return f_sql;
		}
		public void setF_sql(boolean f_sql) {
			this.f_sql = f_sql;
		}
		public boolean isF_math() {
			return f_math;
		}
		public void setF_math(boolean f_math) {
			this.f_math = f_math;
		}
		public String getPrimary_colmun() {
			return primary_colmun;
		}
		public void setPrimary_colmun(String primary_colmun) {
			this.primary_colmun = primary_colmun;
		}
		public String[] getColnames() {
			return colnames;
		}
		public void setColnames(String[] colnames) {
			this.colnames = colnames;
		}
		public String[] getColTypes() {
			return colTypes;
		}
		public void setColTypes(String[] colTypes) {
			this.colTypes = colTypes;
		}
		public String[] getFiledTypes() {
			return filedTypes;
		}
		public void setFiledTypes(String[] filedTypes) {
			this.filedTypes = filedTypes;
		}
		public String[] getFieldNames() {
			return fieldNames;
		}
		public void setFieldNames(String[] fieldNames) {
			this.fieldNames = fieldNames;
		}
		public String[] getTable_filelds() {
			return table_filelds;
		}
		public void setTable_filelds(String[] table_filelds) {
			this.table_filelds = table_filelds;
		}
		public String getEntityName() {
			return entityName;
		}
		public void setEntityName(String entityName) {
			this.entityName = entityName;
		}
		
}
