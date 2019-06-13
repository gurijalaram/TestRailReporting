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

    /**
     * Test private criteria part
     */
    @Test
    public void testPrivateCriteriaPart() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Part", "Part Name", "Contains", "15136");
        //Assert.assertTrue();
    }

    /**
     * Test private criteria attribute
     */
    @Test
    public void testPrivateCriteriaAttribute() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Part", "Process Group", "is", "Casting");
        //Assert.assertTrue();
    }

    /**
     * Test private criteria part 'contains'
     */
    @Test
    public void testPrivateCriteriaContains() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Test");
        //Assert.assertTrue();
    }

    /**
     * Test private criteria value option
     */
    @Test
    public void testPrivateCriteriaPartValue() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Part", "Status", "is", "Waiting");
        //Assert.assertFalse();
    }

    /**
     * Test private criteria assembly
     */
    @Test
    public void testPrivateCriteriaAssembly() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Assembly", "Part Name", "Contains", "15136");
        //Assert.assertTrue();
    }

    /**
     * Test private criteria assembly status
     */
    @Test
    public void testPrivateCriteriaAssemblyStatus() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPrivateCriteria("Assembly", "Status", "is", "Nothing selected");
        //Assert.assertTrue();
    }

    /**
     * Test public criteria part
     */
    @Test
    public void testPublicCriteriaPart() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPublicCriteria("Part", "Part Name", "Contains", "15136");
        //Assert.assertFalse();
    }

    /**
     * Test public criteria assembly description
     */
    @Test
    public void testPublicCriteriaAssemblyDesc() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPublicCriteria("Assembly", "Description", "Contains", "Test");
        //Assert.assertFalse();
    }

    /**
     * Test public criteria comparison
     */
    @Test
    public void testPublicCriteriaComparison() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .filterPublicCriteria("Comparison", "Cost Maturity", "is", "Nothing selected");
        //Assert.assertTrue();
    }
}