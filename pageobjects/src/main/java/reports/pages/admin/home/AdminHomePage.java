package reports.pages.admin.home;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reports.pages.admin.logout.Logout;
import reports.pages.admin.manage.ScenarioExport;
import reports.pages.admin.manage.SystemDataExport;
import reports.pages.homepage.HomePage;

public class AdminHomePage extends LoadableComponent<AdminHomePage> {

    private final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private WebDriver driver;
    private PageUtils pageUtils;

    @FindBy(id = "manage.scenario-export-manager")
    private WebElement manageScenarioExportMenuOption;

    @FindBy(id = "manage.system-data-export-manager")
    private WebElement manageSystemDataExportMenuOption;

    @FindBy(id = "jasper")
    private WebElement reportMenuOption;

    @FindBy(id = "help")
    private WebElement helpMenuOption;

    @FindBy(id = "help.cost-insight_rep")
    private WebElement helpCIReportGuideMenuOption;

    @FindBy(id = "help.cost-insight_adm")
    private WebElement helpCIAdminGuideMenuOption;

    @FindBy(id = "help.scenario-expt")
    private WebElement helpScenarioExportChapterMenuOption;

    @FindBy(id = "user")
    private WebElement userMenuOption;

    @FindBy(id = "user.log-out")
    private WebElement logoutMenuOption;

    public AdminHomePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Navigates to Manage Scenario Export
     * @return Manage Scenario Export Page Object Model
     */
    public ScenarioExport navigateToManageScenarioExport() {
        pageUtils.waitForElementToAppear(manageScenarioExportMenuOption);
        manageScenarioExportMenuOption.click();
        return new ScenarioExport(driver);
    }

    /**
     * Navigates to Manage System Data Export
     * @return System Data Export Page Object Model
     */
    public SystemDataExport navigateToManageSystemDataExport() {
        pageUtils.waitForElementToAppear(manageSystemDataExportMenuOption);
        manageSystemDataExportMenuOption.click();
        return new SystemDataExport(driver);
    }

    /**
     * Navigates to Reports System
     * @return Reports Page Object Model
     */
    public HomePage navigateToReports() {
        pageUtils.waitForElementToAppear(reportMenuOption);
        reportMenuOption.click();
        return new HomePage(driver);
    }

    /**
     * Navigates to Reports System User Guide
     * @return Home Page Page Object Model (since help is external to system)
     */
    public HomePage navigateToHelpReportsGuide() {
        pageUtils.waitForElementToAppear(helpMenuOption);
        helpMenuOption.click();
        helpCIReportGuideMenuOption.click();
        return new HomePage(driver);
    }

    /**
     * Navigates to Admin System User Guide
     * @return Home Page Page Object Model (since help is external to system)
     */
    public HomePage navigateToHelpAdminGuide() {
        pageUtils.waitForElementToAppear(helpMenuOption);
        helpMenuOption.click();
        helpCIAdminGuideMenuOption.click();
        return new HomePage(driver);
    }

    /**
     * Navigates to Scenario Export Chapter Page
     * @return Scenario Export Chapter Page Object Model
     */
    public HomePage navigateToScenarioExportChapterPage() {
        pageUtils.waitForElementToAppear(helpMenuOption);
        helpMenuOption.click();
        helpScenarioExportChapterMenuOption.click();
        return new HomePage(driver);
    }

    /**
     * Navigates to Logout/Login page
     * @return Logout Page Object Model
     */
    public Logout navigateToAdminLogout() {
        pageUtils.waitForElementToAppear(userMenuOption);
        userMenuOption.click();
        logoutMenuOption.click();
        return new Logout(driver);
    }
}
