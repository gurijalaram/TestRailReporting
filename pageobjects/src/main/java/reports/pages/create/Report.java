package reports.pages.create;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reports.pages.login.LoginPage;

public class Report extends LoadableComponent<AdHocView> {

    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(id = "reportGeneratorProperties")
    private WebElement createReportDialog;

    public Report(WebDriver driver) {
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

    /**]
     * Gets dialog isDisplayed value
     * @return boolean - isDisplayed
     */
    public boolean isDialogDisplayed() {
        pageUtils.waitForElementToAppear(createReportDialog);
        return createReportDialog.isDisplayed();
    }

    /**
     * Gets dialog isEnabled value
     * @return boolean - is Enabled
     */
    public boolean isDialogEnabled() {
        pageUtils.waitForElementToAppear(createReportDialog);
        return createReportDialog.isEnabled();
    }
}
