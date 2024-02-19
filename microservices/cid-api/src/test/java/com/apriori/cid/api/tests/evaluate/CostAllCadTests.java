package com.apriori.cid.api.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.IterationsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.componentiteration.AnalysisOfScenario;
import com.apriori.shared.util.models.response.component.componentiteration.ComponentIteration;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CostAllCadTests {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final IterationsUtil iterationsUtil = new IterationsUtil();
    private final SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    // TODO: 20/12/2023 cn - all these tests have the same testrail id. this is not correct
    @Test
    @Tags({
        @Tag(SMOKE),
        @Tag(API_SANITY)
    })
    @TestRail(id = {5421, 565, 567})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void cadFormatSLDPRT() {
        component = new ComponentRequestUtil().getComponentByExtension("SLDPRT");

        componentsUtil.postComponent(component);

        scenariosUtil.postCostScenario(component);

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(component);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        softAssertions.assertThat(analysisOfScenario.getMaterialCost()).isCloseTo(Double.valueOf(27.44), Offset.offset(15.0));
        softAssertions.assertThat(analysisOfScenario.getLaborCost()).isCloseTo(Double.valueOf(6.30), Offset.offset(5.0));
        softAssertions.assertThat(analysisOfScenario.getDirectOverheadCost()).isCloseTo(Double.valueOf(1.69), Offset.offset(5.0));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - par")
    public void cadFormatPar() {
        component = new ComponentRequestUtil().getComponent("26136");

        uploadCostCadPartAndAssert(component);
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - CATPart")
    public void testCADFormatCATPart() {
        component = new ComponentRequestUtil().getComponent("Plastic moulded cap DFM");

        uploadCostCadPartAndAssert(component);
    }

    @Test
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - prt.4")
    public void testCADFormatPRT4() {
        component = new ComponentRequestUtil().getComponent("turning");

        uploadCostCadPartAndAssert(component);
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - NX")
    public void testCADFormatNX() {
        component = new ComponentRequestUtil().getComponent("Locker_bottom_panel");

        uploadCostCadPartAndAssert(component);
    }

    @Test
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - Inventor")
    public void testCADFormatInventor() {
        component = new ComponentRequestUtil().getComponent("VERTICAL PLATE");

        uploadCostCadPartAndAssert(component);
    }

    @Test
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - STEP")
    public void testCADFormatSTEP() {
        component = new ComponentRequestUtil().getComponent("partbody_2");

        uploadCostCadPartAndAssert(component);
    }

    @Test
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - Parasolid")
    public void testCADFormatParaSolid() {
        component = new ComponentRequestUtil().getComponent("bracket_basic_steel_PMI");

        uploadCostCadPartAndAssert(component);
    }

    @Test
    @TestRail(id = {5421})
    @Description("CAD file from all supported CAD formats - ACIS")
    public void testCADFormatParaACIS() {
        component = new ComponentRequestUtil().getComponent("Plastic moulded cap thinPart");

        uploadCostCadPartAndAssert(component);
    }

    private void uploadCostCadPartAndAssert(ComponentInfoBuilder component) {
        ComponentInfoBuilder componentResponse = componentsUtil.postComponent(component);

        scenariosUtil.postGroupCostScenarios(component);

        ScenarioResponse scenarioRepresentation = scenariosUtil.getScenarioCompleted(componentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioState()).isEqualTo(NewCostingLabelEnum.COST_COMPLETE.name());

        softAssertions.assertAll();
    }
}
