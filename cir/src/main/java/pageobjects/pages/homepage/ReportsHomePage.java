package pageobjects.pages.homepage;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.header.ReportsPageHeader;

public class ReportsHomePage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(ReportsHomePage.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    public ReportsHomePage(WebDriver driver) {
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
     * Check if Create button is displayed
     *
     * @return Visibility of button
     */
    public boolean isCreateDashboardsButtonDisplayed() {
        pageUtils.waitForElementToAppear(createDashboardsButton);
        return createDashboardsButton.isDisplayed();
    }
}