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
import main.java.pages.evaluate.designguidance.DesignGuidancePage;
import main.java.pages.evaluate.designguidance.GuidancePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.properties.reader.FileResourceReader;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class DTCMouldingDraftTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private DesignGuidancePage designGuidancePage;

    public DTCMouldingDraftTests() {
        super();
    }

    @Test
    @Description("Testing DTC Machining Moulding Draft")
    @Severity(SeverityLevel.NORMAL)
    public void testDTCMouldingDraft() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("ScenarioNoDraft", new FileResourceReader().getResourceFile("Plastic moulded cap noDraft.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openGuidanceTab()
            .selectIssueTypeAndGCD("Draft Issue", "Draft Angle", "CurvedWall:1");

        assertThat(new GuidancePage(driver).getGuidanceMessage(), containsString("The minimum and maximum draft angle are below the recommended draft angle."));
    }
}