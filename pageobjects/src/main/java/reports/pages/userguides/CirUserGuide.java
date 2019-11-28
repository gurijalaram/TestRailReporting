package reports.pages.userguides;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reports.pages.header.ReportsHeader;

public class CirUserGuide extends ReportsHeader {

    private Logger logger = LoggerFactory.getLogger(CirUserGuide.class);

    @FindBy(xpath = "//*[contains(text(), 'Cost Insight Report:User Guide')]")
    private WebElement pageTitle;

    @FindBy(css = "iframe[id='page_iframe']")
    private WebElement mainContentIframe;

    @FindBy(css = ".Documentation_Cover_Page_Title")
    private WebElement reportsUserGuideTitle;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CirUserGuide(WebDriver driver) {
        super(driver);
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
     * Gets current URL of new tab
     * @return String
     */
    public String getCurrentUrl() {
        return pageUtils.getTabTwoUrl();
    }

    /**
     * Gets count of open tabs
     * @return int
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }

    /**
     * Gets page heading of Reports Help page
     * @return String - page title
     */
    public String getReportsUserGuidePageHeading() {
        pageUtils.windowHandler();
        pageUtils.waitForElementToAppear(mainContentIframe);
        driver.switchTo().frame(mainContentIframe);
        return reportsUserGuideTitle.getAttribute("textContent");
    }
}
