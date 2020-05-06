package compare;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.common.ScenarioTablePage;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.toolbars.GenericHeader;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.AssemblyProcessGroupEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class EditPublicComparisonTests extends TestBase {

    private CIDLoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public EditPublicComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"421"})
    @Description("Test publishing a comparison shows the comparison in the comparison table")
    public void testPublishComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CIDLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);

        explorePage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace());

        assertThat(explorePage.findComparison(testComparisonName).isDisplayed(), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"427"})
    @Description("Test editing a published comparison shows the comparison view")
    public void testEditPublicComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CIDLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);

        explorePage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.editScenario(ComparePage.class);

        assertThat(comparePage.isComparisonName(testComparisonName.toUpperCase()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"458"})
    @Description("Delete private scenarios included in comparison from private workspace")
    public void testRemoveFromComparison() {

        resourceFile = new FileResourceUtil().getResourceFile("PowderMetalShaft.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CIDLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "PowderMetalShaft")
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(testScenarioName, "PowderMetalShaft")
            .apply(ComparePage.class)
            .removeScenarioFromCompareView("PowderMetalShaft", testScenarioName);

        assertThat(comparePage.scenarioIsNotInComparisonView(testScenarioName, "PowderMetalShaft"), is(true));
    }

    @Test
    @Issue("BA-999")
    @Description("Test you can change the basis of your comparison")
    public void testChangeComparisonBasis() {

        resourceFile = new FileResourceUtil().getResourceFile("Assembly2.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testAssemblyName = "Assembly2";
        String partName = "PART0001";

        loginPage = new CIDLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(AssemblyProcessGroupEnum.ASSEMBLY.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .openScenario(scenarioName, partName)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Assembly", "Part Name", "Contains", testAssemblyName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, testAssemblyName)
            .apply(ComparePage.class);

        new ComparePage(driver).addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", partName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, partName)
            .apply(ComparePage.class);

        new ComparePage(driver).setBasis(partName,scenarioName);

        assertThat(new ComparePage(driver).isBasis(partName, scenarioName), is(true));
        assertThat(new ComparePage(driver).isBasisButtonPresent(testAssemblyName, scenarioName), is(true));
    }
}