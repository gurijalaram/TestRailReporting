package main.java.explore;

import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.explore.FilterCriteriaPage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

public class FilterCriteriaTests extends TestBase {

    private LoginPage loginPage;
    private FilterCriteriaPage filterCriteriaPage;

    public FilterCriteriaTests() {
        super();
    }

    @Test
    public void testPrivateCriteriaPart() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Part", "Part Name", "Contains", "15136");
        filterCriteriaPage = new FilterCriteriaPage(driver);
        filterCriteriaPage.apply();
    }

    @Test
    public void testPrivateCriteriaAttribute() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Part", "Process Group", "is", "Casting");
        filterCriteriaPage = new FilterCriteriaPage(driver);
        filterCriteriaPage.apply();
    }

    @Test
    public void testPrivateCriteriaContains() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Test");
        filterCriteriaPage = new FilterCriteriaPage(driver);
        filterCriteriaPage.apply();
    }

    @Test
    public void testPrivateCriteriaPartValue() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Part", "Status", "is", "Waiting");
        filterCriteriaPage = new FilterCriteriaPage(driver);
        filterCriteriaPage.cancel();
    }

    @Test
    public void testPrivateCriteriaAssembly() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Assembly", "Part Name", "Contains", "15136");
        filterCriteriaPage = new FilterCriteriaPage(driver);
        filterCriteriaPage.apply();
    }

    @Test
    public void testPrivateCriteriaAssemblyStatus() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Assembly", "Status", "is", "Nothing selected");
        filterCriteriaPage = new FilterCriteriaPage(driver);
        filterCriteriaPage.apply();
    }

    @Test
    public void testPublicCriteriaPart() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPublicCriteria("Part", "Part Name", "Contains", "15136");
        filterCriteriaPage = new FilterCriteriaPage(driver);
        filterCriteriaPage.cancel();
    }

    @Test
    public void testPublicCriteriaAssemblyDesc() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Assembly", "Description", "Contains", "Test");
        filterCriteriaPage = new FilterCriteriaPage(driver);
        filterCriteriaPage.cancel();
    }

    @Test
    public void testPublicCriteriaComparison() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Comparison", "Cost Maturity", "is", "Nothing selected");
        filterCriteriaPage = new FilterCriteriaPage(driver);
        filterCriteriaPage.apply();
    }
}
