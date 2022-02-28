package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ImportCadFilePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.Keys;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class CostAllCadTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private File resourceFile;
    private CostDetailsPage costDetailsPage;
    private ImportCadFilePage importCadFilePage;

    public CostAllCadTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5421", "565", "567"})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void testCADFormatSLDPRT() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "Machined Box AMERICAS";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        costDetailsPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .openCostDetails()
            .expandDropDown("Piece Part Cost,Total Variable Cost");

        assertThat(costDetailsPage.getCostContributionValue("Material Cost"), (closeTo(27.44, 15)));
        assertThat(costDetailsPage.getCostContributionValue("Labor"), (closeTo(6.30, 5)));
        assertThat(costDetailsPage.getCostContributionValue("Direct Overhead"), (closeTo(1.69, 5)));
    }

    // TODO: 23/10/2020 uncomment when functionality is implemented in app
    /*@Test
    @TestRail(testCaseId = {"566"})
    @Description("Be able to determine whether a decision has caused a cost increase or decrease")
    public void costIncreaseDecrease() {

        String componentName = "powderMetal";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        referenceComparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
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
    @TestRail(testCaseId = {"1605"})
    @Description("Upload large GCD part. Part should be displayed in the viewer within 60 seconds")
    public void translationTest() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        String componentName = "LargePart";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.NOT_COSTED), (is(true)));
    }

    @Test
    @Ignore("awaiting a response if this is allowed or not for new CID")
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"5447", "2317"})
    @Description("Ensure scripts cannot be entered into all available text input fields")
    public void failedUpload() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "LargePart.prt.1");

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit("<script>alert(document.cookie)</script>", resourceFile, ImportCadFilePage.class);

        assertThat(importCadFilePage.getAlertWarning(), containsString("error occurred"));
    }

    @Test
    @TestRail(testCaseId = {"5426"})
    @Description("Failure to create a new scenario that has a blank scenario name or is named using unsupported characters")
    public void failedBlankScenarioName() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(UserUtil.getUser())
            .uploadComponent(" " + Keys.BACK_SPACE, resourceFile);

        assertThat(importCadFilePage.getFieldWarningText(), containsString("Required."));
    }
}