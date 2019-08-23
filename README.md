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
3. Running test on docker (and zalenium if required):
    * Install docker
    * Pull docker images for selenium hub, chrome and firefox (dosel/zalenium if required)
    * In the terminal enter `docker-compose up -d`. For zalenium enter `docker run --rm -ti --name zalenium -p 4444:4444 -p 5555:5555 -v /var/run/docker.sock:/var/run/docker.sock -v /tmp/videos:/home/seluser/videos dosel/zalenium start`
    * In the terminal go to the base directory (eg. C:\automation-qa\apriori-qa\uitests) and enter the following goals `mvn clean test -Dtest={TestName}.java -Dbrowser=chrome -Dmode=QA`

## Run Sonarqube static code analysis
1. go to `build` directory, run `mvn sonar:sonar -Psonar` which will only run Sonarqube analysis and posts result to https://sonarqube.apriori.com dashboard

## Add TestRail testCaseIDs to test methods
Annotate method that needs testRailID using following format. Tags is optional so if you don't add, its ok
`@TestRail(testCaseId = {"C717"}, tags = {"smoke"})`
