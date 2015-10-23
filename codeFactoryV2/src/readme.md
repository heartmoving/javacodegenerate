#接口项目调用生成器方法
private static void codeRun(String tablename,String author,boolean isCover){
		String basePackage = "com.meilidian.api";
//		String ip = "192.168.2.103";
		String ip = "192.168.0.155";
		 String realPath = CodeGenerate.class.getResource("/").toString();
		 realPath = realPath.substring(0, realPath.indexOf("/target/"))+"/";
		 String projectPath = (realPath + "src/main/").replaceAll("file:/", "");
		 System.out.println(projectPath);
		try {
			DbEntity dbEntity = new DbEntity("com.mysql.jdbc.Driver", "jdbc:mysql://"+ip+":3306/api", "root", "root");
			FileEntity fileEntity = new FileEntity(projectPath,basePackage,author,isCover);
			FlagEntity flagEntity = new FlagEntity();
			//控制不生成aciton web.xml page，默认为true
			flagEntity.setCreateAction(false);
			flagEntity.setCreateWebXml(false);
			flagEntity.setCreatePage(false);
			flagEntity.setCreatePropertie(true);
			//设置加载模版方式为从引用项目加载
			fileEntity.setTempateReadType(2);
			//设置模版路径
			fileEntity.setTemplatePath("src/main/java/templates");
			CodeFactoryService.codeGenerateRun(tablename, dbEntity, fileEntity,flagEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	#web项目调用生成器代码
	private static void codeRun(String tablename,String author,boolean isCover){
		String basePackage = "com.meilidian.api.manage";
		String ip = "192.168.0.155";
//		String ip = "192.168.0.155";
		 String realPath = CodeGenerate.class.getResource("/").toString();
		 realPath = realPath.substring(0, realPath.indexOf("/target/"))+"/";
		 String projectPath = (realPath + "src/main/").replaceAll("file:/", "");
		 System.out.println(projectPath);
		try {
			DbEntity dbEntity = new DbEntity("com.mysql.jdbc.Driver", "jdbc:mysql://"+ip+":3306/melidian", "root", "root");
			FileEntity fileEntity = new FileEntity(projectPath,basePackage,author,isCover);
			CodeFactoryService.codeGenerateRun(tablename, dbEntity, fileEntity,new FlagEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}