package com.evaluate;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.response.PostComponentResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.IterationsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import io.restassured.http.Headers;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.RegressionTestSuite;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class ComponentRedirectTests {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final IterationsUtil iterationsUtil = new IterationsUtil();

    @Test
    @Category(RegressionTestSuite.class)
    @TestRail(testCaseId = "14197")
    @Description("Verify receipt of 301 response when getting component details of a file which already exists")
    public void receive301AfterUploadOfExistingComponent() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

//        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCSS(
//            ComponentInfoBuilder.builder()
//                .componentName(componentName)
//                .scenarioName(scenarioName)
//                .resourceFile(resourceFile)
//                .user(currentUser)
//                .build());

        ResponseWrapper<PostComponentResponse> componentResponse = componentsUtil.postComponent(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build()
        );

        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_BY_COMPONENT_ID, null)
                .inlineVariables(componentResponse.getResponseEntity().getSuccesses().get(0).getComponentIdentity())
                .token(currentUser.getToken())
                .followRedirection(false)
                .expectedResponseCode(HttpStatus.SC_MOVED_PERMANENTLY);

        //ToDo:- Ask Ciene what the allowed method of sleeping is

        ResponseWrapper response = HTTPRequest.build(requestEntity).get();
        response.getStatusCode();
    }

}
