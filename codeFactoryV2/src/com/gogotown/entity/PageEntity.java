package com.gogotown.entity;


public class PageEntity {
	private TableEntity table;
	
	public PageEntity(TableEntity table) {
		super();
		this.table = table;
	}

	public TableEntity getTable() {
		return table;
	}

	public void setTable(TableEntity table) {
		this.table = table;
	}
	
}
