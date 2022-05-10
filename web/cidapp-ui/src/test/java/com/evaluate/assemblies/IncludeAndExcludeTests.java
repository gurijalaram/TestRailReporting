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
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class IncludeAndExcludeTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ComponentsListPage componentsListPage;
    private static ComponentInfoBuilder componentAssembly;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();

    public IncludeAndExcludeTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "11154")
    @Description("Include and Exclude buttons disabled by default")
    public void testIncludeAndExcludeDisabledButtons() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents();

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE), is(false));
        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(false));
    }

    @Test
    @TestRail(testCaseId = "11150")
    @Description("Exclude all sub-components from top-level assembly")
    public void testExcludeButtons() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.EXCLUDE);

        Stream.of(subComponentNames.toArray())
            .forEach(componentName ->
                assertThat(componentsListPage.isTextDecorationStruckOut(componentName.toString()), is(true)));
    }

    @Test
    @TestRail(testCaseId = "11979")
    @Description("Verify Include and Exclude buttons disabled for a component that is part both of the top level assembly and sub-assembly")
    public void testIncludeAndExcludeDisabledForAssembly() {

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        String assembly1 = "sub-sub-asm";
        List<String> subSubComponentNames = Arrays.asList("3570823", "3571050");

        String assembly2 = "sub-assembly";
        List<String> subAssemblyComponentNames = Arrays.asList("3570824", "0200613", "0362752");

        String assemblyName = "top-level";
        List<String> subComponentNames = Arrays.asList("3575135", "3574255", "3575134", "3575132", "3538968", "3575133");

        final String componentExtension = ".prt.1";
        final String assemblyExtension = ".asm.1";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(assembly1, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subSubComponentNames, componentExtension, processGroupEnum, scenarioName, currentUser)
            .uploadsAndOpenAssembly(assembly2, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subAssemblyComponentNames, componentExtension, processGroupEnum, scenarioName, currentUser)
            .uploadsAndOpenAssembly(assemblyName, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subComponentNames, componentExtension, processGroupEnum, scenarioName, currentUser)
            .openComponents()
            .expandSubAssembly(assembly2)
            .multiSelectSubcomponents("3571050, " + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE), is(false));
        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(false));
    }

    @Test
    @TestRail(testCaseId = {"11874", "11843", "11842"})
    @Description("Verify Include and Exclude buttons disabled if mixture selected")
    public void testIncludeAndExcludeDisabledButtonsWithMixedSelections() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .multiSelectSubcomponents("PIN, " + scenarioName + "")
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .multiSelectSubcomponents("SMALL RING, " + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE), is(false));
        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(true));

        componentsListPage.multiSelectSubcomponents("pin, " + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE), is(false));
        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(false));

        componentsListPage.multiSelectSubcomponents("small ring, " + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE), is(true));
        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(false));

        componentsListPage.multiSelectSubcomponents("small ring, " + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE), is(false));
        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(false));
    }

    @Test
    @TestRail(testCaseId = "11158")
    @Description("Verify Exclude button disabled when selecting included sub-component from sub-assembly")
    public void testExcludeButtonDisabledSubAssembly() {

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        String assembly1 = "sub-sub-asm";
        List<String> subSubComponentNames = Arrays.asList("3570823", "3571050");

        String assembly2 = "sub-assembly";
        List<String> subAssemblyComponentNames = Arrays.asList("3570824", "0200613", "0362752");

        String assemblyName = "top-level";
        List<String> subComponentNames = Arrays.asList("3575135", "3574255", "3575134", "3575132", "3538968", "3575133");

        final String componentExtension = ".prt.1";
        final String assemblyExtension = ".asm.1";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(assembly1, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subSubComponentNames, componentExtension, processGroupEnum, scenarioName, currentUser)
            .uploadsAndOpenAssembly(assembly2, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subAssemblyComponentNames, componentExtension, processGroupEnum, scenarioName, currentUser)
            .uploadsAndOpenAssembly(assemblyName, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subComponentNames, componentExtension, processGroupEnum, scenarioName, currentUser)
            .openComponents()
            .expandSubAssembly(assembly2)
            .multiSelectSubcomponents("0200613, " + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(false));
    }

    @Test
    @TestRail(testCaseId = "11157")
    @Description("Verify Include button disabled when selecting excluded sub-component from sub-assembly")
    public void testIncludeButtonDisabledSubAssembly() {
        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        String assembly1 = "sub-sub-asm";
        List<String> subSubComponentNames = Arrays.asList("3570823", "3571050");
        String assembly2 = "sub-assembly";
        List<String> subAssemblyComponentNames = Arrays.asList("3570824", "0200613", "0362752");
        String assemblyName = "top-level";
        List<String> subComponentNames = Arrays.asList("3575135", "3574255", "3575134", "3575132", "3538968", "3575133");

        final String componentExtension = ".prt.1";
        final String assemblyExtension = ".asm.1";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(assembly1, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subSubComponentNames, componentExtension, processGroupEnum, scenarioName, currentUser)
            .uploadsAndOpenAssembly(assembly2, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subAssemblyComponentNames, componentExtension, processGroupEnum, scenarioName, currentUser)
            .uploadsAndOpenAssembly(assemblyName, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subComponentNames, componentExtension, processGroupEnum, scenarioName, currentUser)
            .openComponents()
            .expandSubAssembly(assembly2)
            .selectSubAssemblySubComponent(assembly2.toUpperCase(), 5)
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .selectSubAssemblySubComponent(assembly2.toUpperCase(), 5);

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE), is(true));
    }

    @Test
    @TestRail(testCaseId = "11156")
    @Description("Verify Exclude button is available when an included sub-component is selected")
    public void testExcludeButtonAvailableWithCostedComponents() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser).navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents("PIN, " + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(true));
    }
}
