package reports.pages.homepage;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HomePage extends LoadableComponent<HomePage> {

    private final Logger logger = LoggerFactory.getLogger(HomePage.class);

    @FindBy(css = "button[aria-label='Create Data Sources']")
    private WebElement createButton;

    @FindBy(id = "helpLink")
    private WebElement helpButton;

    // div[id='results'] > div > div:nth-child(1) > div
    @FindBy(xpath = "//div[contains(text(), 'Repository')]")
    private WebElement repositoryPageTitle;

    @FindBy(xpath = "//h2[contains(text(), 'Data Sources')]")
    private WebElement dataSourcesLink;

    @FindBy(id = "main_view")
    private WebElement viewMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(1)")
    private WebElement  viewSearchResultsMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(2)")
    private WebElement viewRepositoryMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(3)")
    private WebElement viewSchedulesMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(4)")
    private WebElement viewMessagesMenuOption;

    private Map<String, WebElement> menuOptionElements = new HashMap<>();

    private WebDriver driver;
    private PageUtils pageUtils;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
        initializeNavLocators();
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
    public boolean isCreateButtonDisplayed() {
        pageUtils.waitForElementToAppear(createButton);
        return createButton.isDisplayed();
    }

    /**
     * Wait for  help link to become visible
     * @return current page object
     */
    public HomePage waitForHelpLinkVisibility() {
        pageUtils.waitForElementToAppear(helpButton);
        return this;
    }

    /**
     * Gets isDisplayed property of Help button
     * @return isDisplayed property
     */
    public boolean isHelpButtonDisplayed() {
        return helpButton.isDisplayed();
    }

    public HomePage navigateToPage(String page) {
        pageUtils.waitForElementAndClick(viewMenuOption);
        menuOptionElements.get(page).click();
        return this;
    }

    public String getRepositoryTitleText() {
        return repositoryPageTitle.getText();
    }

    private void initializeNavLocators() {
        menuOptionElements.put("View Search Results", viewSearchResultsMenuOption);
        menuOptionElements.put("View Repository", viewRepositoryMenuOption);
        menuOptionElements.put("View Schedules", viewSchedulesMenuOption);
        menuOptionElements.put("View Messages", viewMessagesMenuOption);
;    }
}