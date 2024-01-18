package com.apriori.cid.ui.tests.evaluate.assemblies;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ButtonTypeEnum;
import com.apriori.cid.ui.utils.ColourEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

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
        ComponentInfoBuilder assembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(assembly.getUser())
            .navigateToScenario(assembly)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11150)
    @Description("Exclude all sub-components from top-level assembly")
    public void testExcludeButtons() {
        ComponentInfoBuilder assembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(assembly.getUser())
            .navigateToScenario(assembly)
            .openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.EXCLUDE);

        assembly.getSubComponents().forEach(componentName ->
            assertThat(componentsTreePage.isTextDecorationStruckOut(componentName.toString()), is(true)));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11874, 11843, 11842, 11155, 11148})
    @Description("Verify Include and Exclude buttons disabled if mixture selected")
    public void testIncludeAndExcludeDisabledButtonsWithMixedSelections() {
        ComponentInfoBuilder assembly = new AssemblyRequestUtil().getAssembly("Hinge assembly");
        ComponentInfoBuilder pin = assembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("pin")).findFirst().get();
        ComponentInfoBuilder smallRing = assembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("small ring")).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(assembly.getUser())
            .navigateToScenario(assembly)
            .openComponents()
            .multiSelectSubcomponents(pin.getComponentName() + "," + assembly.getScenarioName())
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .multiSelectSubcomponents(smallRing.getComponentName() + "," + assembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(true);

        componentsTreePage.multiSelectSubcomponents(pin.getComponentName() + "," + assembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        componentsTreePage.multiSelectSubcomponents(smallRing.getComponentName() + "," + assembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        componentsTreePage.multiSelectSubcomponents(smallRing.getComponentName() + "," + assembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11153, 11152, 11151})
    @Description("Include all sub-components from top-level assembly")
    public void testIncludeButtonEnabledWithCostedComponents() {
        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .selectCheckAllBox();

        assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE), is(true));

        componentsTreePage.selectButtonType(ButtonTypeEnum.INCLUDE);

        componentAssembly.getSubComponents().forEach(component ->
            assertThat(componentsTreePage.isTextDecorationStruckOut(component.getComponentName()), is(false)));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11150, 11149, 11156})
    @Description("Include all sub-components from top-level assembly")
    public void testExcludeButtonEnabledWithCostedComponents() {
        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectCheckAllBox();

        assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE), is(true));

        componentsTreePage.selectButtonType(ButtonTypeEnum.EXCLUDE);

        componentAssembly.getSubComponents().forEach(component ->
            assertThat(componentsTreePage.isTextDecorationStruckOut(component.getComponentName()), is(true)));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {12089, 6554})
    @Description("Verify Excluded scenarios are not highlighted in flattened view")
    public void testExcludedScenarioInFlattenedView() {
        ComponentInfoBuilder assembly = new AssemblyRequestUtil().getAssembly("Hinge assembly");
        ComponentInfoBuilder pin = assembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("pin")).findFirst().get();
        ComponentInfoBuilder smallRing = assembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("small ring")).findFirst().get();

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(assembly.getUser())
            .navigateToScenario(assembly)
            .openComponents()
            .multiSelectSubcomponents(pin.getComponentName() + ", " + pin.getScenarioName(), smallRing.getComponentName() + "," + smallRing.getScenarioName())
            .selectButtonType(ButtonTypeEnum.EXCLUDE);

        componentsTablePage = componentsTreePage.selectTableView();

        softAssertions.assertThat(componentsTablePage.getCellColour(pin.getComponentName(), pin.getScenarioName())).isEqualTo(ColourEnum.YELLOW_LIGHT.getColour());
        softAssertions.assertThat(componentsTablePage.getCellColour(smallRing.getComponentName(), smallRing.getScenarioName())).isEqualTo(ColourEnum.YELLOW_LIGHT.getColour());

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11921, 11920, 11919})
    @Description("Include all sub-components from top-level assembly")
    public void testIncludeSubcomponentsAndCost() {

        componentAssembly = new AssemblyRequestUtil().getAssembly("flange c");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentAssembly.getSubComponents()
            .forEach(subComponent -> subComponent.setCostingTemplate(
                CostingTemplate.builder()
                    .processGroupName(componentAssembly.getSubComponents().get(0).getProcessGroup().getProcessGroup())
                    .build())
            );

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
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

        componentAssembly.getSubComponents().forEach(component ->
            assertThat(componentsTreePage.isTextDecorationStruckOut(component.getComponentName()), is(false)));

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
        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(componentAssembly.getSubComponents().get(0).getProcessGroup().getProcessGroup())
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

        componentAssembly.getSubComponents().forEach(component ->
            assertThat(componentsTreePage.isTextDecorationStruckOut(component.getComponentName()), is(true)));

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
            .openComponents();

        List<ScenarioItem> autoHelmDetails = componentsUtil.getUnCostedComponent(componentName, scenarioName, currentUser);

        ComponentInfoBuilder helmInfo = ComponentInfoBuilder.builder()
            .scenarioName(scenarioName)
            .scenarioIdentity(autoHelmDetails.get(0).getScenarioIdentity())
            .componentIdentity(autoHelmDetails.get(0).getComponentIdentity())
            .componentName(componentName)
            .user(currentUser)
            .build();

        assemblyInfo.getSubComponents().add(helmInfo);

        componentsTreePage = componentsTreePage.closePanel()
            .clickRefresh(ComponentsTreePage.class);

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

