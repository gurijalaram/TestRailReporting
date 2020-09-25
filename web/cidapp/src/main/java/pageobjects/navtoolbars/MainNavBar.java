package pageobjects.navtoolbars;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.explore.ExplorePage;

/**
 * @author cfrith
 */

public class MainNavBar extends LoadableComponent<MainNavBar> {

    private final Logger LOGGER = LoggerFactory.getLogger(ExplorePage.class);

    @FindBy(xpath = "//button[.='Explore']")
    private WebElement exploreButton;

    @FindBy(xpath = "//button[.='Evaluate']")
    private WebElement evaluateButton;

    @FindBy(xpath = "//button[.='Compare']")
    private WebElement compareButton;

    @FindBy(xpath = "//button[.='Settings']")
    private WebElement settingsButton;

    @FindBy(xpath = "//button[@class='transparent dropdown-toggle btn btn-secondary'] [.='Help']")
    private WebElement helpDropdown;

    @FindBy(xpath = "//button[@class='dropdown-item'] [.='Help']")
    private WebElement helpButton;

    @FindBy(xpath = "//button[@class='dropdown-item'] [.='About']")
    private WebElement aboutButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public MainNavBar(WebDriver driver) {
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
        pageUtils.waitForElementAppear(settingsButton);
        pageUtils.waitForElementAppear(helpDropdown);
    }

    /**
     * Selects the settings button
     *
     * @return new page object
     */
    public MainNavBar openSettings() {
        pageUtils.waitForElementAndClick(settingsButton);
        return this;
    }

    /**
     * Selects the help dropdown and go to Help
     *
     * @retun new page object
     */
    public MainNavBar goToHelp() {
        pageUtils.waitForElementAndClick(helpDropdown);
        pageUtils.waitForElementAndClick(helpButton);
        return this;
    }

    /**
     * Selects the help dropdown and go to About
     *
     * @retun new page object
     */
    public MainNavBar goToAbout() {
        pageUtils.waitForElementAndClick(helpDropdown);
        pageUtils.waitForElementAndClick(aboutButton);
        return this;
    }
}
