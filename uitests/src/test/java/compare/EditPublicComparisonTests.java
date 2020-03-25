package compare;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.GenericHeader;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.compare.ComparisonTablePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
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

        String testComparisonName = new Util().getComparisonName();

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

        String testComparisonName = new Util().getComparisonName();

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
        String testScenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

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
            .apply(ComparisonTablePage.class)
            .selectScenario(testScenarioName, "PowderMetalShaft")
            .apply()
            .removeScenarioFromCompareView("PowderMetalShaft", testScenarioName);

        assertThat(comparePage.getScenarioInComparisonView(testScenarioName, "PowderMetalShaft"), is(0));
    }

    @Test
    @Description("Test you can change the basis of your comparison")
    public void testChangeComparisonBasis() {

        resourceFile = new FileResourceUtil().getResourceFile("SandCast.x_t");
        String testScenarioName = new Util().getScenarioName();
        String testScenarioName2 = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();
        String testPartName = "SANDCAST";

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario()
            .createNewScenario()
            .enterScenarioName(testScenarioName2)
            .save();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.SAVING_NEW_SCENARIO.getCostingText()), is(true));
        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));

        evaluatePage = new EvaluatePage(driver);
        comparePage = evaluatePage.createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", testPartName)
            .apply(ComparisonTablePage.class)
            .selectScenario(testScenarioName, testPartName)
            .apply()
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", testPartName)
            .apply(ComparisonTablePage.class)
            .selectScenario(testScenarioName2, testPartName)
            .apply()
            .setBasis(testScenarioName2);

        assertThat(comparePage.isComparisonBasis(testScenarioName2), is(false));
    }
}