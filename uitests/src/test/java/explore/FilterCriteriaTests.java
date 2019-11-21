package explore;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Assert;
import org.junit.Test;

public class FilterCriteriaTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public FilterCriteriaTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"2276"})
    @Issue("AP-56845")
    @Description("Test private criteria part")
    public void testPrivateCriteriaPart() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("SheetMetal.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "SheetMetal")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "SheetMetal"), is(equalTo(1)));
    }

    @Test
    @Issue("AP-56845")
    @Description("Test private criteria attribute")
    public void testPrivateCriteriaAttribute() {
        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .filterCriteria()
            .filterPrivateCriteria("Part", "Process Group", "is", "Casting")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }

    @Test
    @Issue("AP-56845")
    @Description("Test private criteria part contains")
    public void testPrivateCriteriaContains() {
        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Test")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }

    @Test
    @Issue("AP-56845")
    @Description("Test private criteria value option")
    public void testPrivateCriteriaPartValue() {
        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .filterCriteria()
            .filterPrivateCriteria("Part", "Status", "is", "Waiting")
            .apply(ExplorePage.class);
        //Assert.assertFalse();
    }

    @Test
    @Issue("AP-56845")
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {
        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .filterCriteria()
            .filterPrivateCriteria("Assembly", "Part Name", "Contains", "15136")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }

    @Test
    @Issue("AP-56845")
    @Description("Test private criteria assembly status")
    public void testPrivateCriteriaAssemblyStatus() {
        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .filterCriteria()
            .filterPrivateCriteria("Assembly", "Status", "is", "Nothing selected")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }

    @Test
    @TestRail(testCaseId = {"2277"})
    @Issue("AP-56845")
    @Description("Test public criteria part")
    public void testPublicCriteriaPart() {
        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Push Pin.stp"))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "Push Pin")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "Push Pin"), is(equalTo(1)));
    }

    @Test
    @Issue("AP-56845")
    @Description("Test public criteria assembly description")
    public void testPublicCriteriaAssemblyDesc() {
        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .filterCriteria()
            .filterPublicCriteria("Assembly", "Description", "Contains", "Test")
            .apply(ExplorePage.class);
        //Assert.assertFalse();
    }

    @Test
    @Issue("AP-56845")
    @Description("Test public criteria comparison")
    public void testPublicCriteriaComparison() {
        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .filterCriteria()
            .filterPublicCriteria("Comparison", "Cost Maturity", "is", "Nothing selected")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }
}