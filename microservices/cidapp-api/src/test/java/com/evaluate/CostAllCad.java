package com.evaluate;

import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.cidappapi.utils.CostComponentInfo;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuite.suiteinterface.SmokeTests;

import java.io.File;

public class CostAllCad {

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();
    private UserCredentials currentUser;
    private File resourceFile;

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421", "565", "567"})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void testCADFormatSLDPRT() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cidAppTestUtil.postCostScenario(
                CostComponentInfo.builder()
                    .componentName(componentName)
                    .scenarioName(scenarioName)
                    .componentId(componentResponse.getComponentIdentity())
                    .scenarioId(componentResponse.getScenarioIdentity())
                    .processGroup(processGroupEnum)
                    .mode("manual")
                    .material("Steel, Hot Worked, AISI 1010")
                    .user(currentUser)
                    .build());

    }
}
