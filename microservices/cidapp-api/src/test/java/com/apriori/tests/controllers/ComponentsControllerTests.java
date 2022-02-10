package com.apriori.tests.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.cidappapi.entity.response.ComponentIdentityResponse;
import com.apriori.cidappapi.entity.response.GetComponentItems;
import com.apriori.cidappapi.entity.response.GetComponentResponse;
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
import java.util.stream.Collectors;

public class ComponentsControllerTests {

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private UserCredentials currentUser;

    @Test
    @Description("Add a new component")
    public void postComponents() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.CASTING_DIE;
        final String componentName = "Casting";
        File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ScenarioItem postComponentResponse = componentsUtil.postComponentQueryCSS(componentName, scenarioName, resourceFile, currentUser);

        assertThat(postComponentResponse.getScenarioIdentity(), is(not(emptyString())));
    }

    @Test
    @Description("Find components for the current user matching a specified query")
    public void getComponents() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.CASTING_DIE;
        final String componentName = "Casting";
        File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ScenarioItem postComponentResponse = componentsUtil.postComponentQueryCSS(componentName, scenarioName, resourceFile, currentUser);

        ResponseWrapper<GetComponentResponse> getComponentResponse = componentsUtil.getComponents();

        assertThat(getComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(getComponentResponse.getResponseEntity().getItems().size(), is(greaterThan(0)));
        assertThat(getComponentResponse.getResponseEntity().getItems().stream().map(GetComponentItems::getIdentity).collect(Collectors.toList()), contains(postComponentResponse.getComponentIdentity()));
    }

    @Test
    @Description("Get the current representation of a component")
    public void getComponentIdentity() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.CASTING_DIE;
        final String componentName = "Casting";
        File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ScenarioItem postComponentResponse = componentsUtil.postComponentQueryCSS(componentName, scenarioName, resourceFile, currentUser);

        ResponseWrapper<ComponentIdentityResponse> componentIdentityResponse = componentsUtil.getComponentIdentity(postComponentResponse.getComponentIdentity());

        assertThat(componentIdentityResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
