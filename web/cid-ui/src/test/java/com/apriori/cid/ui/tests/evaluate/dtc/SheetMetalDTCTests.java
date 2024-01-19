package com.apriori.cid.ui.tests.evaluate.dtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.IGNORE;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.InvestigationPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SheetMetalDTCTests extends TestBaseUI {

    private SoftAssertions softAssertions = new SoftAssertions();
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private GuidanceIssuesPage guidanceIssuesPage;
    private ExplorePage explorePage;
    private InvestigationPage investigationPage;
    private ComponentInfoBuilder component;

    public SheetMetalDTCTests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (component != null) {
            new UserPreferencesUtil().resetSettings(component.getUser());
        }
    }

    @Test
    @TestRail(id = {6496, 6499, 6500})
    @Description("Testing DTC Sheet Metal")
    public void sheetMetalDTCHoles() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("SheMetDTC", ProcessGroupEnum.SHEET_METAL);

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue, Hole - Min Diameter", "Simple Hole", "SimpleHole:2");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Hole cannot be made by a Plasma Cutting operation on the Plasma Cut process as the kerf width is too large.");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue, Hole - Blind", "Simple Hole", "SimpleHole:4");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("SimpleHole:4 is blind and Plasma Cutting cannot make blind holes.");

        softAssertions.assertAll();

        /*guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Machined GCDs", "Facing", "PlanarFace:13");
        assertThat(guidanceIssuesPage.getGcdCount("Facing"), is(equalTo("1")));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Machined GCDs", "Center Drilling / Pecking", "SimpleHole:2");
        assertThat(guidanceIssuesPage.getGcdCount("Center Drilling / Pecking"), is(equalTo("1")));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Machined GCDs", "Center Drilling / Drilling", "SimpleHole:3");
        assertThat(guidanceIssuesPage.getGcdCount("Center Drilling / Drilling"), is(equalTo("3")));*/
    }

    @Test
    @TestRail(id = {6497, 6498})
    @Description("Verify Proximity Issues Are Highlighted")
    public void sheetMetalProximity() {
        component = new ComponentRequestUtil().getComponent("SheetMetalTray");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario(3)
            .openDesignGuidance()
            .selectIssueTypeGcd("Proximity Warning, Hole - Hole Proximity", "Complex Hole", "ComplexHole:14");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("The thin strip of material between the holes is at risk of damage from forces during manufacture or service.");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Proximity Warning, Hole - Hole Proximity", "Simple Hole", "SimpleHole:2");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("The thin strip of material between the holes is at risk of damage from forces during manufacture or service.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6495, 6501})
    @Description("Verify Bend Issues Are Highlighted")
    public void sheetMetalBends() {
        component = new ComponentRequestUtil().getComponent("extremebends");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Bend Issue, Bend - Intersects Form", "Straight Bend", "StraightBend:4");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("The intersection between the bend and form cannot be accessed by the tool when using Bending");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Bend Issue, Bend - Min Flap Size", "Straight Bend", "StraightBend:11");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Bend flap is too short to be easily made with standard bending operations.");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Bend Issue, Bend - Min Radius", "Straight Bend", "StraightBend:4");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Bend radius is too small for the machine capability.");

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6486})
    @Description("Verify the Design Guidance tile presents the correct counts for number of GCDs, warnings, guidance issues, & tolerances for a part")
    public void tileDTC() {
        component = new ComponentRequestUtil().getComponent("extremebends");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getGuidanceResult("Design Warnings")).isEqualTo("18");
        softAssertions.assertThat(evaluatePage.getGuidanceResult("Design Failures")).isEqualTo("4");
        softAssertions.assertThat(evaluatePage.getGuidanceResult("GCDs with Tolerances")).isEqualTo("22");

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6491, 6492, 6493, 6494})
    @Description("Testing DTC Sheet Metal")
    public void sheetMetalDTCInvestigation() {
        component = new ComponentRequestUtil().getComponent("SheMetDTC");

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(component.getUser())
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class);

        investigationPage = explorePage
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectTopic("Holes and Fillets");

        softAssertions.assertThat(investigationPage.getGcdCount("Hole - Standard (2 Tools)")).isEqualTo(4);

        investigationPage.selectTopic("Distinct Sizes Count");

        softAssertions.assertThat(investigationPage.getGcdCount("Bend Radius (1 Tool)")).isEqualTo(1);
        softAssertions.assertThat(investigationPage.getGcdCount("Hole Size (2 Tools)")).isEqualTo(4);

        investigationPage.selectTopic("Machining Setups");

        softAssertions.assertThat(investigationPage.getGcdCount("SetupAxis:1")).isEqualTo(14);

        softAssertions.assertAll();
    }

    @Test
    //TODO update testrail case 719 when editing tolerances are ported
    @Disabled("Requires tolerances for additional operation")
    @Tags({@Tag(SMOKE),
        @Tag(IGNORE)})
    @TestRail(id = {6502})
    @Description("Verify tolerances which induce an additional operation")
    public void toleranceAdditionalOp() {
        component = new ComponentRequestUtil().getComponent("bracket_basic_matPMI");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            /*.openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidanceIssuesPage = settingsPage.save(ExplorePage.class)*/
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("GCDs With Special Finishing", "Reaming", "SimpleHole:2");

        softAssertions.assertThat(guidanceIssuesPage.getGcdCurrent("SimpleHole:2")).isEqualTo(0.02);
        softAssertions.assertThat(guidanceIssuesPage.getGcdCurrent("SimpleHole:2")).isEqualTo(0.06);

        softAssertions.assertAll();
    }
}
