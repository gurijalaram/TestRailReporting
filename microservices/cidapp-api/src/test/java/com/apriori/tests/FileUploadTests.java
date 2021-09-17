package com.apriori.tests;

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
import java.util.List;

public class FileUploadTests extends CidAppTestUtil {

    @Test
    @Description("Upload, cost and publish a part")
    public void uploadCostPublish() {

        String componentName = "Casting";
        File resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_DIE, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String mode = "manual";
        String materialName = "Aluminum, Cast, ANSI AL380.0";
        UserCredentials currentUser = UserUtil.getUser();

        Item componentResponse = new CidAppTestUtil().postCssComponents(componentName, scenarioName, resourceFile, currentUser);

        List<Item> costResponse = new CidAppTestUtil().postCostScenario(componentName, scenarioName, componentResponse.getComponentIdentity(), componentResponse.getScenarioIdentity(), ProcessGroupEnum.CASTING_DIE, DigitalFactoryEnum.APRIORI_USA, mode, materialName, currentUser);
        String costState = costResponse.get(0).getScenarioState();

        ResponseWrapper<ScenarioResponse> publishResponse = new CidAppTestUtil().postPublishScenario(componentResponse, componentResponse.getComponentIdentity(), componentResponse.getScenarioIdentity(), currentUser);
        String publishState = publishResponse.getResponseEntity().getScenarioState();
        //new CidAppTestUtil().publishScenario(componentResponse.getComponentUpdatedBy(), componentResponse.getComponentIdentity(), componentResponse.getScenarioIdentity(), currentUser);
        //publish();
    }


}
