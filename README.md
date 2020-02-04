## How to setup automation:

1. Java 8 is used and required
2. Clone the apriori-qa repo using `git clone git@github.com:aPrioriTechnologies/apriori-qa.git`
3. Import all of the modules into an IDE of your choice
4. You are ready to run the tests if you have chrome installed, install it in case you don't

NOTE: By default, there is already chromedriver.exe and geckodriver.exe commited to repo so for local run, you don't need to point to anything. It will always take from repo

## Building the project (you will need this for the very first time after you clone repo):

* Create a maven build config with apbuild as the base directory
* Enter the following goals `clean install -DskipTests=true`

## How to run a test:

1. Running a single test or test class in your IDE
	* Right click and Run on a test method or the test class
2. Running multiple test in parallel using maven build(configuration):  NOTE: not working yet
	* Building the project is required first. See section below
	* Select the module where the test is as the base directory
	* Enter the following goals `clean test -Dtest={TestName}.java -Dbrowser=chrome -Dmode=LOCAL -DthreadCount=3`. To run more tests in parallel, change `-DthreadCount=10` number
	* To see supported browsers, check `DriverFactory.java`
	* To run an adhoc set of tests use "@Category(AdhocTests.class)". See testSaveAsPrivateComparison as an example. Ensure your test's class is listed within the AdhocTestSuite. You can use 'mvn clean test -Dtest=AdhocTestSuite.java' or push your branch and run on jenkins.
	* **OPTIONAL environment configurations**:
	    * `-Denv={environment name}` e.g. `-Denv=cid-te`, environment by default `cid-te`: which environment properties we need to use (URL, authorization users, etc.) you can find properties by `utils/resources/{environment}/{environment}.properties`
	    * `-Durl={base url for properties}` e.g. `-Durl=http:\\my-base-url`, by default value from environment properties file `url.default` :  initialize base url by overriding `url.default`, from environment properties, to value inserted in `-Durl`, you can find properties by `utils/resources/{environment}/{environment}.properties`
3. Running test on docker (and zalenium if required):
    * Install docker
    * Pull docker images for selenium hub, chrome and firefox (dosel/zalenium if required)
    * In the terminal enter `docker-compose up -d`. For zalenium enter `docker run --rm -ti --name zalenium -p 4444:4444 -p 5555:5555 -v /var/run/docker.sock:/var/run/docker.sock -v /tmp/videos:/home/seluser/videos dosel/zalenium start`
    * In the terminal go to the base directory (eg. C:\automation-qa\apriori-qa\uitests) and enter the following goals `mvn clean test -Dtest={TestName}.java -Dbrowser=chrome -Dmode=QA`
## Users functionality
Get user functionality has reference to `{environment}.properties` file. 

**Reference properties:**
   * **different.users**
       - true: will return each time new user
       - false: will return each time the same (first from list) user
   * **users.csv.file**: the name of csv file with users list from `resources/ + {environment}` folder
        - if there are no users, return default user with:
           - username:{com.apriori.utils.constants.Constants#defaultUserName} (admin@apriori.com)
           - password:{com.apriori.utils.constants.Constants#defaultPassword} (admin)
           - accessLevel:{com.apriori.utils.constants.Constants#defaultAccessLevel} (admin)
 
   Users list is global for two Collections:
   * security users collection
   * common users collection
 
   Each collection has a copy of this list and after getting the user, this user will be pushed to the end of the queue
    
   **Example:**
   - security collection - user1, user2, user3
   - common collection - user1, user2, user3
   
   after getting the security user
   
   - security collection - user2, user3, user1
   - common collection - user1, user2, user3
       
   **Example of execution API in code** :
   - to receive common user use: `UserUtil.getUser()`
   - to receive user by access level use: `UserUtil.getUser("Needed access level")  e.g. UserUtil.getUser("admin")`
   
   **UserUtil.getUser()/getUser(accessLevel)**: return UserCredentials.class
   
   **UserCredentials.class** contains:
   - username (String)
   - password (String)
   - securityLevel (String)
   
**Users csv file format:**
 * {username},{password},{accessLevel}
    - {username}: required
    - {password}: required
    - {accessLevel}: is optional, if it is empty, the user will have default accessLevel from  {com.apriori.utils.constants.Constants#defaultAccessLevel} (admin)

## Run Sonarqube static code analysis
1. go to `build` directory, run `mvn sonar:sonar -Psonar` which will only run Sonarqube analysis and posts result to https://sonarqube.apriori.com dashboard

## Add TestRail configuration to _Test Suite_
Annotate suite class that needs ProjectRunID using following format: `@ProjectRunID("999")`

Annotate suite class that needs RunWith using following format (_class_ should be added from com.apriori.utils.runers, it is required) : `@RunWith(CategorySuiteRunner.class)`

## Add TestRail testCaseIDs to test methods
Annotate method that needs testRailID using following format. Tags is optional so if you don't add, its ok
`@TestRail(testCaseId = {"717"})`
