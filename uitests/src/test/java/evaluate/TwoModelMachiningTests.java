package evaluate;

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
    private File twoModelFile2;

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
    @TestRail(testCaseId = {"3941"})
    public void testOpenSourceModel() {

        String sourceScenarioName = new Util().getScenarioName();
        String sourcePartName = "VulcainCasting";

        resourceFile = new FileResourceUtil().getResourceFile("VulcainCasting.CATPart");
        twoModelFile = new FileResourceUtil().getResourceFile("VulcainMachined.CATPart");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(sourceScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
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

    @Test
    @Description("Validate the user can have multi level 2 model parts (source has been 2 model machined)")
    @TestRail(testCaseId = {"4133"})
    public void multiLevel2Model() {

        String sourceScenarioName = new Util().getScenarioName();
        String twoModel1ScenarioName = new Util().getScenarioName();
        String twoModel2ScenarioName = new Util().getScenarioName();
        String sourcePartName = "2modeltest-cast";
        String twoModel1PartName = "2modeltest-machine1";

        resourceFile = new FileResourceUtil().getResourceFile("2modeltest-cast.SLDPRT");
        twoModelFile = new FileResourceUtil().getResourceFile("2modeltest-machine1.SLDPRT");
        twoModelFile2 = new FileResourceUtil().getResourceFile("2modeltest-machine2.SLDPRT");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(sourceScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getUtilizationPercentage(), is("96.98"));
        assertThat(evaluatePage.getBurdenedCost("12.88"), is(true));
        assertThat(evaluatePage.isFinishMass("2.33"), is(true));

        evaluatePage.selectExploreButton()
            .refreshCurrentPage()
            .uploadFile(twoModel1ScenarioName, twoModelFile)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .apply(EvaluatePage.class)
            .costScenario();

        //assertThat(evaluatePage.getUtilizationPercentage(), is("82.71"));
        assertThat(evaluatePage.getBurdenedCost("16.69"), is(true));
        assertThat(evaluatePage.isFinishMass("1.93"), is(true));

        evaluatePage.selectExploreButton()
            .refreshCurrentPage()
            .uploadFile(twoModel2ScenarioName, twoModelFile2)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(twoModel1ScenarioName, twoModel1PartName)
            .apply(EvaluatePage.class)
            .costScenario();

        //assertThat(evaluatePage.getUtilizationPercentage(), is("83.78"));
        assertThat(evaluatePage.getBurdenedCost("19.99"), is(true));
        assertThat(evaluatePage.isFinishMass("1.62"), is(true));
    }
}
