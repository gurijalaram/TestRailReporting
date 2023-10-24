# How to setup automation:

1. Java 11 is used and required
2. Clone the apriori-qa repo using `git clone git@github.com:aPrioriTechnologies/apriori-qa.git`
3. Import all the modules into an IDE of your choice
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
9.You are ready to run the tests..


## List of special modules that are not included into a common build
 - **database** module location "../db"

## Building the project
- to build without tests, add to the end of the Gradle build command: `-x test` e.g. `gradle clean build -x test`

 - ### You will need this for the very first time:
    - Open Terminal to project root directory
    - Right click `build.gradle` and `Import Gradle project`

 - ### Building all modules (include special modules):
    - Open Terminal to project root directory
    - Run `gradle clean build -Dall`

- ### Building specific module:
    - Open Terminal to project root directory
    - Run `gradle :<module name>:<build command>` e.g. `gradle :web:cia:build`
 
- ### Building specific configuration (set of modules)
    - Open Terminal to project root directory
    - Run `gradle -Dconf=<configuration name> clean build`
    
    ### Possible configurations
    - #### All web modules
         - Open Terminal to project root directory
         - Run `gradle -Dconf=web clean build`
            
    - #### All microservices (new approach)
        - Open Terminal to project root directory
        - Run `gradle -Dconf=microservices clean build`
    
    - #### All microservices (old approach) 
        - Open Terminal to project root directory
        - Run `gradle --continue :microservices:ats:build :microservices:cds:build :microservices:bcs:build :microservices:edc:build :microservices:fms:build`
     

## Run Gradle tests with JVM args
1. Open Terminal to project root directory
2. Run `gradle clean :web:cidapp-ui:test --tests "{parentFolder.suiteName}"` eg `gradle clean :web:cidapp-ui:test --tests "testsuites.SmokeTestSuite"`
3. To pass in JVM args `gradle clean :web:cidapp-ui:test --tests {modulename}:test --test "{parentFolder.nameOfTest}" -Darg=someArg` eg. `gradle clean :web:cidapp-ui:test --tests "testsuites.SmokeTestSuite" -Djunit.jupiter.execution.parallel.config.fixed.max-pool-size=3 -Denv=qa-test -Dcsv="common-users.csv"`

## How to run single suite
1. Open Terminal to project root directory
2. Run `gradle clean :web:cidapp-ui:test --tests "{fully qualified packagename.nameOfClass.nameOfTest}"` eg `gradle clean :web:cidapp-ui:test --tests "evaluate.designguidance.failures.failedCostingCount"`

## How to run multiple suites
1. Open Terminal to project root directory
Run `gradle clean :web:cidapp-ui:test --tests "{parentFolder.suiteName}" --tests "{parentFolder.suiteName}"` eg `gradle clean :web:cidapp-ui:test --tests "testsuites.SanityTestSuite" --tests "testsuites.SmokeTestSuite"`

## Build Gradle jar files
1. Download and install Gradle 6.1.1 (this is the version that was first used on the project)
2. Open Terminal to project root directory
3. Run `gradle clean fatjar`
    - if Gradle is not installed use `gradlew`
    - `clean` deletes existing `build` folder in each module
    - `fatjar` is the task that creates the zip file (nb. this task name is not a constant)
4. When the jar is complete -> Open Terminal to `..\cid\build\libs`
5. Run `java -jar {nameOfJar}.jar` eg. `java -jar automation-qa-0.0.1-SNAPSHOT.jar`
    - To pass command line arguments: `java {arg} -jar {nameOfJar}.jar` eg. `java -Denv=qa-test -jar automation-qa-0.0.1-SNAPSHOT.jar`
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

- To init default RequestEntity use `com.apriori.utils.http.utils.RequestEntityUtil` :
- You may add special parameters e.g.: 
    - `RequestEntityUtil.init(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, BillOfMaterialsWrapper.class)
          .body("")
          .apUserContext("")
          .inlineVariables("");`
- If you don't want to validate and map response body, _returnType_ should be null e.g.:
    - `RequestEntityUtil.init(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, null);`

