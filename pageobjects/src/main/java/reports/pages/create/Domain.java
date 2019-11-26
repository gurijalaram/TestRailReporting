package reports.pages.create;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Domain extends LoadableComponent<Domain> {

    private final Logger logger = LoggerFactory.getLogger(Domain.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(css = "div[data-name='repositoryResourceChooserDialog']")
    private WebElement domainDialog;

    public Domain(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(domainDialog);
        return domainDialog.isDisplayed();
    }

    /**
     * Gets dialog isEnabled value
     * @return boolean - is Enabled
     */
    public boolean isDialogEnabled() {
        pageUtils.waitForElementToAppear(domainDialog);
        return domainDialog.isEnabled();
    }
}
