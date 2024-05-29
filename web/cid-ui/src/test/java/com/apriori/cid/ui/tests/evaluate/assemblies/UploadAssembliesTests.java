package com.apriori.cid.ui.tests.evaluate.assemblies;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ASSEMBLY;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.ui.pageobjects.common.ConfigurePage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColourEnum;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.ComponentIconEnum;
import com.apriori.cid.ui.utils.DirectionEnum;
import com.apriori.cid.ui.utils.MultiUpload;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.UnitsEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Tag(ASSEMBLY)
public class UploadAssembliesTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ComponentsTablePage componentsTablePage;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ExplorePage explorePage;
    private ConfigurePage configurePage;
    private ComponentsTreePage componentsTreePage;
    private ComponentInfoBuilder componentAssembly;
    private ComponentInfoBuilder autoSword;

    public UploadAssembliesTests() {
        super();
    }

    @AfterEach
    public void cleanUp() {
        Arrays.asList(autoSword, componentAssembly).forEach(assembly -> {
            if (assembly != null) {
                assemblyUtils.deleteAssemblyAndComponents(assembly);
            }
        });
    }

    @Test
    @TestRail(id = {11908, 11905, 10764, 11867, 11906, 10765, 11868, 11907, 10766})
    @Description("Upload multiple Assemblies of differing platforms")
    public void testMultipleAssemblyUpload() {
        ComponentInfoBuilder inventorAsm = new AssemblyRequestUtil().getAssembly("Assembly01");
        ComponentInfoBuilder nxAsm = new AssemblyRequestUtil().getAssembly("v6 piston assembly_asm1");
        ComponentInfoBuilder solidedgeAsm = new AssemblyRequestUtil().getAssembly("oldham");
        nxAsm.setUser(inventorAsm.getUser());
        nxAsm.setScenarioNameForAll(inventorAsm.getScenarioName());
        solidedgeAsm.setUser(inventorAsm.getUser());
        solidedgeAsm.setScenarioNameForAll(inventorAsm.getScenarioName());

        List<MultiUpload> assembliesMultiBatch = new ArrayList<>();
        inventorAsm.getSubComponents().forEach(component -> assembliesMultiBatch.add(new MultiUpload(component.getResourceFile(), component.getScenarioName())));
        assembliesMultiBatch.add(new MultiUpload(inventorAsm.getResourceFile(), inventorAsm.getScenarioName()));
        nxAsm.getSubComponents().forEach(component -> assembliesMultiBatch.add(new MultiUpload(component.getResourceFile(), component.getScenarioName())));
        assembliesMultiBatch.add(new MultiUpload(nxAsm.getResourceFile(), nxAsm.getScenarioName()));
        solidedgeAsm.getSubComponents().forEach(component -> assembliesMultiBatch.add(new MultiUpload(component.getResourceFile(), component.getScenarioName())));
        assembliesMultiBatch.add(new MultiUpload(solidedgeAsm.getResourceFile(), inventorAsm.getScenarioName()));

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(inventorAsm.getUser())
            .importCadFile()
            .inputMultiComponents(assembliesMultiBatch)
            .inputDefaultScenarioName(inventorAsm.getScenarioName())
            .submit()
            .clickClose()
            .openComponent(inventorAsm.getComponentName(), inventorAsm.getScenarioName(), inventorAsm.getUser())
            .openComponents();

        inventorAsm.getSubComponents().forEach(component ->
            softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.getComponentName().toUpperCase()))
                .as("Verify " + component.getComponentName() + " displayed for assembly " + inventorAsm.getComponentName()).isTrue());

        componentsTreePage.closePanel()
            .clickExplore()
            .openComponent(nxAsm.getComponentName(), nxAsm.getScenarioName(), nxAsm.getUser())
            .openComponents();

        nxAsm.getSubComponents().forEach(component ->
            softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.getComponentName().toUpperCase()))
                .as("Verify " + component.getComponentName() + " displayed for assembly " + nxAsm.getComponentName()).isTrue());

        componentsTreePage.closePanel()
            .clickExplore()
            .openComponent(solidedgeAsm.getComponentName(), solidedgeAsm.getScenarioName(), solidedgeAsm.getUser())
            .openComponents();

        solidedgeAsm.getSubComponents().forEach(component ->
            softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.getComponentName().toUpperCase()))
                .as("Verify " + component.getComponentName() + " displayed for assembly " + solidedgeAsm.getComponentName()).isTrue());

        componentsTreePage.clickScenarioCheckbox(solidedgeAsm.getSubComponents().get(0).getComponentName(), solidedgeAsm.getScenarioName());

        softAssertions.assertThat(
            componentsTreePage.getCellColour(solidedgeAsm.getSubComponents().get(0).getComponentName(), solidedgeAsm.getScenarioName()))
            .as("Verify sub-component is selected in Tree View").isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        componentsTablePage = componentsTreePage.selectTableView();

        softAssertions.assertThat(componentsTablePage.getCellColour(solidedgeAsm.getSubComponents().get(0).getComponentName(), solidedgeAsm.getScenarioName()))
            .as("Verify sub-component remains selected in Table View").isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        componentsTablePage.clickScenarioCheckbox(solidedgeAsm.getSubComponents().get(1).getComponentName(), solidedgeAsm.getScenarioName());

        componentsTreePage = componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getCellColour(solidedgeAsm.getSubComponents().get(1).getComponentName(), solidedgeAsm.getScenarioName()))
            .as("Verify second selected sub-component remains selected in Tree View").isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {5620, 6513, 6514})
    @Description("User can upload an assembly when the same assembly with same scenario name exists in the public workspace")
    public void uploadAnAssemblyExistingInThePublicWorkspace() {
        ComponentInfoBuilder componentAssembly1 = new AssemblyRequestUtil().getAssembly("Hinge assembly");
        ComponentInfoBuilder componentAssembly2 = new AssemblyRequestUtil().getAssembly("Hinge assembly");
        componentAssembly2.setUser(componentAssembly1.getUser());
        componentAssembly2.setScenarioNameForAll(componentAssembly1.getScenarioName());

        assemblyUtils.uploadSubComponents(componentAssembly1).uploadAssembly(componentAssembly1);
        assemblyUtils.costSubComponents(componentAssembly1).costAssembly(componentAssembly1);
        assemblyUtils.publishSubComponents(componentAssembly1).publishAssembly(componentAssembly1);

        List<MultiUpload> secondAssemblyBatch = new ArrayList<>();
        componentAssembly2.getSubComponents().forEach(component -> secondAssemblyBatch.add(new MultiUpload(component.getResourceFile(), component.getScenarioName())));
        secondAssemblyBatch.add(new MultiUpload(componentAssembly2.getResourceFile(), componentAssembly2.getScenarioName()));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(componentAssembly1.getUser())
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentAssembly1.getComponentName(), componentAssembly1.getScenarioName()))
            .as("Verify Published Assembly displayed in Public Filter").isEqualTo(1);
        softAssertions.assertThat(explorePage.getRowDetails(componentAssembly1.getComponentName(), componentAssembly1.getScenarioName()))
            .as("Verify assembly shown as Assembly type").contains(ComponentIconEnum.ASSEMBLY.getIcon());

        explorePage.highlightScenario(componentAssembly1.getComponentName(), componentAssembly1.getScenarioName());
        softAssertions.assertThat(explorePage.openPreviewPanel().isImageDisplayed()).isEqualTo(true);

        explorePage.importCadFile()
            .inputMultiComponents(secondAssemblyBatch)
            .inputDefaultScenarioName(componentAssembly2.getScenarioName())
            .submit()
            .clickClose()
            .selectFilter("Private");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentAssembly2.getComponentName(), componentAssembly2.getScenarioName()))
            .as("Verify newly uploaded assembly using same scenario name is present in Private filter").isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 5621)
    @Description("Validate sub components such as bolts or screws can exist in multiple assemblies")
    public void uploadAnAssemblyThatIsPartOfAnotherAssembly() {
        ComponentInfoBuilder assembly1 = new AssemblyRequestUtil().getAssembly("titan battery ass");
        List<MultiUpload> firstAssemblyBatch = new ArrayList<>();
        assembly1.getSubComponents().forEach(component -> firstAssemblyBatch.add(new MultiUpload(component.getResourceFile(), component.getScenarioName())));
        firstAssemblyBatch.add(new MultiUpload(assembly1.getResourceFile(), assembly1.getScenarioName()));

        ComponentInfoBuilder assembly2 = new AssemblyRequestUtil().getAssembly("titan charger ass");
        assembly2.setUser(assembly1.getUser());
        assembly2.setScenarioNameForAll(assembly1.getScenarioName());

        ComponentInfoBuilder assembly3 = new AssemblyRequestUtil().getAssembly("titan cordless drill 2");
        assembly3.setUser(assembly1.getUser());
        assembly3.setScenarioNameForAll(assembly1.getScenarioName());

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(assembly1.getUser())
            .importCadFile()
            .inputDefaultScenarioName(assembly1.getScenarioName())
            .inputMultiComponents(firstAssemblyBatch)
            .submit()
            .clickClose();

        assembly2.getSubComponents().removeIf(sub -> sub.getComponentName().equals(assembly1.getComponentName()));
        assemblyUtils.uploadSubComponents(assembly2).uploadAssembly(assembly2);

        assembly3.getSubComponents().removeIf(sub -> sub.getComponentName().equals(assembly1.getComponentName()));
        assemblyUtils.uploadSubComponents(assembly3).uploadAssembly(assembly3);

        componentsTreePage = explorePage.openComponent(assembly2.getComponentName(), assembly2.getScenarioName(), assembly2.getUser())
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(assembly1.getComponentName()))
            .as("Verify that first assembly is displayed as sub-component").isEqualTo(true);

        componentsTreePage = explorePage.openComponent(assembly3.getComponentName(), assembly3.getScenarioName(), assembly3.getUser())
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(assembly1.getComponentName()))
            .as("Verify that first assembly is displayed as sub-component").isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12156, 6557, 6524})
    @Description("Column Configuration button in Tree View is clickable and opens menu")
    public void testColumnConfigurationButton() {
        componentAssembly = new AssemblyRequestUtil().getAssembly("titan charger ass");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        configurePage = loginPage.login(componentAssembly.getUser())
            .openComponent(componentAssembly.getComponentName(), componentAssembly.getScenarioName(), componentAssembly.getUser())
            .openComponents()
            .configure();

        softAssertions.assertThat(configurePage.getChoicesList()).containsAnyOf("Assigned At", "Assigned By", "Cost Maturity");
        softAssertions.assertThat(configurePage.getChosenList()).containsAnyOf("Component Name", "Scenario Name", "Component Type");

        componentsTreePage = configurePage.selectColumn(ColumnsEnum.NOTES)
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ComponentsTreePage.class);

        softAssertions.assertThat(componentsTreePage.getTableHeaders()).contains(ColumnsEnum.NOTES.getColumns());

        componentsTreePage.configure()
            .selectColumn(ColumnsEnum.PUBLISHED)
            .moveColumn(DirectionEnum.RIGHT)
            .selectColumn(ColumnsEnum.LOCKED)
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ComponentsTreePage.class);

        softAssertions.assertThat(componentsTreePage.getTableHeaders()).contains(ColumnsEnum.PUBLISHED.getColumns());
        softAssertions.assertThat(componentsTreePage.getTableHeaders()).contains(ColumnsEnum.LOCKED.getColumns());

        componentsTablePage = componentsTreePage.selectTableView()
            .configure()
            .selectColumn(ColumnsEnum.PUBLISHED)
            .moveColumn(DirectionEnum.LEFT)
            .selectColumn(ColumnsEnum.LOCKED)
            .moveColumn(DirectionEnum.LEFT)
            .submit(ComponentsTablePage.class);

        softAssertions.assertThat(componentsTablePage.getTableHeaders()).doesNotContain(ColumnsEnum.PUBLISHED.getColumns());
        softAssertions.assertThat(componentsTablePage.getTableHeaders()).doesNotContain(ColumnsEnum.LOCKED.getColumns());

        componentsTreePage = componentsTablePage.selectTreeView()
            .configure()
            .selectChoices()
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ComponentsTreePage.class);

        softAssertions.assertThat(componentsTreePage.getTableHeaders()).contains(ColumnsEnum.PROCESS_ROUTING.getColumns());
        softAssertions.assertThat(componentsTreePage.getTableHeaders()).contains(ColumnsEnum.LABOR_COST.getColumns());
        softAssertions.assertThat(componentsTreePage.getTableHeaders()).contains(ColumnsEnum.FINISH_MASS.getColumns());
        softAssertions.assertThat(componentsTreePage.getTableHeaders()).contains(ColumnsEnum.BATCH_SIZE.getColumns());

        componentsTreePage.configure()
            .selectChosen()
            .moveColumn(DirectionEnum.LEFT);

        softAssertions.assertThat((configurePage.isSubmitButtonDisabled())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12139, 12101, 12136})
    @Description("Column configuration in Tree View with All filter does not affect column configuration in List View")
    public void testColumnConfigurationListView() {
        componentAssembly = new AssemblyRequestUtil().getAssembly("titan charger ass");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .openComponent(componentAssembly.getComponentName(), componentAssembly.getScenarioName(), componentAssembly.getUser())
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario(3)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Medium")
            .editDescription("Test Description")
            .editNotes("Test Notes")
            .submit(EvaluatePage.class)
            .clickExplore()
            .openComponent(componentAssembly.getComponentName(), componentAssembly.getScenarioName(), componentAssembly.getUser())
            .openComponents()
            .configure()
            .selectChoices()
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ComponentsTreePage.class);

        softAssertions.assertThat(componentsTreePage.getRowDetails(componentAssembly.getComponentName(), componentAssembly.getScenarioName())).contains("High", "Test Description", "Test Notes",
            "New", "Casting - Die", "Melting / High Pressure Die Casting / Trim / 3 Axis Mill");

        componentsTablePage = componentsTreePage.selectTableView()
            .configure()
            .selectChosen()
            .moveColumn(DirectionEnum.LEFT)
            .selectColumn(ColumnsEnum.COMPONENT_NAME)
            .moveColumn(DirectionEnum.RIGHT)
            .selectColumn(ColumnsEnum.CAD_CONNECTED)
            .moveColumn(DirectionEnum.RIGHT)
            .selectColumn(ColumnsEnum.LOCKED)
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ComponentsTablePage.class);

        softAssertions.assertThat(componentsTablePage.isElementDisplayed("All", "text-overflow")).isEqualTo(true);

        softAssertions.assertThat(componentsTablePage.getTableHeaders()).doesNotContain(ColumnsEnum.MATERIAL_NAME.getColumns());
        softAssertions.assertThat(componentsTablePage.getTableHeaders()).doesNotContain(ColumnsEnum.PIECE_PART_COST.getColumns());
        softAssertions.assertThat(componentsTablePage.getTableHeaders()).contains(ColumnsEnum.COMPONENT_NAME.getColumns());
        softAssertions.assertThat(componentsTablePage.getTableHeaders()).contains(ColumnsEnum.CAD_CONNECTED.getColumns());
        softAssertions.assertThat(componentsTablePage.getTableHeaders()).contains(ColumnsEnum.LOCKED.getColumns());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6546})
    @Description("Changing unit user preferences when viewing assembly")
    public void testUnitPreferenceInAssembly() {
        componentAssembly = new AssemblyRequestUtil().getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openSettings()
            .selectUnits(UnitsEnum.FPM)
            .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getFinishMass()).isEqualTo("0.25lb");

        evaluatePage.openSettings()
            .selectUnits(UnitsEnum.MMKS)
            .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getFinishMass()).isEqualTo("0.11kg");
    }

    @Test
    @TestRail(id = {6564})
    @Description("Assembly costs with multiple quantity of parts")
    public void costAssemblyWithMultipleQuantityOfParts() {
        componentAssembly = new AssemblyRequestUtil().getAssembly("flange c");
        ComponentInfoBuilder subcomponentA = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("flange")).findFirst().get();
        ComponentInfoBuilder subcomponentB = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("nut")).findFirst().get();
        ComponentInfoBuilder subcomponentC = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("bolt")).findFirst().get();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .uploadComponentAndOpen(subcomponentA)
            .selectProcessGroup(subcomponentA.getProcessGroup())
            .costScenario()
            .uploadComponentAndOpen(subcomponentB)
            .selectProcessGroup(subcomponentB.getProcessGroup())
            .costScenario()
            .uploadComponentAndOpen(subcomponentC)
            .selectProcessGroup(subcomponentC.getProcessGroup())
            .costScenario()
            .uploadComponentAndOpen(componentAssembly)
            .selectProcessGroup(componentAssembly.getProcessGroup())
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        softAssertions.assertThat(evaluatePage.getComponentResults("Total")).isEqualTo(10);
        softAssertions.assertThat(evaluatePage.getComponentResults("Unique")).isEqualTo(3);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 5625)
    @Description("Validate missing sub-assembly association when manually uploaded")
    public void verifySubAsmAssociationOnUpload() {
        componentAssembly = new AssemblyRequestUtil().getAssembly("autobotasm");
        autoSword = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("autosword")).findFirst().get();
        componentAssembly.setSubComponents(componentAssembly.getSubComponents().stream().filter(o -> !o.getComponentName().equalsIgnoreCase("autosword")).collect(Collectors.toList()));

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentsTreePage = new CidAppLoginPage(driver).login(componentAssembly.getUser())
            .openScenario(componentAssembly.getComponentName(), componentAssembly.getScenarioName())
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(autoSword.getComponentName())).as("Verify Missing Sub-Assembly is struck out").isTrue();
        softAssertions.assertThat(componentsTreePage.getRowDetails(autoSword.getComponentName(), autoSword.getScenarioName()).contains(StatusIconEnum.DISCONNECTED.getStatusIcon()))
            .as("Verify sub-assembly is shown as CAD disconnected").isTrue();

        explorePage = componentsTreePage.closePanel()
            .importCadFile()
            .inputDefaultScenarioName(componentAssembly.getScenarioName())
            .enterFilePath(autoSword.getResourceFile())
            .submit()
            .clickClose();

        componentsTreePage = explorePage.openScenario(componentAssembly.getComponentName(), componentAssembly.getScenarioName())
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(autoSword.getComponentName())).as("Verify Sub-Assembly is no longer struck out").isFalse();
        softAssertions.assertThat(componentsTreePage.getRowDetails(autoSword.getComponentName(), componentAssembly.getScenarioName()).contains(StatusIconEnum.CAD.getStatusIcon()))
            .as("Verify sub-assembly is shown as CAD connected").isTrue();
        softAssertions.assertThat(componentsTreePage.getRowDetails(autoSword.getComponentName(), componentAssembly.getScenarioName())).as("Verify 'missing' sub-asm overridden").isNotEmpty();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10748, 10761})
    @Description("Changing unit user preferences when viewing assembly")
    public void testCopyScenarioRetainsAsmAssociation() {
        final String newScenarioNamePrivateEvaluate = new GenerateStringUtil().generateScenarioName();
        final String newScenarioNamePrivateExplore = new GenerateStringUtil().generateScenarioName();
        final String newScenarioNamePublicEvaluate = new GenerateStringUtil().generateScenarioName();
        final String newScenarioNamePublicExplore = new GenerateStringUtil().generateScenarioName();

        componentAssembly = new AssemblyRequestUtil().getAssembly("Hinge assembly");
        ComponentInfoBuilder bigRing = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("big_ring")).findFirst().get();
        ComponentInfoBuilder pin = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("pin")).findFirst().get();
        ComponentInfoBuilder smallRing = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("small_ring")).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(componentAssembly.getSubComponents().get(0).getProcessGroup().getProcessGroup())
                .build()));

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        List<String> bigRingDetails = componentsTreePage.getRowDetails(bigRing.getComponentName(), bigRing.getScenarioName());
        List<String> smallRingDetails = componentsTreePage.getRowDetails(smallRing.getComponentName(), smallRing.getScenarioName());
        List<String> pinDetails = componentsTreePage.getRowDetails(pin.getComponentName(), pin.getScenarioName());

        evaluatePage = componentsTreePage.closePanel()
            .copyScenario()
            .enterScenarioName(newScenarioNamePrivateEvaluate)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2);

        softAssertions.assertThat(evaluatePage.getCurrentScenarioName()).as("Verify new Scenario Name From Evaluate").isEqualTo(newScenarioNamePrivateEvaluate);

        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing.getComponentName(), bigRing.getScenarioName())).as("Verify sub-component details").isEqualTo(bigRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing.getComponentName(), smallRing.getScenarioName())).as("Verify sub-component details").isEqualTo(smallRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin.getComponentName(), pin.getScenarioName())).as("Verify sub-component details").isEqualTo(pinDetails);

        evaluatePage = componentsTreePage.closePanel()
            .clickExplore()
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .copyScenario()
            .enterScenarioName(newScenarioNamePrivateExplore)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2);

        softAssertions.assertThat(evaluatePage.getCurrentScenarioName()).as("Verify new Scenario Name from Explore").isEqualTo(newScenarioNamePrivateExplore);

        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing.getComponentName(), bigRing.getScenarioName())).as("Verify sub-component details").isEqualTo(bigRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing.getComponentName(), smallRing.getScenarioName())).as("Verify sub-component details").isEqualTo(smallRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin.getComponentName(), pin.getScenarioName())).as("Verify sub-component details").isEqualTo(pinDetails);

        evaluatePage = componentsTreePage.closePanel()
            .clickExplore()
            .openScenario(componentAssembly.getComponentName(), componentAssembly.getScenarioName())
            .openComponents()
            .selectCheckAllBox()
            .publishSubcomponent()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(EvaluatePage.class);

        componentAssembly.getSubComponents().forEach(subComponent -> scenariosUtil.getScenarioCompleted(subComponent));

        evaluatePage.clickRefresh(EvaluatePage.class);

        componentsTreePage = evaluatePage.openComponents();
        bigRingDetails = componentsTreePage.getRowDetails(bigRing.getComponentName(), bigRing.getScenarioName());
        smallRingDetails = componentsTreePage.getRowDetails(smallRing.getComponentName(), smallRing.getScenarioName());
        pinDetails = componentsTreePage.getRowDetails(pin.getComponentName(), pin.getScenarioName());

        evaluatePage = componentsTreePage.closePanel()
            .copyScenario()
            .enterScenarioName(newScenarioNamePublicEvaluate)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2);

        softAssertions.assertThat(evaluatePage.getCurrentScenarioName()).as("Verify new Scenario Name From Evaluate").isEqualTo(newScenarioNamePublicEvaluate);

        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing.getComponentName(), bigRing.getScenarioName())).as("Verify sub-component details").isEqualTo(bigRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing.getComponentName(), smallRing.getScenarioName())).as("Verify sub-component details").isEqualTo(smallRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin.getComponentName(), pin.getScenarioName())).as("Verify sub-component details").isEqualTo(pinDetails);

        evaluatePage = componentsTreePage.closePanel()
            .clickExplore()
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .copyScenario()
            .enterScenarioName(newScenarioNamePublicExplore)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2);

        softAssertions.assertThat(evaluatePage.getCurrentScenarioName()).as("Verify new Scenario Name from Explore").isEqualTo(newScenarioNamePublicExplore);

        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing.getComponentName(), bigRing.getScenarioName())).as("Verify sub-component details").isEqualTo(bigRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing.getComponentName(), smallRing.getScenarioName())).as("Verify sub-component details").isEqualTo(smallRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin.getComponentName(), pin.getScenarioName())).as("Verify sub-component details").isEqualTo(pinDetails);

        softAssertions.assertAll();
    }
}