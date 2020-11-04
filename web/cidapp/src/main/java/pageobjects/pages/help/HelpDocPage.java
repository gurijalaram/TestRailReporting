package pageobjects.pages.help;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpDocPage extends LoadableComponent<HelpDocPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(HelpDocPage.class);

    @FindBy(css = ".navbar-brand")
    private WebElement brandImage;

    @FindBy(xpath = "//div[@id='gdpr']//button[.='Yes I Agree']")
    private WebElement agreeButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public HelpDocPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(brandImage);
    }

    /**
     * Selects button to agree to gdpr
     * @return current page object
     */
    public HelpDocPage clickAgreeButton() {
        pageUtils.waitForElementAndClick(agreeButton);
        return this;
    }

    /**
     * Gets the page title
     * @return string
     */
    public String getChildPageTitle() {
        return pageUtils.windowHandler(1).getTitle();
    }
}
