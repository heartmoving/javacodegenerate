package com.gogotown.entity;

public class FileEntity {
	/** 
	* @Fields projectPath :代码生成到的位置 com的上一层
	*/ 
	private String projectPath = null;
	 /** 
	* @Fields basePackage : 指定实体生成所在包的根路径 
	*/ 
	private String basePackage = null;
	 /** 
	* @Fields authorName : 作者名字
	*/ 
	private String authorName = null;
	
	 /** 
	* @Fields flag : 是否覆盖已经生成的代码
	*/ 
	private boolean is_cover = false;
	
	 /**@Fields tempateReadType : 模版读取方式 
	  * 1 从生成器项目下的template读取 2从引用项目指定路径下读取，
	  * 默认读取生成器下的template下的模版
	  * */ 
	private int tempateReadType = 1;
	
	 /**@Fields templatePath : 模版加载路径 tempateReadType值为2的时候传入有效*/ 
	private String templatePath = "src/templates";

	public FileEntity(String projectPath, String packagePath,
			String authorName, boolean is_cover) {
		super();
		this.projectPath = projectPath;
		this.basePackage = packagePath;
		this.authorName = authorName;
		this.is_cover = is_cover;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public boolean isIs_cover() {
		return is_cover;
	}

	public void setIs_cover(boolean is_cover) {
		this.is_cover = is_cover;
	}

	public int getTempateReadType() {
		return tempateReadType;
	}

	public void setTempateReadType(int tempateReadType) {
		this.tempateReadType = tempateReadType;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	
}
