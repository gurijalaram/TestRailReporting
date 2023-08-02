package com.apriori.evaluate.assemblies;

import static com.apriori.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import com.utils.ButtonTypeEnum;
import com.utils.ColourEnum;
import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class IncludeAndExcludeNestedAssemblyTests extends TestBaseUI {

    private static final String SUB_SUB_ASSEMBLY = "sub-sub-asm";
    private static final String SUB_ASSEMBLY = "sub-assembly";
    private static final String TOP_LEVEL = "top-level";
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ComponentInfoBuilder componentAssembly1;
    private static ComponentInfoBuilder componentAssembly2;
    private static ComponentInfoBuilder componentAssembly3;
    private static UserCredentials currentUser;
    private static String scenarioName;
    SoftAssertions softAssertions = new SoftAssertions();
    private CidAppLoginPage loginPage;
    private ComponentsTablePage componentsTablePage;
    private ComponentsTreePage componentsTreePage;
    private CssComponent cssComponent = new CssComponent();

    public IncludeAndExcludeNestedAssemblyTests() {
        super();
    }

    @BeforeEach
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
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = 11979)
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
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = 11158)
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
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11157, 11862, 6556})
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
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11827, 11826})
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
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = 12196)
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

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11873, 11863, 11950})
    @Description("Validate that in instances where multiple iterations of a part exist in an assembly, selection of this in table view will highlight all upon switch to tree view")
    public void testMultipleIterationOfPartHighlightedInTreeAndTableView() {

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getListOfScenarios(SUB_SUB_ASSEMBLY, scenarioName)).isEqualTo(0);

        componentsTablePage = componentsTreePage.selectTableView()
            .setPagination()
            .highlightScenario("3571050", scenarioName);

        softAssertions.assertThat(componentsTablePage.getCellColour("3571050", scenarioName)).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        componentsTreePage = componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getCellColour("3571050", scenarioName)).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        componentsTreePage.multiSelectSubcomponents(SUB_ASSEMBLY + "," + scenarioName + "")
            .expandSubAssembly(SUB_ASSEMBLY, scenarioName);

        softAssertions.assertThat(componentsTreePage.isScenarioCheckboxSelected(SUB_ASSEMBLY, scenarioName)).isTrue();

        softAssertions.assertThat(componentsTreePage.getCellColour("3571050", scenarioName)).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        softAssertions.assertThat(componentsTreePage.isScenarioCheckboxSelected("0200613", scenarioName)).isFalse();

        componentsTreePage.expandSubAssembly(SUB_SUB_ASSEMBLY, scenarioName);

        softAssertions.assertThat(componentsTreePage.getCellColour("3571050", scenarioName)).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());
        softAssertions.assertThat(componentsTreePage.getListOfScenarios(SUB_SUB_ASSEMBLY, scenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }
}
