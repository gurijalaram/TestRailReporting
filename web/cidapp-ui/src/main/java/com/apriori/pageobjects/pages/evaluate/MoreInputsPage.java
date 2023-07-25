package com.apriori.pageobjects.pages.evaluate;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.help.HelpDocPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class MoreInputsPage extends LoadableComponent<MoreInputsPage> {

    private static final Logger logger = LoggerFactory.getLogger(MoreInputsPage.class);

    @FindBy(xpath = "//div[.='Basic Attributes']")
    private WebElement attributesText;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;

    public MoreInputsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(attributesText);
    }

    /**
     * Closes current panel
     *
     * @return new page object
     */
    public EvaluatePage closePanel() {
        return panelController.closePanel();
    }

    /**
     * Opens the help page
     *
     * @return new page object
     */
    public HelpDocPage openHelp() {
        return panelController.openHelp();
    }
}