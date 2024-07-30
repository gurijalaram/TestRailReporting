package com.apriori.acs.api.tests;

import com.apriori.acs.api.models.response.acs.costresults.CostResultsRootItem;
import com.apriori.acs.api.models.response.acs.costresults.CostResultsRootResponse;
import com.apriori.acs.api.models.response.acs.costresults.ProcessInstanceKey;
import com.apriori.acs.api.models.response.acs.costresults.PropertyInfoMap;
import com.apriori.acs.api.models.response.acs.costresults.PropertyValueMap;
import com.apriori.acs.api.models.response.acs.costresults.ResultMapBean;
import com.apriori.acs.api.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.api.tests.workorders.WorkorderApiUtils;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CostResultsTests {
    private WorkorderApiUtils workorderApiUtils;
    private SoftAssertions softAssertions;
    private AcsResources acsResources;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        workorderApiUtils = new WorkorderApiUtils(requestEntityUtil);
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = 21569)
    @Description("Get Root Cost Results after Costing Additive Manufacturing")
    public void testGetCostRootResultsAdditiveManufacturing() {
        String processGroup = ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "ADD-LOW-001.SLDPRT", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Additive Manufacturing");
    }

    @Test
    @TestRail(id = 21570)
    @Description("Get Root Cost Results after Costing Bar & Tube")
    public void testGetCostRootResultsBarAndTubeFab() {
        String processGroup = ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "B&T-LOW-001.SLDPRT", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Bar & Tube Fab");
    }

    @Test
    @TestRail(id = 21574)
    @Description("Get Root Cost Results after Costing Forging")
    public void testGetCostRootResultsForging() {
        String processGroup = ProcessGroupEnum.FORGING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "Pin.SLDPRT", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Forging");
    }

    @Test
    @TestRail(id = 21576)
    @Description("Get Root Cost Results after Costing Powder Metal")
    public void testGetCostRootResultsPowderMetal() {
        String processGroup = ProcessGroupEnum.POWDER_METAL.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "116-5809.prt.1", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Powder Metal");
    }

    @Test
    @TestRail(id = 21577)
    @Description("Get Root Cost Results after Costing Rapid Prototyping")
    public void testGetCostRootResultsRapidPrototyping() {
        String processGroup = ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "blow_mold_duct_1.prt.1", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Rapid Prototyping");
    }

    @Test
    @TestRail(id = 21578)
    @Description("Get Root Cost Results after Costing Roto and Blow Molding")
    public void testGetCostRootResultsRotoBlowMolding() {
        String processGroup = ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "blow_mold_duct_1.prt.1", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Roto & Blow Molding");
    }

    @Test
    @TestRail(id = 21579)
    @Description("Get Root Cost Results after Costing Sheet Metal")
    public void testGetCostRootResultsSheetMetal() {
        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "bracket_basic.prt", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Sheet Metal");
    }

    @Test
    @TestRail(id = 21581)
    @Description("Get Root Cost Results after Costing Sheet Metal - Hydroforming")
    public void testGetCostRootResultsSheetMetalHydroforming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "FlangedRound.SLDPRT", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Sheet Metal - Hydroforming");
    }

    @Test
    @TestRail(id = 21580)
    @Description("Get Root Cost Results after Costing Sheet Metal - Transfer Die")
    public void testGetCostRootResultsSheetMetalTransferDie() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "bracket_basic.prt", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Sheet Metal - Transfer Die");
    }

    @Test
    @TestRail(id = 21583)
    @Description("Get Root Cost Results after Costing Sheet Metal - Stretch Forming")
    public void testGetCostRootResultsSheetMetalStretchForming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "Hydroforming.stp", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Sheet Metal - Stretch Forming");
    }

    @Test
    @TestRail(id = 21582)
    @Description("Get Root Cost Results after Costing Sheet Metal - Roll Forming")
    public void testGetCostRootResultsSheetMetalRollForming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_ROLLFORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "z_purlin.prt.1", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Sheet Metal - Roll Forming");
    }

    @Test
    @TestRail(id = 21584)
    @Description("Get Root Cost Results after Costing Stock Machining")
    public void testGetCostRootResultsStockMachining() {
        String processGroup = ProcessGroupEnum.STOCK_MACHINING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "Machining-DTC_Issue_SideMillingLengthDia.SLDPRT", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Stock Machining");
    }

    @Test
    @TestRail(id = 21575)
    @Description("Get Root Cost Results after Costing Plastic Molding")
    public void testGetCostRootResultsPlasticMolding() {
        String processGroup = ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "M3CapScrew.CATPart", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Plastic Molding");
    }

    @Test
    @TestRail(id = 21571)
    @Description("Get Root Cost Results after Costing Casting - Die")
    public void testGetCostRootResultsCastingDie() {
        String processGroup = ProcessGroupEnum.CASTING_DIE.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "CastedPart.CATPart", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Casting - Die");
    }

    @Test
    @TestRail(id = 21572)
    @Description("Get Root Cost Results after Costing Casting - Sand")
    public void testGetCostRootResultsCastingSand() {
        String processGroup = ProcessGroupEnum.CASTING_SAND.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "SandCastIssues.SLDPRT", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Casting - Sand");
    }

    @Test
    @TestRail(id = 21573)
    @Description("Get Root Cost Results after Costing Casting - Investment")
    public void testGetCostRootResultsCastingInvestment() {
        String processGroup = ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "piston_model1.prt", workorderApiUtils.setupProductionInfoInputs());
        CostResultsRootResponse costResultsRootResponse = acsResources.getCostResults(costOutputs.getScenarioIterationKey(), "ROOT", CostResultsRootResponse.class).getResponseEntity();

        performCostResultsAssertions(costResultsRootResponse, "Casting - Investment");
    }

    private void performCostResultsAssertions(CostResultsRootResponse costResultsRootItems, String processGroupName) {
        CostResultsRootItem costResultsRootItem = costResultsRootItems.get(0);
        ProcessInstanceKey processInstanceKey = costResultsRootItem.getProcessInstanceKey();
        ResultMapBean resultMapBean = costResultsRootItem.getResultMapBean();
        PropertyValueMap propertyValueMap = resultMapBean.getPropertyValueMap();
        PropertyInfoMap propertyInfoMap = resultMapBean.getPropertyInfoMap();

        softAssertions.assertThat(processInstanceKey.getProcessGroupName()).isEqualTo(processGroupName);
        softAssertions.assertThat(resultMapBean).isNotNull();
        softAssertions.assertThat(propertyValueMap.getTotalCarbon()).isNotNull();
        softAssertions.assertThat(propertyInfoMap.getAnnualCarbon().getDisplayName()).isNotNull();
        softAssertions.assertThat(propertyInfoMap.getTotalCarbon().getCategory()).isNotNull();
        softAssertions.assertThat(costResultsRootItem.getCostingFailed()).isEqualTo(false);
        softAssertions.assertThat(costResultsRootItem.getDepth()).isEqualTo("ROOT");
        softAssertions.assertThat(costResultsRootItem.getSecondaryProcess()).isEqualTo(false);
        softAssertions.assertAll();
    }
}
