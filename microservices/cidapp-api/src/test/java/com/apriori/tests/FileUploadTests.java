package com.apriori.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class FileUploadTests {

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    @Test
    @Description("Upload, cost and publish a part")
    public void uploadCostPublish() {

        String componentName = "Casting";
        File resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_DIE, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String mode = "manual";
        String materialName = "Aluminum, Cast, ANSI AL380.0";
        UserCredentials currentUser = UserUtil.getUser();

        Item componentResponse = cidAppTestUtil.postCssComponents(componentName, scenarioName, resourceFile, currentUser);

        cidAppTestUtil.postCostScenario(componentName, scenarioName, componentResponse.getComponentIdentity(), componentResponse.getScenarioIdentity(), ProcessGroupEnum.CASTING_DIE, DigitalFactoryEnum.APRIORI_USA, mode, materialName, currentUser);

        ResponseWrapper<ScenarioResponse> publishResponse = cidAppTestUtil.postPublishScenario(componentResponse, componentResponse.getComponentIdentity(), componentResponse.getScenarioIdentity(), currentUser);

        assertThat(publishResponse.getResponseEntity().getLastAction(), is("PUBLISH"));
        assertThat(publishResponse.getResponseEntity().getPublished(), is(true));
    }
}
