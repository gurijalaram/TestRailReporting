package evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class DTCMouldingDraftTests extends TestBase {

    private LoginPage loginPage;
    private GuidancePage guidancePage;
    private EvaluatePage evaluatePage;

    public DTCMouldingDraftTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"1066"})
    @Description("Min. draft for Injection Moulding & Reaction Injection Moulding (>0.25 Degrees)")
    public void testDTCMouldingDraft() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap noDraft.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(5)
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Draft  Issue, Draft Angle", "Curved Walls", "CurvedWall:3");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Part of this surface is below the minimum recommended draft angle."));

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        guidancePage = evaluatePage.openProcessDetails()
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
            .selectIssueTypeAndGCD("Draft  Issue, Draft Angle", "Curved Walls", "CurvedWall:3");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Part of this surface is below the minimum recommended draft angle."));
        assertThat(guidancePage.getGuidanceCell("Curved Walls", "Count"), is(equalTo("22")));
    }
}