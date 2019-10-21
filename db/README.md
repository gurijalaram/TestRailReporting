Steps to runs DB project:
0) https://confluence.apriori.com/display/ENG/How+to+set+up+Cost+Insight+applications+running+on+local+machine (Use Approach #1 to install aserver) !!!
1) Clone Git repository: https://github.com/aPrioriTechnologies/apriori-qa/tree/BA-751/db
2) To install different types of databases (MySQL, MSSQL), please use confluence pages:
	- https://confluence.apriori.com/pages/viewpage.action?pageId=15336243 to install MSSQL 
	- https://confluence.apriori.com/display/ENG/How+To+Build to install MYSQL
3) Open Eclipse 
4) Import project into workspace: File -> Import -> Maven -> Existing Maven Project -> Set path to folder of DB project -> Finish
5) To let script work with particular DB, we need to store hibernate.cfg.xml file (which contains connections to DB etc.) in folder: [Some Path]\aserver\conf\tenancy\default\default. 
	But, because of DB type difference, we have to store 3 files with the same name "hibernate.cfg.xml" (because we have: mssql, mssql and oracle DBs) in folder: [Some Path]\aserver\conf\tenancy\default\default... 
	Windows will not allow to do this, so this issue is in development stage.
	So, currently when you need to change type of database or name of database which you want to use, please perform next steps:
	5.1) Open files for particular DB (For example: src\main\resources\mssql_hibernate.cfg.xml)
	5.2) In connection string, change only name of DB: 
		Example:
			for MSSQL <property name="hibernate.connection.url">jdbc:sqlserver://localhost:1433;databaseName=apriori_r89633</property> -> apriori_r89633 - name of MSSQL DB 
			for MYSQL <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/apriori_r89513_qa_trunk</property> -> apriori_r89513_qa_trunk - name of MYSQL DB
	5.3) Change value of "hibernate.connection.dbname":
		Find "hibernate.connection.dbname" in hibernate config file, set value: <property name="hibernate.connection.dbname">[HERE SHOULD BE DB NAME]</property>
	5.4) Rename file to "hibernate.cfg.xml"
	5.5) COPY file from src\main\resources\ to [Some Path]\aserver\conf\tenancy\default\default
7) Open file: db\src\main\resources\aServerProps.properties -> set values for:
	7.1) com.apriori.home.dir=[Path to aserver folder]. Example: com.apriori.home.dir=C:\\apriori-platform\\aserver\\
	7.2) apriori.test.properties.path=[Path to apriori.properties file]. Example: apriori.test.properties.path=C:\\apriori-platform\\aserver\\conf\\tenancy\\default\\default\\apriori.properties
	Please Note! "\\aserver\\conf\\tenancy\\default\\default\\apriori.properties" - this part of path is always the same.
	7.3) fbc.hibernate.dir=[Path to hibernate.cfg.xml file]. Example: fbc.hibernate.dir=C:\\apriori-platform\\aserver\\conf\\tenancy\\default\\default\\hibernate.cfg.xml
	Please Note! "\\aserver\\conf\\tenancy\\default\\default\\hibernate.cfg.xml" - this part of path is always the same.
8) Open DB project POM file, and set values of version, for "commons.testutilLib.version" and "fbc.datamodel.version" libs. (Based ob version of: VPE/UDA/AP files, which you going to import. Also you can check comments in POM file, for additional clarification.)
9) In static initialization block of test class, you have to set the type of DB (mysql, mssql, oracle)
10) Your nerves are made of steel!!!

Note!!! Currently, every time when you want to change DB name/type you should perform STEP 5.