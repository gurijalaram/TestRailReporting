package com.apriori.tests;

import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class FileUploadTests extends CidAppTestUtil {

    @Test
    @Description("Upload, cost and publish a part")
    public void uploadCostPublish() {

        String componentName = "Casting";
        File resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_DIE, componentName + ".prt");
        UserCredentials currentUser = UserUtil.getUser();

        Item componentResponse = new CidAppTestUtil().postCssComponents(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser);

        new CidAppTestUtil().costScenario(componentResponse.getComponentIdentity(), componentResponse.getScenarioIdentity(), ProcessGroupEnum.CASTING_DIE, DigitalFactoryEnum.APRIORI_USA, currentUser);
        //new CidAppTestUtil().publishScenario(componentResponse.getComponentUpdatedBy(), componentResponse.getComponentIdentity(), componentResponse.getScenarioIdentity(), currentUser);
        //publish();
    }


}
