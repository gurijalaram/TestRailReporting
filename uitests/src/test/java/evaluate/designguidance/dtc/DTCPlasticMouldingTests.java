package evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.investigation.InvestigationPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DTCPlasticMouldingTests extends TestBase {

    private CIDLoginPage loginPage;
    private GuidancePage guidancePage;
    private InvestigationPage investigationPage;
    private DesignGuidancePage designGuidancePage;

    private File resourceFile;

    public DTCPlasticMouldingTests() {
        super();
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1066", "1593"})
    @Description("Min. draft for Injection Moulding & Reaction Injection Moulding (>0.25 Degrees)")
    public void testDTCMouldingDraft() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap noDraft.CATPart");

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(5)
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Draft Issue, Draft Angle", "Curved Walls", "CurvedWall:3");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Part of this surface is below the minimum recommended draft angle."));

        designGuidancePage = new DesignGuidancePage(driver);
        guidancePage = designGuidancePage.closeDesignGuidance()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Reaction Injection Mold")
            .apply()
            .closeProcessPanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Nylon, Type 6")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Draft Issue, Draft Angle", "Curved Walls", "CurvedWall:3");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Part of this surface is below the minimum recommended draft angle."));
        assertThat(guidancePage.getGuidanceCell("Curved Walls", "Count"), is(equalTo("22")));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1067", "1593", "1068"})
    @Description("Min. draft for SFM Moulding (>0.5 Degrees)")
    public void structuralFoamMouldDraft() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap noDraft.CATPart");

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closeProcessPanel()
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Draft Issue, Draft Angle", "Planar Faces", "PlanarFace:11");

        assertThat(guidancePage.getGuidanceMessage(), containsString("No Surface draft has been applied."));
        assertThat(guidancePage.getGuidanceCell("Planar Faces", "Count"), is(equalTo("4")));
    }

    @Test
    @Description("Testing DTC Plastic Moulding Edge Radius Internal")
    public void testMouldingEdgeInternal() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap edge Radius.CATPart");

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Radii Issue", "Minimum Internal Edge Radius", "SharpEdge:8");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Internal Edge Radius is less than the minimum limit"));
    }

    @Test
    @Description("Testing DTC Plastic Moulding Edge Radius External")
    public void testMouldingEdgeExternal() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap edge Radius.CATPart");

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Radii Issue", "Minimum External Edge Radius", "SharpEdge:7");

        assertThat(guidancePage.getGuidanceMessage(), containsString("External Edge Radius is less than the minimum limit"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1076", "1070", "1081", "1082"})
    @Description("Min. wall thickness for Structural Foam Moulding")
    public void minWallThicknessSFM() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap thinPart.SLDPRT");

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closeProcessPanel()
            .costScenario(1)
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Material Issue", "Minimum Wall Thickness", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Structural Foam Mold is not feasible. Part Thickness is less than the minimum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Radii Issue", "Minimum Edge Radius on Parting Line", "SharpEdge:1");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Sharp Edge may partially be off the parting line, in which case no feasible molding options could be found."));

        guidancePage.selectIssueTypeAndGCD("Radii Issue", "Minimum External Edge Radius", "SharpEdge:14");
        assertThat(guidancePage.getGuidanceMessage(), containsString("External Edge Radius is less than the minimum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Radii Issue", "Minimum Internal Edge Radius", "SharpEdge:5");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Internal Edge Radius is less than the minimum limit with this material."));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1078", "1079", "1080"})
    @Description("Testing DTC Moulding Max Wall Thickness")
    public void plasticMaxWallThickness() {

        resourceFile = new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart");

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Material Issue", "Maximum Wall Thickness", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Injection Mold is not feasible. Part Thickness is more than the maximum limit with this material."));
        assertThat(guidancePage.getGCDGuidance("Component:1", "Suggested"), is(equalTo("<= 3.556 mm")));

        designGuidancePage = new DesignGuidancePage(driver);
        guidancePage = designGuidancePage.closeDesignGuidance()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closeProcessPanel()
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Material Issue", "Maximum Wall Thickness", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Structural Foam Mold is not feasible. Part Thickness is more than the maximum limit with this material."));
        assertThat(guidancePage.getGCDGuidance("Component:1", "Suggested"), is(equalTo("<= 15 mm")));

        designGuidancePage = new DesignGuidancePage(driver);
        guidancePage = designGuidancePage.closeDesignGuidance()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Reaction Injection Mold")
            .apply()
            .closeProcessPanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Polyurethane, Polymeric MDI")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Material Issue", "Maximum Wall Thickness", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Reaction Injection Mold is not feasible. Part Thickness is more than the maximum limit with this material."));
        assertThat(guidancePage.getGCDGuidance("Component:1", "Suggested"), is(equalTo("<= 50.8 mm")));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1075", "1077"})
    @Description("Testing DTC Moulding Thickness Min")
    public void plasticMinWallThickness() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap thinPart.CATPart");

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Material Issue", "Minimum Wall Thickness", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Injection Mold is not feasible. Part Thickness is less than the minimum limit with this material."));

        designGuidancePage = new DesignGuidancePage(driver);
        guidancePage = designGuidancePage.closeDesignGuidance()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Reaction Injection Mold")
            .apply()
            .closeProcessPanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Polyurethane, Polymeric MDI")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Material Issue", "Minimum Wall Thickness", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Reaction Injection Mold is not feasible. Part Thickness is less than the minimum limit with this material."));
    }

    @Test
    @TestRail(testCaseId = {"1071", "1072", "1083"})
    @Description("Testing DTC Moulding Max Wall Thickness")
    public void plasticSlideLift() {

        resourceFile = new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart");

        loginPage = new CIDLoginPage(driver);
        investigationPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Slides and Lifters");

        assertThat(investigationPage.getInvestigationCell("SlideBundle", "GCD Count"), is(equalTo("1")));
        assertThat(investigationPage.getInvestigationCell("LifterBundle", "GCD Count"), is(equalTo("8")));

        investigationPage.selectInvestigationTopic("Special Mold Tooling");

        assertThat(investigationPage.getInvestigationCell("Threading Mechanisms", "GCD Count"), is(equalTo("8")));
        assertThat(investigationPage.getInvestigationCell("Ribs", "GCD Count"), is(equalTo("1")));
    }
}
