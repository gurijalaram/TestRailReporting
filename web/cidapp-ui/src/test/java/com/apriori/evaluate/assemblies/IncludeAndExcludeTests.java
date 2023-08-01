package com.apriori.evaluate.assemblies;

import static com.apriori.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.enums.NewCostingLabelEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.ButtonTypeEnum;
import com.utils.ColourEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Issues;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class IncludeAndExcludeTests extends TestBaseUI {

    private static ComponentInfoBuilder componentAssembly;
    private static ComponentsUtil componentsUtil = new ComponentsUtil();
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static String scenarioName;
    private static ComponentInfoBuilder componentAssembly1;
    private static ComponentInfoBuilder componentAssembly2;
    private final String SUB_SUB_ASSEMBLY = "sub-sub-asm";
    private final String SUB_ASSEMBLY = "sub-assembly";
    private CidAppLoginPage loginPage;
    private ComponentsTablePage componentsTablePage;
    private ComponentsTreePage componentsTreePage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();
    private File assemblyResourceFile;
    private File componentResourceFile;
    private ComponentInfoBuilder assemblyInfo;

    public IncludeAndExcludeTests() {
        super();
    }

    @AfterEach
    public void deleteScenarios() {
        if (currentUser != null) {
            if (assemblyInfo != null) {
                assemblyUtils.deleteAssemblyAndComponents(assemblyInfo);
                assemblyInfo = null;
            }
            if (componentAssembly1 != null) {
                assemblyUtils.deleteAssemblyAndComponents(componentAssembly1);
            }
            if (componentAssembly2 != null) {
                assemblyUtils.deleteAssemblyAndComponents(componentAssembly2);
            }
        }
    }

    @Test
    @TestRail(id = 11154)
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
        componentsTreePage = loginPage.login(currentUser)
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

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11150)
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
        componentsTreePage = loginPage.login(currentUser)
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
                assertThat(componentsTreePage.isTextDecorationStruckOut(componentName.toString()), is(true)));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11874, 11843, 11842, 11155, 11148})
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
        componentsTreePage = loginPage.login(currentUser)
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

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(true);

        componentsTreePage.multiSelectSubcomponents("pin, " + scenarioName + "");

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        componentsTreePage.multiSelectSubcomponents("small ring, " + scenarioName + "");

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        componentsTreePage.multiSelectSubcomponents("small ring, " + scenarioName + "");

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11153, 11152, 11151})
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
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .selectCheckAllBox();

        assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE), is(true));

        componentsTreePage.selectButtonType(ButtonTypeEnum.INCLUDE);

        Stream.of(subComponentNames.toArray())
            .forEach(componentName ->
                assertThat(componentsTreePage.isTextDecorationStruckOut(componentName.toString()), is(false)));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11150, 11149, 11156})
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
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectCheckAllBox();

        assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(true));

        componentsTreePage.selectButtonType(ButtonTypeEnum.EXCLUDE);

        Stream.of(subComponentNames.toArray())
            .forEach(componentName ->
                assertThat(componentsTreePage.isTextDecorationStruckOut(componentName.toString()), is(true)));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {12089, 6554})
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
        componentsTreePage = loginPage.login(currentUser)
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
            .multiSelectSubcomponents("PIN, " + scenarioName + ",SMALL RING, " + scenarioName + "")
            .selectButtonType(ButtonTypeEnum.EXCLUDE);

        componentsTablePage = componentsTreePage.selectTableView();

        softAssertions.assertThat(componentsTablePage.getCellColour("pin", scenarioName)).isEqualTo(ColourEnum.YELLOW_LIGHT.getColour());
        softAssertions.assertThat(componentsTablePage.getCellColour("small ring", scenarioName)).isEqualTo(ColourEnum.YELLOW_LIGHT.getColour());

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11921, 11920, 11919})
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
        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(processGroupEnum.getProcessGroup())
                .build()));
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

        componentsTreePage = evaluatePage.openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.INCLUDE);

        subComponentNames.forEach(componentName ->
            assertThat(componentsTreePage.isTextDecorationStruckOut(componentName), is(false)));

        evaluatePage = componentsTreePage.closePanel()
            .costScenario();

        double modifiedTotalCost = evaluatePage.getCostResults("Total Cost");
        double modifiedComponentsCost = evaluatePage.getCostResults("Components Cost");

        softAssertions.assertThat(modifiedTotalCost).isGreaterThan(initialTotalCost);
        softAssertions.assertThat(modifiedComponentsCost).isGreaterThan(initialComponentsCost);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11918, 11917, 11916})
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
        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(processGroupEnum.getProcessGroup())
                .build()));
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly);

        double initialTotalCost = evaluatePage.getCostResults("Total Cost");
        double initialComponentsCost = evaluatePage.getCostResults("Components Cost");

        componentsTreePage = evaluatePage.openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.EXCLUDE);

        subComponentNames.forEach(componentName ->
            assertThat(componentsTreePage.isTextDecorationStruckOut(componentName), is(true)));

        evaluatePage = componentsTreePage.closePanel()
            .costScenario();

        double modifiedTotalCost = evaluatePage.getCostResults("Total Cost");
        double modifiedComponentsCost = evaluatePage.getCostResults("Components Cost");

        softAssertions.assertThat(initialTotalCost).isGreaterThan(modifiedTotalCost);
        softAssertions.assertThat(initialComponentsCost).isGreaterThan(modifiedComponentsCost);

        softAssertions.assertAll();
    }

    @Test
    @Issues({@Issue("AP-74028")})
    @TestRail(id = {12135, 12052, 12138})
    @Description("Missing sub-component automatically included on update - test with alternate CAD file for Assembly with additional components not on system")
    public void testMissingSubcomponentIncludedOnUpdate() {
        String assemblyName = "autobotasm";
        final String assemblyExtension = ".asm.2";

        final ProcessGroupEnum assemblyProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;
        assemblyResourceFile = FileResourceUtil.getCloudFile(assemblyProcessGroupEnum, assemblyName + assemblyExtension);

        List<String> subComponentNames = Arrays.asList(
            "autoparthead", "autoparttorso", "autopartarm", "autoparthand", "autopartleg", "autopartfoot");
        String componentName = "autoparthelm";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        final String componentExtension = ".prt.1";
        componentResourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + componentExtension);

        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        assemblyInfo = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroupEnum,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(assemblyInfo)
            .uploadAssembly(assemblyInfo);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(assemblyInfo)
            .openComponents();

        subComponentNames.forEach(component ->
            softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component)).isTrue());

        componentsTreePage.closePanel()
            .clickActions()
            .updateCadFile(assemblyResourceFile)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 5)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(componentName)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(componentName)).isEqualTo(true);

        componentsTreePage.clickScenarioCheckbox(componentName)
            .updateCadFile(componentResourceFile)
            .closePanel()
            .clickRefresh(ComponentsTreePage.class);

        List<ScenarioItem> autoHelmDetails = componentsUtil.getUnCostedComponent(componentName, scenarioName, currentUser);

        ComponentInfoBuilder helmInfo = ComponentInfoBuilder.builder()
            .scenarioName(scenarioName)
            .scenarioIdentity(autoHelmDetails.get(0).getScenarioIdentity())
            .componentIdentity(autoHelmDetails.get(0).getComponentIdentity())
            .componentName(componentName)
            .user(currentUser)
            .build();

        assemblyInfo.getSubComponents().add(helmInfo);

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(componentName)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @Issue("2657")
    @TestRail(id = {11099})
    @Description("Validate  the set inputs button cannot be selected when sub assemblies and parts are selected")
    public void testInputsDisabledPartsSubassemblies() {

        List<String> subSubComponentNames = Arrays.asList("3570823", "3571050");
        List<String> subAssemblyComponentNames = Arrays.asList("3570824", "0200613", "0362752");

        final String componentExtension = ".prt.1";
        final String assemblyExtension = ".asm.1";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        componentAssembly1 = assemblyUtils.uploadsAndOpenAssembly(SUB_SUB_ASSEMBLY, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subSubComponentNames,
            componentExtension, processGroupEnum, scenarioName, currentUser);

        componentAssembly2 = assemblyUtils.uploadsAndOpenAssembly(SUB_ASSEMBLY, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subAssemblyComponentNames,
            componentExtension, processGroupEnum, scenarioName, currentUser);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly2)
            .openComponents()
            .multiSelectSubcomponents(SUB_SUB_ASSEMBLY + "," + scenarioName, "0200613" + "," + scenarioName);

        assertThat(componentsTreePage.isSetInputsEnabled(), is(equalTo(false)));
    }
}

