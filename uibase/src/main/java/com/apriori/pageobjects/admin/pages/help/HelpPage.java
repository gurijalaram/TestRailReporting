package com.apriori.pageobjects.admin.pages.help;

import pageobjects.header.PageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpPage extends PageHeader {

    private final Logger logger = LoggerFactory.getLogger(HelpPage.class);

    @FindBy(css = "[data-ap-help='CIDesign:NGHELP:CID_QS_title_page']")
    private WebElement onlineHelpButton;

    @FindBy(css = "[data-ap-help='CIDesign:NGHELP:NG_DQS_chap_get_to_work']")
    private WebElement gettingStartedButton;

    @FindBy(css = "[data-ap-help='CIDesign:NGRN:NG_whats_new_2']")
    private WebElement whatsNewButton;

    @FindBy(css = "[data-ap-nav-dialog='showAbout']")
    private WebElement aboutaPrioriButton;

    @FindBy(css = "a.navbar-help")
    private WebElement helpButton;

    @FindBy(css = "body > h1")
    private WebElement heading;

    private WebDriver driver;
    private PageUtils pageUtils;

    public HelpPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(aboutaPrioriButton);
        pageUtils.waitForElementToAppear(onlineHelpButton);
    }

    /**
     * Closes the Help menu
     *
     * @param className - the class the method should return
     * @param <T>       - the return type
     * @return new page object
     */
    public <T> T closeHelpMenu(Class<T> className) {
        pageUtils.waitForElementAndClick(helpButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects the Online Help Button
     *
     * @return new page object
     */
    public HelpPage clickOnlineHelp() {
        pageUtils.waitForElementAndClick(onlineHelpButton);
        return this;
    }

    public String getChildPageTitle() {
        return pageUtils.windowHandler(1).getTitle();
        // TODO remove code duplication
    }

    /**
     * Ensures page is loaded before continuing
     */
    public HelpPage ensurePageIsLoaded() {
        pageUtils.waitForElementToAppear(heading);
        pageUtils.waitForElementToBeClickable(heading);
        return this;
    }

    /**
     * Gets page heading
     *
     * @return - string
     */
    public String getPageHeading() {
        return heading.getText();
    }

    /**
     * Gets window url
     *
     * @return - string
     */
    public String getChildWindowURL() {
        return pageUtils.getTabTwoUrl();
    }

    /**
     * Gets number of open tabs
     *
     * @return int - number of tabs
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }
}
