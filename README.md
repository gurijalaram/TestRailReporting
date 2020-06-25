## How to setup automation:

1. Java 8 is used and required
2. Clone the apriori-qa repo using `git clone git@github.com:aPrioriTechnologies/apriori-qa.git`
3. Import all of the modules into an IDE of your choice
4. You are ready to run the tests if you have chrome installed, install it in case you don't

## List of special modules that are not included into a common build
 - **database** module location "../db"

## Building the project (you will need this for the very first time):
1. Open Terminal to root `build` directory
2. Right click `build.gradle` and `Import Gradle project`

## Building all modules (include special modules):
1. Open Terminal to root `build` directory
2. Run `gradle clean build -x test -Dall`

## Building all microservices 
1. Open Terminal to root `build` directory
2. Run `gradle --continue :microservices:ats:build :microservices:cds:build :microservices:cis:build :microservices:edc:build :microservices:fms:build`


## Run Gradle tests with JVM args
1. Open Terminal to root `build` directory
2. Run `gradle clean :uitests:test --tests "{parentFolder.suiteName}"` eg `gradle clean :uitests:test --tests "testsuites.CIDTestSuite"`
3. To pass in JVM args `gradle clean :uitests:test --tests {modulename}:test --test "{parentFolder.nameOfTest}" -Darg=someArg` eg. `gradle clean :uitests:test --tests "testsuites.CIDTestSuite" -DthreadCounts=3 -Denv=cid-te`

## How to run single
1. Open Terminal to root `build` directory
2. Run `gradle clean :uitests:test --tests "{fully qualified packagename.nameOfClass.nameOfTest}"` eg `gradle clean :uitests:test --tests "evaluate.designguidance.failures.failedCostingCount"`

## How to run multiple suites
1. Open Terminal to root `build` directory
Run `gradle clean :uitests:test --tests "{parentFolder.suiteName}" --tests "{parentFolder.suiteName}"` eg `gradle clean :uitests:test --tests "testsuites.CIDTestSuite" --tests "testsuites.SmokeTestSuite"`

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


## API Request functionality

**`RequestEntity`** - it is a transfer object, which contain all needed information about the request

- To init default RequestEntity use _init_ method:
    - `init(String endpoint, final UserCredentials userCredentials, Class<?> returnType)` e.g.: `RequestEntity.init(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, userData.getUserCredentials(), BillOfMaterialsWrapper.class);`
- You may add special parameters e.g.: 
    - `RequestEntity.init(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, userData.getUserCredentials(), BillOfMaterialsWrapper.class).setUrlEncoding(true).setInlineVariables("test")`
- If you don't want to validate and map response body, _returnType_ should be null e.g.:
    - `RequestEntity.init(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, userData.getUserCredentials(), null);`

**`ResponseWrapper`** - transfer object, which contain information about the response.
_ResponseWrapper_ fields: <br>
    - statusCode : Integer - Response status code. <br>
    - body : String - Response body as String. <br>
    - responseEntity - mapped response entity. The type of mapping should be inserted in _RequestEntity_ as returnType

**`RequestArea`** - requests specification. Contain templates and specific functions for HTTP request area.
 - `RequestAreaUiAuth` - provide capabilities to do HTTP requests which require UI authorization
    - if `RequestEntity` - doesn't contain headers and token for username is not cached (first HTTP request from user), will do UI authorization and cache the token for this user. 
 - `RequestAreaCds` -  provide capabilities to do HTTP requests for CDS
 - `RequestAreaFms` -  provide capabilities to do HTTP requests for FMS

**`ConnectionManager`** - class which send API requests and validate/extract results. The response is mapping into ResponseWrapper (see ResponseWrapper)

_Validation:_
 * if RequestEntity contain _ReturnType_, the response will be validated by Schema and mapped to returnType
 * if RequestEntity contain _statusCode_, the response will be validated on presents of inserted status code.
 * if RequestEntity has _isUrlEncodingEnabled_ "true" - request URL will be encoded
 
**`GenericRequestUtil`** - Wrapper for HTTP requests, provide easy way, to do requests

- To init request `GenericRequestUtil` use 
`<request type get, post...>(RequestEntity requestEntity, RequestArea requestArea)`
- Examples of usage:  
`GenericRequestUtil.get(RequestEntity.init(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, userData.getUserCredentials(), BillOfMaterialsWrapper.class), new RequestAreaUiAuth());`
- To provide more simple view you may split RequestEntity initialization and GenericRequestUtil :
`RequestEntity requestEntity = RequestEntity.init(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS,
    userData.getUserCredentials(),
    BillOfMaterialsWrapper.class)`
`GenericRequestUtil.get(requestEntity, new RequestAreaUiAuth());`
   
  

## Run Checkstyle analysis from command line
1. go to `build` directory, run `gradle check -x test`

## Add TestRail configuration to _Test Suite_
Annotate tests.suite class that needs ProjectRunID using following format: `@ProjectRunID("999")`

Annotate tests.suite class that needs RunWith using following format (_class_ should be added from com.apriori.utils.runers, it is required) : `@RunWith(CategorySuiteRunner.class)`

## Add TestRail testCaseIDs to test methods
Annotate method that needs testRailID using following format. Tags is optional so if you don't add, its ok
`@TestRail(testCaseId = {"717"})`

## How to run tests against local dev env
When we want to run tests against local env we need to override **env** value `-Denv=localhost` 

If we want to run against Eclipse dev env we also need to change **url** and **ignore ssl check** 
`-Durl=https://localhost:8543/ -DignoreSslCheck=true`