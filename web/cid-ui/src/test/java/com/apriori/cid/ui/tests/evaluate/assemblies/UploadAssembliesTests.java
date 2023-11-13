package com.apriori.cid.ui.tests.evaluate.assemblies;

import static com.apriori.shared.util.enums.ProcessGroupEnum.ASSEMBLY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.api.models.dto.AssemblyDTORequest;
import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.ComponentsUtil;
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
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.UnitsEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadAssembliesTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ComponentsTablePage componentsTablePage;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ExplorePage explorePage;
    private ConfigurePage configurePage;
    private ComponentsTreePage componentsTreePage;
    private ComponentInfoBuilder assemblyInfo;
    private ComponentInfoBuilder subAssemblyInfo;

    public UploadAssembliesTests() {
        super();
    }

    @AfterEach
    public void deleteScenarios() {
        if (currentUser != null && assemblyInfo != null) {
            assemblyUtils.deleteAssemblyAndComponents(assemblyInfo);
            assemblyInfo = null;
        }
        if (subAssemblyInfo != null) {
            assemblyUtils.deleteAssemblyAndComponents(subAssemblyInfo);
            subAssemblyInfo = null;
        }
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6511, 10510})
    @Description("Upload Assembly file with no missing sub-components")
    public void uploadAssemblyTest() {
        ComponentInfoBuilder componentAssembly = new AssemblyDTORequest().getAssembly("Hinge assembly");
        ComponentInfoBuilder subcomponentA = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("big ring")).findFirst().get();
        ComponentInfoBuilder subcomponentB = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("Pin")).findFirst().get();
        ComponentInfoBuilder subcomponentC = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("small ring")).findFirst().get();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .uploadComponentAndOpen(subcomponentA.getComponentName(), componentAssembly.getScenarioName(), subcomponentA.getResourceFile(), componentAssembly.getUser())
            .selectProcessGroup(subcomponentA.getProcessGroup())
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        evaluatePage.uploadComponentAndOpen(subcomponentB.getComponentName(), componentAssembly.getScenarioName(), subcomponentB.getResourceFile(), componentAssembly.getUser())
            .selectProcessGroup(subcomponentB.getProcessGroup())
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        evaluatePage.uploadComponentAndOpen(subcomponentC.getComponentName(), componentAssembly.getScenarioName(), subcomponentC.getResourceFile(), componentAssembly.getUser())
            .selectProcessGroup(subcomponentC.getProcessGroup())
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        evaluatePage.uploadComponentAndOpen(componentAssembly.getComponentName(), componentAssembly.getScenarioName(), componentAssembly.getResourceFile(), componentAssembly.getUser())
            .selectProcessGroup(componentAssembly.getProcessGroup())
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        softAssertions.assertThat(evaluatePage.getComponentResults("Total")).isEqualTo(3.0);
        softAssertions.assertThat(evaluatePage.getComponentResults("Unique")).isEqualTo(3.0);
        softAssertions.assertThat(evaluatePage.getComponentResults("Uncosted Unique")).isEqualTo(0.0);

        softAssertions.assertAll();

        //TODO uncomment when BA-2155 is complete
        /*componentsListPage = evaluatePage.openComponents();
        assertThat(componentsListPage.getRowDetails("Small Ring", "Initial"), hasItems("$1.92", "Casting - Die", PART.getIcon(), COSTED.getIcon(), HIGH.getIcon()));
        assertThat(componentsListPage.getRowDetails("Big Ring", "Initial"), hasItems("$2.19", "Casting - Die", PART.getIcon(), COSTED.getIcon(), HIGH.getIcon()));
        assertThat(componentsListPage.getRowDetails("Pin", "Initial"), hasItems("$1.97", "Casting - Die", PART.getIcon(), COSTED.getIcon(), HIGH.getIcon()));*/
    }

    @Test
    @TestRail(id = {11902, 10762, 11861})
    @Description("Upload Assembly with sub-components from Catia")
    public void testCatiaMultiUpload() {
        ComponentInfoBuilder componentAssembly = new AssemblyDTORequest().getAssembly("flange c");

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .uploadAndOpenComponents(componentAssembly);

        componentAssembly.getSubComponents().forEach(subcomponent ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(subcomponent.getComponentName().toUpperCase()), is(true)));
    }

    @Test
    @TestRail(id = {11903, 10767, 6562, 11909})
    @Description("Upload Assembly with sub-components from Creo")
    public void testCreoMultiUpload() {
        ComponentInfoBuilder componentAssembly = new AssemblyDTORequest().getAssembly("piston_assembly");

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .uploadAndOpenComponents(componentAssembly);

        componentAssembly.getSubComponents().forEach(subcomponent ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(subcomponent.getComponentName().toUpperCase()), is(true)));
    }

    @Test
    @TestRail(id = 11904)
    @Description("Upload Assembly with sub-components from Solidworks")
    public void testSolidworksMultiUpload() {
        ComponentInfoBuilder componentAssembly = new AssemblyDTORequest().getAssembly("Hinge assembly");

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .uploadAndOpenComponents(componentAssembly);

        componentAssembly.getSubComponents().forEach(subcomponent ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(subcomponent.getComponentName().toUpperCase()), is(true)));
    }

    @Test
    @TestRail(id = {11905, 10764, 11867})
    @Description("Upload Assembly with sub-components from SolidEdge")
    public void testSolidEdgeMultiUpload() {
        ComponentInfoBuilder componentAssembly = new AssemblyDTORequest().getAssembly("oldham");

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .uploadAndOpenComponents(componentAssembly);

        componentAssembly.getSubComponents().forEach(subcomponent ->
            softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(subcomponent.getComponentName().toUpperCase())).isTrue());

        componentsTreePage.clickScenarioCheckbox("stand", componentAssembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.getCellColour("stand", componentAssembly.getScenarioName())).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        componentsTablePage = componentsTreePage.selectTableView();

        softAssertions.assertThat(componentsTablePage.getCellColour("stand", componentAssembly.getScenarioName())).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11906, 10765, 11868})
    @Description("Upload Assembly with sub-components from NX")
    public void testNxMultiUpload() {
        ComponentInfoBuilder componentAssembly = new AssemblyDTORequest().getAssembly("v6 piston assembly_asm1");

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .uploadAndOpenComponents(componentAssembly);

        componentAssembly.getSubComponents().forEach(subcomponent ->
            softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(subcomponent.getComponentName().toUpperCase())).isTrue());

        componentsTablePage = componentsTreePage.selectTableView()
            .clickScenarioCheckbox("piston_model1", componentAssembly.getScenarioName());

        softAssertions.assertThat(componentsTablePage.getCellColour("piston_model1", componentAssembly.getScenarioName())).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        componentsTreePage = componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getCellColour("piston_model1", componentAssembly.getScenarioName())).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11907, 10766})
    @Description("Upload Assembly with sub-components from Inventor")
    public void testInventorMultiUpload() {
        ComponentInfoBuilder componentAssembly = new AssemblyDTORequest().getAssembly("Assembly01");

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .uploadAndOpenComponents(componentAssembly);

        componentAssembly.getSubComponents().forEach(subcomponent ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(subcomponent.getComponentName().toUpperCase()), is(true)));
    }

    @Test
    @TestRail(id = 11908)
    @Description("Upload multiple Assemblies")
    public void testMultipleAssemblyUpload() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String assemblyName1 = "Assembly01";
        String assemblyName2 = "v6 piston assembly_asm1";
        List<String> componentNames1 = Arrays.asList("Part0001", "Part0002", "Part0003", "Part0004");
        List<String> componentNames2 = Arrays.asList("piston rod_model1", "piston_model1", "piston cover_model1", "piston pin_model1");
        currentUser = UserUtil.getUser();

        List<MultiUpload> firstMultiComponentBatch = new ArrayList<>();
        firstMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0001.ipt"), scenarioName));
        firstMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0002.ipt"), scenarioName));
        firstMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0003.ipt"), scenarioName));
        firstMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0004.ipt"), scenarioName));
        firstMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ASSEMBLY, "Assembly01.iam"), scenarioName));

        List<MultiUpload> secondMultiComponentBatch = new ArrayList<>();
        secondMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston rod_model1.prt"), scenarioName));
        secondMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston_model1.prt"), scenarioName));
        secondMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston cover_model1.prt"), scenarioName));
        secondMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston pin_model1.prt"), scenarioName));
        secondMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ASSEMBLY, "v6 piston assembly_asm1.prt"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .importCadFile()
            .inputMultiComponents(firstMultiComponentBatch)
            .inputMultiComponents(secondMultiComponentBatch)
            .inputScenarioName(scenarioName)
            .submit()
            .clickClose()
            .openComponent(assemblyName1, scenarioName, currentUser)
            .openComponents();

        componentNames1.forEach(component ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));

        componentsTreePage.closePanel()
            .clickExplore()
            .openComponent(assemblyName2, scenarioName, currentUser)
            .openComponents();

        componentNames2.forEach(component ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {5620, 6513, 6514})
    @Description("User can upload an assembly when the same assembly with same scenario name exists in the public workspace")
    public void uploadAnAssemblyExistingInThePublicWorkspace() {
        currentUser = UserUtil.getUser();

        final String assemblyName1 = "Hinge assembly";
        final String assemblyExtension1 = ".SLDASM";
        final List<String> subComponentNames1 = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension1 = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup1 = ProcessGroupEnum.FORGING;

        final String assemblyScenarioName1 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly1 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName1,
            assemblyExtension1,
            ASSEMBLY,
            subComponentNames1,
            subComponentExtension1,
            subComponentProcessGroup1,
            assemblyScenarioName1,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly1).uploadAssembly(componentAssembly1);
        assemblyUtils.costSubComponents(componentAssembly1).costAssembly(componentAssembly1);
        assemblyUtils.publishSubComponents(componentAssembly1).publishAssembly(componentAssembly1);

        final String assemblyName2 = "Hinge assembly";
        final List<String> subComponentNames2 = Arrays.asList("big ring", "Pin", "small ring");
        List<MultiUpload> secondAssemblyBatch = new ArrayList<>();
        secondAssemblyBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring.SLDPRT"), assemblyScenarioName1));
        secondAssemblyBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "Pin.SLDPRT"), assemblyScenarioName1));
        secondAssemblyBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "small ring.SLDPRT"), assemblyScenarioName1));
        secondAssemblyBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ASSEMBLY, "Hinge assembly.SLDASM"), assemblyScenarioName1));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName1, assemblyScenarioName1)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getRowDetails(assemblyName1, assemblyScenarioName1)).contains(ComponentIconEnum.ASSEMBLY.getIcon());

        explorePage.highlightScenario(assemblyName1, assemblyScenarioName1);
        softAssertions.assertThat(explorePage.openPreviewPanel().isImageDisplayed()).isEqualTo(true);

        explorePage.importCadFile()
            .inputMultiComponents(secondAssemblyBatch)
            .inputScenarioName(assemblyScenarioName1)
            .submit()
            .clickClose()
            .selectFilter("Private");

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName2, assemblyScenarioName1)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = 5621)
    @Description("Validate sub components such as bolts or screws can exist in multiple assemblies")
    public void uploadAnAssemblyThatIsPartOfAnotherAssembly() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String assemblyName1 = "titan battery ass";
        final List<String> subComponentNames1 = Arrays.asList("titan battery release", "titan battery");
        List<MultiUpload> firstAssemblyBatch = new ArrayList<>();
        firstAssemblyBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "titan battery release.SLDPRT"), scenarioName));
        firstAssemblyBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.STOCK_MACHINING, "titan battery.SLDPRT"), scenarioName));
        firstAssemblyBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ASSEMBLY, "titan battery ass.SLDASM"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(firstAssemblyBatch)
            .submit()
            .clickClose();

        final String assemblyName2 = "titan charger ass";
        final String assemblyExtension2 = ".SLDASM";
        final List<String> subComponentNames2 = Arrays.asList("titan charger base", "titan charger lead", "titan charger upper");
        final String subComponentExtension2 = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup2 = ProcessGroupEnum.PLASTIC_MOLDING;

        ComponentInfoBuilder componentAssembly2 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName2,
            assemblyExtension2,
            ASSEMBLY,
            subComponentNames2,
            subComponentExtension2,
            subComponentProcessGroup2,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly2).uploadAssembly(componentAssembly2);

        final String assemblyName3 = "titan cordless drill";
        final String assemblyExtension3 = ".SLDASM";
        final List<String> subComponentNames3 = Arrays.asList("titan body RH", "titan body LH", "titan power switch", "titan speed switch", "titan bulk head",
            "titan bit holder", "titan forward reverse switch", "titan torque setting");
        final String subComponentExtension3 = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup3 = ProcessGroupEnum.SHEET_METAL;

        ComponentInfoBuilder componentAssembly3 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName3,
            assemblyExtension3,
            ASSEMBLY,
            subComponentNames3,
            subComponentExtension3,
            subComponentProcessGroup3,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly3).uploadAssembly(componentAssembly3);

        componentsTreePage = explorePage.openComponent(assemblyName2, scenarioName, currentUser)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(assemblyName1)).isEqualTo(true);

        componentsTreePage = explorePage.openComponent(assemblyName3, scenarioName, currentUser)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(assemblyName1)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12156, 6557, 6524})
    @Description("Column Configuration button in Tree View is clickable and opens menu")
    public void testColumnConfigurationButton() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String assemblyName = "titan charger ass";
        final String assemblyExtension = ".SLDASM";
        final List<String> subComponentNames = Arrays.asList("titan charger base", "titan charger lead", "titan charger upper");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        configurePage = loginPage.login(currentUser)
            .openComponent(assemblyName, scenarioName, currentUser)
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
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {12139, 12101, 12136})
    @Description("Column configuration in Tree View with All filter does not affect column configuration in List View")
    public void testColumnConfigurationListView() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String assemblyName = "titan charger ass";
        final String assemblyExtension = ".SLDASM";
        final List<String> subComponentNames = Arrays.asList("titan charger base", "titan charger lead", "titan charger upper");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .openComponent("titan charger base", scenarioName, currentUser)
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
            .openComponent(assemblyName, scenarioName, currentUser)
            .openComponents()
            .configure()
            .selectChoices()
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ComponentsTreePage.class);

        softAssertions.assertThat(componentsTreePage.getRowDetails("titan charger base", scenarioName)).contains("High", "Test Description", "Test Notes",
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
        currentUser = UserUtil.getUser();

        final String hinge_assembly = "Hinge assembly";
        final ProcessGroupEnum assemblyProcessGroup = ASSEMBLY;
        final String assemblyExtension = ".SLDASM";
        final String big_ring = "big ring";
        final String pin = "Pin";
        final String small_ring = "small ring";
        final String subComponentExtension = ".SLDPRT";
        final List<String> subComponentNames = Arrays.asList(big_ring, pin, small_ring);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            hinge_assembly,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
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
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {6564})
    @Description("Assembly costs with multiple quantity of parts")
    public void costAssemblyWithMultipleQuantityOfParts() {
        ComponentInfoBuilder componentAssembly = new AssemblyDTORequest().getAssembly("flange c");
        ComponentInfoBuilder subcomponentA = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("flange")).findFirst().get();
        ComponentInfoBuilder subcomponentB = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("nut")).findFirst().get();
        ComponentInfoBuilder subcomponentC = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("bolt")).findFirst().get();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .uploadComponentAndOpen(subcomponentA.getComponentName(), subcomponentA.getScenarioName(), subcomponentA.getResourceFile(), componentAssembly.getUser())
            .selectProcessGroup(subcomponentA.getProcessGroup())
            .costScenario()

            .uploadComponentAndOpen(subcomponentB.getComponentName(), subcomponentB.getScenarioName(), subcomponentB.getResourceFile(), componentAssembly.getUser())
            .selectProcessGroup(subcomponentB.getProcessGroup())
            .costScenario()

            .uploadComponentAndOpen(subcomponentC.getComponentName(), subcomponentC.getScenarioName(), subcomponentC.getResourceFile(), componentAssembly.getUser())
            .selectProcessGroup(subcomponentC.getProcessGroup())
            .costScenario()

            .uploadComponentAndOpen(componentAssembly.getComponentName(), componentAssembly.getScenarioName(), componentAssembly.getResourceFile(), componentAssembly.getUser())
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
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String componentExtension = ".prt.1";
        final String originalAsmExtension = ".asm.1";
        final String topLevelAsmExtension = ".asm.3";

        final String autoBotAsm = "autobotasm";
        final String autoHelm = "autoparthelm";
        final String autoHead = "autoparthead";
        final String autoTorso = "autoparttorso";
        final String autoArm = "autopartarm";
        final String autoHand = "autoparthand";
        final String autoLeg = "autopartleg";
        final String autoFoot = "autopartfoot";
        final String autoSword = "autosword";

        final String autoPommel = "autopommel";
        final String autoHandle = "autohandle";
        final String autoGuard = "autoguard";
        final String autoBlade = "autoblade";

        final File autoSwordFile = FileResourceUtil.getCloudFile(ASSEMBLY, autoSword + originalAsmExtension);

        List<String> components = Arrays.asList(autoHelm, autoHead, autoTorso, autoArm, autoHand, autoLeg, autoFoot);
        List<String> subAsmComponents = Arrays.asList(autoPommel, autoHandle, autoGuard, autoBlade);

        assemblyInfo = assemblyUtils.associateAssemblyAndSubComponents(autoBotAsm, topLevelAsmExtension, ASSEMBLY,
            components, componentExtension, ASSEMBLY, scenarioName, currentUser);

        assemblyUtils.uploadSubComponents(assemblyInfo);
        assemblyUtils.uploadAssembly(assemblyInfo);

        evaluatePage = new CidAppLoginPage(driver).login(currentUser)
            .openScenario(autoBotAsm, scenarioName);
        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(autoSword)).as("Verify Missing Sub-Assembly is struck out").isTrue();
        softAssertions.assertThat(componentsTreePage.getRowDetails(autoSword, scenarioName).contains(StatusIconEnum.DISCONNECTED.getStatusIcon()))
            .as("Verify sub-assembly is shown as CAD disconnected").isTrue();

        explorePage = componentsTreePage.closePanel()
            .importCadFile()
            .inputScenarioName(scenarioName)
            .enterFilePath(autoSwordFile)
            .submit()
            .clickClose();

        subAssemblyInfo = assemblyUtils.associateAssemblyAndSubComponents(autoSword, originalAsmExtension, ASSEMBLY,
            subAsmComponents, componentExtension, ASSEMBLY, scenarioName, currentUser);
        List<ScenarioItem> autoSwordDetails = componentsUtil.getUnCostedComponent(autoSword, scenarioName, currentUser);
        subAssemblyInfo.setScenarioIdentity(autoSwordDetails.get(0).getScenarioIdentity());
        subAssemblyInfo.setComponentIdentity(autoSwordDetails.get(0).getComponentIdentity());

        subAssemblyInfo.getSubComponents().forEach(subComponent -> {
            List<ScenarioItem> componenetDetails = componentsUtil.getUnCostedComponent(subComponent.getComponentName(), scenarioName, currentUser);
            subComponent.setScenarioIdentity(componenetDetails.get(0).getScenarioIdentity());
            subComponent.setComponentIdentity(componenetDetails.get(0).getComponentIdentity());
        });

        componentsTreePage = explorePage.openScenario(autoBotAsm, scenarioName)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(autoSword)).as("Verify Sub-Assembly is no longer struck out").isFalse();
        softAssertions.assertThat(componentsTreePage.getRowDetails(autoSword, scenarioName).contains(StatusIconEnum.CAD.getStatusIcon()))
            .as("Verify sub-assembly is shown as CAD connected").isTrue();
        softAssertions.assertThat(componentsTreePage.getRowDetails(autoSword, scenarioName)).as("Verify 'missing' sub-asm overridden").isNotEmpty();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10748, 10761})
    @Description("Changing unit user preferences when viewing assembly")
    public void testCopyScenarioRetainsAsmAssociation() {
        currentUser = UserUtil.getUser();

        final String hinge_assembly = "Hinge assembly";
        final ProcessGroupEnum assemblyProcessGroup = ASSEMBLY;
        final String assemblyExtension = ".SLDASM";
        final String big_ring = "big ring";
        final String pin = "Pin";
        final String small_ring = "small ring";
        final String subComponentExtension = ".SLDPRT";
        final List<String> subComponentNames = Arrays.asList(big_ring, pin, small_ring);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioNamePrivateEvaluate = new GenerateStringUtil().generateScenarioName();
        final String newScenarioNamePrivateExplore = new GenerateStringUtil().generateScenarioName();
        final String newScenarioNamePublicEvaluate = new GenerateStringUtil().generateScenarioName();
        final String newScenarioNamePublicExplore = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            hinge_assembly,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(subComponentProcessGroup.getProcessGroup())
                .build()));
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        List<String> bigRingDetails = componentsTreePage.getRowDetails(big_ring, scenarioName);
        List<String> smallRingDetails = componentsTreePage.getRowDetails(small_ring, scenarioName);
        List<String> pinDetails = componentsTreePage.getRowDetails(pin, scenarioName);

        evaluatePage = componentsTreePage.closePanel()
            .copyScenario()
            .enterScenarioName(newScenarioNamePrivateEvaluate)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2);

        softAssertions.assertThat(evaluatePage.getCurrentScenarioName()).as("Verify new Scenario Name From Evaluate").isEqualTo(newScenarioNamePrivateEvaluate);

        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(big_ring, scenarioName)).as("Verify sub-component details").isEqualTo(bigRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(small_ring, scenarioName)).as("Verify sub-component details").isEqualTo(smallRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).as("Verify sub-component details").isEqualTo(pinDetails);

        evaluatePage = componentsTreePage.closePanel()
            .clickExplore()
            .multiSelectScenarios(hinge_assembly + "," + scenarioName)
            .copyScenario()
            .enterScenarioName(newScenarioNamePrivateExplore)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2);

        softAssertions.assertThat(evaluatePage.getCurrentScenarioName()).as("Verify new Scenario Name from Explore").isEqualTo(newScenarioNamePrivateExplore);

        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(big_ring, scenarioName)).as("Verify sub-component details").isEqualTo(bigRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(small_ring, scenarioName)).as("Verify sub-component details").isEqualTo(smallRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).as("Verify sub-component details").isEqualTo(pinDetails);

        evaluatePage = componentsTreePage.closePanel()
            .clickExplore()
            .openScenario(hinge_assembly, scenarioName)
            .openComponents()
            .selectCheckAllBox()
            .publishSubcomponent()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(EvaluatePage.class);

        componentAssembly.getSubComponents().forEach(subComponent -> scenariosUtil.getScenarioCompleted(subComponent));

        evaluatePage.clickRefresh(EvaluatePage.class);

        componentsTreePage = evaluatePage.openComponents();
        bigRingDetails = componentsTreePage.getRowDetails(big_ring, scenarioName);
        smallRingDetails = componentsTreePage.getRowDetails(small_ring, scenarioName);
        pinDetails = componentsTreePage.getRowDetails(pin, scenarioName);

        evaluatePage = componentsTreePage.closePanel()
            .copyScenario()
            .enterScenarioName(newScenarioNamePublicEvaluate)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2);

        softAssertions.assertThat(evaluatePage.getCurrentScenarioName()).as("Verify new Scenario Name From Evaluate").isEqualTo(newScenarioNamePublicEvaluate);

        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(big_ring, scenarioName)).as("Verify sub-component details").isEqualTo(bigRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(small_ring, scenarioName)).as("Verify sub-component details").isEqualTo(smallRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).as("Verify sub-component details").isEqualTo(pinDetails);

        evaluatePage = componentsTreePage.closePanel()
            .clickExplore()
            .multiSelectScenarios(hinge_assembly + "," + scenarioName)
            .copyScenario()
            .enterScenarioName(newScenarioNamePublicExplore)
            .submit(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2);

        softAssertions.assertThat(evaluatePage.getCurrentScenarioName()).as("Verify new Scenario Name from Explore").isEqualTo(newScenarioNamePublicExplore);

        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(big_ring, scenarioName)).as("Verify sub-component details").isEqualTo(bigRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(small_ring, scenarioName)).as("Verify sub-component details").isEqualTo(smallRingDetails);
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).as("Verify sub-component details").isEqualTo(pinDetails);

        softAssertions.assertAll();
    }
}