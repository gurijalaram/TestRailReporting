package com.compare;

import static com.utils.ColumnsEnum.COMPONENT_NAME;
import static com.utils.ColumnsEnum.COST_MATURITY;
import static com.utils.ColumnsEnum.SCENARIO_NAME;
import static com.utils.ColumnsEnum.STATUS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.compare.ModifyComparisonPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.PropertyEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.ComparisonCardEnum;
import com.utils.ComparisonDeltaEnum;
import com.utils.DirectionEnum;
import com.utils.EvaluateDfmIconEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ComparisonTests extends TestBase {

    private final String notFoundMessage = "Oops! Looks like the component or scenario you were looking for could not be found.";
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ComparePage comparePage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ModifyComparisonPage modifyComparisonPage;
    private File resourceFile;
    private File resourceFile2;
    private File resourceFile3;
    private File resourceFile4;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ComponentInfoBuilder cidComponentItemC;
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder cidComponentItem;
    private SoftAssertions softAssertions = new SoftAssertions();

    public ComparisonTests() {
        super();
    }

    @Test
    @Category({SmokeTests.class})
    @TestRail(testCaseId = {"7019"})
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
            .createComparison();

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);
        softAssertions.assertThat(comparePage.getScenariosInComparison()).contains(componentName2.toUpperCase() + "  / " + scenarioName2);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7035"})
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
            .clickExplore();

        softAssertions.assertThat(explorePage.getTableHeaders()).contains(COMPONENT_NAME.getColumns(), SCENARIO_NAME.getColumns());

        comparePage = explorePage.clickCompare();

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5778"})
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
            .openScenario(componentName2, scenarioName2);

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(scenarioName2)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5782"})
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
    @TestRail(testCaseId = {"8680"})
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
    @TestRail(testCaseId = {"5783", "6200"})
    @Description("User can add scenarios to the currently open comparison via UI within current comparison")
    public void addScenarioToComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        String componentName2 = "manifold";
        String componentName3 = "Casting-Die";
        String componentName4 = "partbody_2";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".prt.1");
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

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5784"})
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
    @TestRail(testCaseId = {"5789"})
    @Description("Be able to filter table contents within Add Scenarios dialog box")
    public void filterScenariosAddDialog() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        String componentName2 = "manifold";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".prt.1");
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
            .modify()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, componentName)
            .submit(ModifyComparisonPage.class);

        assertThat(modifyComparisonPage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"7032"})
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
            .createComparison();

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);

        comparePage.dragDropToBasis(componentName2, scenarioName2);

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(componentName2.toUpperCase() + "  / " + scenarioName2);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7033"})
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
            .createComparison();

        softAssertions.assertThat(comparePage.getCardHeader()).containsExactly("Info & Inputs", "Material & Utilization", "Design Guidance", "Process", "Cost Result");

        comparePage.dragDropCard("Material & Utilization", "Info & Inputs");

        softAssertions.assertThat(comparePage.getCardHeader()).containsExactly("Material & Utilization", "Info & Inputs", "Design Guidance", "Process", "Cost Result");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5797"})
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
            .createComparison();

        softAssertions.assertThat(comparePage.getOutput(componentName2, scenarioName2, ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("Low");

        softAssertions.assertAll();
    }

    @Test
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"5799"})
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
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(componentName2, scenarioName2)
            .delete()
            .submit(ExplorePage.class)
            .clickCompare()
            .openScenario(componentName2, scenarioName2);

        softAssertions.assertThat(evaluatePage.getNotFoundMessage()).isEqualTo(notFoundMessage);

        comparePage = evaluatePage.backFromError(ComparePage.class);

        softAssertions.assertThat(comparePage.getScenariosInComparison()).doesNotContain(componentName2.toUpperCase() + "  / " + scenarioName2);

        softAssertions.assertAll();
    }

    @Test
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"5798"})
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
            .clickExplore()
            .selectFilter("Public")
            .highlightScenario(componentName, scenarioName)
            .delete()
            .submit(ExplorePage.class)
            .clickCompare()
            .openScenario(componentName, scenarioName);

        softAssertions.assertThat(evaluatePage.getNotFoundMessage()).isEqualTo(notFoundMessage);

        comparePage = evaluatePage.backFromError(ComparePage.class);

        softAssertions.assertThat(comparePage.getScenariosInComparison()).doesNotContain(componentName.toUpperCase() + "  / " + scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5800"})
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
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(componentName2, scenarioName2)
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemB, ExplorePage.class)
            .clickCompare()
            .openScenario(componentName2, scenarioName2);

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(scenarioName2)).isEqualTo(true);

        comparePage = evaluatePage.clickCompare();

        softAssertions.assertThat(comparePage.isIconDisplayed(componentName2, scenarioName2, StatusIconEnum.PUBLIC)).isEqualTo(true);

        evaluatePage = comparePage.clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(componentName, scenarioName)
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemC, ExplorePage.class)
            .clickCompare()
            .openBasisScenario();

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(scenarioName)).isEqualTo(true);

        comparePage = evaluatePage.clickCompare();

        softAssertions.assertThat(comparePage.isIconDisplayed(componentName, scenarioName, StatusIconEnum.PUBLIC)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7020"})
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
            .createComparison();

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
    @TestRail(testCaseId = {"7021", "5906"})
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
            .createComparison();

        softAssertions.assertThat(comparePage.getDeltaPercentage(componentName2, scenarioName2, ComparisonCardEnum.MATERIAL_FINISH_MASS)).isEqualTo("31.97%");
        softAssertions.assertThat(comparePage.getDeltaPercentage(componentName2, scenarioName2, ComparisonCardEnum.DESIGN_DESIGN_WARNINGS)).isEqualTo("75.00%");
        softAssertions.assertThat(comparePage.getDeltaPercentage(componentName2, scenarioName2, ComparisonCardEnum.PROCESS_TOTAL_CYCLE_TIME)).isEqualTo("139.70%");
        softAssertions.assertThat(comparePage.getDeltaPercentage(componentName2, scenarioName2, ComparisonCardEnum.COST_TOTAL_CAPITAL_INVESTMENT)).isEqualTo("3.56%");

        softAssertions.assertAll();
    }
}
