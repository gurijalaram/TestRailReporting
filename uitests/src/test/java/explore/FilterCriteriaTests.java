package test.java.explore;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

public class FilterCriteriaTests extends TestBase {

    private LoginPage loginPage;

    public FilterCriteriaTests() {
        super();
    }

    @Test
    @Description("Test private criteria part")
    @Severity(SeverityLevel.MINOR)
    public void testPrivateCriteriaPart() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "15136")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }

    @Test
    @Description("Test private criteria attribute")
    @Severity(SeverityLevel.MINOR)
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
    @Severity(SeverityLevel.MINOR)
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
    @Severity(SeverityLevel.MINOR)
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
    @Severity(SeverityLevel.MINOR)
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
    @Severity(SeverityLevel.MINOR)
    public void testPrivateCriteriaAssemblyStatus() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .filterCriteria()
            .filterPrivateCriteria("Assembly", "Status", "is", "Nothing selected")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }

    @Test
    @Description("Test public criteria part")
    @Severity(SeverityLevel.MINOR)
    public void testPublicCriteriaPart() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "15136")
            .apply(ExplorePage.class);
        //Assert.assertFalse();
    }

    @Test
    @Description("Test public criteria assembly description")
    @Severity(SeverityLevel.MINOR)
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
    @Severity(SeverityLevel.MINOR)
    public void testPublicCriteriaComparison() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .filterCriteria()
            .filterPublicCriteria("Comparison", "Cost Maturity", "is", "Nothing selected")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }
}