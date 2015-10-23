package com.gogotown.entity;

import com.gogotown.commons.Constans;
import com.gogotown.utils.DateUtil;


public class PojoEntity {
	String base_packge;
	String type_model;
	String author;
	String current_now;
	TableEntity table;
	
	public PojoEntity(String base_packge,String author,TableEntity table) {
		super();
		this.base_packge = base_packge;
		this.type_model = Constans.TYPE_MODEL;
		this.author = author;
		this.current_now = DateUtil.getCurrentTime();
		this.table = table;
	}
	public String getBase_packge() {
		return base_packge;
	}
	public void setBase_packge(String base_packge) {
		this.base_packge = base_packge;
	}
	public String getType_model() {
		return type_model;
	}
	public void setType_model(String type_model) {
		this.type_model = type_model;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCurrent_now() {
		return current_now;
	}
	public void setCurrent_now(String current_now) {
		this.current_now = current_now;
	}
	public TableEntity getTable() {
		return table;
	}
	public void setTable(TableEntity table) {
		this.table = table;
	}
	
}
