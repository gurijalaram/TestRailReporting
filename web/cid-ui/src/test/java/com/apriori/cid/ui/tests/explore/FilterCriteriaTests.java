package com.apriori.cid.ui.tests.explore;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cid.api.models.dto.ComponentDTORequest;
import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.common.FilterPage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.CurrencyEnum;
import com.apriori.cid.ui.utils.MassEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.cid.ui.utils.TimeEnum;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.UnitsEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FilterCriteriaTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private File resourceFile;
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ComponentInfoBuilder cidComponentItem;
    private FilterPage filterPage;
    private SoftAssertions softAssertion = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();

    public FilterCriteriaTests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Test
    @TestRail(id = {6213})
    @Description("Test private criteria part")
    public void testPrivateCriteriaPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        String componentName = "SheetMetal";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, "SheetMetal")
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(id = {6214})
    @Description("Test private criteria attribute")
    public void testPrivateCriteriaAttribute() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.PROCESS_GROUP, OperationEnum.IN, ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(id = {6215})
    @Description("Test private criteria part contains")
    public void testPrivateCriteriaContains() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "CurvedWall";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, "Wall")
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(id = {6216})
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        String filename = "oldham.asm.1";
        String componentName = "OLDHAM";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, filename);
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, "oldham")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("oldham", scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(id = {6217})
    @Description("Test public criteria assembly status")
    public void testPublicCriteriaAssemblyStatus() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        final List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";
        String filterName = generateStringUtil.generateFilterName();

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .clickActions()
            .info()
            .selectStatus("Analysis")
            .inputCostMaturity("High")
            .inputDescription("Test Description")
            .inputNotes("Test Notes")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(componentAssembly, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.STATUS, OperationEnum.IN, "Analysis")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(id = {6218})
    @Description("Test public criteria part")
    public void testPublicCriteriaPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, "Push Pin")
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios("Push Pin", scenarioName), is(equalTo(1)));
    }

    @Test
    @Issue("BA-2610")
    @Tag(SMOKE)
    @TestRail(id = {6221, 6532})
    @Description("Test multiple attributes")
    public void testFilterAttributes() {
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();

        ComponentInfoBuilder component = new ComponentDTORequest().getComponent();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(component.getUser())
            .uploadComponent(component.getComponentName(), component.getScenarioName(), component.getResourceFile(), component.getUser());

        ScenarioItem scenarioCreated = cssComponent.findFirst(cidComponentItem.getComponentName(), cidComponentItem.getScenarioName(), cidComponentItem.getUser());

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .publishScenario(PublishPage.class)
            .selectStatus("Analysis")
            .selectCostMaturity("Initial")
            .selectAssignee(cidComponentItem.getUser())
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.ASSIGNEE, OperationEnum.IN, scenarioCreated.getScenarioCreatedByName())
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .lock(ExplorePage.class)
            .filter()
            .newFilter()
            .inputName(filterName2)
            .addCriteria(PropertyEnum.STATUS, OperationEnum.IN, "Analysis")
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Initial")
            .addCriteria(PropertyEnum.ASSIGNEE, OperationEnum.IN, scenarioCreated.getScenarioCreatedByName())
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName()), is(equalTo(1)));
    }

    @Test
    @TestRail(id = {6169, 6170})
    @Description("Check that user cannot Delete Preset Filters")
    public void testDeleteButtonDisabledForPresetFilters() {
        currentUser = UserUtil.getUser();
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser);

        filterPage = new ExplorePage(driver)
            .filter()
            .selectFilter("Private");

        softAssertion.assertThat(filterPage.isDeleteButtonEnabled()).isFalse();
        softAssertion.assertThat(filterPage.isRenameButtonEnabled()).isFalse();

        softAssertion.assertAll();
    }

    @Test
    @TestRail(id = {6100})
    @Description("Validate that user can cancel action New, Rename, Save As before saving")
    public void testCancelNewSaveRename() {
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser);

        filterPage = new ExplorePage(driver)
            .filter()
            .newFilter()
            .inputName(filterName)
            .save(FilterPage.class)
            .newFilter();

        softAssertion.assertThat(filterPage.isNameFieldDisplayed()).isTrue();
        softAssertion.assertThat(filterPage.isCancelBtnDisplayed()).isTrue();

        filterPage.cancel(FilterPage.class)
            .saveAs();

        softAssertion.assertThat(filterPage.isNameFieldDisplayed()).isTrue();
        softAssertion.assertThat(filterPage.isCancelBtnDisplayed()).isTrue();

        filterPage.cancel(FilterPage.class)
            .rename();

        softAssertion.assertThat(filterPage.isNameFieldDisplayed()).isTrue();
        softAssertion.assertThat(filterPage.isCancelBtnDisplayed()).isTrue();

        filterPage.cancel(FilterPage.class);

        softAssertion.assertThat(filterPage.isElementDisplayed(filterName, "text-overflow")).isTrue();

        softAssertion.assertAll();
    }

    @Test
    @TestRail(id = {9108})
    @Description("Verify that filter values for cost results are converted after changing unit preferences")
    public void testFilterValuesAfterChangingUnitPreferencesToEUR() {
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser);

        explorePage = new ExplorePage(driver)
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.TOTAL_CAPITAL_INVESTMENT, OperationEnum.LESS_THAN, "1")
            .save(FilterPage.class)
            .submit(ExplorePage.class);

        filterPage = explorePage.openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectCurrency(CurrencyEnum.EUR)
            .submit(ExplorePage.class)
            .filter();

        assertThat(filterPage.getFilterValue(PropertyEnum.TOTAL_CAPITAL_INVESTMENT), equalTo("0.847946"));
    }

    @Test
    @TestRail(id = {9109})
    @Description("Verify that filter value for Finish Mass is converted after changing unit preferences")
    public void testFilterValuesAfterChangingUnitPreferencesToGram() {
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser);

        explorePage = new ExplorePage(driver)
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.FINISH_MASS, OperationEnum.GREATER_THAN, "1")
            .save(FilterPage.class)
            .submit(ExplorePage.class);

        filterPage = explorePage.openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectMass(MassEnum.GRAM)
            .submit(ExplorePage.class)
            .filter();

        assertThat(filterPage.getFilterValue(PropertyEnum.FINISH_MASS), equalTo("1000"));
    }

    @Test
    @TestRail(id = {9110})
    @Description("Verify that filter value for Cycle Time is converted after changing unit preferences")
    public void testFilterValuesAfterChangingUnitPreferencesForCycleTime() {
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser);

        explorePage = new ExplorePage(driver)
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.CYCLE_TIME, OperationEnum.GREATER_THAN, "60")
            .save(FilterPage.class)
            .submit(ExplorePage.class);

        filterPage = explorePage.openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectTime(TimeEnum.MINUTE)
            .submit(ExplorePage.class)
            .filter();

        assertThat(filterPage.getFilterValue(PropertyEnum.CYCLE_TIME), equalTo("1"));
    }
}