package com.apriori.pageobjects.common;

import com.apriori.PageUtils;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.help.HelpDocPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class PanelController {

    private static final Logger logger = LoggerFactory.getLogger(PanelController.class);

    @FindBy(css = "svg[data-icon='question']")
    private WebElement questionButton;

    @FindBy(css = "svg[data-icon='xmark']")
    private WebElement closeButton;

    @FindBy(xpath = "//span[normalize-space(@class)='Resizer Resizer horizontal']")
    private WebElement panelResizer;

    private PageUtils pageUtils;
    private WebDriver driver;

    public PanelController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Closes current panel
     *
     * @return new page object
     */
    public EvaluatePage closePanel() {
        pageUtils.waitForElementAndClick(closeButton);
        return new EvaluatePage(driver);
    }

    /**
     * Opens the help page
     *
     * @return new page object
     */
    public HelpDocPage openHelp() {
        pageUtils.waitForElementAndClick(questionButton);
        pageUtils.switchToWindow(1);
        return new HelpDocPage(driver);
    }
}
