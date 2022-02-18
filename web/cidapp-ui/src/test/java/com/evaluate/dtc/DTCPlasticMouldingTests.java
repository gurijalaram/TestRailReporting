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
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
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

    @Category(SmokeTests.class)
    @Test
    @TestRail(testCaseId = {"6410", "8334"})
    @Description("Min. draft for Injection Moulding & Reaction Injection Moulding (>0.25 Degrees)")
    public void testDTCMouldingDraft() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Plastic moulded cap noDraft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario(5)
            .openDesignGuidance()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Curved Wall", "CurvedWall:3");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Part of this surface has a draft angle less than the recommended draft angle for this material."));

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
    @TestRail(testCaseId = {"6411", "6412"})
    @Description("Min. draft for SFM Moulding (>0.5 Degrees)")
    public void structuralFoamMouldDraft() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

       String componentName = "Plastic moulded cap noDraft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
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
    }*/

    @Test
    @TestRail(testCaseId = {"6426"})
    @Description("Testing DTC Plastic Moulding Edge Radius Internal")
    public void testMouldingEdgeInternal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Plastic moulded cap edge radius";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Radii Issue, Minimum Internal Edge Radius", "Sharp Edge", "SharpEdge:8");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Internal edge radius is less than the recommended internal edge radius for this material."));
    }

    @Test
    @TestRail(testCaseId = {"6425"})
    @Description("Testing DTC Plastic Moulding Edge Radius External")
    public void testMouldingEdgeExternal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Plastic moulded cap edge radius";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Radii Issue, Minimum External Edge Radius", "Sharp Edge", "SharpEdge:7");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("External edge radius is less than the recommended external edge radius for this material."));
    }

    /*@Test
    @TestRail(testCaseId = {"6463", "6421", "6414", "6425", "6426"})
    @Description("Min. wall thickness for Structural Foam Moulding")
    public void minWallThicknessSFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Plastic moulded cap thinPart";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
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
    }*/

    @Test
    @TestRail(testCaseId = {"6420", "6421", "6424"})
    @Description("Testing DTC Moulding Max Wall Thickness")
    public void plasticMaxWallThickness() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue, Maximum Wall Thickness", "Component", "Component:1");

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Maximum wall thickness is greater than the recommended thickness for this material.");
        softAssertions.assertThat(guidanceIssuesPage.getGcdSuggested("Component:1").contains("<= 3.56mm"));

        softAssertions.assertAll();

        /*designguidanceIssuesPage = new DesignguidanceIssuesPage(driver);
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
    */
    }

    @Test
    @TestRail(testCaseId = {"6419", "6423"})
    @Description("Testing DTC Moulding Thickness Min")
    public void plasticMinWallThickness() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Plastic moulded cap thinPart";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue, Minimum Wall Thickness", "Component", "Component:1");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Minimum wall thickness is less than the recommended thickness for this material."));

        /*designguidanceIssuesPage = new DesignguidanceIssuesPage(driver);
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
    @TestRail(testCaseId = {"6415", "6416", "6427"})
    @Description("Testing DTC Moulding Max Wall Thickness")
    public void plasticSlideLift() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
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
}