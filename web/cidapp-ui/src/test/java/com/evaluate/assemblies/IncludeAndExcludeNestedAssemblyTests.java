package com.evaluate.assemblies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ButtonTypeEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class IncludeAndExcludeNestedAssemblyTests extends TestBase {

    private CidAppLoginPage loginPage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ComponentInfoBuilder componentAssembly1;
    private static ComponentInfoBuilder componentAssembly2;
    private static ComponentInfoBuilder componentAssembly3;
    private ComponentsListPage componentsListPage;
    private static UserCredentials currentUser;
    private static String scenarioName;
    SoftAssertions softAssertions = new SoftAssertions();

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
    @TestRail(testCaseId = "11979")
    @Description("Verify Include and Exclude buttons disabled for a component that is part both of the top level assembly and sub-assembly")
    public void testIncludeAndExcludeDisabledForAssembly() {
        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .expandSubAssembly(SUB_ASSEMBLY, scenarioName)
            .selectSubAssemblySubComponent("3571050", SUB_ASSEMBLY);

        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(true);
        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "11158")
    @Description("Verify Exclude button disabled when selecting included sub-component from sub-assembly")
    public void testExcludeButtonDisabledWithSubcomponentsFromSubAssembly() {
        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .expandSubAssembly(SUB_ASSEMBLY, scenarioName)
            .multiSelectSubcomponents("0200613, " + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(false));
    }

    @Test
    @TestRail(testCaseId = {"11157", "11862", "6556"})
    @Description("Verify Include button disabled when selecting excluded sub-component from sub-assembly")
    public void testIncludeButtonDisabledWithSubcomponentsFromSubAssembly() {
        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .expandSubAssembly(SUB_ASSEMBLY, scenarioName)
            .selectSubAssemblySubComponent("3571050", SUB_ASSEMBLY)
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .expandSubAssembly(SUB_ASSEMBLY, scenarioName);

        softAssertions.assertThat(componentsListPage.getListOfScenarios("3571050", scenarioName)).isEqualTo(2);

        componentsListPage.collapseSubassembly(SUB_ASSEMBLY, scenarioName);

        softAssertions.assertThat(componentsListPage.getListOfScenarios("3571050", scenarioName)).isEqualTo(1);

        componentsListPage.expandSubAssembly(SUB_ASSEMBLY, scenarioName)
                .selectSubAssemblySubComponent("3571050", SUB_ASSEMBLY);

        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11827", "11826"})
    @Description("Validate  the publish button will only be enabled in the tree view and now the list view")
    public void testPublishButtonEnabledInTreeView() {
        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .multiSelectSubcomponents("3575135, " + scenarioName + "", "3574255, " + scenarioName + "", "3575134, " + scenarioName + "", "3575132, " + scenarioName + "", "3538968, " + scenarioName + "");

        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(true);

        componentsListPage.tableView()
            .setPagination()
            .multiSelectSubcomponents("3575135, " + scenarioName + "", "3574255, " + scenarioName + "", "3575134, " + scenarioName + "", "3575132, " + scenarioName + "", "3538968, " + scenarioName + "");

        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(false);
    }
}
