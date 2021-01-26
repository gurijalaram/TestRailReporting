package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.FileUploadPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class CostAllCadTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;
    private CostDetailsPage costDetailsPage;
    private FileUploadPage fileUploadPage;

    public CostAllCadTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421", "565", "567"})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void testCADFormatSLDPRT() {

        resourceFile = FileResourceUtil.getResourceAsFile("Machined Box AMERICAS.SLDPRT");

        loginPage = new CidAppLoginPage(driver);
        costDetailsPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario()
            .openCostDetails()
            .expandDropDown("Piece Part Cost,Total Variable Cost");

        assertThat(costDetailsPage.getCostContribution("Material Cost"), is(equalTo("$17.66")));
        assertThat(costDetailsPage.getCostContribution("Labor"), is(equalTo("$6.30")));
        assertThat(costDetailsPage.getCostContribution("Direct Overhead"), is(equalTo("$1.74")));
    }

    // TODO: 23/10/2020 uncomment when functionality is implemented in app
    /*@Test
    @TestRail(testCaseId = {"566"})
    @Description("Be able to determine whether a decision has caused a cost increase or decrease")
    public void costIncreaseDecrease() {

        resourceFile = FileResourceUtil.getResourceAsFile("powderMetal.stp");

        loginPage = new CidAppLoginPage(driver);
        referenceComparePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialCompositionTable()
            .selectMaterialComposition("FN-0205")
            .apply()
            .costScenario()
            .expandReferencePanel();

        assertThat(referenceComparePage.getMaterialCostDelta(), containsString("up"));
        assertThat(referenceComparePage.getPiecePartCostDelta(), containsString("down"));
        assertThat(referenceComparePage.getFullyBurdenedCostDelta(), containsString("down"));
        assertThat(referenceComparePage.getTotalCapitalInvestmentsDelta(), containsString("up"));
    }*/

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - par")
    public void testCADFormatPar() {

        resourceFile = FileResourceUtil.getResourceAsFile("26136.par");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - CATPart")
    public void testCADFormatCATPart() {

        resourceFile = FileResourceUtil.getResourceAsFile("Plastic moulded cap DFM.CATPart");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - prt.4")
    public void testCADFormatPRT4() {

        resourceFile = FileResourceUtil.getResourceAsFile("turning.prt.4");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - Creo")
    public void testCADFormatCreo() {

        resourceFile = FileResourceUtil.getResourceAsFile("turning.prt.4");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - NX")
    public void testCADFormatNX() {

        resourceFile = FileResourceUtil.getResourceAsFile("Locker_bottom_panel.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - Inventor")
    public void testCADFormatInventor() {

        resourceFile = FileResourceUtil.getResourceAsFile("VERTICAL PLATE.ipt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - STEP")
    public void testCADFormatSTEP() {

        resourceFile = FileResourceUtil.getResourceAsFile("partbody_2.stp");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - Parasolid")
    public void testCADFormatParaSolid() {

        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic_steel_PMI.x_t");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421"})
    @Description("CAD file from all supported CAD formats - ACIS")
    public void testCADFormatParaACIS() {

        resourceFile = FileResourceUtil.getResourceAsFile("Plastic moulded cap thinPart.SAT");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), (is(true)));
    }

    @Test
    @TestRail(testCaseId = {"1605"})
    @Description("Upload large GCD part. Part should be displayed in the viewer within 60 seconds")
    public void translationTest() {

        resourceFile = FileResourceUtil.getResourceAsFile("LargePart.prt.1");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UNCOSTED_SCENARIO.getCostingText()), (is(true)));
    }

    @Test
    @TestRail(testCaseId = {"5447", "2317"})
    @Description("Ensure scripts cannot be entered into all available text input fields")
    public void failedUpload() {

        resourceFile = FileResourceUtil.getResourceAsFile("LargePart.prt.1");

        loginPage = new CidAppLoginPage(driver);
        fileUploadPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit("<script>alert(document.cookie)</script>", resourceFile, FileUploadPage.class);

        assertThat(fileUploadPage.getAlertWarning(), containsString("error occurred"));
    }

    @Test
    @TestRail(testCaseId = {"5426"})
    @Description("Failure to create a new scenario that has a blank scenario name or is named using unsupported characters")
    public void failedBlankScenarioName() {

        resourceFile = FileResourceUtil.getResourceAsFile("PowderMetalShaft.stp");

        loginPage = new CidAppLoginPage(driver);
        fileUploadPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit("", resourceFile, FileUploadPage.class);

        assertThat(fileUploadPage.getFieldWarningText(), containsString("Required."));
    }
}