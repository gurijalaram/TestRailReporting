package com.apriori.pageobjects.help;

import com.apriori.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpPage extends LoadableComponent<HelpPage> {

    private static final Logger logger = LoggerFactory.getLogger(HelpPage.class);

    @FindBy(css = "[data-icon='user-check']")
    private WebElement userGuideButton;

    @FindBy(css = "[data-icon='flag']")
    private WebElement gettingStartedButton;

    @FindBy(css = "[data-ap-help='CIDesign:NGRN:NG_whats_new_2']")
    private WebElement whatsNewButton;

    @FindBy(xpath = "//button[.='About aPriori']")
    private WebElement aboutaPrioriButton;

    @FindBy(css = "a.navbar-help")
    private WebElement helpButton;

    @FindBy(css = ".DocumentationCoverPageTitle")
    private WebElement userGuideTitle;

    private WebDriver driver;
    private PageUtils pageUtils;

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
        pageUtils.waitForElementToAppear(userGuideButton);
    }

    /**
     * Closes the Help menu
     *
     * @param klass - the class the method should return
     * @param <T>   - the return type
     * @return new page object
     */
    public <T> T closeHelpMenu(Class<T> klass) {
        pageUtils.waitForElementAndClick(helpButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Selects the Online Help Button
     *
     * @return new page object
     */
    public HelpPage clickUserGuide() {
        pageUtils.waitForElementAndClick(userGuideButton);
        return this;
    }

    /**
     * Gets child page title
     *
     * @return string
     */
    public String getChildPageTitle() {
        pageUtils.windowHandler(1);
        return pageUtils.waitForElementToAppear(userGuideTitle).getText();
    }
}
