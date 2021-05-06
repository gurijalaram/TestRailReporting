package com.evaluate.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DTCPlasticMouldingTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private GuidanceIssuesPage guidanceIssuesPage;

    private UserCredentials currentUser;
    private File resourceFile;

    public DTCPlasticMouldingTests() {
        super();
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1066", "1593"})
    @Description("Min. draft for Injection Moulding & Reaction Injection Moulding (>0.25 Degrees)")
    public void testDTCMouldingDraft() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap noDraft.CATPart");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit()
            .costScenario(5)
            .openDesignGuidance()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Curved Wall", "CurvedWall:3");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Part of this surface is below the minimum recommended draft angle."));

        /*guidanceIssuesPage.closePanel()
            .openProcesses()
            .selectRoutingsButton()
            .selectRouting("Reaction Injection Mold")
            .apply()
            .closePanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Nylon, Type 6")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Curved Walls", "CurvedWall:3");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Part of this surface is below the minimum recommended draft angle."));
        assertThat(guidanceIssuesPage.getGuidanceCell("Curved Walls", "Count"), is(equalTo("22")));*/
    }

    /*@Category({CustomerSmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1067", "1593", "1068"})
    @Description("Min. draft for SFM Moulding (>0.5 Degrees)")
    public void structuralFoamMouldDraft() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap noDraft.CATPart");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closePanel()
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Planar Faces", "PlanarFace:11");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("No Surface draft has been applied."));
        assertThat(guidanceIssuesPage.getGuidanceCell("Planar Faces", "Count"), is(equalTo("4")));
    }

    @Test
    @Description("Testing DTC Plastic Moulding Edge Radius Internal")
    public void testMouldingEdgeInternal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap edge radius.CATPart");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Radii Issue", "Minimum Internal Edge Radius", "SharpEdge:8");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Internal Edge Radius is less than the minimum limit"));
    }

    @Test
    @Description("Testing DTC Plastic Moulding Edge Radius External")
    public void testMouldingEdgeExternal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap edge radius.CATPart");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Radii Issue", "Minimum External Edge Radius", "SharpEdge:7");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("External Edge Radius is less than the minimum limit"));
    }

    @Test
    @TestRail(testCaseId = {"3841", "1076", "1070", "1081", "1082"})
    @Description("Min. wall thickness for Structural Foam Moulding")
    public void minWallThicknessSFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap thinPart.SLDPRT");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-low-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Low"), is(true));

        guidanceIssuesPage = evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closePanel()
            .costScenario(1)
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Material Issue", "Minimum Wall Thickness", "Component:1");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Structural Foam Mold is not feasible. Part Thickness is less than the minimum limit with this material."));

        guidanceIssuesPage.selectIssueTypeGcd("Radii Issue", "Minimum Edge Radius on Parting Line", "SharpEdge:1");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Sharp Edge may partially be off the parting line, in which case no feasible molding options could be found."));

        guidanceIssuesPage.selectIssueTypeGcd("Radii Issue", "Minimum External Edge Radius", "SharpEdge:14");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("External Edge Radius is less than the minimum limit with this material."));

        guidanceIssuesPage.selectIssueTypeGcd("Radii Issue", "Minimum Internal Edge Radius", "SharpEdge:5");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Internal Edge Radius is less than the minimum limit with this material."));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1078", "1079", "1080"})
    @Description("Testing DTC Moulding Max Wall Thickness")
    public void plasticMaxWallThickness() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Material Issue", "Maximum Wall Thickness", "Component:1");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Injection Mold is not feasible. Part Thickness is more than the maximum limit with this material."));
        assertThat(guidanceIssuesPage.getGCDGuidance("Component:1", "Suggested"), is(equalTo("<= 3.556 mm")));

        designguidanceIssuesPage = new DesignguidanceIssuesPage(driver);
        guidanceIssuesPage = designguidanceIssuesPage.closePanel()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closePanel()
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Material Issue", "Maximum Wall Thickness", "Component:1");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Structural Foam Mold is not feasible. Part Thickness is more than the maximum limit with this material."));
        assertThat(guidanceIssuesPage.getGCDGuidance("Component:1", "Suggested"), is(equalTo("<= 15 mm")));

        designguidanceIssuesPage = new DesignguidanceIssuesPage(driver);
        guidanceIssuesPage = designguidanceIssuesPage.closePanel()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Reaction Injection Mold")
            .apply()
            .closePanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Polyurethane, Polymeric MDI")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Material Issue", "Maximum Wall Thickness", "Component:1");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Reaction Injection Mold is not feasible. Part Thickness is more than the maximum limit with this material."));
        assertThat(guidanceIssuesPage.getGCDGuidance("Component:1", "Suggested"), is(equalTo("<= 50.8 mm")));
    }

    @Test
    @TestRail(testCaseId = {"1075", "1077"})
    @Description("Testing DTC Moulding Thickness Min")
    public void plasticMinWallThickness() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap thinPart.CATPart");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Material Issue", "Minimum Wall Thickness", "Component:1");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Injection Mold is not feasible. Part Thickness is less than the minimum limit with this material."));

        designguidanceIssuesPage = new DesignguidanceIssuesPage(driver);
        guidanceIssuesPage = designguidanceIssuesPage.closePanel()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Reaction Injection Mold")
            .apply()
            .closePanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Polyurethane, Polymeric MDI")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Material Issue", "Minimum Wall Thickness", "Component:1");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Reaction Injection Mold is not feasible. Part Thickness is less than the minimum limit with this material."));
    }

    @Test
    @TestRail(testCaseId = {"1071", "1072", "1083"})
    @Description("Testing DTC Moulding Max Wall Thickness")
    public void plasticSlideLift() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");

        loginPage = new CidAppLoginPage(driver);
        investigationPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Slides and Lifters");

        assertThat(investigationPage.getInvestigationCell("SlideBundle", "GCD Count"), is(equalTo("1")));
        assertThat(investigationPage.getInvestigationCell("LifterBundle", "GCD Count"), is(equalTo("7")));

        investigationPage.selectInvestigationTopic("Special Mold Tooling");

        assertThat(investigationPage.getInvestigationCell("Threading Mechanisms", "GCD Count"), is(equalTo("9")));
        assertThat(investigationPage.getInvestigationCell("Ribs", "GCD Count"), is(equalTo("1")));
    }*/
}
