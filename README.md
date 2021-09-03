# How to setup automation:

1. Java 8 is used and required
2. Clone the apriori-qa repo using `git clone git@github.com:aPrioriTechnologies/apriori-qa.git`
3. Import all of the modules into an IDE of your choice
4. If you have chrome installed, install it in case you don't
5. To run the tests, you should have an AWS account and authorize it on the local machine
    - How to create AWS user account: https://confluence.apriori.com/display/ENG/Logging+into+AWS+Console
6. Install the AWS CLI, either via pip (if you already have Python installed) or using the MSI installer: 
     - http://docs.aws.amazon.com/cli/latest/userguide/awscli-install-windows.html#install-msi-on-windows
     - http://docs.aws.amazon.com/cli/latest/userguide/installing.html
7. Once you have installed the various tools, configure your AWS credentials. <br />  
  You can do this from a command prompt with the AWS CLI on your PATH: 
    - `aws configure` This command will prompt for your IAM user access key and secret access key (an admin user will be able to generate these details).
  You should also set your `default region` to `_us-east-1_`.
  You should set `default format` to `text`
  Your settings will be stored in your _%USERPROFILE%\.aws_ directory.
8. Verify that things are configured properly using the following command. Confirm that the resulting ARN ends with your user name. 
    - `aws sts get-caller-identity `
     ```
      {
          "UserId": "AIDAIPQQ3V7G3TSKB2I5U",
          "Account": "563229348140",
          "Arn": "arn:aws:iam::563229348140:user/test@apriori.com"
      }
     ```  
9.You are ready to run the tests

## List of special modules that are not included into a common build
 - **database** module location "../db"

## Building the project
- to build without tests, add to the end of the Gradle build command: `-x test` e.g. `gradle clean build -x test`

 - ### You will need this for the very first time:
    - Open Terminal to root `build` directory
    - Right click `build.gradle` and `Import Gradle project`

 - ### Building all modules (include special modules):
    - Open Terminal to root `build` directory
    - Run `gradle clean build -Dall`

- ### Building specific module:
    - Open Terminal to root `build` directory
    - Run `gradle :<module name>:<build command>` e.g. `gradle :web:cia:build`
 
- ### Building specific configuration (set of modules)
    - Open Terminal to root `build` directory
    - Run `gradle -Dconf=<configuration name> clean build`
    
    ### Possible configurations
    - #### All web modules
         - Open Terminal to root `build` directory
         - Run `gradle -Dconf=web clean build`
            
    - #### All microservices (new approach)
        - Open Terminal to root `build` directory
        - Run `gradle -Dconf=microservices clean build`
    
    - #### All microservices (old approach) 
        - Open Terminal to root `build` directory
        - Run `gradle --continue :microservices:ats:build :microservices:cds:build :microservices:bcs:build :microservices:edc:build :microservices:fms:build`
     

## Run Gradle tests with JVM args
1. Open Terminal to root `build` directory
2. Run `gradle clean :cid:test --tests "{parentFolder.suiteName}"` eg `gradle clean :cid:test --tests "testsuites.CIDNonSmokeTestSuite"`
3. To pass in JVM args `gradle clean :cid:test --tests {modulename}:test --test "{parentFolder.nameOfTest}" -Darg=someArg` eg. `gradle clean :cid:test --tests "testsuites.CIDNonSmokeTestSuite" -DthreadCounts=3 -Denv=cid-te -Dcsv=nameOfCsv.csv`

## How to run single suite
1. Open Terminal to root `build` directory
2. Run `gradle clean :cid:test --tests "{fully qualified packagename.nameOfClass.nameOfTest}"` eg `gradle clean :cid:test --tests "evaluate.designguidance.failures.failedCostingCount"`

## How to run multiple suites
1. Open Terminal to root `build` directory
Run `gradle clean :cid:test --tests "{parentFolder.suiteName}" --tests "{parentFolder.suiteName}"` eg `gradle clean :cid:test --tests "testsuites.CIDNonSmokeTestSuite" --tests "testsuites.CIDSmokeTestSuite"`

## Build Gradle jar files
1. Download and install Gradle 6.1.1 (this is the version that was first used on the project)
2. Open Terminal to root `build` directory
3. Run `gradle clean fatjar`
    - if Gradle is not installed use `gradlew`
    - `clean` deletes existing `build` folder in each module
    - `fatjar` is the task that creates the zip file (nb. this task name is not a constant)
4. When the jar is complete -> Open Terminal to `..\cid\build\libs`
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
           - username:{com.apriori.utils.constants.CommonConstants#defaultUserName} (admin@apriori.com)
           - password:{com.apriori.utils.constants.CommonConstants#defaultPassword} (admin)
           - accessLevel:{com.apriori.utils.constants.CommonConstants#defaultAccessLevel} (admin)
 
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
    - {accessLevel}: is optional, if it is empty, the user will have default accessLevel from  {com.apriori.utils.constants.CommonConstants#defaultAccessLevel} (admin)


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

## How to use assertj in your test class
1. Import the assertj lib `import org.assertj.core.api.SoftAssertions;`
2. Create a new object `private SoftAssertions softAssert = new SoftAssertions();`
3. Compose the assertions eg. `softAssert.assertThat(explorePage.getComponentsFound()).isEqualTo(100); 
   softAssert.assertThat(explorePage.isScenarioCountPresent()).isTrue();`
4. The assertAll method must be called to carry out the assertions `softAssert.assertAll();`   


## Project properties
### Get properties 
To get any project property use `com.apriori.utils.properties.PropertiesContext` - this is global class to work with properties.
`com.apriori.utils.properties.PropertiesContext` - contains get methods, that allow to get the property value to mapped type
 - `get(String propertyName)` - return property by name mapped to String

### Process of receiving property
 - at first there is a search in `System properties`
 - if in system properties no such variable, there is a search in `utils/src/main/resources/config.yml`

Property name represent String path of YML file e.g: 
   to get <br>
   >`global:` <br>
   > `prop: example` <br>

`PropertiesContext.get("global.prop");`

### Search Templates
Search in `system environments` and in `config.yml` file require special naming template.
 - for System property, in the request property name, will be automatically replaced all `"."` with `"_"`
   Please NOTE: if you need to specify a property name as a `System environment`, you need to replace all `"."` with `"_"` in the property name, taken from java code.
    e.g. `com.apriori.utils.properties.PropertiesContext("global.users_csv_file")` - will search system environment with name: `global_users_csv_file`
   
  - for `utils/src/main/resources/config.yml`, in requested property name, will be automatically replaced all `"."` with `"/"`
    e.g. `com.apriori.utils.properties.PropertiesContext("global.users_csv_file")` - will search config.yml property with name: `global/users_csv_file`   


#### Property based on environment
To get property based on environment use environment reference by key `${env}` 
   e.g: `PropertiesContext.get("${env}.prop");`

An example of properties usage you can find in `com.apriori.util.test.PropertiesTestPropertiesTest`

### Properties location and template
All properties are located in `utils/src/main/resources/config.yml`

Properties are separated into two sections:
- **global** - contains properties for all modules, common properties for microservices, that are not related to the environment
- **environment** - contains properties for all modules and microservices, that are related to the environment. They always start from environment name.
   