package com.apriori.cid.ui.tests.evaluate.dtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.ThreadsPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.LengthEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.UnitsEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ThreadTests extends TestBaseUI {

    private SoftAssertions softAssertions = new SoftAssertions();
    private CidAppLoginPage loginPage;
    private ThreadsPage threadingPage;
    private ComponentInfoBuilder component;

    public ThreadTests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (component != null) {
            new UserPreferencesUtil().resetSettings(component.getUser());
        }
    }

    @Test
    @TestRail(id = {8902, 8903})
    @Description("Testing to verify costed thread with attribute change")
    public void selectScenario() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("DTCCastingIssues", ProcessGroupEnum.STOCK_MACHINING);

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("4 Axis Mill Routing")
            .submit(EvaluatePage.class)
            .costScenario(7)
            .openDesignGuidance()
            .openThreadsTab()
            .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        softAssertions.assertThat(threadingPage.getThreaded("SimpleHole:1")).contains("check");
        softAssertions.assertThat(threadingPage.getLength("SimpleHole:1")).isEqualTo("20.00mm");

        threadingPage.closePanel()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL)
            .openMaterialSelectorTable()
            .search("1095")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1095.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("[CTL]/Waterjet/[Bend]")
            .submit(EvaluatePage.class)
            .costScenario(7)
            .openDesignGuidance()
            .openThreadsTab()
            .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        softAssertions.assertThat(threadingPage.getLength("SimpleHole:1")).isEqualTo("20.00mm");
        softAssertions.assertThat(threadingPage.getThreaded("SimpleHole:1")).contains("check");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {8904, 6358, 6359, 8905, 6299, 6362, 8268})
    @Description("Testing thread units persist when changed to inches")
    public void validateThreadUnitsInches() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("DTCCastingIssues", ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Gravity Die Cast")
            .submit(EvaluatePage.class)
            .costScenario(7)
            .openDesignGuidance()
            .openThreadsTab()
            .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        softAssertions.assertThat(threadingPage.getLength("SimpleHole:1")).isEqualTo("20.00mm");

        threadingPage.closePanel()
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .setSystem("Imperial")
            .selectLength(LengthEnum.INCHES)
            .submit(EvaluatePage.class)
            .openDesignGuidance()
            .openThreadsTab()
            .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        softAssertions.assertThat(threadingPage.getLength("SimpleHole:1")).isEqualTo("0.79in");

        threadingPage.closePanel()
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .setSystem("Metric")
            .selectLength(LengthEnum.CENTIMETER)
            .submit(EvaluatePage.class)
            .openDesignGuidance()
            .openThreadsTab()
            .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        softAssertions.assertThat(threadingPage.getLength("SimpleHole:1")).isEqualTo("2.00cm");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {8268, 8906})
    @Description("Testing compatible thread length for NX files and SP")
    public void threadsCompatibleCadNX() {
        component = new ComponentRequestUtil().getComponent("100plusThreads");

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Injection Mold")
            .submit(EvaluatePage.class)
            .costScenario(7)
            .openDesignGuidance()
            .openThreadsTab()
            .selectIssueTypeGcd("Simple Holes", "SimpleHole:15");

        softAssertions.assertThat(threadingPage.getLength("SimpleHole:15")).isEqualTo("15.00mm");

        threadingPage.closePanel()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .openThreadsTab()
            .selectIssueTypeGcd("Simple Holes", "SimpleHole:15");

        softAssertions.assertThat(threadingPage.getLength("SimpleHole:15")).isEqualTo("15.00mm");
        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {8268})
    @Description("Testing compatible thread length for Creo files")
    public void threadsCompatibleCadCreo() {
        component = new ComponentRequestUtil().getComponent("CREO-PMI-Threads");

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("[CTL]/[Bend]")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .openThreadsTab()
            .selectIssueTypeGcd("Simple Holes", "SimpleHole:13");

        assertThat(threadingPage.getLength("SimpleHole:13"), is("4.06mm"));
    }
}