- To validate a response code automatically `RequestEntity` contain `expectedResponseCode` property.
 - To insert expected code use `.expectedResponseCode(<expected response code>)` e.g.
    - `RequestEntityUtil.init(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, BillOfMaterialsWrapper.class)
        .expectedResponseCode(HttpStatus.SC_OK)`

    
### To execute request
To execute request use `com.apriori.utils.http.builder.request.HTTPRequest` it allows you to create the request. <br>
`com.apriori.utils.http.builder.request.HTTPRequest` class <br>
This class has single method, `build` that requires RequestEntity object with all request configurations.<br>
After `HTTPRequest.build()` method you will be able to select a type of request, as results you will have:
 - `HTTPRequest.build({requestEntity}).{type of request}`, e.g. `HTTPRequest.build(requestEntity).get()`

**`ResponseWrapper`** - transfer object, which contain information about the response.
_ResponseWrapper_ fields: <br>
    - statusCode : Integer - Response status code. <br>
    - body : String - Response body as String. <br>
    - responseEntity - mapped response entity. The type of mapping should be inserted in _RequestEntity_ as returnType

### Final example of HTTP request: 
```
final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_ITERATIONS_BY_COMPONENT_SCENARIO_IDS, ScenarioIterationItemsResponse.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioIterationItemsResponse> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
```

_Validation:_
 * if RequestEntity contain _ReturnType_, the response will be validated by Schema and mapped to returnType
 * if RequestEntity has _isUrlEncodingEnabled_ "true" - request URL will be encoded
  
## Upload/find test parts on AWS
1. Login to AWS and go to the `S3` section
2. Locate `qa-test-parts` folder
3. Upload parts to either `cad` or `common` folder

## Run Checkstyle analysis from command line
1. go to `build` directory, run `gradle check -x test`

## Add TestRail Run ID
Add `ProjectRunId.properties` to resources folder for each module and populate eg. `project_run_id=768`

## Add TestRail testCaseIDs to test methods
Annotate method that needs testRailID using following format. Tags is optional so if you don't add, its ok
`@TestRail(id = 717)`

## Tagging and running a suite of tests
1. Create a [suite] class in the test folder of your module and annotate with `@Suite @SelectPackages("PackageName") @IncludeTags("SomeTagName")`
2. Annotate your test with `@Tag("SomeTagName")` 

## How to run tests against local dev env

If you have both local dev stack and ACS you need to modify user list cause only your user can upload file to FMS.

When we want to run tests against local env we need to override change two files:
* In `config.yml` change next keys:
  - set local env by default 
  ```yaml 
   env: local
  ```
  - Change secret_key to what defined in dev stack in `.env` file as value of `SHARED_SECRET` key  
  ``` yaml
  local:
    secret_key: ABC123DEF456
  ```
  - Change token_username and token_email to refer your own user
  ``` yaml
  local:
  ats:
      token_username: dbondar
      token_email: dbondar@apriori.com
  ```
* In `common-users.csv` set only your user 
``` csv
dbondar@apriori.com,YourAuth0IntPassword,admin
```

## How to use assertj in your test class
1. Import the assertj lib `import org.assertj.core.api.SoftAssertions;`
2. Create a new object `private SoftAssertions softAssert = new SoftAssertions();`
3. Compose the assertions eg. `softAssert.assertThat(explorePage.getComponentsFound()).isEqualTo(100); 
   softAssert.assertThat(explorePage.isScenarioCountPresent()).isTrue();`
4. The assertAll method must be called to carry out the assertions `softAssert.assertAll();`   


## Project properties

### Properties location and template
All properties (instead of system properties) are located in `utils/src/main/resources/configurations` folder.
These properties are divided into three sections
- environment properties `utils/src/main/resources/configurations/environments-config.yml` - properties related to a specific environment. Refer to this file as `EnvironmentProperties` <br>
- customers properties `utils/src/main/resources/configurations/customers-config.yml` - properties related to a specific customer. Refer to this file as `CustomersProperties` <br>
- global properties `utils/src/main/resources/configurations/global-config.yml` - common properties file. Refer to this file as `GlobalProperties`  Contains properties:
  - which are independent of the environment
  - properties in **default** section, that will be used if there is no corresponding variable in the environment file


