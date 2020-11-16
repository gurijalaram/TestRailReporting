package com.pageobjects.toolbars;

import com.apriori.utils.PageUtils;

import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.help.HelpDocPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluatePanelToolbar extends LoadableComponent<EvaluatePanelToolbar> {

    private final Logger logger = LoggerFactory.getLogger(EvaluatePanelToolbar.class);

    @FindBy(css = "button[data-ap-comp='expandPanelButton']")
    private WebElement chevronButton;

    @FindBy(css = ".panel .glyphicon-remove")
    private WebElement closePanelButton;

    @FindBy(css = ".details-viewport-part .glyphicon-question-sign")
    private WebElement helpButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public EvaluatePanelToolbar(WebDriver driver) {
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

    /**
     * Closes the design guidance
     *
     * @return current page object
     */
    public EvaluatePage closePanel() {
        pageUtils.waitForElementAndClick(closePanelButton);
        return new EvaluatePage(driver);
    }

    /**
     * Expands the guidance panel
     *
     * @return current page object
     */
    public EvaluatePanelToolbar expandPanel() {
        pageUtils.waitForElementAndClick(chevronButton);
        return this;
    }

    /**
     * Clicks the help button
     *
     * @return the current page object
     */
    public HelpDocPage clickHelp() {
        pageUtils.waitForElementAndClick(helpButton);
        return new HelpDocPage(driver);
    }
}