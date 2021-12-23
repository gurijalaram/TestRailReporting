package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.builder.ScenarioRepresentationBuilder;
import com.apriori.cidappapi.entity.response.componentiteration.AnalysisOfScenario;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.CidAppTestUtil;
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

public class CostAllCadTests {

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();
    private UserCredentials currentUser;
    private File resourceFile;

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421", "565", "567"})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void cadFormatSLDPRT() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cidAppTestUtil.postCostScenario(
            ComponentInfoBuilder.builder()
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
            ComponentInfoBuilder.builder()
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
    public void cadFormatPar() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "26136";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".par");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cadInfo(processGroupEnum, componentName, scenarioName, componentResponse, "Steel, Hot Worked, AISI 1010");
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - CATPart")
    public void testCADFormatCATPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Plastic moulded cap DFM";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cadInfo(processGroupEnum, componentName, scenarioName, componentResponse, "ABS");
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - prt.4")
    public void testCADFormatPRT4() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "turning";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.4");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cadInfo(processGroupEnum, componentName, scenarioName, componentResponse, "Steel, Hot Worked, AISI 1010");
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - Creo")
    public void testCADFormatCreo() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "turning";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.4");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cadInfo(processGroupEnum, componentName, scenarioName, componentResponse, "Steel, Hot Worked, AISI 1010");
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - NX")
    public void testCADFormatNX() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "Locker_bottom_panel";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cadInfo(processGroupEnum, componentName, scenarioName, componentResponse, "Steel, Hot Worked, AISI 1010");
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - Inventor")
    public void testCADFormatInventor() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "VERTICAL PLATE";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".ipt");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cadInfo(processGroupEnum, componentName, scenarioName, componentResponse, "Steel, Hot Worked, AISI 1010");
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - STEP")
    public void testCADFormatSTEP() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cadInfo(processGroupEnum, componentName, scenarioName, componentResponse, "Steel, Hot Worked, AISI 1010");
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - Parasolid")
    public void testCADFormatParaSolid() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "bracket_basic_steel_PMI";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".x_t");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cadInfo(processGroupEnum, componentName, scenarioName, componentResponse, "Steel, Hot Worked, AISI 1010");
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - ACIS")
    public void testCADFormatParaACIS() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Plastic moulded cap thinPart";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SAT");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        cadInfo(processGroupEnum, componentName, scenarioName, componentResponse, "ABS");
    }

    private void cadInfo(ProcessGroupEnum processGroupEnum, String componentName, String scenarioName, Item componentResponse, String material) {
        cidAppTestUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentId(componentResponse.getComponentIdentity())
                .scenarioId(componentResponse.getScenarioIdentity())
                .processGroup(processGroupEnum)
                .mode("manual")
                .material(material)
                .user(currentUser)
                .build());

        ResponseWrapper<ScenarioResponse> scenarioResponseResponse = cidAppTestUtil.getScenarioRepresentation(
            ScenarioRepresentationBuilder.builder()
                .item(componentResponse)
                .user(currentUser)
                .build());

        assertThat(scenarioResponseResponse.getResponseEntity().getScenarioState(), is(equalTo(NewCostingLabelEnum.COST_COMPLETE.name())));
    }
}
