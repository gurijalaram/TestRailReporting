package com.apriori.evaluate;

import static com.apriori.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.NewCostingLabelEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ImportCadFilePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.io.File;

public class CostAllCadTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private File resourceFile;
    private CostDetailsPage costDetailsPage;
    private ImportCadFilePage importCadFilePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    public CostAllCadTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5421, 565, 567, 6624, 6626})
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
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openCostDetails()
            .expandDropDown("Piece Part Cost,Total Variable Cost");

        softAssertions.assertThat(costDetailsPage.getCostContributionValue("Material Cost")).isCloseTo(10.95, Offset.offset(15.00));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("Labor")).isCloseTo(6.30, Offset.offset(5.00));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("Direct Overhead")).isCloseTo(1.69, Offset.offset(5.00));

        softAssertions.assertAll();
    }

    // TODO: 23/10/2020 uncomment when reference panel functionality is implemented in app
    /*@Test
    @TestRail(id = {566"})
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
    @TestRail(id = {1605})
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
    @TestRail(id = {5426})
    @Description("Failure to create a new scenario that has a blank scenario name or is named using unsupported characters")
    public void failedBlankScenarioName() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(UserUtil.getUser())
            .importCadFile()
            .inputComponentDetails(" " + Keys.BACK_SPACE, resourceFile);

        assertThat(importCadFilePage.getFieldWarningText(), containsString("Required."));
    }
}