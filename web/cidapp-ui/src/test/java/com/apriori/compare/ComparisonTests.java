package com.apriori.compare;

import static com.apriori.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;
import static com.utils.ColumnsEnum.COMPONENT_NAME;
import static com.utils.ColumnsEnum.COST_MATURITY;
import static com.utils.ColumnsEnum.SCENARIO_NAME;
import static com.utils.ColumnsEnum.STATUS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.OperationEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.enums.PropertyEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.compare.CompareExplorePage;
import com.apriori.pageobjects.compare.ComparePage;
import com.apriori.pageobjects.compare.CreateComparePage;
import com.apriori.pageobjects.compare.ModifyComparisonPage;
import com.apriori.pageobjects.compare.SaveComparisonPage;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.utils.ColumnsEnum;
import com.utils.ComparisonCardEnum;
import com.utils.ComparisonDeltaEnum;
import com.utils.DirectionEnum;
import com.utils.EvaluateDfmIconEnum;
import com.utils.SortOrderEnum;
import com.utils.StatusIconEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ComparisonTests extends TestBaseUI {

    private final String notFoundMessage = "Oops! Looks like the component or scenario you were looking for could not be found.";
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private CompareExplorePage compareExplorePage;
    private ComparePage comparePage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ModifyComparisonPage modifyComparisonPage;
    private File resourceFile;
    private File resourceFile2;
    private File resourceFile3;
    private File resourceFile4;
    private File resourceFile5;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ComponentInfoBuilder cidComponentItemC;
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder cidComponentItem;
    private SoftAssertions softAssertions = new SoftAssertions();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();

    public ComparisonTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {7019})
    @Description("User can create a comparison by multi selection two or more components on explore page")
    public void createComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        String componentName2 = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);
        softAssertions.assertThat(comparePage.getScenariosInComparison()).contains(componentName2.toUpperCase() + "  / " + scenarioName2);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {7035})
    @Description("Validate user can select explore tab and then comparison tab again")
    public void goToExploreReturnCompare() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        String componentName2 = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .clickExplore();

        softAssertions.assertThat(explorePage.getTableHeaders()).contains(COMPONENT_NAME.getColumns(), SCENARIO_NAME.getColumns());

        comparePage = explorePage.clickCompare(ComparePage.class);

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5778})
    @Description("In comparison view, user can access any scenario included in the comparison (private and public)")
    public void accessAnyScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "700-33770-01_A0";
        String componentName2 = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItemC = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        cidComponentItemB = new ExplorePage(driver).uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .openBasisScenario();

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(scenarioName)).isEqualTo(true);

        evaluatePage.selectProcessGroup(processGroupEnum)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemC, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(componentName2, scenarioName2)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemB, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .openScenario(componentName2, scenarioName2);

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(scenarioName2)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5782})
    @Description("While in an open private comparison, user is able to expand and collapse each section of the comparison (Info & Inputs, Process, etc.)")
    public void expandCollapseSectionsInPrivateComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "testpart-4";
        String componentName2 = "prt0001";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".prt.1");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .collapse("Info & Inputs")
            .collapse("Material & Utilization")
            .collapse("Design Guidance")
            .collapse("Process")
            .collapse("Cost Result");

        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Description")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Finish Mass")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("DFM Risk")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Routing")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Investment")).isEqualTo(false);

        comparePage.expand("Info & Inputs")
            .expand("Material & Utilization")
            .expand("Design Guidance")
            .expand("Process")
            .expand("Cost Result");

        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Description")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Finish Mass")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("DFM Risk")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Routing")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Investment")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {8680})
    @Description("While in an open public comparison, user is able to expand and collapse each section of the comparison (Info & Inputs, Process, etc.)")
    public void expandCollapseSectionsInPublicComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "testpart-4";
        String componentName2 = "prt0001";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".prt.1");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore();

        cidComponentItemB = new ExplorePage(driver).uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);

        comparePage = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemB, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .collapse("Info & Inputs")
            .collapse("Material & Utilization")
            .collapse("Design Guidance")
            .collapse("Process")
            .collapse("Cost Result");

        softAssertions.assertThat(comparePage.isSectionExpanded("Info & Inputs")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Material & Utilization")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Design Guidance")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Process")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Cost Result")).isEqualTo(false);

        comparePage.expand("Info & Inputs")
            .expand("Material & Utilization")
            .expand("Design Guidance")
            .expand("Process")
            .expand("Cost Result");

        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Description")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Finish Mass")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("DFM Risk")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Routing")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Investment")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5783, 6200, 26148})
    @Issue("APD-1663")
    @Description("User can add scenarios to the currently open comparison via UI within current comparison")
    public void addScenarioToComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        String componentName2 = "Y_shape";
        String componentName3 = "Casting-Die";
        String componentName4 = "partbody_2";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".prt");
        resourceFile3 = FileResourceUtil.getCloudFile(processGroupEnum, componentName3 + ".stp");
        resourceFile4 = FileResourceUtil.getCloudFile(processGroupEnum, componentName4 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String scenarioName3 = new GenerateStringUtil().generateScenarioName();
        String scenarioName4 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        comparePage = new ExplorePage(driver).uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .uploadComponentAndOpen(componentName3, scenarioName3, resourceFile3, currentUser)
            .uploadComponentAndOpen(componentName4, scenarioName4, resourceFile4, currentUser)
            .navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(componentName3, scenarioName3)
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getScenariosInComparison()).contains(componentName3.toUpperCase() + "  / " + scenarioName3);

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(componentName4, scenarioName4)
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getScenariosInComparison()).contains(componentName4.toUpperCase() + "  / " + scenarioName4);

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(componentName, scenarioName)
            .clickScenarioCheckbox(componentName2, scenarioName2)
            .clickScenarioCheckbox(componentName3, scenarioName3)
            .clickScenarioCheckbox(componentName4, scenarioName4)
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getListOfBasis()).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {5784})
    @Description("User can add columns to the part table within the Add Scenarios dialog box")
    public void addColumnsConfigure() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting-Die";
        String componentName2 = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        modifyComparisonPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .modify()
            .configure()
            .selectColumn(COST_MATURITY)
            .selectColumn(STATUS)
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ModifyComparisonPage.class);

        assertThat(modifyComparisonPage.getTableHeaders(), hasItems(STATUS.getColumns(), COST_MATURITY.getColumns()));

        modifyComparisonPage.configure()
            .selectColumn(COST_MATURITY)
            .selectColumn(STATUS)
            .moveColumn(DirectionEnum.LEFT)
            .submit(ModifyComparisonPage.class);
    }

    @Test
    @Issue("APD-1663")
    @TestRail(id = {5789})
    @Description("Be able to filter table contents within Add Scenarios dialog box")
    public void filterScenariosAddDialog() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        String componentName2 = "Y_shape";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".prt");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        modifyComparisonPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .modify()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, componentName)
            .submit(ModifyComparisonPage.class);

        assertThat(modifyComparisonPage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(id = {7032})
    @Description("Validate user can drag and drop basis of comparison")
    public void dragAndDropToBasis() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        String componentName2 = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);

        comparePage.dragDropToBasis(componentName2, scenarioName2);

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(componentName2.toUpperCase() + "  / " + scenarioName2);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {7033})
    @Description("Validate user can drag and drop rows of comparison")
    public void dragAndDropCard() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        String componentName2 = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getCardHeader()).containsExactly(
            "Info & Inputs", "Material & Utilization", "Design Guidance", "Process", "Sustainability", "Cost Result");

        comparePage.dragDropCard("Material & Utilization", "Info & Inputs");

        softAssertions.assertThat(comparePage.getCardHeader()).containsExactly(
            "Material & Utilization", "Info & Inputs", "Design Guidance", "Process", "Sustainability", "Cost Result");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5797})
    @Description("All Design Guidance from scenarios respected in comparison when scenario is added")
    public void designGuidanceInComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "testpart-4";
        String componentName2 = "prt0001";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".prt.1");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.LOW.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        comparePage = evaluatePage.clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getOutput(componentName2, scenarioName2, ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("Low");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5799})
    @Description("Delete private scenarios that are included in the comparison")
    public void deletePrivateScenarioOfComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        String componentName2 = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(componentName2, scenarioName2)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .clickCompare(ComparePage.class)
            .openScenario(componentName2, scenarioName2);

        softAssertions.assertThat(evaluatePage.getNotFoundMessage()).isEqualTo(notFoundMessage);

        comparePage = evaluatePage.backFromError(ComparePage.class);

        softAssertions.assertThat(comparePage.getScenariosInComparison()).doesNotContain(componentName2.toUpperCase() + "  / " + scenarioName2);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5798})
    @Description("Delete public scenarios that are included in the comparison")
    public void deletePublicScenarioOfComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "case_005_flat end mill contouring";
        String componentName2 = "Case_001_-_Rockwell_2075-0243G";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        cidComponentItemB = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);

        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemB)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemB, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName2 + ", " + scenarioName2 + "", "" + componentName + ", " + scenarioName + "")
            .createComparison()
            .selectManualComparison()
            .clickExplore()
            .selectFilter("Public")
            .highlightScenario(componentName, scenarioName)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .clickCompare(ComparePage.class)
            .openScenario(componentName, scenarioName);

        softAssertions.assertThat(evaluatePage.getNotFoundMessage()).isEqualTo(notFoundMessage);

        comparePage = evaluatePage.backFromError(ComparePage.class);

        softAssertions.assertThat(comparePage.getScenariosInComparison()).doesNotContain(componentName.toUpperCase() + "  / " + scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5800, 6458, 6459})
    @Description("Publish private scenarios that are included in the comparison")
    public void publishScenarioOfComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        String componentName2 = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItemC = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        cidComponentItemB = new ExplorePage(driver).uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemC)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .navigateToScenario(cidComponentItemB)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(componentName2, scenarioName2)
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemB, ExplorePage.class)
            .clickCompare(ComparePage.class)
            .openScenario(componentName2, scenarioName2);

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(scenarioName2)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        comparePage = evaluatePage.clickCompare(ComparePage.class);

        softAssertions.assertThat(comparePage.isIconDisplayed(componentName2, scenarioName2, StatusIconEnum.PUBLIC)).isEqualTo(true);

        evaluatePage = comparePage.clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(componentName, scenarioName)
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemC, ExplorePage.class)
            .clickCompare(ComparePage.class)
            .openBasisScenario();

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(scenarioName)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        comparePage = evaluatePage.clickCompare(ComparePage.class);

        softAssertions.assertThat(comparePage.isIconDisplayed(componentName, scenarioName, StatusIconEnum.PUBLIC)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7020})
    @Description("Validate arrows are correct colour and direction in comparisons")
    public void validateArrowsInComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        String componentName2 = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.isArrowColour(componentName2, scenarioName2, ComparisonCardEnum.MATERIAL_FINISH_MASS, ComparisonDeltaEnum.GREEN)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(componentName2, scenarioName2, ComparisonCardEnum.MATERIAL_FINISH_MASS, ComparisonDeltaEnum.ARROW_DOWN)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isArrowColour(componentName2, scenarioName2, ComparisonCardEnum.DESIGN_DESIGN_WARNINGS, ComparisonDeltaEnum.GREEN)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(componentName2, scenarioName2, ComparisonCardEnum.DESIGN_DESIGN_WARNINGS, ComparisonDeltaEnum.ARROW_DOWN)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isArrowColour(componentName2, scenarioName2, ComparisonCardEnum.PROCESS_TOTAL_CYCLE_TIME, ComparisonDeltaEnum.RED)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(componentName2, scenarioName2, ComparisonCardEnum.PROCESS_TOTAL_CYCLE_TIME, ComparisonDeltaEnum.ARROW_UP)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isArrowColour(componentName2, scenarioName2, ComparisonCardEnum.COST_TOTAL_CAPITAL_INVESTMENT, ComparisonDeltaEnum.GREEN)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(componentName2, scenarioName2, ComparisonCardEnum.COST_TOTAL_CAPITAL_INVESTMENT, ComparisonDeltaEnum.ARROW_DOWN)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7021, 5906})
    @Description("Validate percentages are correct in comparison")
    public void validatePercentageInComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        String componentName2 = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getDeltaPercentage(componentName2, scenarioName2, ComparisonCardEnum.MATERIAL_FINISH_MASS))
            .as("Material Finish Mass").isEqualTo("31.97%");
        softAssertions.assertThat(comparePage.getDeltaPercentage(componentName2, scenarioName2, ComparisonCardEnum.DESIGN_DESIGN_WARNINGS))
            .as("Design Warnings").isEqualTo("70.59%");
        softAssertions.assertThat(comparePage.getDeltaPercentage(componentName2, scenarioName2, ComparisonCardEnum.PROCESS_TOTAL_CYCLE_TIME))
            .as("Total Cycle Time").isEqualTo("138.55%");
        softAssertions.assertThat(comparePage.getDeltaPercentage(componentName2, scenarioName2, ComparisonCardEnum.COST_TOTAL_CAPITAL_INVESTMENT))
            .as("Total Capital Investment").isEqualTo("3.57%");

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {6534})
    @Description("User can add assemblies to existing comparison containing part scenario")
    public void addAssemblyToExistingComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String componentName2 = "Push Pin";
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final String assemblyScenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            assemblyScenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(assemblyName, assemblyScenarioName)
            .submit(ComparePage.class);

        assertThat(comparePage.getAllScenariosInComparison(), containsInRelativeOrder(assemblyName.toUpperCase() + "  / " + assemblyScenarioName));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {6537, 6535})
    @Description("Assemblies in comparison can be interacted with in a similar way as part scenarios - open, basis, delete")
    public void interactWithAssemblyInComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String componentName2 = "Push Pin";
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final String assemblyScenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            assemblyScenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(assemblyName, assemblyScenarioName)
            .submit(ComparePage.class);

        evaluatePage = comparePage.openScenario(assemblyName, assemblyScenarioName);
        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(assemblyScenarioName)).isEqualTo(true);

        comparePage = evaluatePage.clickCompare(ComparePage.class)
            .dragDropToBasis(assemblyName, assemblyScenarioName);

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(assemblyName.toUpperCase() + "  / " + assemblyScenarioName);

        comparePage.collapse("Info & Inputs")
            .collapse("Assembly Info")
            .collapse("Material & Utilization")
            .collapse("Design Guidance")
            .collapse("Process")
            .collapse("Cost Result");

        softAssertions.assertThat(comparePage.isSectionExpanded("Info & Inputs")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Assembly Info")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Material & Utilization")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Design Guidance")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Process")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Cost Result")).isEqualTo(false);

        comparePage.expand("Info & Inputs")
            .expand("Assembly Info")
            .expand("Material & Utilization")
            .expand("Design Guidance")
            .expand("Process")
            .expand("Cost Result");

        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Description")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Components Cost")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Finish Mass")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("DFM Risk")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Routing")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Investment")).isEqualTo(true);

        comparePage.deleteBasis();
        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {6482, 6483})
    @Description("Validate the user can create a comparison including parts with all dfm risk ratings for all process groups")
    public void comparisonWithAllProcessGroupsAndDFM() {
        final ProcessGroupEnum processGroupEnum1 = ProcessGroupEnum.STOCK_MACHINING;
        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.SHEET_METAL;
        final ProcessGroupEnum processGroupEnum3 = ProcessGroupEnum.PLASTIC_MOLDING;
        final ProcessGroupEnum processGroupEnum4 = ProcessGroupEnum.CASTING_SAND;

        String componentName1 = "DTCCastingIssues";
        String componentName2 = "Part0005b";
        String componentName3 = "titan charger lead";
        String componentName4 = "SandCast";
        String componentName5 = "Part0004";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum1, componentName1 + ".catpart");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".ipt");
        resourceFile3 = FileResourceUtil.getCloudFile(processGroupEnum3, componentName3 + ".SLDPRT");
        resourceFile4 = FileResourceUtil.getCloudFile(processGroupEnum4, componentName4 + ".x_t");
        resourceFile5 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName5 + ".ipt");

        currentUser = UserUtil.getUser();
        String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String scenarioName3 = new GenerateStringUtil().generateScenarioName();
        String scenarioName4 = new GenerateStringUtil().generateScenarioName();
        String scenarioName5 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName1, scenarioName1, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum1)
            .costScenario(4)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .selectProcessGroup(processGroupEnum2)
            .costScenario()
            .uploadComponentAndOpen(componentName3, scenarioName3, resourceFile3, currentUser)
            .selectProcessGroup(processGroupEnum3)
            .costScenario()
            .uploadComponentAndOpen(componentName4, scenarioName4, resourceFile4, currentUser)
            .selectProcessGroup(processGroupEnum4)
            .costScenario()
            .uploadComponentAndOpen(componentName5, scenarioName5, resourceFile5, currentUser)
            .selectProcessGroup(processGroupEnum2)
            .costScenario()
            .clickExplore()
            .selectFilter("Private")
            .multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "", "" + componentName2 + ", " + scenarioName2 + "", "" + componentName3 + ", " + scenarioName3 + "", "" + componentName4 + ", " + scenarioName4 + "", "" + componentName5 + ", " + scenarioName5)
            .createComparison()
            .selectManualComparison()
            .expand("Design Guidance");

        softAssertions.assertThat(comparePage.getOutput(componentName1, scenarioName1, ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("Critical");
        softAssertions.assertThat(comparePage.getOutput(componentName2, scenarioName2, ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("High");
        softAssertions.assertThat(comparePage.getOutput(componentName3, scenarioName3, ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("Low");
        softAssertions.assertThat(comparePage.getOutput(componentName4, scenarioName4, ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("Medium");
        softAssertions.assertThat(comparePage.getOutput(componentName5, scenarioName5, ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("Low");

        softAssertions.assertThat(comparePage.isArrowColour(componentName2, scenarioName2, ComparisonCardEnum.DESIGN_DFM_RISK, ComparisonDeltaEnum.GREEN)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(componentName2, scenarioName2, ComparisonCardEnum.DESIGN_DFM_RISK, ComparisonDeltaEnum.ARROW_DOWN)).isEqualTo(true);

        comparePage.dragDropToBasis(componentName3, scenarioName3);

        softAssertions.assertThat(comparePage.isArrowColour(componentName2, scenarioName2, ComparisonCardEnum.DESIGN_DFM_RISK, ComparisonDeltaEnum.RED)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(componentName2, scenarioName2, ComparisonCardEnum.DESIGN_DFM_RISK, ComparisonDeltaEnum.ARROW_UP)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(componentName5, scenarioName5, ComparisonCardEnum.DESIGN_DFM_RISK, ComparisonDeltaEnum.MINUS)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 24358)
    @Description("Verify that Compare button is enabled in Explore view when nothing selected")
    public void testCompareButtonEnabledWithNoSelection() {
        currentUser = UserUtil.getUser();
        loginPage = new CidAppLoginPage(driver);
        CreateComparePage createComparePage = loginPage.login(currentUser)
            .createComparison();

        softAssertions.assertThat(createComparePage.manualComparisonButtonEnabled()).as("Create Comparison modal launched and manual is enabled")
            .isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {25983, 25984, 25986})
    @Description("Verify that Save button is present and enabled for initial save and can only be clicked when changes made")
    public void testSaveComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        String componentName2 = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String comparisonName = new GenerateStringUtil().generateComparisonName();

        ComponentInfoBuilder bracketBasic = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ComponentInfoBuilder panel = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName2)
            .scenarioName(scenarioName2)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile2)
            .user(currentUser)
            .build());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .multiSelectScenarios(bracketBasic.getComponentName() + "," + bracketBasic.getScenarioName(), panel.getComponentName() + "," + panel.getScenarioName())
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getComparisonName())
            .as("Verify Unsaved Comparison Default Name in Nav Bar").isEqualTo("Untitled Comparison");

        comparePage.saveNew()
            .inputName(comparisonName)
            .save(ComparePage.class);

        softAssertions.assertThat(comparePage.saveButtonEnabled()).as("Verify that Save button is disabled after save").isFalse();

        comparePage.dragDropToBasis(panel.getComponentName(), panel.getScenarioName());
        softAssertions.assertThat(comparePage.saveButtonEnabled()).as("Verify Save button enabled after change").isTrue();

        comparePage = comparePage.saveChanges()
            .waitForSavingSpinner();
        softAssertions.assertThat(comparePage.saveButtonEnabled()).as("Verify that Save button is disabled after changes saved").isFalse();
        softAssertions.assertThat(comparePage.getComparisonName())
            .as("Verify Comparison Name in Nav Bar").isEqualTo(comparisonName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 25985)
    @Description("Verify that a Comparison cannot be saved using a name that already exists")
    public void testSaveComparisonWithExistingName() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        String componentName2 = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String comparisonName = new GenerateStringUtil().generateComparisonName();
        String comparisonName2 = new GenerateStringUtil().generateComparisonName();

        ComponentInfoBuilder bracketBasic = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ComponentInfoBuilder panel = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName2)
            .scenarioName(scenarioName2)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile2)
            .user(currentUser)
            .build());

        loginPage = new CidAppLoginPage(driver);
        SaveComparisonPage saveComparePage = loginPage.login(currentUser)
            .multiSelectScenarios(bracketBasic.getComponentName() + "," + bracketBasic.getScenarioName(), panel.getComponentName() + "," + panel.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .saveNew()
            .inputName(comparisonName)
            .save(ComparePage.class)
            .clickExplore()
            .multiSelectScenarios(panel.getComponentName() + "," + panel.getScenarioName(), bracketBasic.getComponentName() + "," + bracketBasic.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .saveNew()
            .inputName(comparisonName)
            .save(SaveComparisonPage.class);

        softAssertions.assertThat(saveComparePage.getToastifyError()).as("Verify error message displayed")
            .isEqualTo("HTTP 409: A comparison with the name '" + comparisonName + "' already exists for user '" + currentUser.getUsername() + "'");

        comparePage = saveComparePage.inputName(comparisonName2)
            .save(ComparePage.class);

        softAssertions.assertThat(comparePage.saveButtonEnabled()).as("Verify that Save button is disabled after save with new name").isFalse();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {26149, 26176})
    @Description("Validate scenarios can be deleted from a new manual comparison via modify comparison")
    public void testDeleteNewManualComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        String componentName2 = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName2)
            .scenarioName(scenarioName)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile2)
            .user(currentUser)
            .build());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .createComparison()
            .selectManualComparison();

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(componentName, scenarioName)
            .clickScenarioCheckbox(componentName2, scenarioName)
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getListOfBasis()).isEqualTo(1);

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(componentName, scenarioName)
            .clickScenarioCheckbox(componentName2, scenarioName)
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getListOfBasis()).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {26173, 26174})
    @Description("Verify that deleted Private scenarios are removed from saved comparison")
    public void testDeletePrivateScenarioInComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "Part0004";
        String componentName2 = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".ipt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String comparisonName = new GenerateStringUtil().generateComparisonName();

        componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ComponentInfoBuilder panel = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName2)
            .scenarioName(scenarioName2)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile2)
            .user(currentUser)
            .build());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .selectFilter("Recent")
            .multiSelectScenarios("" + componentName2 + ", " + scenarioName2 + "")
            .publishScenario(PublishPage.class)
            .publish(ExplorePage.class)
            .selectFilter("Private")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .saveNew()
            .inputName(comparisonName)
            .save(ComparePage.class)
            .openScenario(componentName2, scenarioName2)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(panel)
            .clickCompare(ComparePage.class);

        softAssertions.assertThat(comparePage.getListOfComparisons()).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {26171, 26172})
    @Description("Verify that deleted Public scenarios are removed from saved comparison")
    public void testDeletePublicScenarioInComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "Part0004";
        String componentName2 = "700-33770-01_A0";
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String comparisonName = new GenerateStringUtil().generateComparisonName();

        scenariosUtil.postAndPublishComponent(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .extension(".ipt")
            .scenarioName(scenarioName)
            .processGroup(processGroupEnum)
            .user(currentUser)
            .build());

        ComponentInfoBuilder panel = scenariosUtil.postAndPublishComponent(ComponentInfoBuilder.builder()
            .componentName(componentName2)
            .extension(".stp")
            .scenarioName(scenarioName2)
            .processGroup(processGroupEnum)
            .user(currentUser)
            .build());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .selectFilter("Recent")
            .multiSelectScenarios("" + componentName2 + ", " + scenarioName2 + "")
            .editScenario(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .saveNew()
            .inputName(comparisonName)
            .save(ComparePage.class)
            .openScenario(componentName2, scenarioName2)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(panel)
            .clickCompare(ComparePage.class);

        softAssertions.assertThat(comparePage.getListOfComparisons()).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {26175})
    @Description("Verify that scenario in position 2 will replace basis, if it is deleted")
    public void testDeleteReplacesBasis() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "Part0004";
        String componentName2 = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".ipt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String comparisonName = new GenerateStringUtil().generateComparisonName();

        ComponentInfoBuilder part = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName2)
            .scenarioName(scenarioName2)
            .processGroup(processGroupEnum)
            .resourceFile(resourceFile2)
            .user(currentUser)
            .build());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .saveNew()
            .inputName(comparisonName)
            .save(ComparePage.class);

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);

        comparePage.openScenario(componentName, scenarioName)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(part)
            .clickCompare(ComparePage.class);

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(componentName2.toUpperCase() + "  / " + scenarioName2);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {25983, 25984, 26956, 26957, 26958, 27005, 27995})
    @Description("Verify Comparison Explorer can be launched and saved comparisons can be opened from it")
    public void testComparisonExplorer() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "Part0004";
        String componentName2 = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".ipt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String comparisonName = new GenerateStringUtil().generateComparisonName();

        ComponentInfoBuilder part1 = componentsUtil.postComponentCID(componentName, scenarioName, processGroupEnum, resourceFile, currentUser);
        ComponentInfoBuilder part2 = componentsUtil.postComponentCID(componentName2, scenarioName2, processGroupEnum, resourceFile2, currentUser);

        loginPage = new CidAppLoginPage(driver);
        compareExplorePage = loginPage.login(currentUser)
            .clickCompare(CompareExplorePage.class);

        softAssertions.assertThat(compareExplorePage.isComparisonNameFilterDisplayed()).as("Verify Comparison Explorer was Loaded")
            .isTrue();

        comparePage = compareExplorePage.clickExplore()
            .multiSelectScenarios(
                part1.getComponentName() + "," + part1.getScenarioName(),
                part2.getComponentName() + "," + part2.getScenarioName())
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify comparison loaded as expected")
            .isEqualTo(part1.getComponentName().toUpperCase() + "  / " + part1.getScenarioName());
        softAssertions.assertThat(comparePage.getComparisonName()).as("Verify comparison is unsaved").isEqualTo("Untitled Comparison");
        softAssertions.assertThat(comparePage.isRefreshEnabled()).as("Verify that Refresh button is disabled while comparison unsaved").isFalse();

        comparePage = comparePage.clickExplore()
            .clickCompare(ComparePage.class);

        softAssertions.assertThat(comparePage.getBasis()).as("Verify comparison retained as expected")
            .isEqualTo(part1.getComponentName().toUpperCase() + "  / " + part1.getScenarioName());
        softAssertions.assertThat(comparePage.getComparisonName()).as("Verify comparison remains unsaved").isEqualTo("Untitled Comparison");

        compareExplorePage = comparePage
            .clickAllComparisons();

        softAssertions.assertThat(compareExplorePage.isComparisonNameFilterDisplayed()).as("Verify Comparison Explorer was Loaded from Comparison")
            .isTrue();

        comparePage = compareExplorePage.clickExplore()
            .multiSelectScenarios(
                part1.getComponentName() + "," + part1.getScenarioName(),
                part2.getComponentName() + "," + part2.getScenarioName())
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.isRefreshEnabled()).as("Verify that Refresh button is disabled while comparison unsaved").isFalse();

        comparePage = comparePage.saveNew()
            .inputName(comparisonName)
            .save(ComparePage.class);

        softAssertions.assertThat(comparePage.isRefreshEnabled()).as("Verify that Refresh button is enabled now comparison is saved").isTrue();

        compareExplorePage = comparePage.clickAllComparisons()
            .clickRefresh(CompareExplorePage.class);

        softAssertions.assertThat(comparisonName).as("Verify comparison visible in table").isIn(compareExplorePage.getListOfComparisons());

        comparePage = compareExplorePage.openComparison(comparisonName);

        softAssertions.assertThat(comparePage.getComparisonName()).as("Verify that correct Comparison Name displayed").isEqualTo(comparisonName);
        softAssertions.assertThat(comparePage.getBasis()).as("Verify correct Basis in Comparison")
            .isEqualTo(part1.getComponentName().toUpperCase() + "  / " + part1.getScenarioName());
        softAssertions.assertThat(part2.getComponentName() + "  / " + part2.getScenarioName()).as("Verify correct Compared Scenario in Comparison")
            .isIn(comparePage.getScenariosInComparison());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {27967, 27968, 27969})
    @Description("Rename Comparison from Explorer and Comparison Views")
    public void testRenameComparison() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "Casting";
        String componentName2 = "SandCastBox";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".SLDPRT");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String comparisonName1 = new GenerateStringUtil().generateComparisonName();
        String comparisonName2 = new GenerateStringUtil().generateComparisonName();
        String comparisonViewRename = new GenerateStringUtil().generateComparisonName();
        String comparisonExplorerRename = new GenerateStringUtil().generateComparisonName();
        String invalidComparisonName = "Special+Characters~100%";
        String invalidCharacterErrorText = "Must only contain characters, numbers, spaces and the following special characters: . - _ ( )";

        ComponentInfoBuilder part1 = componentsUtil.postComponentCID(componentName, scenarioName, processGroupEnum, resourceFile, currentUser);
        ComponentInfoBuilder part2 = componentsUtil.postComponentCID(componentName2, scenarioName2, processGroupEnum, resourceFile2, currentUser);

        explorePage = new CidAppLoginPage(driver)
            .login(currentUser);

        compareExplorePage = explorePage.multiSelectScenarios(
                part1.getComponentName() + "," + part1.getScenarioName(),
                part2.getComponentName() + "," + part2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .saveNew()
            .inputName(comparisonName1)
            .save(ComparePage.class)
            .clickAllComparisons()
            .clickExplore()
            .multiSelectScenarios(
                part2.getComponentName() + "," + part2.getScenarioName(),
                part1.getComponentName() + "," + part1.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .saveNew()
            .inputName(comparisonName2)
            .save(ComparePage.class)
            .clickAllComparisons();

        softAssertions.assertThat(compareExplorePage.isRenameEnabled()).as("Verify that Rename button is disabled when nothing selected").isFalse();

        compareExplorePage.selectComparison(comparisonName1);

        softAssertions.assertThat(compareExplorePage.isRenameEnabled()).as("Verify that Rename button is enabled when comparison selected").isTrue();

        SaveComparisonPage renamePage = compareExplorePage.rename()
            .inputName(invalidComparisonName);

        softAssertions.assertThat(renamePage.isInvalidCharacterErrorDisplayed()).as("Verify that invalid character error is displayed").isTrue();
        softAssertions.assertThat(renamePage.getInvalidCharacterErrorText()).as("Verify invlaid character error message").isEqualTo(invalidCharacterErrorText);

        renamePage.inputName(comparisonName1);
        softAssertions.assertThat(renamePage.isSaveEnabled()).as("Verify that Submit button disabled while existing name used").isFalse();

        compareExplorePage = renamePage.inputName(comparisonExplorerRename)
            .save(CompareExplorePage.class);

        softAssertions.assertThat(comparisonName1).as("Verify that original comparison name no longer in Explore list")
            .isNotIn(compareExplorePage.getListOfComparisons());
        softAssertions.assertThat(comparisonExplorerRename).as("Verify that new Comparison Name displayed in explore table")
            .isIn(compareExplorePage.getListOfComparisons());

        comparePage = compareExplorePage.openComparison(comparisonName2)
            .rename()
            .inputName(comparisonViewRename)
            .save(ComparePage.class);

        softAssertions.assertThat(comparePage.getComparisonName()).as("Verify Comparison name updated").isEqualTo(comparisonViewRename);

        compareExplorePage = comparePage.clickAllComparisons()
            .clickRefresh(CompareExplorePage.class);

        softAssertions.assertThat(comparisonViewRename).as("Verify that new Comparison Name displayed in explore table")
            .isIn(compareExplorePage.getListOfComparisons());

        softAssertions.assertAll();

    }

    @Test
    @TestRail(id = {26150, 26151, 27965})
    @Description("Verify Deleting Comparisons from Explore and Viewer pages")
    public void testDeleteComparisons() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "Casting";
        String componentName2 = "SandCastBox";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".SLDPRT");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String comparisonName1 = new GenerateStringUtil().generateComparisonName();
        String comparisonName2 = new GenerateStringUtil().generateComparisonName();

        ComponentInfoBuilder part1 = componentsUtil.postComponentCID(componentName, scenarioName, processGroupEnum, resourceFile, currentUser);
        ComponentInfoBuilder part2 = componentsUtil.postComponentCID(componentName2, scenarioName2, processGroupEnum, resourceFile2, currentUser);

        explorePage = new CidAppLoginPage(driver)
            .login(currentUser);

        comparePage = explorePage.multiSelectScenarios(
                part1.getComponentName() + "," + part1.getScenarioName(),
                part2.getComponentName() + "," + part2.getScenarioName())
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.isDeleteEnabled()).as("Verify Delete button disabled when comparison has not been saved").isFalse();

        comparePage = comparePage.saveNew()
            .inputName(comparisonName1)
            .save(ComparePage.class);

        softAssertions.assertThat(comparePage.isDeleteEnabled()).as("Verify Delete button enabled after comparison has been saved").isTrue();

        compareExplorePage = comparePage.clickAllComparisons()
            .clickExplore()
            .multiSelectScenarios(
                part2.getComponentName() + "," + part2.getScenarioName(),
                part1.getComponentName() + "," + part1.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .saveNew()
            .inputName(comparisonName2)
            .save(ComparePage.class)
            .clickAllComparisons()
            .clickRefresh(CompareExplorePage.class);

        softAssertions.assertThat(compareExplorePage.isDeleteEnabled()).as("Verify that delete button is disabled when no comparison selected")
            .isFalse();

        compareExplorePage.selectComparison(comparisonName1);
        softAssertions.assertThat(compareExplorePage.isDeleteEnabled()).as("Verify that delete button is enabled when single comparison selected")
            .isTrue();

        compareExplorePage = compareExplorePage.delete()
            .clickDelete(CompareExplorePage.class)
            .clickRefresh(CompareExplorePage.class);

        softAssertions.assertThat(comparisonName1).as("Verify that deleted comparison no longer shown in explorer table")
            .isNotIn(compareExplorePage.getListOfComparisons());

        softAssertions.assertThat(comparisonName2).as("Verify that second saved comparison still displayed in explorer table")
            .isIn(compareExplorePage.getListOfComparisons());

        compareExplorePage = compareExplorePage.openComparison(comparisonName2)
            .delete()
            .clickDelete(CompareExplorePage.class)
            .clickRefresh(CompareExplorePage.class);

        softAssertions.assertThat(comparisonName2).as("Verify that second saved comparison no longer displayed in explorer table")
            .isNotIn(compareExplorePage.getListOfComparisons());

        softAssertions.assertAll();
    }

}