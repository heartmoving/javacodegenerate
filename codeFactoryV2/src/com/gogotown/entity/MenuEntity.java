package com.gogotown.entity;

public class MenuEntity {
	 /**@Fields tableName : 表名称*/ 
	private String tableName;
	 /**@Fields tableDesc : 描述*/ 
	private String tableDesc;
	 /**@Fields topName : 一级菜单名称*/ 
	private String topName;
	
	public MenuEntity(String topName,String tableName, String tableDesc) {
		super();
		this.tableName = tableName;
		this.topName = topName;
		this.tableDesc = tableDesc;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTopName() {
		return topName;
	}
	public void setTopName(String topName) {
		this.topName = topName;
	}
	public String getTableDesc() {
		return tableDesc;
	}
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	
	
}
