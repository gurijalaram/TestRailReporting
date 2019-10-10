package evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.AssemblyProcessGroupEnum;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import io.qameta.allure.Issue;
import org.junit.Test;

public class AssemblyUploadTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public AssemblyUploadTests() {
        super();
    }

    @Test
    @Issue("AP-56584")
    @TestRail(testCaseId = {"2628"})
    @Description("Assembly File Upload - STEP")
    public void testAssemblyFormatSTEP() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Piston_assembly.stp"))
            .selectProcessGroup(AssemblyProcessGroupEnum.ASSEMBLY.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }
}