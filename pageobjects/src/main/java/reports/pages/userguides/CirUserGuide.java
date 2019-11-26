package reports.pages.userguides;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CirUserGuide extends LoadableComponent<CirUserGuide> {

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
     * Gets count of open tabs
     * @return int - open tab count
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }

    /**
     * Gets page heading of Reports Help page
     * @return String - page title
     */
    public String getReportsUserGuidePageHeading() {
        driver.switchTo().frame(mainContentIframe);
        pageUtils.waitForElementToAppear(reportsUserGuideTitle);
        String retVal = reportsUserGuideTitle.getAttribute("textContent");
        return retVal;
    }

    /**
     * Gets page title
     * @return String - page title
     */
    public String getPageTitle() {
        String retVal = reportsUserGuideTitle.getAttribute("innerText");
        return retVal;
    }

    /**
     * Gets child window URL
     * @return String - child window URL
     */
    public String getChildWindowURL() {
        return pageUtils.windowHandler().getCurrentUrl();
    }
}
