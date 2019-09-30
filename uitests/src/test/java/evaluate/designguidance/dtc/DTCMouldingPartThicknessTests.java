package test.java.evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class DTCMouldingPartThicknessTests extends TestBase {

    private LoginPage loginPage;
    private GuidancePage guidancePage;

    public DTCMouldingPartThicknessTests() {
        super();
    }

    @Test
    @Description("Testing DTC Moulding Thickness Minimum")
    public void testDTCMouldingThicknessMin() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap thinPart.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Material  Issue", "Minimum Wall Thickness", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Injection Mold is not feasible. Part Thickness is less than the minimum limit with this material."));
    }

    @Test
    @Description("Testing DTC Moulding Thickness Maximum")
    public void testDTCMouldingThicknessMax() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap thickPart.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Material  Issue", "Maximum Wall Thickness", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Injection Mold is not feasible. Part Thickness is more than the maximum limit with this material."));
    }
}