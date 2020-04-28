package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class TwoModelMachiningTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;
    private File twoModelFile;

    public TwoModelMachiningTests() {
        super();
    }

    @Test
    @Description("Validate Source and util tile appears when 2 MM is selected")
    @TestRail(testCaseId = {"3927", "3928", "3929", "3930"})
    public void testTwoModelMachining() {

        String testScenarioName = new Util().getScenarioName();

        resourceFile = new FileResourceUtil().getResourceFile("casting_BEFORE_machining.stp");
        twoModelFile = new FileResourceUtil().getResourceFile("casting_AFTER_machining.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadFile(new Util().getScenarioName(), twoModelFile)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(testScenarioName, "casting_BEFORE_machining")
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getSourceMaterial(), is("Aluminum, Cast, ANSI AL380.0"));
        assertThat(evaluatePage.getSourcePartName(), is("CASTING_BEFORE_MACHINING"));
        assertThat(evaluatePage.getSourceScenarioName(), is(testScenarioName));
    }

    @Test
    @Description("Validate the User can open the source part in the evaluate tab")
    @TestRail(testCaseId = {"3844", "3941"})
    public void testOpenSourceModel() {

        String sourceScenarioName = new Util().getScenarioName();
        String sourcePartName = "VulcainCasting";

        resourceFile = new FileResourceUtil().getResourceFile("VulcainCasting.CATPart");
        twoModelFile = new FileResourceUtil().getResourceFile("VulcainMachined.CATPart");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(sourceScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-medium-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Medium"));

        evaluatePage.selectExploreButton()
            .refreshCurrentPage()
            .uploadFile(new Util().getScenarioName(), twoModelFile)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .apply(EvaluatePage.class)
            .costScenario()
            .openSourceScenario();

        assertThat(evaluatePage.getCurrentScenarioName(sourceScenarioName), is(true));
    }
}
