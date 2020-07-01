package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessSetupOptionsPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class TwoModelMachiningTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ProcessSetupOptionsPage processSetupOptionsPage;
    private GuidancePage guidancePage;

    private File resourceFile;
    private File twoModelFile;
    private File twoModelFile2;

    public TwoModelMachiningTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate Source and util tile appears when 2 MM is selected")
    @TestRail(testCaseId = {"3927", "3928", "3929", "3930", "3947"})
    public void testTwoModelMachining() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "casting_before_machining";

        resourceFile = new FileResourceUtil().getResourceFile("casting_BEFORE_machining.stp");
        twoModelFile = new FileResourceUtil().getResourceFile("casting_AFTER_machining.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), twoModelFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(testScenarioName, "casting_BEFORE_machining")
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getSourceMaterial(), is("Aluminum, Cast, ANSI AL380.0"));
        assertThat(evaluatePage.getSourcePartName(), is(sourcePartName.toUpperCase()));
        assertThat(evaluatePage.getSourceScenarioName(), is(testScenarioName));

        processSetupOptionsPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Source Component")
            .selectOptions()
            .selectOverrideSensitivityButton()
            .setCadModelSensitivity("42")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Source Component")
            .selectOptions();

        assertThat(processSetupOptionsPage.getCadModelSensitivity(), is("42"));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate the User can open the source part in the evaluate tab")
    @TestRail(testCaseId = {"3844", "3941"})
    public void testOpenSourceModel() {

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "VulcainCasting";

        resourceFile = new FileResourceUtil().getResourceFile("VulcainCasting.CATPart");
        twoModelFile = new FileResourceUtil().getResourceFile("VulcainMachined.CATPart");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(sourceScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-medium-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));

        evaluatePage.selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), twoModelFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .apply(EvaluatePage.class)
            .costScenario()
            .openSourceScenario();

        assertThat(evaluatePage.getCurrentScenarioName(sourceScenarioName), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate the user can have multi level 2 model parts (source has been 2 model machined)")
    @TestRail(testCaseId = {"3940", "3946", "4133"})
    public void multiLevel2Model() {

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModel1ScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModel2ScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "2modeltest-cast";
        String twoModel1PartName = "2modeltest-machine1";

        resourceFile = new FileResourceUtil().getResourceFile("2modeltest-cast.SLDPRT");
        twoModelFile = new FileResourceUtil().getResourceFile("2modeltest-machine1.SLDPRT");
        twoModelFile2 = new FileResourceUtil().getResourceFile("2modeltest-machine2.SLDPRT");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(sourceScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getUtilizationPercentage(), is(closeTo(96.98, 1)));
        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(15.36, 1)));
        assertThat(evaluatePage.getFinishMass(), is(closeTo(2.33, 1)));

        evaluatePage.selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(twoModel1ScenarioName, twoModelFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getTwoModelUtilizationPercentage(), is(closeTo(82.71, 1)));
        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(18.86, 1)));
        assertThat(evaluatePage.getTwoModelFinishMass(), is(closeTo(1.93, 1)));

        evaluatePage.selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(twoModel2ScenarioName, twoModelFile2, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(twoModel1ScenarioName, twoModel1PartName)
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getTwoModelUtilizationPercentage(), is(closeTo(83.78, 1)));
        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(22.61, 1)));
        assertThat(evaluatePage.getTwoModelFinishMass(), is(closeTo(1.62, 1)));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate the User can open a public source part in the evaluate tab")
    @TestRail(testCaseId = {"4178", "4138"})
    public void testOpenPublicSourceModel() {

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Raw Casting";
        String twoModelPartName = "Machined Casting";

        resourceFile = new FileResourceUtil().getResourceFile("Raw Casting.prt");
        twoModelFile = new FileResourceUtil().getResourceFile("Machined Casting.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(sourceScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(twoModelScenarioName, twoModelFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .apply(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Scenario Name", "Contains", twoModelScenarioName)
            .apply(ExplorePage.class)
            .openScenario(twoModelScenarioName, twoModelPartName)
            .openSourceScenario();

        assertThat(evaluatePage.getCurrentScenarioName(sourceScenarioName), is(true));
    }

    @Test
    @Description("Validate the user can switch the source part")
    @TestRail(testCaseId = {"3845", "4134", "4315"})
    public void switchSourcePart() {

        String sourceScenarioName = new GenerateStringUtil().generateScenarioName();
        String source2ScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "Die Casting Lower Control Arm (As Cast)";
        String source2PartName = "Die Casting Lower Control Arm (Source1)";

        resourceFile = new FileResourceUtil().getResourceFile("Die Casting Lower Control Arm (As Cast).SLDPRT");
        twoModelFile = new FileResourceUtil().getResourceFile("Die Casting Lower Control Arm (Source1).SLDPRT");
        twoModelFile2 = new FileResourceUtil().getResourceFile("Die Casting Lower Control Arm (As Machined2).SLDPRT");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(sourceScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-low-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Low"), is(true));

        evaluatePage.selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(source2ScenarioName, twoModelFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(twoModelScenarioName, twoModelFile2, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(sourceScenarioName, sourcePartName)
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getSourceScenarioName(), is(sourceScenarioName));
        assertThat(evaluatePage.getSourcePartName(), is(sourcePartName.toUpperCase()));
        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(18.05, 1)));

        evaluatePage.selectSourcePart()
            .highlightScenario(source2ScenarioName, source2PartName)
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getSourceScenarioName(), is(source2ScenarioName));
        assertThat(evaluatePage.getSourcePartName(), is(source2PartName.toUpperCase()));
        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(8.74, 1)));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate the user cannot use two completely different CAD models")
    @TestRail(testCaseId = {"3948"})
    public void testTwoModelCorrectCADModels() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String sourcePartName = "casting_before_machining";

        resourceFile = new FileResourceUtil().getResourceFile("PowderMetalShaft.stp");
        twoModelFile = new FileResourceUtil().getResourceFile("casting_AFTER_machining.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), twoModelFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart()
            .highlightScenario(testScenarioName, "PowderMetalShaft")
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_FAILURE.getCostingText()), is(true));

        guidancePage = evaluatePage.openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Costing Failed", "Units of the model of the stock differ from the units of the finished model.", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Units of the model of the stock differ from the units of the finished model."));
    }
}
