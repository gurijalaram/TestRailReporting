package com.evaluate.assemblies;

import static com.apriori.utils.enums.ProcessGroupEnum.ASSEMBLY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.navtoolbars.DeletePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ComponentIconEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.DirectionEnum;
import com.utils.MultiUpload;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadAssembliesTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ComponentsTablePage componentsTablePage;
    private File subComponentA;
    private File subComponentB;
    private File subComponentC;
    private File assembly;
    private UserCredentials currentUser = UserUtil.getUser();
    private SoftAssertions softAssertions = new SoftAssertions();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ExplorePage explorePage;
    private ConfigurePage configurePage;
    private ComponentsTreePage componentsTreePage;
    private File resourceFile;

    public UploadAssembliesTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6511", "10510"})
    @Description("Upload Assembly file with no missing sub-components")
    public void uploadAssemblyTest() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        String subComponentAName = "big ring";
        String subComponentBName = "Pin";
        String subComponentCName = "small ring";
        String assemblyName = "Hinge assembly";

        subComponentA = FileResourceUtil.getCloudFile(processGroupEnum, subComponentAName + ".SLDPRT");
        subComponentB = FileResourceUtil.getCloudFile(processGroupEnum, subComponentBName + ".SLDPRT");
        subComponentC = FileResourceUtil.getCloudFile(processGroupEnum, subComponentCName + ".SLDPRT");
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(subComponentAName, scenarioName, subComponentA, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        evaluatePage.uploadComponentAndOpen(subComponentBName, scenarioName, subComponentB, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        evaluatePage.uploadComponentAndOpen(subComponentCName, scenarioName, subComponentC, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        evaluatePage.uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser)
            .selectProcessGroup(ASSEMBLY)
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
    @TestRail(testCaseId = {"11902", "10762", "11861"})
    @Description("Upload Assembly with sub-components from Catia")
    public void testCatiaMultiUpload() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String assemblyName = "FLANGE C";
        List<String> componentNames = Arrays.asList("BOLT", "NUT", "FLANGE");

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "flange.CATPart"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "nut.CATPart"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "bolt.CATPart"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "flange c.CATProduct"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
    }

    @Test
    @TestRail(testCaseId = {"11903", "10767", "6562", "11909"})
    @Description("Upload Assembly with sub-components from Creo")
    public void testCreoMultiUpload() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String assemblyName = "piston_assembly";
        List<String> componentNames = Arrays.asList("piston_pin", "piston");

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston_pin.prt.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston.prt.5"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "piston_assembly.asm.1"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
    }

    @Test
    @TestRail(testCaseId = "11904")
    @Description("Upload Assembly with sub-components from Solidworks")
    public void testSolidworksMultiUpload() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String assemblyName = "Hinge assembly";
        List<String> componentNames = Arrays.asList("big ring", "Pin", "small ring");
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "Pin.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "small ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "Hinge assembly.SLDASM"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
    }

    @Test
    @TestRail(testCaseId = {"11905", "10764"})
    @Description("Upload Assembly with sub-components from SolidEdge")
    public void testSolidEdgeMultiUpload() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String assemblyName = "oldham";
        List<String> componentNames = Arrays.asList("stand", "drive", "joint");

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "stand.prt.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "drive.prt.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "joint.prt.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "oldham.asm.1"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
    }

    @Test
    @TestRail(testCaseId = {"11906", "10765"})
    @Description("Upload Assembly with sub-components from NX")
    public void testNxMultiUpload() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String assemblyName = "v6 piston assembly_asm1";
        List<String> componentNames = Arrays.asList("piston rod_model1", "piston_model1", "piston cover_model1", "piston pin_model1");

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston rod_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston cover_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston pin_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "v6 piston assembly_asm1.prt"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
    }

    @Test
    @TestRail(testCaseId = {"11907", "10766"})
    @Description("Upload Assembly with sub-components from Inventor")
    public void testInventorMultiUpload() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String assemblyName = "Assembly01";
        List<String> componentNames = Arrays.asList("Part0001", "Part0002", "Part0003", "Part0004");
        currentUser = UserUtil.getUser();

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0001.ipt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0002.ipt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0003.ipt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0004.ipt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "Assembly01.iam"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsTreePage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
    }

    @Test
    @TestRail(testCaseId = "11908")
    @Description("Upload multiple Assemblies")
    public void testMultipleAssemblyUpload() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String assemblyName1 = "Assembly01";
        String assemblyName2 = "v6 piston assembly_asm1";
        List<String> componentNames1 = Arrays.asList("Part0001", "Part0002", "Part0003", "Part0004");
        List<String> componentNames2 = Arrays.asList("piston rod_model1", "piston_model1", "piston cover_model1", "piston pin_model1");

        List<MultiUpload> firstMultiComponentBatch = new ArrayList<>();
        firstMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0001.ipt"), scenarioName));
        firstMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0002.ipt"), scenarioName));
        firstMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0003.ipt"), scenarioName));
        firstMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0004.ipt"), scenarioName));
        firstMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "Assembly01.iam"), scenarioName));

        List<MultiUpload> secondMultiComponentBatch = new ArrayList<>();
        secondMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston rod_model1.prt"), scenarioName));
        secondMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston_model1.prt"), scenarioName));
        secondMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston cover_model1.prt"), scenarioName));
        secondMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston pin_model1.prt"), scenarioName));
        secondMultiComponentBatch.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "v6 piston assembly_asm1.prt"), scenarioName));

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
    @TestRail(testCaseId = {"5620", "6513", "6514"})
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
            ProcessGroupEnum.ASSEMBLY,
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
    @TestRail(testCaseId = "5621")
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
            ProcessGroupEnum.ASSEMBLY,
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
            ProcessGroupEnum.ASSEMBLY,
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
    @TestRail(testCaseId = {"12156", "6557", "6524"})
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
            ProcessGroupEnum.ASSEMBLY,
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
    @TestRail(testCaseId = {"12139", "12101", "12136"})
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
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .openComponent("titan charger base", scenarioName, currentUser)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .costScenario()
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

        softAssertions.assertThat(componentsTreePage.getRowDetails("titan charger base", scenarioName)).contains("Medium", "Test Description", "Test Notes",
            "New", "Plastic Molding", "Injection Molding", "ABS");

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
    @TestRail(testCaseId = {"6546"})
    @Description("Changing unit user preferences when viewing assembly")
    public void testUnitPreferenceInAssembly() {
        final String hinge_assembly = "Hinge assembly";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
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
    @TestRail(testCaseId = {"6564"})
    @Description("Assembly costs with multiple quantity of parts")
    public void costAssemblyWithMultipleQuantityOfParts() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        String subComponentAName = "flange";
        String subComponentBName = "nut";
        String subComponentCName = "bolt";
        String assemblyName = "flange c";

        subComponentA = FileResourceUtil.getCloudFile(processGroupEnum, subComponentAName + ".CATPart");
        subComponentB = FileResourceUtil.getCloudFile(processGroupEnum, subComponentBName + ".CATPart");
        subComponentC = FileResourceUtil.getCloudFile(processGroupEnum, subComponentCName + ".CATPart");
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".CATProduct");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(subComponentAName, scenarioName, subComponentA, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .uploadComponentAndOpen(subComponentBName, scenarioName, subComponentB, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .uploadComponentAndOpen(subComponentCName, scenarioName, subComponentC, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser)
            .selectProcessGroup(ASSEMBLY)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        softAssertions.assertThat(evaluatePage.getComponentResults("Total")).isEqualTo(10);
        softAssertions.assertThat(evaluatePage.getComponentResults("Unique")).isEqualTo(3);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "12129")
    @Description("Validate that creating a new component scenario will automatically override a missing scenario, regardless of override flag")
    public void testUploadAssemblyWithMissingSubComponents() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String assemblyName = "Product1";
        final String assemblyExtension = ".CATProduct";
        final String DRILL  = "Drill005";
        final String SCREW = "Screw";
        final String EDON = "edon+";

        final List<String> subComponentNames = Arrays.asList(DRILL, SCREW);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.SHEET_METAL;
        final String subComponentExtension = ".CATPart";

        resourceFile = FileResourceUtil.getCloudFile(subComponentProcessGroup, EDON + subComponentExtension);

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut("edon+")).isTrue();

        evaluatePage = componentsTreePage.openAssembly("edon+", scenarioName);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.DISCONNECTED)).isTrue();

        componentsTreePage = evaluatePage.clickExplore()
            .uploadComponentAndOpen(EDON, scenarioName, resourceFile, currentUser)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2)
            .navigateToScenario(componentAssembly)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut("edon+")).isFalse();

        componentsTreePage.openAssembly("edon+", scenarioName);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.CAD)).isTrue();

        softAssertions.assertAll();

        evaluatePage.clickExplore()
            .multiSelectScenarios("" + assemblyName + ", " + scenarioName + "")
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .selectFilter("Recent")
            .multiHighlightScenarios("" + DRILL + ", " + scenarioName + "",
                "" + SCREW + ", " + scenarioName + "",
                "" + EDON + ", " + scenarioName + "")
            .clickDeleteIcon()
            .clickDelete(DeletePage.class)
            .clickClose(ExplorePage.class);
    }

    @Test
    @TestRail(testCaseId = {"12130", "5625"})
    @Description("Validate that creating a new assembly scenario will automatically override a missing scenario, regardless of override flag")
    public void testUploadAssemblyWithMissingSubAssembly() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String assemblyName = "7";
        final String assemblyExtension = ".prt";
        final String COMPONENT  = "MiscellaneousComponents of SuspensionSeries";
        final String SUBASSEMBLY = "Hex Drive";

        final List<String> subComponentNames = Arrays.asList(COMPONENT);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.SHEET_METAL;
        final String subComponentExtension = ".prt";

        resourceFile = FileResourceUtil.getCloudFile(subComponentProcessGroup, SUBASSEMBLY + subComponentExtension);

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut("Hex Drive")).isTrue();

        evaluatePage = componentsTreePage.openAssembly("Hex Drive", scenarioName);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.DISCONNECTED)).isTrue();

        componentsTreePage = evaluatePage.clickExplore()
            .uploadComponentAndOpen(SUBASSEMBLY, scenarioName, resourceFile, currentUser)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2)
            .navigateToScenario(componentAssembly)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut("Hex Drive")).isFalse();

        componentsTreePage.openAssembly("Hex Drive", scenarioName);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.CAD)).isTrue();

        softAssertions.assertAll();

        evaluatePage.clickExplore()
            .selectFilter("Recent")
            .multiSelectScenarios("" + assemblyName + ", " + scenarioName + "", "" + SUBASSEMBLY + ", " + scenarioName + "")
            .clickDeleteIcon()
            .clickDelete(DeletePage.class)
            .clickClose(ExplorePage.class)
            .multiHighlightScenarios("" + COMPONENT + ", " + scenarioName + "",
                "" + "Hex Retaining O-Ring" + ", " + scenarioName + "")
            .clickDeleteIcon()
            .clickDelete(DeletePage.class)
            .clickClose(ExplorePage.class);
    }
}
