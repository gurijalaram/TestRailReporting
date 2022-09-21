package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.enums.CidAppAPIEnum;
import com.apriori.cidappapi.entity.response.ComponentIdentityResponse;
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
        final String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        ResponseWrapper<PostComponentResponse> componentResponse = componentsUtil.postComponent(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName1)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build()
        );

        ResponseWrapper<PostComponentResponse> componentResponseReupload = componentsUtil.postComponent(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName2)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build()
        );

        RequestEntity requestEntityExpect301 =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_BY_COMPONENT_ID, null)
                .inlineVariables(componentResponseReupload.getResponseEntity().getSuccesses().get(0).getComponentIdentity())
                .token(currentUser.getToken())
                .followRedirection(false)
                .expectedResponseCode(HttpStatus.SC_MOVED_PERMANENTLY);

        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ResponseWrapper response = HTTPRequest.build(requestEntityExpect301).get();

        String redirectLocation = response.getHeaders().getValue("Location");
        String originalEndpoint = String.format(CidAppAPIEnum.COMPONENTS_BY_COMPONENT_ID.getEndpointString(), componentResponseReupload.getResponseEntity().getSuccesses().get(0).getComponentIdentity());

        assertThat(redirectLocation, is(not(originalEndpoint)));

        RequestEntity requestEntityRedirected =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENTS_BY_COMPONENT_ID, ComponentIdentityResponse.class)
                .inlineVariables(componentResponse.getResponseEntity().getSuccesses().get(0).getComponentIdentity())
                .token(currentUser.getToken())
                .followRedirection(true)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ComponentIdentityResponse> responseRedirected = HTTPRequest.build(requestEntityRedirected).get();
        assertThat(responseRedirected.getResponseEntity().getIdentity(), is(equalTo(redirectLocation.substring(redirectLocation.length() - 12))));
    }

}
