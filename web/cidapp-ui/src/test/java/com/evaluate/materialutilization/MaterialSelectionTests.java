package com.evaluate.materialutilization;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.StockPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import testsuites.RegressionTestSuite;

import java.io.File;

@RunWith(JUnitParamsRunner.class)
public class MaterialSelectionTests extends TestBase {

    UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private StockPage stockPage;
    private MaterialUtilizationPage materialUtilizationPage;
    private File resourceFile;
    private SoftAssertions softAssertions = new SoftAssertions();

    public MaterialSelectionTests() {
        super();
    }

    private Object[] testParameters() {
        return new Object[] {
            new Object[] {ProcessGroupEnum.ADDITIVE_MANUFACTURING, "Aluminum AlSi10Mg", "ADD-LOW-001", ".SLDPRT"},
            new Object[] {ProcessGroupEnum.BAR_TUBE_FAB, "Steel, Hot Worked, AISI 1010", "AP-000-006", ".step"},
            new Object[] {ProcessGroupEnum.CASTING, "Aluminum, Cast, ANSI AL380.0", "CastedPart", ".CATPart"},
            new Object[] {ProcessGroupEnum.CASTING_DIE, "Aluminum, Cast, ANSI AL380.0", "CurvedWall", ".CATPart"},
            new Object[] {ProcessGroupEnum.CASTING_INVESTMENT, "Aluminum, Cast, ANSI AL380.0", "AP-000-506", ".prt.1"},
            new Object[] {ProcessGroupEnum.CASTING_SAND, "Aluminum, Cast, ANSI AL380.0", "casting_q5_thinvalve", ".prt"},
            new Object[] {ProcessGroupEnum.FORGING, "Steel, Cold Worked, AISI 1010", "big ring", ".SLDPRT"},
            new Object[] {ProcessGroupEnum.PLASTIC_MOLDING, "ABS", "bolt", ".CATPart"},
            new Object[] {ProcessGroupEnum.POWDER_METAL, "F-0005", "case_31_test_part_6_small", ".prt.2"},
            new Object[] {ProcessGroupEnum.RAPID_PROTOTYPING, "Default", "Plastic moulded cap DFM", ".CATPart"},
            new Object[] {ProcessGroupEnum.ROTO_BLOW_MOLDING, "Polyethylene, High Density (HDPE)", "Plastic moulded cap DFM", ".CATPart"},
            new Object[] {ProcessGroupEnum.SHEET_METAL, "Steel, Cold Worked, AISI 1020", "3571050_cad", ".prt.1"},
            new Object[] {ProcessGroupEnum.SHEET_METAL_HYDROFORMING, "Aluminum, Stock, ANSI 2017", "FlangedRound", ".SLDPRT"},
            //new Object[] {ProcessGroupEnum.SHEET_METAL_ROLLFORMING, "Steel, Cold Worked, AISI 1020", "", ""},
            new Object[] {ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING, "Aluminum, Stock, ANSI 2024", "bracket_basic", ".prt"},
            new Object[] {ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE, "Steel, Cold Worked, AISI 1020", "SheetMetal", ".prt"},
            new Object[] {ProcessGroupEnum.SHEET_PLASTIC, "Polyethylene, HDPE Extrusion Sheet", "r151294", ".prt.1"},
            new Object[] {ProcessGroupEnum.STOCK_MACHINING, "Steel, Hot Worked, AISI 1010", "case_005_flat end mill contouring", ".SLDPRT"}
        };
    }

    @Test
    @Parameters(method = "testParameters")
    @TestCaseName(value = "{method}-{0}")
    @Category(RegressionTestSuite.class)
    @TestRail(testCaseId = {"5901"})
    @Description("Verify default material for each Process Group")
    public void defaultMaterialTest(ProcessGroupEnum pg, String defaultMaterial, String componentName, String componentExt) {

        resourceFile = FileResourceUtil.getCloudFile(pg, componentName + componentExt);
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        EvaluatePage evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(pg)
            .costScenario();

        String costedDefaultMaterial = evaluatePage.getCurrentlySelectedMaterial();
        softAssertions.assertThat(costedDefaultMaterial).as("Expected Default Material").isEqualTo(defaultMaterial);
        softAssertions.assertAll();
    }
}
