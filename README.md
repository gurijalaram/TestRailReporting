## How to setup automation:

1. Java 8 is used and required
2. Clone the apriori-qa repo using `git clone git@github.com:aPrioriTechnologies/apriori-qa.git`
3. Import all of the modules into an IDE of your choice
4. You are ready to run the tests if you have chrome installed, install it in case you don't

## Building the project (you will need this for the very first time):
1. Open Terminal to root `build` directory
2. Right click `build.gradle` and `Import Gradle project`

## Run Gradle tests with JVM args
1. Open Terminal to root `build` directory
2. Run `gradle clean :uitests:test --tests {modulename}:test --test "{parentFolder.nameOfTest}"` eg `gradle clean :uitests:test --tests "testsuites.CIDTestSuite"`
3. To pass in JVM args `gradle clean :uitests:test --tests {modulename}:test --test "{parentFolder.nameOfTest}" -Darg=someArg` eg. `gradle clean :uitests:test --tests "testsuites.CIDTestSuite" -DthreadCount=3 -Denv=cid-te`

## Build Gradle jar files
1. Download and install Gradle 6.1.1 (this is the version that was first used on the project)
2. Open Terminal to root `build` directory
3. Run `gradle clean fatjar`
    - if Gradle is not installed use `gradlew`
    - `clean` deletes existing `build` folder in each module
    - `fatjar` is the task that creates the zip file (nb. this task name is not a constant)
4. When the jar is complete -> Open Terminal to `..\uitests\build\libs`
5. Run `java -jar {nameOfJar}.jar` eg. `java -jar automation-qa-0.0.1-SNAPSHOT.jar`
    - To pass command line arguments: `java {arg} -jar {nameOfJar}.jar` eg. `java -Denv=cid-te -jar automation-qa-0.0.1-SNAPSHOT.jar`
    - To run jar with single test class: `java -jar {namOfJar}` eg. `java -jar automation-qa-0.0.1-SNAPSHOT.jar -test evaluate.ListOfVPETests`
    
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

## Run Checkstyle analysis from command line
1. go to `build` directory, run `gradle check -x test`

## Add TestRail configuration to _Test Suite_
Annotate suite class that needs ProjectRunID using following format: `@ProjectRunID("999")`

Annotate suite class that needs RunWith using following format (_class_ should be added from com.apriori.utils.runers, it is required) : `@RunWith(CategorySuiteRunner.class)`

## Add TestRail testCaseIDs to test methods
Annotate method that needs testRailID using following format. Tags is optional so if you don't add, its ok
`@TestRail(testCaseId = {"717"})`