### Get properties 
To get any project property use `com.apriori.utils.properties.PropertiesContext` - this is a global class to work with properties.
<br>`PropertyContext` - the main object to work with properties and contains all properties from properties sections. (see **Properties location and template**)

#### Main configurations
`GlobalProperties` file contain four main configurations properties:
 - `env` : the section of environment properties to process.<br> 
( e.g. if `env` has value `qa-test`, `PropertyContext` will try to find property in `EnvironmentProperties` file under `qa-test` section. 
 - `deployment`: deployment configuration. The section of deployment properties to process <br>
  ( e.g. if `deployment` has value `sandbox`, `PropertyContext` will try to find property in `GlobalProperties` file under `sandbox` section.
- `customer`: the section of customer properties to process.<br>
  ( e.g. if `customer` has value `sprockets`, `PropertyContext` will try to find property in `CustomersProperties` file under `sprockets` section.
- `version`: version used in Deployment url service. See URLs under  Deployment URL comment in `GlobalProperties` file.

<br><br>`com.apriori.utils.properties.PropertiesContext` - contains get method, that allow to get the property value to mapped type
 - `get(String propertyName)` - return property by name mapped to String

Property name represent String path of YML file e.g:
to get <br>
>`global:` <br>
> `prop: example` <br>

`PropertiesContext.get("global.prop");`

### Process of receiving property
 - 1). at first there is a search in `System properties`
 - 2). if in system properties there is no such variable, there is a search in **PropertyContext** for requested property path.
 - 3). if requested property path does not exist, then path will be updated to **environment value** and try to get it by environment path.
 - 4). if environment property path does not exist, then path will be updated to **default** and try to get it by default path.
 - 5). if default property does not exist, then will be thrown `java.lang.IllegalArgumentException` with a text `Property with path: {propety name} not present.`

e.g. PropertiesContext.get("fms.api_url") | note that env = qa-cid-perf <br>
   1). Get system environment `fms_api_url`, <br>
   2). if step 1 returns nothing | get the property from `PropertiesContext` with path `fms/api_url` <br>
   3). if step 2 returns nothing | get the property from `PropertiesContext` but with adding to the path environment part`qa-cid-perf/` as result get property `/qa-cid-perf/fms/api_url` <br>
   4). if step 3 returns nothing | get the property from `PropertiesContext` but with replacing of the first path part `qa-cid-perf/` to `default/` as result get property `/default/fms/api_url` <br>
   5). if step 4 returns nothing | thrown `java.lang.IllegalArgumentException` with a text `Property with path: fms/api_url not present.` <br>

### Search Templates
Search in `system environments` and in  `PropertiesContext` require special naming template.
 - for System property, in the request property name, will be automatically replaced all `"."` with `"_"`
   Please NOTE: if you need to specify a property name as a `System environment`, you need to replace all `"."` with `"_"` in the property name, taken from java code.
    e.g. `com.apriori.utils.properties.PropertiesContext("global.users_csv_file")` - will search system environment with name: `global_users_csv_file`
   
  - for `PropertiesContext`, in requested property name, will be automatically replaced all `"."` with `"/"`
    e.g. `com.apriori.utils.properties.PropertiesContext("global.users_csv_file")` - will search config.yml property with name: `global/users_csv_file`   


#### Property references
To get a property with a reference use template `${reference}.property_path` <br>
e.g: `PropertiesContext.get` (`https://${default.version}/apriori.com`);<br>
To get a property with a reference that may have another reference inside, use template `${${another.refference}.property/path}`
    <br>e.g: `PropertiesContext.get("${${env}.bcs.api_url}");`
 - it will get a property in a couple of steps:
   - replace reference inside `${env}` to appropriate value e.g. `qa-test` as result it will be `${qa-test.bcs.api_url}`
   - get the value from a property `qa-test/bcs/api_url`

<br>An example of properties usage you can find in `com.apriori.util.test.PropertiesContextTestTest`
