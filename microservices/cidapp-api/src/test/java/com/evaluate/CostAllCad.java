package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cidappapi.entity.response.componentiteration.AnalysisOfScenario;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.cidappapi.utils.CostComponentInfo;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
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

        ResponseWrapper<ComponentIteration> componentIterationResponse = cidAppTestUtil.getComponentIterationLatest(
            CostComponentInfo.builder()
                .componentId(componentResponse.getComponentIdentity())
                .scenarioId(componentResponse.getScenarioIdentity())
                .user(currentUser)
                .build());

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getResponse().getAnalysisOfScenario();

        assertThat(analysisOfScenario.getMaterialCost(), is(closeTo(27.44, 15)));
        assertThat(analysisOfScenario.getLaborCost(), is(closeTo(6.30, 5)));
        assertThat(analysisOfScenario.getDirectOverheadCost(), is(closeTo(1.69, 5)));
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - par")
    public void testCADFormatPar() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "26136";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".par");
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

        ResponseWrapper<ScenarioResponse> scenarioResponseResponse = cidAppTestUtil.getScenarioRepresentation(componentResponse, "COST", false, currentUser);

        assertThat(scenarioResponseResponse.getResponseEntity().getScenarioState(), is(equalTo(NewCostingLabelEnum.COST_COMPLETE.name())));
    }
}
