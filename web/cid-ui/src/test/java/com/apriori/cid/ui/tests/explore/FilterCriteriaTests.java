package com.apriori.cid.ui.tests.explore;

import static com.apriori.shared.util.enums.ProcessGroupEnum.CASTING_DIE;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

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
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.UnitsEnum;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class FilterCriteriaTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder component;
    private ComponentInfoBuilder assembly;
    private FilterPage filterPage;
    private SoftAssertions softAssertion = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();

    public FilterCriteriaTests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (assembly != null) {
            new UserPreferencesUtil().resetSettings(assembly.getUser());
        }
        if (component != null) {
            new UserPreferencesUtil().resetSettings(component.getUser());
        }
    }

    @Test
    @TestRail(id = {6213})
    @Description("Test private criteria part")
    public void testPrivateCriteriaPart() {
        String filterName = generateStringUtil.generateFilterName();

        component = new ComponentRequestUtil().getComponent();

        explorePage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, component.getComponentName())
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName()), is(equalTo(1)));
    }

    @Test
    @TestRail(id = {6214, 6215})
    @Description("Test private criteria attribute")
    public void testPrivateCriteriaAttribute() {
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();

        component = new ComponentRequestUtil().getComponentByProcessGroup(CASTING_DIE);

        explorePage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.PROCESS_GROUP, OperationEnum.IN, CASTING_DIE.getProcessGroup())
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertion.assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName())).isEqualTo(1);

        explorePage.filter()
            .saveAs()
            .inputName(filterName2)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, component.getComponentName().substring(component.getComponentName().length() / 2))
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertion.assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName())).isEqualTo(1);

        softAssertion.assertAll();
    }

    @Test
    @TestRail(id = {6216})
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {
        String filterName = generateStringUtil.generateFilterName();

        assembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadAssembly(assembly);

        explorePage = new CidAppLoginPage(driver)
            .login(assembly.getUser())
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, assembly.getComponentName().substring(assembly.getComponentName().length() / 2))
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(assembly.getComponentName(), assembly.getScenarioName()), is(equalTo(1)));
    }

    @Test
    @TestRail(id = {6217})
    @Description("Test public criteria assembly status")
    public void testPublicCriteriaAssemblyStatus() {
        String filterName = generateStringUtil.generateFilterName();

        assembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(assembly)
            .uploadAssembly(assembly);
        assemblyUtils.publishSubComponents(assembly)
            .costAssembly(assembly);

        explorePage = new CidAppLoginPage(driver).login(assembly.getUser())
            .navigateToScenario(assembly)
            .clickActions()
            .info()
            .selectStatus("Analysis")
            .inputCostMaturity("High")
            .inputDescription("Test Description")
            .inputNotes("Test Notes")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(assembly, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.STATUS, OperationEnum.IN, "Analysis")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(assembly.getComponentName(), assembly.getScenarioName()), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(id = {6218})
    @Description("Test public criteria part")
    public void testPublicCriteriaPart() {
        String filterName = generateStringUtil.generateFilterName();

        component = new ComponentRequestUtil().getComponent();

        explorePage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .navigateToScenario(component)
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, component.getComponentName().substring(0, component.getComponentName().length() / 2))
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName()), is(equalTo(1)));
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6221, 6532})
    @Description("Test multiple attributes")
    public void testFilterAttributes() {
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();

        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent();

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
    @TestRail(id = {6169, 6170, 6100, 9108, 9109})
    @Description("Validate that user can cancel action New, Rename, Save As before saving")
    public void testCancelNewSaveRename() {
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();
        String filterName3 = generateStringUtil.generateFilterName();
        String filterName4 = generateStringUtil.generateFilterName();

        filterPage = new CidAppLoginPage(driver)
            .login(UserUtil.getUser())
            .filter()
            .selectFilter("Private");

        softAssertion.assertThat(filterPage.isDeleteButtonEnabled()).isFalse();
        softAssertion.assertThat(filterPage.isRenameButtonEnabled()).isFalse();

        filterPage.cancel(ExplorePage.class)
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

        explorePage = filterPage.cancel(ExplorePage.class)
            .filter()
            .newFilter()
            .inputName(filterName2)
            .addCriteria(PropertyEnum.TOTAL_CAPITAL_INVESTMENT, OperationEnum.LESS_THAN, "1")
            .save(FilterPage.class)
            .submit(ExplorePage.class);

        filterPage = explorePage.openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectCurrency(CurrencyEnum.EUR)
            .submit(ExplorePage.class)
            .filter();

        softAssertion.assertThat(filterPage.getFilterValue(PropertyEnum.TOTAL_CAPITAL_INVESTMENT)).isEqualTo("0.847946");

        filterPage.cancel(ExplorePage.class)
            .filter()
            .newFilter()
            .inputName(filterName3)
            .addCriteria(PropertyEnum.FINISH_MASS, OperationEnum.GREATER_THAN, "1")
            .save(FilterPage.class)
            .submit(ExplorePage.class);

        filterPage = explorePage.openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectMass(MassEnum.GRAM)
            .submit(ExplorePage.class)
            .filter();

        softAssertion.assertThat(filterPage.getFilterValue(PropertyEnum.FINISH_MASS)).isEqualTo("1000");

        filterPage.cancel(ExplorePage.class)
            .filter()
            .newFilter()
            .inputName(filterName4)
            .addCriteria(PropertyEnum.CYCLE_TIME, OperationEnum.GREATER_THAN, "60")
            .save(FilterPage.class)
            .submit(ExplorePage.class);

        filterPage = explorePage.openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectTime(TimeEnum.MINUTE)
            .submit(ExplorePage.class)
            .filter();

        softAssertion.assertThat(filterPage.getFilterValue(PropertyEnum.CYCLE_TIME)).isEqualTo("1");

        softAssertion.assertAll();
    }
}