package tests.acs;

import com.apriori.acs.entity.request.workorders.NewPartRequest;
import com.apriori.acs.entity.response.acs.costresults.CostResultsGcdItem;
import com.apriori.acs.entity.response.acs.costresults.CostResultsGcdResponse;
import com.apriori.acs.entity.response.acs.costresults.CostResultsProcessItem;
import com.apriori.acs.entity.response.acs.costresults.CostResultsRootItem;
import com.apriori.acs.entity.response.acs.costresults.ProcessInstanceKey;
import com.apriori.acs.entity.response.acs.costresults.PropertyValueMap;
import com.apriori.acs.entity.response.acs.costresults.ResultMapBean;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import tests.workorders.WorkorderAPITests;
import testsuites.categories.AcsTest;

public class CostResultsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21579")
    @Description("Get Root Cost Results after Costing Sheet Metal")
    public void testGetCostRootResultsSheetMetal() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        CostResultsRootItem response = acsResources.getCostResults(
            costOutputs.getScenarioIterationKey(),
            "ROOT", CostResultsRootItem.class).getResponseEntity();


        SoftAssertions softAssertions = new SoftAssertions();

        //TODO: Assert on sustainability values once BA-2858 complete
        // Object processInstanceKey = ((LinkedHashMap<String, String>) response.get(0)).get("processInstanceKey");
        // Object resultMapBean = ((LinkedHashMap<String, String>) response.get(0)).get("resultMapBean");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) processInstanceKey).get("processGroupName")).isEqualTo("Sheet Metal");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) resultMapBean)).isNotNull();
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("costingFailed")).isEqualTo(false);
        // softAssertions.assertThat(((LinkedHashMap<String, String>) response.get(0)).get("depth")).isEqualTo("ROOT");
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("secondaryProcess")).isEqualTo(false);
        // softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21579")
    @Description("Get Root Cost Results after Costing Stock Machining")
    public void testGetCostRootResultsStockMachining() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.STOCK_MACHINING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        CostResultsRootItem response = acsResources.getCostResults(
            costOutputs.getScenarioIterationKey(),
            "ROOT", CostResultsRootItem.class).getResponseEntity();


        SoftAssertions softAssertions = new SoftAssertions();

        //TODO: Assert on sustainability values once BA-2858 complete
        // Object processInstanceKey = ((LinkedHashMap<String, String>) response.get(0)).get("processInstanceKey");
        // Object resultMapBean = ((LinkedHashMap<String, String>) response.get(0)).get("resultMapBean");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) processInstanceKey).get("processGroupName")).isEqualTo("Sheet Metal");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) resultMapBean)).isNotNull();
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("costingFailed")).isEqualTo(false);
        // softAssertions.assertThat(((LinkedHashMap<String, String>) response.get(0)).get("depth")).isEqualTo("ROOT");
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("secondaryProcess")).isEqualTo(false);
        // softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21579")
    @Description("Get Root Cost Results after Costing Plastic Molding")
    public void testGetCostRootResultsPlasticMolding() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "M3CapScrew.CATPart",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        CostResultsRootItem response = acsResources.getCostResults(
            costOutputs.getScenarioIterationKey(),
            "ROOT", CostResultsRootItem.class).getResponseEntity();


        SoftAssertions softAssertions = new SoftAssertions();

        //TODO: Assert on sustainability values once BA-2858 complete
        // Object processInstanceKey = ((LinkedHashMap<String, String>) response.get(0)).get("processInstanceKey");
        // Object resultMapBean = ((LinkedHashMap<String, String>) response.get(0)).get("resultMapBean");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) processInstanceKey).get("processGroupName")).isEqualTo("Sheet Metal");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) resultMapBean)).isNotNull();
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("costingFailed")).isEqualTo(false);
        // softAssertions.assertThat(((LinkedHashMap<String, String>) response.get(0)).get("depth")).isEqualTo("ROOT");
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("secondaryProcess")).isEqualTo(false);
        // softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21579")
    @Description("Get Root Cost Results after Costing Casting - Die")
    public void testGetCostRootResultsCastingDie() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.CASTING_DIE.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "CastedPart.CATPart",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        CostResultsRootItem response = acsResources.getCostResults(
            costOutputs.getScenarioIterationKey(),
            "ROOT", CostResultsRootItem.class).getResponseEntity();


        SoftAssertions softAssertions = new SoftAssertions();

        //TODO: Assert on sustainability values once BA-2858 complete
        // Object processInstanceKey = ((LinkedHashMap<String, String>) response.get(0)).get("processInstanceKey");
        // Object resultMapBean = ((LinkedHashMap<String, String>) response.get(0)).get("resultMapBean");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) processInstanceKey).get("processGroupName")).isEqualTo("Sheet Metal");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) resultMapBean)).isNotNull();
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("costingFailed")).isEqualTo(false);
        // softAssertions.assertThat(((LinkedHashMap<String, String>) response.get(0)).get("depth")).isEqualTo("ROOT");
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("secondaryProcess")).isEqualTo(false);
        // softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21579")
    @Description("Get Root Cost Results after Costing Casting - Sand")
    public void testGetCostRootResultsCastingSand() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.CASTING_SAND.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "DTCCastingIssues.catpart",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        CostResultsRootItem response = acsResources.getCostResults(
            costOutputs.getScenarioIterationKey(),
            "ROOT", CostResultsRootItem.class).getResponseEntity();


        SoftAssertions softAssertions = new SoftAssertions();

        //TODO: Assert on sustainability values once BA-2858 complete
        // Object processInstanceKey = ((LinkedHashMap<String, String>) response.get(0)).get("processInstanceKey");
        // Object resultMapBean = ((LinkedHashMap<String, String>) response.get(0)).get("resultMapBean");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) processInstanceKey).get("processGroupName")).isEqualTo("Sheet Metal");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) resultMapBean)).isNotNull();
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("costingFailed")).isEqualTo(false);
        // softAssertions.assertThat(((LinkedHashMap<String, String>) response.get(0)).get("depth")).isEqualTo("ROOT");
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("secondaryProcess")).isEqualTo(false);
        // softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21579")
    @Description("Get Root Cost Results after Costing Casting - Investment")
    public void testGetCostRootResultsCastingInvestment() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "AP-000-506.prt.1",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        CostResultsRootItem response = acsResources.getCostResults(
            costOutputs.getScenarioIterationKey(),
            "ROOT", CostResultsRootItem.class).getResponseEntity();


        SoftAssertions softAssertions = new SoftAssertions();

        //TODO: Assert on sustainability values once BA-2858 complete
        // Object processInstanceKey = ((LinkedHashMap<String, String>) response.get(0)).get("processInstanceKey");
        // Object resultMapBean = ((LinkedHashMap<String, String>) response.get(0)).get("resultMapBean");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) processInstanceKey).get("processGroupName")).isEqualTo("Sheet Metal");
        // softAssertions.assertThat(((LinkedHashMap<String, String>) resultMapBean)).isNotNull();
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("costingFailed")).isEqualTo(false);
        // softAssertions.assertThat(((LinkedHashMap<String, String>) response.get(0)).get("depth")).isEqualTo("ROOT");
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("secondaryProcess")).isEqualTo(false);
        // softAssertions.assertAll();
    }


    @Test
    @Ignore
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21579")
    @Description("Get Process Cost Results after Costing Sheet Metal")
    public void testGetCostProcessResultsSheetMetal() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        CostResultsProcessItem response = acsResources.getCostResults(
            costOutputs.getScenarioIterationKey(),
            "PROCESS", CostResultsProcessItem.class).getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        //TODO: Assert on sustainability values once BA-2858 complete
        ProcessInstanceKey processInstanceKey = response.get(0).getProcessInstanceKey();
        ResultMapBean resultMapBean = response.get(0).getResultMapBean();
        // Object resultMapBean = ((LinkedHashMap<String, String>) response.get(0)).get("resultMapBean");
        softAssertions.assertThat(processInstanceKey.getProcessGroupName()).isEqualTo("Sheet Metal");
        // TODO: 17/02/2023 cn - ben/steve pls fix the assertions below that i've intentionally commented
        softAssertions.assertThat(resultMapBean.getPropertyInfoMap()).isNotNull();
        // softAssertions.assertThat(((LinkedHashMap<String, String>) response.get(0)).get("vpeName")).isEqualTo("aPriori USA");
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("costingFailed")).isEqualTo(false);
        // softAssertions.assertThat(((LinkedHashMap<String, String>) response.get(0)).get("depth")).isEqualTo("PROCESS");
        // softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("secondaryProcess")).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @Ignore
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21579")
    @Description("Get GCD Cost Results after Costing Sheet Metal")
    public void testGetCostGcdResultsSheetMetal() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        CostResultsGcdResponse response = acsResources.getCostResults(
            costOutputs.getScenarioIterationKey(),
            "PROCESS", CostResultsGcdResponse.class).getResponseEntity();


        SoftAssertions softAssertions = new SoftAssertions();

        CostResultsGcdItem costResultsGcdResponse = response.get(0).getCostResultsGcdItem();
        ResultMapBean resultMapBean = costResultsGcdResponse.getResultMapBean();
        ProcessInstanceKey processInstanceKey = costResultsGcdResponse.getProcessInstanceKey();

        softAssertions.assertThat(processInstanceKey.getProcessGroupName()).isEqualTo("Sheet Metal");
        softAssertions.assertThat(resultMapBean).isNotNull();
        softAssertions.assertThat(response.getCostResultsGcdItem().getCostingFailed()).isEqualTo(false);
        softAssertions.assertThat(response.getCostResultsGcdItem().getDepth()).isEqualTo("GCD");
        softAssertions.assertThat(response.getCostResultsGcdItem().getSecondaryProcess()).isEqualTo(false);
        softAssertions.assertAll();
    }
}
