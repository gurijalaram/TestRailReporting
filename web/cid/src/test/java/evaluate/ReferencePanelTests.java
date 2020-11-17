package evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.PublishPage;
import pageobjects.pages.evaluate.ReferenceComparePage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;

import java.io.File;

public class ReferencePanelTests extends TestBase {
    private CidLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ReferenceComparePage referenceComparePage;

    private File resourceFile;

    @Test
    @TestRail(testCaseId = {"355"})
    @Description("Validate the compare panel updates the comparison details to the previous iteration of the scenario")
    public void referenceUpdates() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "powderMetal.stp");

        loginPage = new CidLoginPage(driver);
        referenceComparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_GERMANY.getVpe())
            .enterAnnualVolume("2600")
            .enterAnnualYears("3")
            .costScenario(2)
            .expandReferencePanel();

        assertThat(referenceComparePage.getReferenceProcessGroup(), is(ProcessGroupEnum.POWDER_METAL.getProcessGroup()));
        assertThat(referenceComparePage.getReferenceVPE(), is(VPEEnum.APRIORI_USA.getVpe()));
        assertThat(referenceComparePage.getReferenceAnnualVolume(), is("5,500"));
        assertThat(referenceComparePage.getReferenceProductionLife(), is("5.00"));
    }

    @Test
    @TestRail(testCaseId = {"356", "354"})
    @Description("Validate  the compare panel can show the comparison between the most recent public iteration")
    public void referencePublicIteration() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "MultiUpload.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
                .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .selectVPE(VPEEnum.APRIORI_USA.getVpe())
                .costScenario()
                .publishScenario(PublishPage.class)
                .selectPublishButton()
                .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
                .openScenario(scenarioName, "MultiUpload")
                .editScenario(EvaluatePage.class);

        referenceComparePage = evaluatePage
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_MEXICO.getVpe())
            .costScenario(2)
            .expandReferencePanel()
            .selectDropdown()
            .selectDropdownScenario(WorkspaceEnum.PUBLIC.name(), scenarioName);

        assertThat(referenceComparePage.getReferenceProcessGroup(), is(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup()));
        assertThat(referenceComparePage.getReferenceVPE(), is(VPEEnum.APRIORI_USA.getVpe()));
        assertThat(referenceComparePage.getReferenceMaterial(), is("ABS"));
        assertThat(referenceComparePage.getReferenceUtilization(), is(closeTo(38.09, 1)));
    }

    @Test
    @Issue("BA-1212")
    @TestRail(testCaseId = {"358"})
    @Description("Validate The user can show and hide the comparison panel in Evaluate tab")
    public void expandCollapseReferencePanel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "MultiUpload.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .expandReferencePanel()
            .collapseReferencePanel();

        assertThat(evaluatePage.isReferencePanelExpanded(), is(false));
    }

    @Test
    @TestRail(testCaseId = {"357"})
    @Description("Validate the compare panel can show the comparison between any named scenario of the same component")
    public void compareMultiScenario() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String scenarioName3 = new GenerateStringUtil().generateScenarioName();
        String componentName = "Rapid Prototyping";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.RAPID_PROTOTYPING, "Rapid Prototyping.stp");

        loginPage = new CidLoginPage(driver);
        referenceComparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .createNewScenario()
            .enterScenarioName(scenarioName2)
            .save(EvaluatePage.class)
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
            .save(EvaluatePage.class)
            .selectExploreButton()
            .openJobQueue()
            .checkJobQueueActionStatus(componentName, scenarioName3, "Save As", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .openScenario(scenarioName3, componentName)
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario()
            .expandReferencePanel()
            .selectDropdown()
            .selectDropdownScenario(WorkspaceEnum.PRIVATE.name(), scenarioName);

        assertThat(referenceComparePage.getReferenceProcessGroup(), is(ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup()));
        assertThat(referenceComparePage.getReferenceVPE(), is(VPEEnum.APRIORI_USA.getVpe()));
    }
}
