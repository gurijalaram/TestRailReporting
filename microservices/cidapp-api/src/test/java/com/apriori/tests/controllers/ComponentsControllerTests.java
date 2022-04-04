package com.apriori.tests.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.ComponentIdentityResponse;
import com.apriori.cidappapi.entity.response.GetComponentItems;
import com.apriori.cidappapi.entity.response.GetComponentResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentsControllerTests {

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private UserCredentials currentUser;

    @Test
    @Description("Add a new component")
    public void postComponent() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.CASTING_DIE;
        final String componentName = "Casting";
        File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ComponentInfoBuilder postComponentResponse = componentsUtil.postComponentQueryCSS(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        assertThat(postComponentResponse.getScenarioIdentity(), is(not(emptyString())));
    }

    @Test
    @Description("Add new components")
    public void postComponents() {
        final File file1 = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_DIE, "Casting.prt");
        final File file2 = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Plastic moulded cap DFM.CATPart");
        final List<File> resourceFiles = Arrays.asList(file1, file2);
        final String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        final List<String> scenarioNames = Arrays.asList(scenarioName1, scenarioName2);
        final UserCredentials currentUser = UserUtil.getUser();

        // TODO: 04/04/2022 cn - @moya we need to discuss and decide how to write this test
        ComponentsUtil postComponentResponse = componentsUtil.postMultiComponentsQueryCss(ComponentInfoBuilder.builder()
            .scenarioNames(scenarioNames)
            .resourceFiles(resourceFiles)
            .user(currentUser)
            .build());
    }

    @Test
    @Description("Find components for the current user matching a specified query")
    public void getComponents() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.CASTING_DIE;
        final String componentName = "Casting";
        File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ComponentInfoBuilder postComponentResponse = componentsUtil.postComponentQueryCSS(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build());

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

        ComponentInfoBuilder scenarioItem = componentsUtil.postComponentQueryCSS(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build());

        ResponseWrapper<ComponentIdentityResponse> componentIdentityResponse = componentsUtil.getComponentIdentity(scenarioItem);

        assertThat(componentIdentityResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
