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

import java.util.List;

public class IncludeAndExcludeTests extends TestBaseUI {

    private static ComponentsUtil componentsUtil = new ComponentsUtil();
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ComponentInfoBuilder componentAssembly;
    private CidAppLoginPage loginPage;
    private ComponentsTreePage componentsTreePage;
    private EvaluatePage evaluatePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    public IncludeAndExcludeTests() {
        super();
    }

    @AfterEach
    public void deleteScenarios() {
        if (componentAssembly != null) {
            assemblyUtils.deleteAssemblyAndComponents(componentAssembly);
        }
    }

    @Test
    @TestRail(id = 11154)
    @Description("Include and Exclude buttons disabled by default")
    public void testIncludeAndExcludeDisabledButtons() {
        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11150)
    @Description("Exclude all sub-components from top-level assembly")
    public void testExcludeButtons() {
        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectCheckAllBox()
            .selectButtonType(ButtonTypeEnum.EXCLUDE);

        componentAssembly.getSubComponents().forEach(componentName ->
            assertThat(componentsTreePage.isTextDecorationStruckOut(componentName.toString()), is(true)));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11874, 11843, 11842, 11155, 11148})
    @Description("Verify Include and Exclude buttons disabled if mixture selected")
    public void testIncludeAndExcludeDisabledButtonsWithMixedSelections() {
        componentAssembly = new AssemblyRequestUtil().getAssembly("Hinge assembly");
        ComponentInfoBuilder pin = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("pin")).findFirst().get();
        ComponentInfoBuilder smallRing = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("small ring")).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(pin.getComponentName() + "," + componentAssembly.getScenarioName())
            .selectButtonType(ButtonTypeEnum.EXCLUDE)
            .multiSelectSubcomponents(smallRing.getComponentName() + "," + componentAssembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(true);

        componentsTreePage.multiSelectSubcomponents(pin.getComponentName() + "," + componentAssembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(false);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        componentsTreePage.multiSelectSubcomponents(smallRing.getComponentName() + "," + componentAssembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.INCLUDE)).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EXCLUDE)).isEqualTo(false);

        componentsTreePage.multiSelectSubcomponents(smallRing.getComponentName() + "," + componentAssembly.getScenarioName());

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
        componentAssembly = new AssemblyRequestUtil().getAssembly("Hinge assembly");
        ComponentInfoBuilder pin = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("pin")).findFirst().get();
        ComponentInfoBuilder smallRing = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("small ring")).findFirst().get();

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(pin.getComponentName() + ", " + pin.getScenarioName(), smallRing.getComponentName() + "," + smallRing.getScenarioName())
            .selectButtonType(ButtonTypeEnum.EXCLUDE);

        ComponentsTablePage componentsTablePage = componentsTreePage.selectTableView();

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
        evaluatePage = loginPage.login(componentAssembly.getUser())
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
    @Issues( {@Issue("AP-74028")})
    @TestRail(id = {12135, 12052, 12138})
    @Description("Missing sub-component automatically included on update - test with alternate CAD file for Assembly with additional components not on system")
    public void testMissingSubcomponentIncludedOnUpdate() {
        ComponentInfoBuilder componentAssembly = new AssemblyRequestUtil().getAssembly();
        ComponentInfoBuilder excludedComponent = componentAssembly.getSubComponents().remove(componentAssembly.getSubComponents().size() - 1);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents();

        componentAssembly.getSubComponents().forEach(component ->
            softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.getComponentName())).isTrue());

        componentsTreePage.closePanel()
            .clickActions()
            .updateCadFile(componentAssembly.getResourceFile())
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_UPDATE_CAD, 5)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(excludedComponent.getComponentName())).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(excludedComponent.getComponentName())).isEqualTo(true);

        componentsTreePage.clickScenarioCheckbox(excludedComponent.getComponentName())
            .updateCadFile(excludedComponent.getResourceFile())
            .closePanel()
            .openComponents();

        List<ScenarioItem> excludeComponentDetails = componentsUtil.getUnCostedComponent(excludedComponent.getComponentName(), excludedComponent.getScenarioName(), excludedComponent.getUser());

        ComponentInfoBuilder excludedComponentInfo = ComponentInfoBuilder.builder()
            .componentName(excludedComponent.getComponentName())
            .scenarioName(excludedComponent.getScenarioName())
            .scenarioIdentity(excludeComponentDetails.get(0).getScenarioIdentity())
            .componentIdentity(excludeComponentDetails.get(0).getComponentIdentity())
            .user(excludedComponent.getUser())
            .build();

        componentAssembly.getSubComponents().add(excludedComponentInfo);

        componentsTreePage = componentsTreePage.closePanel()
            .clickRefresh(ComponentsTreePage.class);

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(excludedComponent.getComponentName())).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @Issue("2657")
    @TestRail(id = {11099})
    @Description("Validate  the set inputs button cannot be selected when sub assemblies and parts are selected")
    public void testInputsDisabledPartsSubassemblies() {
        componentAssembly = new AssemblyRequestUtil().getAssembly("sub-assembly");
        ComponentInfoBuilder subSubAssembly = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("sub-sub-asm")).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(subSubAssembly)
            .openComponents()
            .multiSelectSubcomponents(subSubAssembly.getComponentName() + "," + subSubAssembly.getScenarioName(),
                subSubAssembly.getSubComponents().get(0).getComponentName() + "," + subSubAssembly.getScenarioName());

        assertThat(componentsTreePage.isSetInputsEnabled(), is(equalTo(false)));
    }
}

