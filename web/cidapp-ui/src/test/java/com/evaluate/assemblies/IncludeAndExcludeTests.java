package com.evaluate.assemblies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ButtonTypeEnum;
import com.utils.ColourEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
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
    private UserCredentials currentUser;
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

        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        softAssertions.assertAll();
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
    @TestRail(testCaseId = {"11874", "11843", "11842", "11155"})
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

        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(true);

        componentsListPage.multiSelectSubcomponents("pin, " + scenarioName + "");

        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        componentsListPage.multiSelectSubcomponents("small ring, " + scenarioName + "");

        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(true);
        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        componentsListPage.multiSelectSubcomponents("small ring, " + scenarioName + "");

        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11153", "11152", "11151"})
    @Description("Include all sub-components from top-level assembly")
    public void testIncludeButtonEnabledWithCostedComponents() {
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
        componentsListPage = loginPage.login(currentUser)
                .navigateToScenario(componentAssembly)
                .openComponents()
                .selectCheckAllBox()
                .selectButtonType(ButtonTypeEnum.EXCLUDE)
                .selectCheckAllBox();

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE), is(true));

        componentsListPage.selectButtonType(ButtonTypeEnum.INCLUDE);

        Stream.of(subComponentNames.toArray())
                .forEach(componentName ->
                        assertThat(componentsListPage.isTextDecorationStruckOut(componentName.toString()), is(false)));
    }

    @Test
    @TestRail(testCaseId = {"11150", "11149", "11156"})
    @Description("Include all sub-components from top-level assembly")
    public void testExcludeButtonEnabledWithCostedComponents() {
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
        componentsListPage = loginPage.login(currentUser)
                .navigateToScenario(componentAssembly)
                .openComponents()
                .selectCheckAllBox();

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(true));

        componentsListPage.selectButtonType(ButtonTypeEnum.EXCLUDE);

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

        currentUser = UserUtil.getUser();
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

        softAssertions.assertThat(componentsListPage.getCellColour("pin", scenarioName)).isEqualTo(ColourEnum.YELLOW_LIGHT.getColour());
        softAssertions.assertThat(componentsListPage.getCellColour("small ring", scenarioName)).isEqualTo(ColourEnum.YELLOW_LIGHT.getColour());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11921", "11920", "11919"})
    @Description("Include all sub-components from top-level assembly")
    public void testIncludeSubcomponentsAndCost() {
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList("flange", "nut", "bolt");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        currentUser = UserUtil.getUser();
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

        double initialTotalCost = evaluatePage.getCostResults("Total Cost");
        double initialComponentsCost = evaluatePage.getCostResults("Components Cost");

        componentsListPage = evaluatePage.openComponents()
                .selectCheckAllBox()
                .selectButtonType(ButtonTypeEnum.INCLUDE);

        subComponentNames.forEach(componentName ->
                assertThat(componentsListPage.isTextDecorationStruckOut(componentName), is(false)));

        evaluatePage = componentsListPage.closePanel()
                .costScenario();

        double modifiedTotalCost = evaluatePage.getCostResults("Total Cost");
        double modifiedComponentsCost = evaluatePage.getCostResults("Components Cost");

        softAssertions.assertThat(modifiedTotalCost).isGreaterThan(initialTotalCost);
        softAssertions.assertThat(modifiedComponentsCost).isGreaterThan(initialComponentsCost);

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

        currentUser = UserUtil.getUser();
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

        double initialTotalCost = evaluatePage.getCostResults("Total Cost");
        double initialComponentsCost = evaluatePage.getCostResults("Components Cost");

        componentsListPage = evaluatePage.openComponents()
                .selectCheckAllBox()
                .selectButtonType(ButtonTypeEnum.EXCLUDE);

        subComponentNames.forEach(componentName ->
                assertThat(componentsListPage.isTextDecorationStruckOut(componentName), is(true)));

        evaluatePage = componentsListPage.closePanel()
                .costScenario();

        double modifiedTotalCost = evaluatePage.getCostResults("Total Cost");
        double modifiedComponentsCost = evaluatePage.getCostResults("Components Cost");

        softAssertions.assertThat(initialTotalCost).isGreaterThan(modifiedTotalCost);
        softAssertions.assertThat(initialComponentsCost).isGreaterThan(modifiedComponentsCost);

        softAssertions.assertAll();
    }

    @Test
    @Issue("AP-74028")
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
                .openComponents();

        subComponentNames.forEach(component ->
                softAssertions.assertThat(componentsListPage.isComponentNameDisplayedInTreeView(component)).isTrue());

        componentsListPage = componentsListPage.closePanel()
                .clickActions()
                .updateCadFile(assemblyResourceFile)
                .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 5)
                .openComponents();

        softAssertions.assertThat(componentsListPage.isComponentNameDisplayedInTreeView(componentName)).isEqualTo(true);
        softAssertions.assertThat(componentsListPage.isTextDecorationStruckOut(componentName)).isEqualTo(true);

        componentsListPage = componentsListPage.clickScenarioCheckbox(componentName)
                .updateCadFile(componentResourceFile)
                .closePanel()
                .clickRefresh(ComponentsListPage.class);

        softAssertions.assertThat(componentsListPage.isTextDecorationStruckOut(componentName)).isEqualTo(false);

        softAssertions.assertAll();

        componentsListPage.closePanel()
                .clickExplore()
                .selectFilter("Recent")
                .highlightScenario(assemblyName, scenarioName)
                .delete()
                .submit(ExplorePage.class)
                .highlightScenario(componentName, scenarioName)
                .delete()
                .submit(ExplorePage.class);
    }
}

