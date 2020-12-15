package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
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

public class ProcessesPage extends LoadableComponent<ProcessesPage> {

    private final Logger logger = LoggerFactory.getLogger(ProcessesPage.class);

    @FindBy(css = "div[dir='ltr']")
    private WebElement chartContainer;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;

    public ProcessesPage(WebDriver driver) {
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
        pageUtils.waitForElementAppear(chartContainer);
    }

    /**
     * Gets total result
     *
     * @param label - the label
     * @return double
     */
    public double getTotalResult(String label) {
        By costResult = By.xpath(String.format("//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return Double.parseDouble(pageUtils.waitForElementToAppear(costResult).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Checks the specified total is displayed
     *
     * @param label - the label
     * @param value - the value
     * @return true/false
     */
    public boolean isTotalResultDisplayed(String label, String value) {
        By costResult = By.xpath(String.format("//span[.='%s']/following-sibling::span[.='%s']", label, value));
        pageUtils.waitForElementToAppear(costResult);
        return driver.findElement(costResult).isDisplayed();
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
