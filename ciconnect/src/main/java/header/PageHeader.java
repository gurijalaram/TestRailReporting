package header;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */
public class PageHeader extends LoadableComponent<PageHeader> {

    private static final Logger logger = LoggerFactory.getLogger(PageHeader.class);

    @FindBy(css = "div#root_button-33 > button")
    private WebElement settingsBtn;

    @FindBy(css = "div#root_button-29 > button")
    private WebElement helpBtn;

    @FindBy(css = "div#root_button-30 > button")
    private WebElement userInfoDropdown;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PageHeader(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(settingsBtn);
        pageUtils.waitForElementToAppear(userInfoDropdown);
    }
}