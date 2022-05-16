package com.evaluate.assemblies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ButtonTypeEnum;
import com.utils.ColourEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class IncludeAndExcludeTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ComponentsListPage componentsListPage;
    private EvaluatePage evaluatePage;
    private static ComponentInfoBuilder componentAssembly;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    SoftAssertions softAssertions = new SoftAssertions();
    private File assemblyResourceFile;
    private File componentResourceFile;

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
    @TestRail(testCaseId = "12089")
    @Description("Verify Excluded scenarios are not highlighted in flattened view")
    public void testExcludedScenarioInFlattenedView() {
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
            .multiSelectSubcomponents("PIN, " + scenarioName + "", "SMALL RING, " + scenarioName + "")
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .tableView();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(componentsListPage.getCellColour("pin", scenarioName)).isEqualTo(ColourEnum.YELLOW_LIGHT.getColour());
        softAssertions.assertThat(componentsListPage.getCellColour("small ring", scenarioName)).isEqualTo(ColourEnum.YELLOW_LIGHT.getColour());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11921", "11920", "11919"})
    @Description("Include all sub-components from top-level assembly")
    public void testIncludeSubcomponentsAndCost() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
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
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .closePanel()
            .costScenario();

        String totalCostValue1 = evaluatePage.getTypeOfCostResultsValue("Total Cost");
        String componentsCostValue1 = evaluatePage.getTypeOfCostResultsValue("Components Cost");

        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Components Cost", componentsCostValue1)).isTrue();
        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Total Cost", totalCostValue1)).isTrue();

        componentsListPage = evaluatePage.openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.INCLUDE);

        Stream.of(subComponentNames.toArray())
            .forEach(componentName ->
                assertThat(componentsListPage.isTextDecorationStruckOut(componentName.toString()), is(false)));

        evaluatePage = componentsListPage.closePanel()
            .costScenario();

        String totalCostValue2 = evaluatePage.getTypeOfCostResultsValue("Total Cost");
        String componentsCostValue2 = evaluatePage.getTypeOfCostResultsValue("Components Cost");
        String latestTotalCost = totalCostValue1.equals(totalCostValue2) ? totalCostValue1 : totalCostValue2;
        String latestComponentCost = componentsCostValue1.equals(componentsCostValue2) ? componentsCostValue1 : componentsCostValue2;

        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Components Cost", latestComponentCost)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Total Cost", latestTotalCost)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11918", "11917", "11916"})
    @Description("Exclude all sub-components from top-level assembly")
    public void testExcludeSubcomponentsAndCost() {
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList("flange", "nut", "bolt");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
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
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly);

        String totalCostValue1 = evaluatePage.getTypeOfCostResultsValue("Total Cost");
        String componentsCostValue1 = evaluatePage.getTypeOfCostResultsValue("Components Cost");

        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Components Cost", componentsCostValue1)).isTrue();
        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Total Cost", totalCostValue1)).isTrue();

        componentsListPage = evaluatePage.openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.EXCLUDE);

        Stream.of(subComponentNames.toArray())
            .forEach(componentName ->
                assertThat(componentsListPage.isTextDecorationStruckOut(componentName.toString()), is(true)));

        evaluatePage = componentsListPage.closePanel()
            .costScenario();

        String totalCostValue2 = evaluatePage.getTypeOfCostResultsValue("Total Cost");
        String componentsCostValue2 = evaluatePage.getTypeOfCostResultsValue("Components Cost");
        String latestTotalCost = totalCostValue1.equals(totalCostValue2) ? totalCostValue1 : totalCostValue2;
        String latestComponentCost = componentsCostValue1.equals(componentsCostValue2) ? componentsCostValue1 : componentsCostValue2;

        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Components Cost", latestComponentCost)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Total Cost", latestTotalCost)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"12135", "12052"})
    @Description("Missing sub-component automatically included on update - test with alternate CAD file for Assembly with additional components not on system")
    public void testMissingSubcomponentIncludedOnUpdate() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        final ProcessGroupEnum assemblyProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;
        assemblyResourceFile = FileResourceUtil.getCloudCadFile(assemblyProcessGroupEnum, assemblyName + assemblyExtension);

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        String componentName = "box";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";
        componentResourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + componentExtension);

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroupEnum,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .updateCadFile(assemblyResourceFile)
            .generateMissingImages("Yes")
            .openComponents();


        softAssertions.assertThat(componentsListPage.isComponentNameDisplayedInTreeView(componentName)).isEqualTo(true);
        softAssertions.assertThat(componentsListPage.isTextDecorationStruckOut(componentName)).isEqualTo(true);

        componentsListPage = componentsListPage.selectScenario(componentName)
            .updateCadFile()
            .enterFilePath(componentResourceFile)
            .submit(ComponentsListPage.class);

        assertThat(componentsListPage.isTextDecorationStruckOut(componentName), is(false));
    }

    @Test
    @TestRail(testCaseId = "12138")
    @Description("Missing sub-component automatically included on update (CID)")
    public void testMissingComponentAutomaticallyIncludedOnUpdate() {
        String assemblyName = "Assembly01";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum assemblyProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;

        List<String> subComponentNames = Arrays.asList("Part0001", "Part0002", "Part0003");
        String componentName = "Part0004";
        final String componentExtension = ".ipt";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        componentResourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + componentExtension);

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroupEnum,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        softAssertions.assertThat(componentsListPage.isTextDecorationStruckOut(componentName)).isEqualTo(true);

        componentsListPage.selectScenario(componentName)
            .updateCadFile()
            .enterFilePath(componentResourceFile)
            .submit(ComponentsListPage.class);

        assertThat(componentsListPage.isTextDecorationStruckOut(componentName), is(false));
    }
}
