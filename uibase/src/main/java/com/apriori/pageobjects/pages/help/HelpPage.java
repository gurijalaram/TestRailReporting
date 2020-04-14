package com.apriori.pageobjects.pages.help;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpPage extends LoadableComponent<HelpPage> {

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

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public HelpPage(WebDriver driver) {
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
        return pageUtils.windowHandler().getTitle();
        // TODO remove code duplication
    }
}
