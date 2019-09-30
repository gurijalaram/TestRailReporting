package test.java.explore;

import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class FilterCriteriaTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public FilterCriteriaTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"2276"})
    @Description("Test private criteria part")
    public void testPrivateCriteriaPart() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("SheetMetal.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "SheetMetal")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "SheetMetal") > 0, is(true));
    }

    @Test
    @Description("Test private criteria attribute")
    public void testPrivateCriteriaAttribute() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .filterCriteria()
            .filterPrivateCriteria("Part", "Process Group", "is", "Casting")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }

    @Test
    @Description("Test private criteria part contains")
    public void testPrivateCriteriaContains() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Test")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }

    @Test
    @Description("Test private criteria value option")
    public void testPrivateCriteriaPartValue() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .filterCriteria()
            .filterPrivateCriteria("Part", "Status", "is", "Waiting")
            .apply(ExplorePage.class);
        //Assert.assertFalse();
    }

    @Test
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .filterCriteria()
            .filterPrivateCriteria("Assembly", "Part Name", "Contains", "15136")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }

    @Test
    @Description("Test private criteria assembly status")
    public void testPrivateCriteriaAssemblyStatus() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .filterCriteria()
            .filterPrivateCriteria("Assembly", "Status", "is", "Nothing selected")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }

    @Test
    @TestRail(testCaseId = {"2277"})
    @Description("Test public criteria part")
    public void testPublicCriteriaPart() {
        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Push Pin.stp"))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .publishScenario();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "Push Pin")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "Push Pin") > 0, is(true));
    }

    @Test
    @Description("Test public criteria assembly description")
    public void testPublicCriteriaAssemblyDesc() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .filterCriteria()
            .filterPublicCriteria("Assembly", "Description", "Contains", "Test")
            .apply(ExplorePage.class);
        //Assert.assertFalse();
    }

    @Test
    @Description("Test public criteria comparison")
    public void testPublicCriteriaComparison() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .filterCriteria()
            .filterPublicCriteria("Comparison", "Cost Maturity", "is", "Nothing selected")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }
}