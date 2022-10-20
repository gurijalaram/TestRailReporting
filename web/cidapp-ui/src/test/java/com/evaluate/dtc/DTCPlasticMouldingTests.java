package com.evaluate.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.InvestigationPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.EvaluateDfmIconEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DTCPlasticMouldingTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private GuidanceIssuesPage guidanceIssuesPage;
    private InvestigationPage investigationPage;

    private UserCredentials currentUser;
    private File resourceFile;
    SoftAssertions softAssertions = new SoftAssertions();

    public DTCPlasticMouldingTests() {
        super();
    }

    @Category(SmokeTests.class)
    @Test
    @Issue("CID-1247")
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

        guidanceIssuesPage.closePanel()
            .openMaterialSelectorTable()
            .search("Nylon, Type 6")
            .selectMaterial("Nylon, Type 6")
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Reaction Injection Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Curved Wall", "CurvedWall:6");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Surface draft is less than the recommended draft angle for this material.");
        softAssertions.assertThat(guidanceIssuesPage.getGcdCount("Curved Wall")).isEqualTo(22);

        softAssertions.assertAll();
    }

    @Category({SmokeTests.class})
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
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Structural Foam Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Planar Face", "PlanarFace:10");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Surface draft is less than the recommended draft angle for this material.");
        softAssertions.assertThat(guidanceIssuesPage.getGcdCount("Planar Face")).isEqualTo(4);

        softAssertions.assertAll();
    }

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

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Internal edge radius is less than the recommended internal edge radius for this material.");

        softAssertions.assertAll();
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

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("External edge radius is less than the recommended external edge radius for this material.");

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2634")
    @TestRail(testCaseId = {"6463", "6421", "6414", "6425", "6426"})
    @Description("Min. wall thickness for Structural Foam Moulding")
    public void minWallThicknessSFM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Plastic moulded cap thinPart";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.LOW.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Low");

        guidanceIssuesPage = evaluatePage.goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Structural Foam Mold")
            .submit(EvaluatePage.class)
            .costScenario(1)
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue", "Minimum Wall Thickness", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Minimum wall thickness is less than the recommended thickness for this material.");

        guidanceIssuesPage.selectIssueTypeGcd("Radii Issue", "Minimum Edge Radius on Parting Line", "SharpEdge:1");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("SharpEdge:1 may partially be off the parting line, in which case the edge radius is less than the recommended external edge radius for this material.");

        guidanceIssuesPage.selectIssueTypeGcd("Radii Issue", "Minimum External Edge Radius", "SharpEdge:14");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("External edge radius is less than the recommended external edge radius for this material.");

        guidanceIssuesPage.selectIssueTypeGcd("Radii Issue", "Minimum Internal Edge Radius", "SharpEdge:5");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Internal edge radius is less than the recommended internal edge radius for this material.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6420", "6421", "6424", "6460"})
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

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Maximum wall thickness is greater than the recommended thickness for this material.");
        softAssertions.assertThat(guidanceIssuesPage.getGcdSuggested("Component:1")).contains("<= 3.56mm");

        evaluatePage = guidanceIssuesPage.closePanel();
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Critical");

        guidanceIssuesPage = evaluatePage.goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Structural Foam Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue", "Maximum Wall Thickness", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Maximum wall thickness is greater than the recommended thickness for this material.");
        softAssertions.assertThat(guidanceIssuesPage.getGcdSuggested("Component:1")).contains("<= 15.00mm");

        guidanceIssuesPage.closePanel()
            .openMaterialSelectorTable()
            .selectMaterial("Polyurethane, Polymeric MDI")
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Reaction Injection Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue", "Maximum Wall Thickness", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Maximum wall thickness is greater than the recommended thickness for this material.");
        softAssertions.assertThat(guidanceIssuesPage.getGcdSuggested("Component:1")).contains("<= 50.80mm");

        softAssertions.assertAll();
    }

    @Test
    @Issue("CID-1247")
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

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Minimum wall thickness is less than the recommended thickness for this material.");

        guidanceIssuesPage.closePanel()
            .openMaterialSelectorTable()
            .selectMaterial("Polyurethane, Polymeric MDI")
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Reaction Injection Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue", "Minimum Wall Thickness", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Minimum wall thickness is less than the recommended thickness for this material.");

        softAssertions.assertAll();
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
        investigationPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectTopic("Slides and Lifters");

        softAssertions.assertThat(investigationPage.getGcdCount("SlideBundle")).isEqualTo(2);
        softAssertions.assertThat(investigationPage.getGcdCount("LifterBundle")).isEqualTo(7);

        investigationPage.selectTopic("Special Mold Tooling");

        softAssertions.assertThat(investigationPage.getGcdCount("Threading Mechanisms")).isEqualTo(9);
        softAssertions.assertThat(investigationPage.getGcdCount("Ribs")).isEqualTo(1);

        softAssertions.assertAll();
    }
}