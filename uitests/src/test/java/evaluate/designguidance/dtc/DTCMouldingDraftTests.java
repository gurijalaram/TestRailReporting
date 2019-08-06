package test.java.evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.designguidance.GuidancePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

public class DTCMouldingDraftTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private GuidancePage guidancePage;

    public DTCMouldingDraftTests() {
        super();
    }

    @Test
    @Description("Testing DTC Machining Moulding Draft")
    @Severity(SeverityLevel.NORMAL)
    public void testDTCMouldingDraft() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        guidancePage = explorePage.uploadFile("ScenarioNoDraft", new FileResourceUtil().getResourceFile("Plastic moulded cap noDraft.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Draft Issue", "Draft Angle", "CurvedWall:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("The minimum and maximum draft angle are below the recommended draft angle."));
    }
}