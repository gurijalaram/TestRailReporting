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
import org.junit.BeforeClass;
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
    private static String scenarioName;
    private static UserCredentials currentUser;
    SoftAssertions softAssertions = new SoftAssertions();
    String assembly2 = "sub-assembly";

    public IncludeAndExcludeNestedAssemblyTests() {
        super();
    }

    @BeforeClass
    public static void assemblySetup() {
        String assembly1 = "sub-sub-asm";
        List<String> subSubComponentNames = Arrays.asList("3570823", "3571050");

        String assembly2 = "sub-assembly";
        List<String> subAssemblyComponentNames = Arrays.asList("3570824", "0200613", "0362752");

        String assemblyName = "top-level";
        List<String> subComponentNames = Arrays.asList("3575135", "3574255", "3575134", "3575132", "3538968", "3575133");

        final String componentExtension = ".prt.1";
        final String assemblyExtension = ".asm.1";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        currentUser = UserUtil.getUser();
        scenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly1 = assemblyUtils.uploadsAndOpenAssembly(assembly1, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subSubComponentNames,
            componentExtension, processGroupEnum, scenarioName, currentUser);

        componentAssembly2 = assemblyUtils.uploadsAndOpenAssembly(assembly2, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subAssemblyComponentNames,
            componentExtension, processGroupEnum, scenarioName, currentUser);

        componentAssembly3 = assemblyUtils.uploadsAndOpenAssembly(assemblyName, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subComponentNames,
            componentExtension, processGroupEnum, scenarioName, currentUser);
    }

    @Test
    @TestRail(testCaseId = "11979")
    @Description("Verify Include and Exclude buttons disabled for a component that is part both of the top level assembly and sub-assembly")
    public void testIncludeAndExcludeDisabledForAssembly() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .expandSubAssembly(assembly2)
            .selectSubAssemblySubComponent("3571050", assembly2);

        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(true);
        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "11158")
    @Description("Verify Exclude button disabled when selecting included sub-component from sub-assembly")
    public void testExcludeButtonDisabledWithSubcomponentsFromSubAssembly() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .expandSubAssembly(assembly2)
            .multiSelectSubcomponents("0200613, " + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(false));
    }

    @Test
    @TestRail(testCaseId = "11157")
    @Description("Verify Include button disabled when selecting excluded sub-component from sub-assembly")
    public void testIncludeButtonDisabledWithSubcomponentsFromSubAssembly() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly3)
            .openComponents()
            .expandSubAssembly(assembly2)
            .selectSubAssemblySubComponent("3571050", assembly2)
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .expandSubAssembly(assembly2)
            .selectSubAssemblySubComponent("3571050", assembly2);

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE), is(true));
    }
}
