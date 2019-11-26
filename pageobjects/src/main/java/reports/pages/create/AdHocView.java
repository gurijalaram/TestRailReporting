package reports.pages.create;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdHocView extends LoadableComponent<AdHocView> {

    private final Logger logger = LoggerFactory.getLogger(AdHocView.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(xpath = "//div[@id='display']/div[2]/div/div[1]/div")
    private WebElement adHocViewPageTitle;

    @FindBy(xpath = "//div[contains(@class, 'sourceDialogNew ')]")
    private WebElement adHocViewDiaolog;

    public AdHocView(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Get page title text
     * @return String - page title text
     */
    public String getAdHocViewTitleText() {
        pageUtils.waitForElementToAppear(adHocViewPageTitle);
        return adHocViewPageTitle.getText();
    }

    /**]
     * Gets dialog isDisplayed value
     * @return boolean - isDisplayed
     */
    public boolean isDialogDisplayed() {
        pageUtils.waitForElementToAppear(adHocViewDiaolog);
        return adHocViewDiaolog.isDisplayed();
    }

    /**
     * Gets dialog isEnabled value
     * @return boolean - is Enabled
     */
    public boolean isDialogEnabled() {
        pageUtils.waitForElementToAppear(adHocViewDiaolog);
        return adHocViewDiaolog.isEnabled();
    }
}
