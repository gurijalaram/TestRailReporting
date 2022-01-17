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
import com.apriori.cidappapi.utils.IterationsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
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
import testsuites.suiteinterfaces.SmokeTests;

import java.io.File;

public class CostAllCadTests {

    private final CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final IterationsUtil iterationsUtil = new IterationsUtil();

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421", "565", "567"})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void cadFormatSLDPRT() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        scenariosUtil.postCostScenario(
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

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentId(componentResponse.getComponentIdentity())
                .scenarioId(componentResponse.getScenarioIdentity())
                .user(currentUser)
                .build());

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        assertThat(analysisOfScenario.getMaterialCost(), is(closeTo(27.44, 15)));
        assertThat(analysisOfScenario.getLaborCost(), is(closeTo(6.30, 5)));
        assertThat(analysisOfScenario.getDirectOverheadCost(), is(closeTo(1.69, 5)));
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - par")
    public void cadFormatPar() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "26136";
        final String componentExtension = ".par";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - CATPart")
    public void testCADFormatCATPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName = "Plastic moulded cap DFM";
        final String componentExtension = ".CATPart";
        final String materialName = "ABS";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - prt.4")
    public void testCADFormatPRT4() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "turning";
        final String componentExtension = ".prt.4";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - Creo")
    public void testCADFormatCreo() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "turning";
        final String componentExtension = ".prt.4";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - NX")
    public void testCADFormatNX() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Locker_bottom_panel";
        final String componentExtension = ".prt";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - Inventor")
    public void testCADFormatInventor() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "VERTICAL PLATE";
        final String componentExtension = ".ipt";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - STEP")
    public void testCADFormatSTEP() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "partbody_2";
        final String componentExtension = ".stp";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - Parasolid")
    public void testCADFormatParaSolid() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "bracket_basic_steel_PMI";
        final String componentExtension = ".x_t";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - ACIS")
    public void testCADFormatParaACIS() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName = "Plastic moulded cap thinPart";
        final String componentExtension = ".SAT";
        final String materialName = "ABS";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    private void uploadCostCadPartAndAssert(final ProcessGroupEnum processGroupEnum, final String componentName, final String extension, final String material) {
        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);

        Item componentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);

        scenariosUtil.postCostScenario(
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

        ResponseWrapper<ScenarioResponse> scenarioRepresentation = scenariosUtil.getScenarioRepresentation(
            ScenarioRepresentationBuilder.builder()
                .item(componentResponse)
                .user(currentUser)
                .build());

        assertThat(scenarioRepresentation.getResponseEntity().getScenarioState(), is(equalTo(NewCostingLabelEnum.COST_COMPLETE.name())));
    }
}
