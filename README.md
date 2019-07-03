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
