package evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.evaluate.ReferenceComparePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateNameUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class ReferencePanelTests extends TestBase {
    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ReferenceComparePage referenceComparePage;

    private File resourceFile;

    @Test
    @TestRail(testCaseId = {"355"})
    @Description("Validate the compare panel updates the comparison details to the previous iteration of the scenario")
    public void referenceUpdates() {

        resourceFile =  new FileResourceUtil().getResourceFile("powderMetal.stp");

        loginPage = new CIDLoginPage(driver);
        referenceComparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateNameUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_GERMANY.getVpe())
            .enterAnnualVolume("2600")
            .enterAnnualYears("3")
            .costScenario(2)
            .openReferenceCompare();

        assertThat(referenceComparePage.isReferenceProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup()), is(true));
        assertThat(referenceComparePage.isReferenceVPE(VPEEnum.APRIORI_USA.getVpe()), is(true));
        assertThat(referenceComparePage.isReferenceAnnualVolume("5,500"), is(true));
        assertThat(referenceComparePage.isReferenceProductionLife("5"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"356", "354", "957"})
    @Description("Validate  the compare panel can show the comparison between the most recent public iteration")
    public void referencePublicIteration() {

        resourceFile = new FileResourceUtil().getResourceFile("MultiUpload.stp");
        String scenarioName = new GenerateNameUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(scenarioName, "MultiUpload")
            .editScenario(EvaluatePage.class);

        evaluatePage = new EvaluatePage(driver);
        referenceComparePage = evaluatePage
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_MEXICO.getVpe())
            .costScenario(2)
            .openReferenceCompare()
            .selectDropdown()
            .selectDropdownScenario(WorkspaceEnum.PUBLIC.name(), scenarioName);

        assertThat(referenceComparePage.isReferenceProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup()), is(true));
        assertThat(referenceComparePage.isReferenceVPE(VPEEnum.APRIORI_USA.getVpe()), is(true));
        assertThat(referenceComparePage.isReferenceMaterial("ABS"), is(true));
        assertThat(referenceComparePage.isReferenceUtilization("38.08"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"358", "958"})
    @Description("Validate The user can show and hide the comparison panel in Evaluate tab")
    public void expandCollapseReferencePanel() {

        resourceFile = new FileResourceUtil().getResourceFile("MultiUpload.stp");
        String scenarioName = new GenerateNameUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        referenceComparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openReferenceCompare();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.collapseReferenceCompare();

        assertThat(evaluatePage.isReferencePanelExpanded(), is(false));
    }

    @Test
    @TestRail(testCaseId = {"357"})
    @Description("Validate the compare panel can show the comparison between any named scenario of the same component")
    public void compareMultiScenario() {

        String scenarioName = new GenerateNameUtil().generateScenarioName();
        String scenarioName2 = new GenerateNameUtil().generateScenarioName();
        String scenarioName3 = new GenerateNameUtil().generateScenarioName();
        String componentName = "Rapid Prototyping";
        resourceFile = new FileResourceUtil().getResourceFile("Rapid Prototyping.stp");

        loginPage = new CIDLoginPage(driver);
        referenceComparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .createNewScenario()
            .enterScenarioName(scenarioName2)
            .save()
            .selectExploreButton()
            .openJobQueue()
            .checkJobQueueActionStatus(componentName, scenarioName2, "Save As", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.RECENT.getWorkspace())
            .openScenario(scenarioName2, componentName)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_MEXICO.getVpe())
            .costScenario(2)
            .createNewScenario()
            .enterScenarioName(scenarioName3)
            .save()
            .selectExploreButton()
            .openJobQueue()
            .checkJobQueueActionStatus(componentName, scenarioName3, "Save As", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .openScenario(scenarioName3, componentName)
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario()
            .openReferenceCompare()
            .selectDropdown()
            .selectDropdownScenario(WorkspaceEnum.PRIVATE.name(), scenarioName);

        assertThat(referenceComparePage.isReferenceProcessGroup(ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup()), is(true));
        assertThat(referenceComparePage.isReferenceVPE(VPEEnum.APRIORI_USA.getVpe()), is(true));
    }
}
