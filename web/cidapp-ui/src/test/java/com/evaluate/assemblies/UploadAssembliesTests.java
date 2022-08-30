package com.evaluate.assemblies;

import static com.apriori.utils.enums.ProcessGroupEnum.ASSEMBLY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

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
    private ComponentsListPage componentsListPage;
    private File subComponentA;
    private File subComponentB;
    private File subComponentC;
    private File assembly;
    private UserCredentials currentUser = UserUtil.getUser();
    private SoftAssertions softAssertions = new SoftAssertions();

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

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE)).isEqualTo(true);

        evaluatePage.uploadComponentAndOpen(subComponentBName, scenarioName, subComponentB, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE)).isEqualTo(true);

        evaluatePage.uploadComponentAndOpen(subComponentCName, scenarioName, subComponentC, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE)).isEqualTo(true);

        evaluatePage.uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser)
            .selectProcessGroup(ASSEMBLY)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE)).isEqualTo(true);

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
        componentsListPage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsListPage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
    }

    @Test
    @TestRail(testCaseId = {"11903", "10767", "6562"})
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
        componentsListPage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsListPage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
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
        componentsListPage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsListPage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
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
        componentsListPage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsListPage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
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
        componentsListPage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsListPage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
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
        componentsListPage = loginPage.login(currentUser)
            .uploadAndOpenComponent(multiComponents, scenarioName, assemblyName, currentUser);

        componentNames.forEach(component ->
            assertThat(componentsListPage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
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
        componentsListPage = loginPage.login(currentUser)
            .importCadFile()
            .inputMultiComponents(firstMultiComponentBatch)
            .inputMultiComponents(secondMultiComponentBatch)
            .inputScenarioName(scenarioName)
            .submit()
            .close()
            .openComponent(assemblyName1, scenarioName, currentUser)
            .openComponents();

        componentNames1.forEach(component ->
            assertThat(componentsListPage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));

        componentsListPage.closePanel()
            .clickExplore()
            .openComponent(assemblyName2, scenarioName, currentUser)
            .openComponents();

        componentNames2.forEach(component ->
            assertThat(componentsListPage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
    }
}
