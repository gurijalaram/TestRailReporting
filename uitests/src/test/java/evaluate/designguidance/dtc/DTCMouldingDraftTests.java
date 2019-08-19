package test.java.evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.designguidance.GuidancePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.time.LocalDateTime;

public class DTCMouldingDraftTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private GuidancePage guidancePage;

    public DTCMouldingDraftTests() {
        super();
    }

    @Test
    @Description("Testing DTC Machining Moulding Draft")
    public void testDTCMouldingDraft() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Plastic moulded cap noDraft.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Draft Issue", "Draft Angle", "CurvedWall:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("The minimum and maximum draft angle are below the recommended draft angle."));
    }
}