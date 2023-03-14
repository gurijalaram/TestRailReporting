package tests.acs;

import com.apriori.acs.entity.response.acs.costresults.CostResultsRootItem;
import com.apriori.acs.entity.response.acs.costresults.CostResultsRootResponse;
import com.apriori.acs.entity.response.acs.costresults.ProcessInstanceKey;
import com.apriori.acs.entity.response.acs.costresults.PropertyValueMap;
import com.apriori.acs.entity.response.acs.costresults.ResultMapBean;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import tests.workorders.WorkorderAPITests;
import testsuites.categories.AcsTest;

public class CostResultsTests {
    private AcsResources acsResources = new AcsResources();
    private WorkorderAPITests workorderAPITests = new WorkorderAPITests();

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21579")
    @Description("Get Root Cost Results after Costing Sheet Metal")
    public void testGetCostRootResultsSheetMetal() {
        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();

        CostResultsRootResponse costResultsRootResponse = acsResources.uploadAndCost(processGroup, "bracket_basic.prt", "ROOT", workorderAPITests.setupProductionInfoInputs());

        SoftAssertions softAssertions = new SoftAssertions();

        CostResultsRootItem costResultsRootItem = costResultsRootResponse.get(0);
        ProcessInstanceKey processInstanceKey = costResultsRootItem.getProcessInstanceKey();
        ResultMapBean resultMapBean = costResultsRootItem.getResultMapBean();
        PropertyValueMap propertyValueMap = resultMapBean.getPropertyValueMap();

        softAssertions.assertThat(processInstanceKey.getProcessGroupName()).isEqualTo("Sheet Metal");
        softAssertions.assertThat(resultMapBean).isNotNull();
        softAssertions.assertThat(propertyValueMap.getTotalCarbon()).isNotNull();
        softAssertions.assertThat(costResultsRootItem.getCostingFailed()).isEqualTo(false);
        softAssertions.assertThat(costResultsRootItem.getDepth()).isEqualTo("ROOT");
        softAssertions.assertThat(costResultsRootItem.getSecondaryProcess()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21584")
    @Description("Get Root Cost Results after Costing Stock Machining")
    public void testGetCostRootResultsStockMachining() {
        String processGroup = ProcessGroupEnum.STOCK_MACHINING.getProcessGroup();

        CostResultsRootResponse costResultsRootResponse = acsResources.uploadAndCost(processGroup, "bracket_basic.prt", "ROOT", workorderAPITests.setupProductionInfoInputs());

        SoftAssertions softAssertions = new SoftAssertions();

        CostResultsRootItem costResultsRootItem = costResultsRootResponse.get(0);
        ProcessInstanceKey processInstanceKey = costResultsRootItem.getProcessInstanceKey();
        ResultMapBean resultMapBean = costResultsRootItem.getResultMapBean();
        PropertyValueMap propertyValueMap = resultMapBean.getPropertyValueMap();

        softAssertions.assertThat(processInstanceKey.getProcessGroupName()).isEqualTo("Stock Machining");
        softAssertions.assertThat(resultMapBean).isNotNull();
        softAssertions.assertThat(propertyValueMap.getTotalCarbon()).isNotNull();
        softAssertions.assertThat(costResultsRootItem.getCostingFailed()).isEqualTo(false);
        softAssertions.assertThat(costResultsRootItem.getDepth()).isEqualTo("ROOT");
        softAssertions.assertThat(costResultsRootItem.getSecondaryProcess()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21575")
    @Description("Get Root Cost Results after Costing Plastic Molding")
    public void testGetCostRootResultsPlasticMolding() {
        String processGroup = ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup();

        CostResultsRootResponse costResultsRootResponse = acsResources.uploadAndCost(processGroup, "M3CapScrew.CATPart", "ROOT", workorderAPITests.setupProductionInfoInputs());

        SoftAssertions softAssertions = new SoftAssertions();

        CostResultsRootItem costResultsRootItem = costResultsRootResponse.get(0);
        ProcessInstanceKey processInstanceKey = costResultsRootItem.getProcessInstanceKey();
        ResultMapBean resultMapBean = costResultsRootItem.getResultMapBean();
        PropertyValueMap propertyValueMap = resultMapBean.getPropertyValueMap();

        softAssertions.assertThat(processInstanceKey.getProcessGroupName()).isEqualTo("Plastic Molding");
        softAssertions.assertThat(resultMapBean).isNotNull();
        softAssertions.assertThat(propertyValueMap.getTotalCarbon()).isNotNull();
        softAssertions.assertThat(costResultsRootItem.getCostingFailed()).isEqualTo(false);
        softAssertions.assertThat(costResultsRootItem.getDepth()).isEqualTo("ROOT");
        softAssertions.assertThat(costResultsRootItem.getSecondaryProcess()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21571")
    @Description("Get Root Cost Results after Costing Casting - Die")
    public void testGetCostRootResultsCastingDie() {
        String processGroup = ProcessGroupEnum.CASTING_DIE.getProcessGroup();

        CostResultsRootResponse costResultsRootResponse = acsResources.uploadAndCost(processGroup, "CastedPart.CATPart", "ROOT", workorderAPITests.setupProductionInfoInputs());

        SoftAssertions softAssertions = new SoftAssertions();

        CostResultsRootItem costResultsRootItem = costResultsRootResponse.get(0);
        ProcessInstanceKey processInstanceKey = costResultsRootItem.getProcessInstanceKey();
        ResultMapBean resultMapBean = costResultsRootItem.getResultMapBean();
        PropertyValueMap propertyValueMap = resultMapBean.getPropertyValueMap();

        softAssertions.assertThat(processInstanceKey.getProcessGroupName()).isEqualTo("Casting - Die");
        softAssertions.assertThat(resultMapBean).isNotNull();
        softAssertions.assertThat(propertyValueMap.getTotalCarbon()).isNotNull();
        softAssertions.assertThat(costResultsRootItem.getCostingFailed()).isEqualTo(false);
        softAssertions.assertThat(costResultsRootItem.getDepth()).isEqualTo("ROOT");
        softAssertions.assertThat(costResultsRootItem.getSecondaryProcess()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21572")
    @Description("Get Root Cost Results after Costing Casting - Sand")
    public void testGetCostRootResultsCastingSand() {
        String processGroup = ProcessGroupEnum.CASTING_SAND.getProcessGroup();

        CostResultsRootResponse costResultsRootResponse = acsResources.uploadAndCost(
            processGroup,
            "SandCastIssues.SLDPRT",
            "ROOT", workorderAPITests.setupProductionInfoInputs()
        );

        SoftAssertions softAssertions = new SoftAssertions();

        CostResultsRootItem costResultsRootItem = costResultsRootResponse.get(0);
        ProcessInstanceKey processInstanceKey = costResultsRootItem.getProcessInstanceKey();
        ResultMapBean resultMapBean = costResultsRootItem.getResultMapBean();
        PropertyValueMap propertyValueMap = resultMapBean.getPropertyValueMap();

        softAssertions.assertThat(processInstanceKey.getProcessGroupName()).isEqualTo("Casting - Sand");
        softAssertions.assertThat(resultMapBean).isNotNull();
        softAssertions.assertThat(propertyValueMap.getTotalCarbon()).isNotNull();
        softAssertions.assertThat(costResultsRootItem.getCostingFailed()).isEqualTo(false);
        softAssertions.assertThat(costResultsRootItem.getDepth()).isEqualTo("ROOT");
        softAssertions.assertThat(costResultsRootItem.getSecondaryProcess()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21573")
    @Description("Get Root Cost Results after Costing Casting - Investment")
    public void testGetCostRootResultsCastingInvestment() {
        String processGroup = ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup();

        CostResultsRootResponse costResultsRootResponse = acsResources.uploadAndCost(processGroup, "AP-000-506.prt.1", "ROOT", workorderAPITests.setupProductionInfoInputs());

        SoftAssertions softAssertions = new SoftAssertions();

        CostResultsRootItem costResultsRootItem = costResultsRootResponse.get(0);
        ProcessInstanceKey processInstanceKey = costResultsRootItem.getProcessInstanceKey();
        ResultMapBean resultMapBean = costResultsRootItem.getResultMapBean();
        PropertyValueMap propertyValueMap = resultMapBean.getPropertyValueMap();

        softAssertions.assertThat(processInstanceKey.getProcessGroupName()).isEqualTo("Casting - Investment");
        softAssertions.assertThat(resultMapBean).isNotNull();
        softAssertions.assertThat(propertyValueMap.getTotalCarbon()).isNotNull();
        softAssertions.assertThat(costResultsRootItem.getCostingFailed()).isEqualTo(false);
        softAssertions.assertThat(costResultsRootItem.getDepth()).isEqualTo("ROOT");
        softAssertions.assertThat(costResultsRootItem.getSecondaryProcess()).isEqualTo(false);
        softAssertions.assertAll();
    }
}
