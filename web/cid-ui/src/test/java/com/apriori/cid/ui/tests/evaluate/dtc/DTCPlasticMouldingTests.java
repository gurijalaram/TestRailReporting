package com.apriori.cid.ui.tests.evaluate.dtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.InvestigationPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.EvaluateDfmIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DTCPlasticMouldingTests extends TestBaseUI {

    private SoftAssertions softAssertions = new SoftAssertions();
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private GuidanceIssuesPage guidanceIssuesPage;
    private InvestigationPage investigationPage;
    private ComponentInfoBuilder component;

    public DTCPlasticMouldingTests() {
        super();
    }

    @Tag(SMOKE)
    @Test
    @TestRail(id = {6410, 8334})
    @Description("Min. draft for Injection Moulding & Reaction Injection Moulding (>0.25 Degrees)")
    public void testDTCMouldingDraft() {
        component = new ComponentRequestUtil().getComponent("Plastic moulded cap noDraft");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario(5)
            .openDesignGuidance()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Curved Wall", "CurvedWall:3");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Part of this surface has a draft angle less than the recommended draft angle for this material."));

        guidanceIssuesPage.closePanel()
            .openMaterialSelectorTable()
            .search("Nylon, Type 6")
            .selectMaterial(MaterialNameEnum.NYLON_TYPE_6.getMaterialName())
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

    @Tag(SMOKE)
    @Test
    @TestRail(id = {6411, 6412})
    @Description("Min. draft for SFM Moulding (>0.5 Degrees)")
    public void structuralFoamMouldDraft() {
        component = new ComponentRequestUtil().getComponent("Plastic moulded cap noDraft");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
    @TestRail(id = {6426})
    @Description("Testing DTC Plastic Moulding Edge Radius Internal")
    public void testMouldingEdgeInternal() {
        component = new ComponentRequestUtil().getComponent("Plastic moulded cap edge radius");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Radii Issue, Minimum Internal Edge Radius", "Sharp Edge", "SharpEdge:8");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Internal edge radius is less than the recommended internal edge radius for this material.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6425})
    @Description("Testing DTC Plastic Moulding Edge Radius External")
    public void testMouldingEdgeExternal() {
        component = new ComponentRequestUtil().getComponent("Plastic moulded cap edge radius");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Radii Issue, Minimum External Edge Radius", "Sharp Edge", "SharpEdge:7");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("External edge radius is less than the recommended external edge radius for this material.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6463, 6421, 6414, 6425, 6426})
    @Description("Min. wall thickness for Structural Foam Moulding")
    public void minWallThicknessSFM() {
        component = new ComponentRequestUtil().getComponent("Plastic moulded cap thinPart");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
    @TestRail(id = {6420, 6421, 6424, 6460})
    @Description("Testing DTC Moulding Max Wall Thickness")
    public void plasticMaxWallThickness() {
        component = new ComponentRequestUtil().getComponent("DTCCastingIssues", ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
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
            .selectMaterial(MaterialNameEnum.POLYURETHANE_POLYMERIC_MDI.getMaterialName())
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
    @TestRail(id = {6419, 6423})
    @Description("Testing DTC Moulding Thickness Min")
    public void plasticMinWallThickness() {
        component = new ComponentRequestUtil().getComponent("Plastic moulded cap thinPart");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue, Minimum Wall Thickness", "Component", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Minimum wall thickness is less than the recommended thickness for this material.");

        guidanceIssuesPage.closePanel()
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.POLYURETHANE_POLYMERIC_MDI.getMaterialName())
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
    @TestRail(id = {6415, 6416, 6427})
    @Description("Testing DTC Moulding Max Wall Thickness")
    public void plasticSlideLift() {
        component = new ComponentRequestUtil().getComponent("DTCCastingIssues", ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        investigationPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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

    @Test
    @TestRail(id = {6422})
    @Description("Max. wall thickness for Structural Foam Moulding")
    public void maxThicknessStructuralFoamMolding() {
        component = new ComponentRequestUtil().getComponent("DTCCastingIssues", ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("Polystyrene")
            .selectMaterial("Polystyrene")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Structural Foam Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue, Maximum Wall Thickness", "Component", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).isEqualTo("Maximum wall thickness is greater than the recommended thickness for this material.");
        softAssertions.assertThat(guidanceIssuesPage.getGcdSuggested("Component:1")).contains("15.00mm");

        softAssertions.assertAll();
    }
}