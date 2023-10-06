package com.apriori.evaluate;

import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.response.CostingTemplate;
import com.apriori.cidappapi.models.response.componentiteration.AnalysisOfScenario;
import com.apriori.cidappapi.models.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.models.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.IterationsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.NewCostingLabelEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

@ExtendWith(TestRulesApi.class)
public class CostAllCadTests {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final IterationsUtil iterationsUtil = new IterationsUtil();
    private SoftAssertions softAssertions;

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5421, 565, 567})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void cadFormatSLDPRT() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build());

        scenariosUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(componentResponse.getComponentIdentity())
                .scenarioIdentity(componentResponse.getScenarioIdentity())
                .costingTemplate(CostingTemplate.builder()
                    .processGroupName(processGroupEnum.getProcessGroup())
                    .build())
                .user(currentUser)
                .build());

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(componentResponse.getComponentIdentity())
                .scenarioIdentity(componentResponse.getScenarioIdentity())
                .user(currentUser)
                .build());

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(analysisOfScenario.getMaterialCost()).isCloseTo(Double.valueOf(27.44), Offset.offset(15.0));
        softAssertions.assertThat(analysisOfScenario.getLaborCost()).isCloseTo(Double.valueOf(6.30), Offset.offset(5.0));
        softAssertions.assertThat(analysisOfScenario.getDirectOverheadCost()).isCloseTo(Double.valueOf(1.69), Offset.offset(5.0));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - par")
    public void cadFormatPar() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "26136";
        final String componentExtension = ".par";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - CATPart")
    public void testCADFormatCATPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName = "Plastic moulded cap DFM";
        final String componentExtension = ".CATPart";
        final String materialName = "ABS";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - prt.4")
    public void testCADFormatPRT4() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "turning";
        final String componentExtension = ".prt.4";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - Creo")
    public void testCADFormatCreo() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "turning";
        final String componentExtension = ".prt.4";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - NX")
    public void testCADFormatNX() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Locker_bottom_panel";
        final String componentExtension = ".prt";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - Inventor")
    public void testCADFormatInventor() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "VERTICAL PLATE";
        final String componentExtension = ".ipt";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - STEP")
    public void testCADFormatSTEP() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "partbody_2";
        final String componentExtension = ".stp";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - Parasolid")
    public void testCADFormatParaSolid() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "bracket_basic_steel_PMI";
        final String componentExtension = ".x_t";
        final String materialName = "Steel, Hot Worked, AISI 1010";

        uploadCostCadPartAndAssert(processGroupEnum, componentName, componentExtension, materialName);
    }

    @Test
    @TestRail(id = {5421})
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

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        scenariosUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(componentResponse.getComponentIdentity())
                .scenarioIdentity(componentResponse.getScenarioIdentity())
                .costingTemplate(CostingTemplate.builder()
                    .processGroupName(processGroupEnum.getProcessGroup())
                    .materialName(material)
                    .build())
                .user(currentUser)
                .build());

        ScenarioResponse scenarioRepresentation = scenariosUtil.getScenarioCompleted(componentResponse);

        assertThat(scenarioRepresentation.getScenarioState(), is(equalTo(NewCostingLabelEnum.COST_COMPLETE.name())));
    }
}
