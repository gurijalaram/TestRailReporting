package com.apriori.pageobjects.actions;

import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.common.response.common.ScenarioIterationKey;
import com.apriori.apibase.http.builder.common.response.common.WorkOrder;
import com.apriori.apibase.http.builder.common.response.common.WorkOrderCommand;
import com.apriori.apibase.http.builder.common.response.common.WorkOrderInputs;
import com.apriori.apibase.http.builder.common.response.common.WorkOrdersWrapper;
import com.apriori.apibase.http.builder.common.response.common.WorkSpaceSearch;
import com.apriori.apibase.http.builder.common.response.common.WorkSpaceSearchCriteria;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.utils.enums.UsersEnum;

import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScenarioAction {


    @Test
    public void FailedIf() {
        forceDelete(null);
    }

    public void forceDelete(String scenarioName) {

        AuthenticateJSON authenticateJSON = (AuthenticateJSON) new HTTPRequest().defaultFormAuthorization("cfrith@apriori.com", "cfrith")
            .customizeRequest()
            .setReturnType(AuthenticateJSON.class)
            .setHeaders(new HashMap<String, String>() {{
                    put("Content-Type", "application/json");
                }
            })
            .setEndpoint("https://cid-te.awsdev.apriori.com/ws/auth/token")
            .setAutoLogin(false)
            .commitChanges()
            .connect()
            .post();


        final HashMap<String, String> authorizationHeader =  new HashMap<String, String>() {{
                put("Authorization", "Bearer " + authenticateJSON.getAccessToken());
                put("apriori.tenantgroup", "default");
                put("apriori.tenant", "default");
            }};

        new HTTPRequest().unauthorized()
            .customizeRequest()
            .setEndpoint("https://cid-te.awsdev.apriori.com/ws/workspace/users/me/saved-searches?name=default222")
//            .setReturnType(WorkOrdersWrapper.class)
            .setHeaders(authorizationHeader)
            .setBody(new WorkSpaceSearch().setVisibility(Arrays.asList("PRIVATE"))
                .setCriteria(new WorkSpaceSearchCriteria()
                    .setWorkspaceIDs(Arrays.asList(0))
                    .setScenarioName("AutoScenario565-81016971697400")
                    .setScenarioTypes(Arrays.asList("PART"))
                    .setIncludeAllStateIterations(false)))
            .commitChanges()
            .connect()
            .post();

        final WorkOrdersWrapper workOrdersWrapper = (WorkOrdersWrapper) new HTTPRequest().unauthorized()
            .customizeRequest()
            .setHeaders(authorizationHeader)
            .setReturnType(WorkOrdersWrapper.class)
            .setEndpoint("https://cid-te.awsdev.apriori.com/ws/scenario-search/search/savedSearch?name=default222&limit=1000")
            .commitChanges()
            .connect()
            .get();

        if (workOrdersWrapper.getTotalAvailableCount() > 0) {
            ScenarioIterationKey scenarioIterationKey = workOrdersWrapper.getScenarioInfos().get(0).getScenarioIterationKey();
            WorkOrderCommand workOrderCommand = new WorkOrderCommand().setCommandType("DELETE")
                .setInputs(new WorkOrderInputs().setIncludeOtherWorkspace(false).setScenarioIterationKey(scenarioIterationKey));

            new HTTPRequest().unauthorized()
                .customizeRequest()
                .setHeaders(authorizationHeader)
                .setReturnType(WorkOrdersWrapper.class)
                .setEndpoint("https://cid-te.awsdev.apriori.com/ws/workorder/orders")
                .setBody(workOrderCommand)
                .setStatusCode(201)
                .commitChanges()
                .connect()
                .post();

            new HTTPRequest().unauthorized()
                .customizeRequest()
                .setHeaders(authorizationHeader)
                .setReturnType(WorkOrdersWrapper.class)
                .setEndpoint("https://cid-te.awsdev.apriori.com/ws/workorder/orderstatus")
                .setBody(workOrderCommand)
                .setStatusCode(200)
                .commitChanges()
                .connect()
                .post();
        }

        System.out.println();


        //{"criteria":{"workspaceIDs":[0], "name":"Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface", "scenarioTypes":["PART"], "includeAllStateIterations":false}, "visibility":["PRIVATE"]}
//        new HTTPRequest().unauthorized()
//            .customizeRequest()
//            .setEndpoint("")
//            .setHeaders()
//            .commitChanges()
//            .connect()
//            .get();


//        RequestSpecBuilder builder = new RequestSpecBuilder();
//
//        builder.setContentType("multipart/form-data")
//            .addHeaders(
//                getHeaders(
//                    getAuthToken(driver)
//                )
//            )
//            .setBody(getBody())
//            .setConfig(getConfig())
//        ;// ObjectMapperType.JACKSON_2); //            builder.setContentType(ContentType.URLENC);
//
//        ValidatableResponse response = RestAssured.given()
//            .spec(builder.build())
//            .log()
//            .all()
//            .expect().statusCode(201)
//            .when()
//            .get(generateURL(driver, "workorder/orders")).then().log().all();
//
//
//        RequestSpecBuilder submitDeleteBuilder = new RequestSpecBuilder();
//
//        submitDeleteBuilder.setContentType("multipart/form-data")
//            .addHeaders(
//                getHeaders(
//                    getAuthToken(driver)
//                )
//            )
//            .setBody(getSubmitBody(null))
//            .setConfig(getConfig())
//            .setBaseUri(generateURL(driver,"workorder/orderstatus"))
//
//        ValidatableResponse response2 = RestAssured.given()
//            .spec(builder.build())
//            .log().all()
//            .expect().statusCode(201)
//            .when().get(generateURL(driver, "workorder/orders"))
//            .then().log().all();

    }

    private String getSubmitBody(String orderId) {
        return "{" +
            "'action':'SUBMIT', " +
            "'orderIds':['" + orderId + "']}";
    }

    private String generateURL(WebDriver driver, String url) {
        return driver.getPageSource() + url;
    }

    private RestAssuredConfig getConfig() {

        return RestAssuredConfig
            .config()
            .httpClient(HttpClientConfig.httpClientConfig()
                            .setParam("http.connection.timeout", 30000)
                            .setParam("http.socket.timeout", 30000)
            );
    }

    private Map<String, String> getHeaders(String token) {

        return new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("ap-cloud-context", "EDC");
            }};
    }

    private String getBody() {
        return "{ " +
            " 'command': { " +
            "  'commandType': 'DELETE', " +
            "  'inputs': { " +
            "   'includeOtherWorkspace': false, " +
            "   'scenarioIterationKey': { " +
            "    'scenarioKey': { " +
            "     'workspaceID': 110, " +
            "     'typeName': 'partState', " +
            "     'stateName': 'Initial1', " +
            "     'masterName': 'DEMO1' " +
            "    }, " +
            "    'iteration': 6981 " +
            "   } " +
            "  } " +
            " } " +
            "}";
    }



    //TODO z: add to work owith a different browsers
    private String getAuthToken(WebDriver driver) {
        return ((ChromeDriver) driver).getLocalStorage().getItem("JSESSIONID");
    }
}
