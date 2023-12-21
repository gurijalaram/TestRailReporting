package com.apriori.cid.ui.tests.compare;

import static com.apriori.cid.ui.utils.ColumnsEnum.COMPONENT_NAME;
import static com.apriori.cid.ui.utils.ColumnsEnum.COST_MATURITY;
import static com.apriori.cid.ui.utils.ColumnsEnum.SCENARIO_NAME;
import static com.apriori.cid.ui.utils.ColumnsEnum.STATUS;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.ui.pageobjects.compare.CompareExplorePage;
import com.apriori.cid.ui.pageobjects.compare.ComparePage;
import com.apriori.cid.ui.pageobjects.compare.CreateComparePage;
import com.apriori.cid.ui.pageobjects.compare.ModifyComparisonPage;
import com.apriori.cid.ui.pageobjects.compare.SaveComparisonPage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.ComparisonCardEnum;
import com.apriori.cid.ui.utils.ComparisonDeltaEnum;
import com.apriori.cid.ui.utils.DirectionEnum;
import com.apriori.cid.ui.utils.EvaluateDfmIconEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ComparisonTests extends TestBaseUI {

    private final String notFoundMessage = "Oops! Looks like the component or scenario you were looking for could not be found.";
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private CompareExplorePage compareExplorePage;
    private ComparePage comparePage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ModifyComparisonPage modifyComparisonPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private SoftAssertions softAssertions = new SoftAssertions();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ComponentInfoBuilder component;
    private ComponentInfoBuilder component2;

    public ComparisonTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {7019})
    @Description("User can create a comparison by multi selection two or more components on explore page")
    public void createComparison() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());
        softAssertions.assertThat(comparePage.getScenariosInComparison()).contains(component2.getComponentName().toUpperCase() + "  / " + component2.getScenarioName());

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {7035})
    @Description("Validate user can select explore tab and then comparison tab again")
    public void goToExploreReturnCompare() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .clickExplore();

        softAssertions.assertThat(explorePage.getTableHeaders()).contains(COMPONENT_NAME.getColumns(), SCENARIO_NAME.getColumns());

        comparePage = explorePage.clickCompare(ComparePage.class);

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5778})
    @Description("In comparison view, user can access any scenario included in the comparison (private and public)")
    public void accessAnyScenario() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .openBasisScenario();

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(component.getScenarioName())).isEqualTo(true);

        evaluatePage.selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component2, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(component2.getComponentName(), component2.getScenarioName())
            .selectProcessGroup(component2.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component2, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .openScenario(component2.getComponentName(), component2.getScenarioName());

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(component2.getScenarioName())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5782})
    @Description("While in an open private comparison, user is able to expand and collapse each section of the comparison (Info & Inputs, Process, etc.)")
    public void expandCollapseSectionsInPrivateComparison() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .collapse("Info & Inputs")
            .collapse("Material & Utilization")
            .collapse("Design Guidance")
            .collapse("Process")
            .collapse("Cost Results");

        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Description")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Finish Mass")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("DFM Risk")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Routing")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Investment")).isEqualTo(false);

        comparePage.expand("Info & Inputs")
            .expand("Material & Utilization")
            .expand("Design Guidance")
            .expand("Process")
            .expand("Cost Results");

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

        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .uploadComponentAndOpen(component2)
            .selectProcessGroup(component2.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component2, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .collapse("Info & Inputs")
            .collapse("Material & Utilization")
            .collapse("Design Guidance")
            .collapse("Process")
            .collapse("Cost Results");

        softAssertions.assertThat(comparePage.isSectionExpanded("Info & Inputs")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Material & Utilization")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Design Guidance")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Process")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Cost Results")).isEqualTo(false);

        comparePage.expand("Info & Inputs")
            .expand("Material & Utilization")
            .expand("Design Guidance")
            .expand("Process")
            .expand("Cost Results");

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
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());
        ComponentInfoBuilder component3 = new ComponentRequestUtil().getComponent();
        component3.setUser(component.getUser());
        ComponentInfoBuilder component4 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .uploadComponentAndOpen(component3)
            .uploadComponentAndOpen(component4)
            .navigateToScenario(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(component3.getComponentName(), component3.getScenarioName())
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getScenariosInComparison()).contains(component3.getComponentName().toUpperCase() + "  / " + component3.getScenarioName());

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(component4.getComponentName(), component4.getScenarioName())
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getScenariosInComparison()).contains(component4.getComponentName().toUpperCase() + "  / " + component4.getScenarioName());

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(component.getComponentName(), component.getScenarioName())
            .clickScenarioCheckbox(component2.getComponentName(), component2.getScenarioName())
            .clickScenarioCheckbox(component3.getComponentName(), component3.getScenarioName())
            .clickScenarioCheckbox(component4.getComponentName(), component4.getScenarioName())
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getListOfBasis()).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {5784})
    @Description("User can add columns to the part table within the Add Scenarios dialog box")
    public void addColumnsConfigure() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        modifyComparisonPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
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
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        modifyComparisonPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .modify()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, component.getComponentName())
            .submit(ModifyComparisonPage.class);

        assertThat(modifyComparisonPage.getListOfScenarios(component.getComponentName(), component.getScenarioName()), is(equalTo(1)));
    }

    @Test
    @TestRail(id = {7032})
    @Description("Validate user can drag and drop basis of comparison")
    public void dragAndDropToBasis() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());

        comparePage.dragDropToBasis(component2.getComponentName(), component2.getScenarioName());

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(component2.getComponentName().toUpperCase() + "  / " + component2.getScenarioName());

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {7033})
    @Description("Validate user can drag and drop rows of comparison")
    public void dragAndDropCard() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getCardHeader()).containsExactly(
            "Info & Inputs", "Material & Utilization", "Design Guidance", "Process", "Sustainability", "Cost Results");

        comparePage.dragDropCard("Material & Utilization", "Info & Inputs");

        softAssertions.assertThat(comparePage.getCardHeader()).containsExactly(
            "Material & Utilization", "Info & Inputs", "Design Guidance", "Process", "Sustainability", "Cost Results");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5797})
    @Description("All Design Guidance from scenarios respected in comparison when scenario is added")
    public void designGuidanceInComparison() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.LOW.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        comparePage = evaluatePage.clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getOutput(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("Low");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5799})
    @Description("Delete private scenarios that are included in the comparison")
    public void deletePrivateScenarioOfComparison() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(component2.getComponentName(), component2.getScenarioName())
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .clickCompare(ComparePage.class)
            .openScenario(component2.getComponentName(), component2.getScenarioName());

        softAssertions.assertThat(evaluatePage.getNotFoundMessage()).isEqualTo(notFoundMessage);

        comparePage = evaluatePage.backFromError(ComparePage.class);

        softAssertions.assertThat(comparePage.getScenariosInComparison()).doesNotContain(component2.getComponentName().toUpperCase() + "  / " + component2.getScenarioName());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5798})
    @Description("Delete public scenarios that are included in the comparison")
    public void deletePublicScenarioOfComparison() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .uploadComponentAndOpen(component2)
            .selectProcessGroup(component2.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component2, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .clickExplore()
            .selectFilter("Public")
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .clickCompare(ComparePage.class)
            .openScenario(component.getComponentName(), component.getScenarioName());

        softAssertions.assertThat(evaluatePage.getNotFoundMessage()).isEqualTo(notFoundMessage);

        comparePage = evaluatePage.backFromError(ComparePage.class);

        softAssertions.assertThat(comparePage.getScenariosInComparison()).doesNotContain(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5800, 6458, 6459})
    @Description("Publish private scenarios that are included in the comparison")
    public void publishScenarioOfComparison() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .navigateToScenario(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .navigateToScenario(component2)
            .selectProcessGroup(component2.getProcessGroup())
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(component2.getComponentName(), component2.getScenarioName())
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemB, ExplorePage.class)
            .clickCompare(ComparePage.class)
            .openScenario(component2.getComponentName(), component2.getScenarioName());

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(component2.getScenarioName())).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        comparePage = evaluatePage.clickCompare(ComparePage.class);

        softAssertions.assertThat(comparePage.isIconDisplayed(component2.getComponentName(), component2.getScenarioName(), StatusIconEnum.PUBLIC)).isEqualTo(true);

        evaluatePage = comparePage.clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemC, ExplorePage.class)
            .clickCompare(ComparePage.class)
            .openBasisScenario();

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(component.getScenarioName())).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        comparePage = evaluatePage.clickCompare(ComparePage.class);

        softAssertions.assertThat(comparePage.isIconDisplayed(component.getComponentName(), component.getScenarioName(), StatusIconEnum.PUBLIC)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7020})
    @Description("Validate arrows are correct colour and direction in comparisons")
    public void validateArrowsInComparison() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .uploadComponentAndOpen(component2)
            .selectProcessGroup(component2.getProcessGroup())
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.isArrowColour(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.MATERIAL_FINISH_MASS, ComparisonDeltaEnum.GREEN))
            .as("Verify Finish Mass Arrow is Green").isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.MATERIAL_FINISH_MASS, ComparisonDeltaEnum.ARROW_DOWN))
            .as("Verify Finish Mass Delta Icon is a Down Arrow").isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.DESIGN_DESIGN_WARNINGS, ComparisonDeltaEnum.MINUS))
            .as("Verify Design Warnings Delta Icon is a Down Arrow").isEqualTo(true);
        softAssertions.assertThat(comparePage.isArrowColour(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.PROCESS_TOTAL_CYCLE_TIME, ComparisonDeltaEnum.RED))
            .as("Verify Total Cycle Time shows a Red Arrow").isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.PROCESS_TOTAL_CYCLE_TIME, ComparisonDeltaEnum.ARROW_UP))
            .as("Verify Total Cycle Time Delta Icon is an Up Arrow").isEqualTo(true);
        softAssertions.assertThat(comparePage.isArrowColour(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.COST_TOTAL_CAPITAL_INVESTMENT, ComparisonDeltaEnum.GREEN))
            .as("Verify Total Capital Investment has a Green Arrow").isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.COST_TOTAL_CAPITAL_INVESTMENT, ComparisonDeltaEnum.ARROW_DOWN))
            .as("Verify Total Capital Investment Delta Icon is a Down Arrow").isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7021, 5906})
    @Description("Validate percentages are correct in comparison")
    public void validatePercentageInComparison() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .uploadComponentAndOpen(component2)
            .selectProcessGroup(component2.getProcessGroup())
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getDeltaPercentage(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.MATERIAL_FINISH_MASS))
            .as("Material Finish Mass").isEqualTo("31.97%");
        softAssertions.assertThat(comparePage.getDeltaPercentage(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.DESIGN_DESIGN_WARNINGS))
            .as("Design Warnings").isEqualTo("");
        softAssertions.assertThat(comparePage.getDeltaPercentage(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.PROCESS_TOTAL_CYCLE_TIME))
            .as("Total Cycle Time").isEqualTo("138.55%");
        softAssertions.assertThat(comparePage.getDeltaPercentage(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.COST_TOTAL_CAPITAL_INVESTMENT))
            .as("Total Capital Investment").isEqualTo("3.48%");

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {6534})
    @Description("User can add assemblies to existing comparison containing part scenario")
    public void addAssemblyToExistingComparison() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        ComponentInfoBuilder componentAssembly = new AssemblyRequestUtil().getAssembly();
        componentAssembly.setUser(component.getUser());

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .uploadComponentAndOpen(component2)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(componentAssembly.getComponentName(), componentAssembly.getScenarioName())
            .submit(ComparePage.class);

        assertThat(comparePage.getAllScenariosInComparison(), containsInRelativeOrder(componentAssembly.getComponentName().toUpperCase() + "  / " + componentAssembly.getScenarioName()));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {6537, 6535})
    @Description("Assemblies in comparison can be interacted with in a similar way as part scenarios - open, basis, delete")
    public void interactWithAssemblyInComparison() {
        component = new ComponentRequestUtil().getComponent();
        component2 = new ComponentRequestUtil().getComponent();
        component2.setUser(component.getUser());

        ComponentInfoBuilder componentAssembly = new AssemblyRequestUtil().getAssembly();
        componentAssembly.setUser(component.getUser());

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .uploadComponentAndOpen(component2)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName())
            .createComparison()
            .selectManualComparison();

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(componentAssembly.getComponentName(), componentAssembly.getScenarioName())
            .submit(ComparePage.class);

        evaluatePage = comparePage.openScenario(componentAssembly.getComponentName(), componentAssembly.getScenarioName());
        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(componentAssembly.getScenarioName())).isEqualTo(true);

        comparePage = evaluatePage.clickCompare(ComparePage.class)
            .dragDropToBasis(componentAssembly.getComponentName(), componentAssembly.getScenarioName());

        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(componentAssembly.getComponentName().toUpperCase() + "  / " + componentAssembly.getScenarioName());

        comparePage.collapse("Info & Inputs")
            .collapse("Assembly Info")
            .collapse("Material & Utilization")
            .collapse("Design Guidance")
            .collapse("Process")
            .collapse("Cost Results");

        softAssertions.assertThat(comparePage.isSectionExpanded("Info & Inputs")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Assembly Info")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Material & Utilization")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Design Guidance")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Process")).isEqualTo(false);
        softAssertions.assertThat(comparePage.isSectionExpanded("Cost Results")).isEqualTo(false);

        comparePage.expand("Info & Inputs")
            .expand("Assembly Info")
            .expand("Material & Utilization")
            .expand("Design Guidance")
            .expand("Process")
            .expand("Cost Results");

        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Description")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Components Cost")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Finish Mass")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("DFM Risk")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Routing")).isEqualTo(true);
        softAssertions.assertThat(comparePage.isComparisonInfoDisplayed("Investment")).isEqualTo(true);

        comparePage.deleteBasis();
        softAssertions.assertThat(comparePage.getBasis()).isEqualTo(component.getComponentName().toUpperCase() + "  / " + component.getScenarioName());

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {6482, 6483})
    @Description("Validate the user can create a comparison including parts with all dfm risk ratings for all process groups")
    public void comparisonWithAllProcessGroupsAndDFM() {
        component = new ComponentRequestUtil().getComponentByExtension("catpart");
        component2 = new ComponentRequestUtil().getComponentByExtension("ipt");
        component2.setUser(component.getUser());
        ComponentInfoBuilder component3 = new ComponentRequestUtil().getComponentByExtension("SLDPRT");
        component3.setUser(component.getUser());
        ComponentInfoBuilder component4 = new ComponentRequestUtil().getComponentByExtension("x_t");
        component4.setUser(component.getUser());
        ComponentInfoBuilder component5 = new ComponentRequestUtil().getComponentByExtension("ipt");
        component5.setUser(component.getUser());


        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario(4)
            .uploadComponentAndOpen(component2)
            .selectProcessGroup(component2.getProcessGroup())
            .costScenario()
            .uploadComponentAndOpen(component3)
            .selectProcessGroup(component3.getProcessGroup())
            .costScenario()
            .uploadComponentAndOpen(component4)
            .selectProcessGroup(component4.getProcessGroup())
            .costScenario()
            .uploadComponentAndOpen(component5)
            .selectProcessGroup(component5.getProcessGroup())
            .costScenario()
            .clickExplore()
            .selectFilter("Private")
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), component2.getComponentName() + ", " + component2.getScenarioName(),
                component3.getComponentName() + ", " + component3.getScenarioName(), component4.getComponentName() + ", " + component4.getScenarioName(),
                component5.getComponentName() + ", " + component5.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .expand("Design Guidance");

        softAssertions.assertThat(comparePage.getOutput(component.getComponentName(), component.getScenarioName(), ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("Critical");
        softAssertions.assertThat(comparePage.getOutput(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("High");
        softAssertions.assertThat(comparePage.getOutput(component3.getComponentName(), component3.getScenarioName(), ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("Low");
        softAssertions.assertThat(comparePage.getOutput(component4.getComponentName(), component4.getScenarioName(), ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("Medium");
        softAssertions.assertThat(comparePage.getOutput(component5.getComponentName(), component5.getScenarioName(), ComparisonCardEnum.DESIGN_DFM_RISK)).isEqualTo("Low");

        softAssertions.assertThat(comparePage.isArrowColour(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.DESIGN_DFM_RISK, ComparisonDeltaEnum.GREEN)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.DESIGN_DFM_RISK, ComparisonDeltaEnum.ARROW_DOWN)).isEqualTo(true);

        comparePage.dragDropToBasis(component3.getComponentName(), component3.getScenarioName());

        softAssertions.assertThat(comparePage.isArrowColour(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.DESIGN_DFM_RISK, ComparisonDeltaEnum.RED)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(component2.getComponentName(), component2.getScenarioName(), ComparisonCardEnum.DESIGN_DFM_RISK, ComparisonDeltaEnum.ARROW_UP)).isEqualTo(true);
        softAssertions.assertThat(comparePage.isDeltaIcon(component5.getComponentName(), component5.getScenarioName(), ComparisonCardEnum.DESIGN_DFM_RISK, ComparisonDeltaEnum.MINUS)).isEqualTo(true);

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
        String comparisonName = new GenerateStringUtil().generateComparisonName();

        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();
        componentB.setUser(componentA.getUser());

        ComponentInfoBuilder bracketBasic = componentsUtil.postComponent(componentA);

        ComponentInfoBuilder panel = componentsUtil.postComponent(componentB);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(component.getUser())
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
        String comparisonName = new GenerateStringUtil().generateComparisonName();
        String comparisonName2 = new GenerateStringUtil().generateComparisonName();

        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();
        componentB.setUser(componentA.getUser());

        ComponentInfoBuilder bracketBasic = componentsUtil.postComponent(componentA);

        ComponentInfoBuilder panel = componentsUtil.postComponent(componentB);

        loginPage = new CidAppLoginPage(driver);
        SaveComparisonPage saveComparePage = loginPage.login(componentA.getUser())
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
        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();
        componentB.setUser(componentA.getUser());

        componentsUtil.postComponent(componentA);

        componentsUtil.postComponent(componentB);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(componentA.getUser())
            .createComparison()
            .selectManualComparison();

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(componentA.getComponentName(), componentA.getScenarioName())
            .clickScenarioCheckbox(componentB.getComponentName(), componentB.getScenarioName())
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getListOfBasis()).isEqualTo(1);

        comparePage.modify()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(componentA.getComponentName(), componentA.getScenarioName())
            .clickScenarioCheckbox(componentB.getComponentName(), componentB.getScenarioName())
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getListOfBasis()).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {26173, 26174})
    @Description("Verify that deleted Private scenarios are removed from saved comparison")
    public void testDeletePrivateScenarioInComparison() {
        String comparisonName = new GenerateStringUtil().generateComparisonName();

        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();
        componentB.setUser(componentA.getUser());

        componentsUtil.postComponent(componentA);

        ComponentInfoBuilder panel = componentsUtil.postComponent(componentB);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(componentA.getUser())
            .selectFilter("Recent")
            .multiSelectScenarios(componentB.getComponentName() + ", " + componentB.getScenarioName())
            .publishScenario(PublishPage.class)
            .publish(ExplorePage.class)
            .selectFilter("Private")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .saveNew()
            .inputName(comparisonName)
            .save(ComparePage.class)
            .openScenario(componentB.getComponentName(), componentB.getScenarioName())
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
        String comparisonName = new GenerateStringUtil().generateComparisonName();

        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();
        componentB.setUser(componentA.getUser());

        ComponentInfoBuilder part = componentsUtil.postComponent(componentA);

        componentsUtil.postComponent(componentB);

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .saveNew()
            .inputName(comparisonName)
            .save(ComparePage.class);

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(componentA.getComponentName().toUpperCase() + "  / " + componentA.getScenarioName());

        comparePage.openScenario(componentA.getComponentName(), componentA.getScenarioName())
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .checkComponentDelete(part)
            .clickCompare(ComparePage.class);

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(componentB.getComponentName().toUpperCase() + "  / " + componentB.getScenarioName());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {25983, 25984, 26956, 26957, 26958, 27005, 27995})
    @Description("Verify Comparison Explorer can be launched and saved comparisons can be opened from it")
    public void testComparisonExplorer() {
        String comparisonName = new GenerateStringUtil().generateComparisonName();

        ComponentInfoBuilder part1 = componentsUtil.postComponent(new ComponentRequestUtil().getComponent());
        ComponentInfoBuilder part2 = componentsUtil.postComponent(new ComponentRequestUtil().getComponent());
        part2.setUser(part1.getUser());

        loginPage = new CidAppLoginPage(driver);
        compareExplorePage = loginPage.login(part1.getUser())
            .clickCompare(CompareExplorePage.class);

        softAssertions.assertThat(compareExplorePage.isComparisonNameFilterDisplayed()).as("Verify Comparison Explorer was Loaded")
            .isTrue();

        comparePage = compareExplorePage.clickExplore()
            .multiSelectScenarios(part1.getComponentName() + "," + part1.getScenarioName(), part2.getComponentName() + "," + part2.getScenarioName())
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
            .multiSelectScenarios(part1.getComponentName() + "," + part1.getScenarioName(), part2.getComponentName() + "," + part2.getScenarioName())
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
        String comparisonName1 = new GenerateStringUtil().generateComparisonName();
        String comparisonName2 = new GenerateStringUtil().generateComparisonName();
        String comparisonViewRename = new GenerateStringUtil().generateComparisonName();
        String comparisonExplorerRename = new GenerateStringUtil().generateComparisonName();
        String invalidComparisonName = "Special+Characters~100%";
        String invalidCharacterErrorText = "Must only contain characters, numbers, spaces and the following special characters: . - _ ( )";

        ComponentInfoBuilder part1 = componentsUtil.postComponent(new ComponentRequestUtil().getComponent());
        ComponentInfoBuilder part2 = componentsUtil.postComponent(new ComponentRequestUtil().getComponent());
        part2.setUser(part1.getUser());

        explorePage = new CidAppLoginPage(driver)
            .login(part1.getUser());

        compareExplorePage = explorePage.multiSelectScenarios(part1.getComponentName() + "," + part1.getScenarioName(), part2.getComponentName() + "," + part2.getScenarioName())
            .createComparison()
            .selectManualComparison()
            .saveNew()
            .inputName(comparisonName1)
            .save(ComparePage.class)
            .clickAllComparisons()
            .clickExplore()
            .multiSelectScenarios(part2.getComponentName() + "," + part2.getScenarioName(), part1.getComponentName() + "," + part1.getScenarioName())
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
        String comparisonName1 = new GenerateStringUtil().generateComparisonName();
        String comparisonName2 = new GenerateStringUtil().generateComparisonName();

        ComponentInfoBuilder part1 = componentsUtil.postComponent(new ComponentRequestUtil().getComponent());
        ComponentInfoBuilder part2 = componentsUtil.postComponent(new ComponentRequestUtil().getComponent());
        part2.setUser(part1.getUser());

        explorePage = new CidAppLoginPage(driver)
            .login(currentUser);

        comparePage = explorePage.multiSelectScenarios(part1.getComponentName() + "," + part1.getScenarioName(), part2.getComponentName() + "," + part2.getScenarioName())
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