package com.evaluate.assemblies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.CssComponent;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ButtonTypeEnum;
import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;

import java.util.Arrays;
import java.util.List;

public class IncludeAndExcludeNestedAssemblyTests extends TestBase {

    private CidAppLoginPage loginPage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ComponentInfoBuilder componentAssembly1;
    private static ComponentInfoBuilder componentAssembly2;
    private static ComponentInfoBuilder componentAssembly3;
    private ComponentsTablePage componentsTablePage;
    private ComponentsTreePage componentsTreePage;
    private static UserCredentials currentUser;
    private static String scenarioName;
    SoftAssertions softAssertions = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();

    public IncludeAndExcludeNestedAssemblyTests() {
        super();
    }

    private static final String SUB_SUB_ASSEMBLY = "sub-sub-asm";
    private static final String SUB_ASSEMBLY = "sub-assembly";
    private static final String TOP_LEVEL = "top-level";

    @Before
    public void assemblySetup() {
        List<String> subSubComponentNames = Arrays.asList("3570823", "3571050");

        List<String> subAssemblyComponentNames = Arrays.asList("3570824", "0200613", "0362752");

        List<String> subComponentNames = Arrays.asList("3575135", "3574255", "3575134", "3575132", "3538968", "3575133");

        final String componentExtension = ".prt.1";
        final String assemblyExtension = ".asm.1";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        componentAssembly1 = assemblyUtils.uploadsAndOpenAssembly(SUB_SUB_ASSEMBLY, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subSubComponentNames,
            componentExtension, processGroupEnum, scenarioName, currentUser);

        componentAssembly2 = assemblyUtils.uploadsAndOpenAssembly(SUB_ASSEMBLY, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subAssemblyComponentNames,
            componentExtension, processGroupEnum, scenarioName, currentUser);

        componentAssembly3 = assemblyUtils.uploadsAndOpenAssembly(TOP_LEVEL, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subComponentNames,
            componentExtension, processGroupEnum, scenarioName, currentUser);
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = "11979")
    @Description("Verify Include and Exclude buttons disabled for a component that is part both of the top level assembly and sub-assembly")
    public void testIncludeAndExcludeDisabledForAssembly() {
        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .expandSubAssembly(SUB_ASSEMBLY, scenarioName)
            .selectSubAssemblySubComponent("3571050", SUB_ASSEMBLY);

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = "11158")
    @Description("Verify Exclude button disabled when selecting included sub-component from sub-assembly")
    public void testExcludeButtonDisabledWithSubcomponentsFromSubAssembly() {
        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .expandSubAssembly(SUB_ASSEMBLY, scenarioName)
            .multiSelectSubcomponents("0200613, " + scenarioName + "");

        assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(false));
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"11157", "11862", "6556"})
    @Description("Verify Include button disabled when selecting excluded sub-component from sub-assembly")
    public void testIncludeButtonDisabledWithSubcomponentsFromSubAssembly() {
        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .expandSubAssembly(SUB_ASSEMBLY, scenarioName)
            .selectSubAssemblySubComponent("3571050", SUB_ASSEMBLY)
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .expandSubAssembly(SUB_ASSEMBLY, scenarioName);

        softAssertions.assertThat(componentsTreePage.getListOfScenarios("3571050", scenarioName)).isEqualTo(2);

        componentsTreePage.collapseSubassembly(SUB_ASSEMBLY, scenarioName);

        softAssertions.assertThat(componentsTreePage.getListOfScenarios("3571050", scenarioName)).isEqualTo(1);

        componentsTreePage.expandSubAssembly(SUB_ASSEMBLY, scenarioName)
                .selectSubAssemblySubComponent("3571050", SUB_ASSEMBLY);

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"11827", "11826"})
    @Description("Validate  the publish button will only be enabled in the tree view and now the list view")
    public void testPublishButtonEnabledInTreeView() {
        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .multiSelectSubcomponents("3575135, " + scenarioName + "", "3574255, " + scenarioName + "", "3575134, " + scenarioName + "", "3575132, " + scenarioName + "", "3538968, " + scenarioName + "");

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(true);

        componentsTablePage = componentsTreePage.selectTableView()
            .setPagination()
            .multiSelectSubcomponents("3575135, " + scenarioName + "", "3574255, " + scenarioName + "", "3575134, " + scenarioName + "", "3575132, " + scenarioName + "", "3538968, " + scenarioName + "");

        softAssertions.assertThat(componentsTablePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @Issue("SC-377")
    @TestRail(testCaseId = "12196")
    @Description("Verify Table View indicates when only some, but not all, sub-components are excluded")
    public void testExcludeRepeatingComponent() {
        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(SUB_ASSEMBLY, scenarioName)
            .openComponents()
            .selectSubAssemblySubComponent("3571050", SUB_ASSEMBLY)
            .selectButtonType(ButtonTypeEnum.EXCLUDE);

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut("3571050")).isEqualTo(true);

        componentsTablePage = componentsTreePage
            .closePanel()
            .costScenario()
            .clickExplore()
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .selectTableView();

        String componentIdentity = cssComponent.findFirst("3571050", scenarioName, currentUser).getScenarioIdentity();

        softAssertions.assertThat(componentsTablePage.getColumnData(ColumnsEnum.QUANTITY, componentIdentity, currentUser)).isEqualTo("3");
        softAssertions.assertThat(componentsTablePage.getColumnData(ColumnsEnum.EXCLUDED, componentIdentity, currentUser)).isEqualTo("1");

        componentsTreePage = componentsTablePage.closePanel()
            .clickExplore()
            .navigateToScenario(componentAssembly1)
            .openComponents()
            .selectSubAssemblySubComponent("3571050", SUB_SUB_ASSEMBLY)
            .selectButtonType(ButtonTypeEnum.EXCLUDE);

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut("3571050")).isEqualTo(true);

        componentsTablePage = componentsTreePage
            .closePanel()
            .costScenario()
            .clickExplore()
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getColumnData(ColumnsEnum.QUANTITY, componentIdentity, currentUser)).isEqualTo("3");
        softAssertions.assertThat(componentsTablePage.getColumnData(ColumnsEnum.EXCLUDED, componentIdentity, currentUser)).isEqualTo("2");

        componentsTablePage.selectTreeView()
            .selectSubAssemblySubComponent("3571050", TOP_LEVEL)
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .closePanel()
            .costScenario()
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getColumnData(ColumnsEnum.QUANTITY, componentIdentity, currentUser)).isEqualTo("3");
        softAssertions.assertThat(componentsTablePage.getColumnData(ColumnsEnum.EXCLUDED, componentIdentity, currentUser)).isEqualTo("3");

        softAssertions.assertAll();
    }
}
