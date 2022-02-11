package com.apriori.tests.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.io.File;

public class IterationsControllerTests {

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private UserCredentials currentUser;

    @Test
    @Description("Get the latest iteration")
    public void getComponentsIterationsLatest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.CASTING_DIE;
        final String componentName = "Casting";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ScenarioItem postComponentResponse = componentsUtil.postComponentQueryCSS(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .user(currentUser)
                .build(),
            resourceFile);

        ResponseWrapper<ComponentIteration> getComponentIterationResponse = componentsUtil.getComponentIterationLatest(postComponentResponse, currentUser);

        assertThat(getComponentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
