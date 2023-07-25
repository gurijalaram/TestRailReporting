package com.apriori.pageobjects.pages.evaluate.materialprocess;

import static org.junit.Assert.assertTrue;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.help.HelpDocPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockPage extends LoadableComponent<StockPage> {

    private static final Logger logger = LoggerFactory.getLogger(StockPage.class);

    @FindBy(xpath = "//button[.='Stock']")
    private WebElement stockTab;

    @FindBy(xpath = "//div[.='Dimensions']")
    private WebElement panelHeading;

    private PageUtils pageUtils;
    private WebDriver driver;
    private PanelController panelController;

    public StockPage(WebDriver driver) {
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
        assertTrue("Part Nesting tab was not selected", stockTab.getAttribute("class").contains("active"));
        pageUtils.waitForElementToAppear(panelHeading);
    }

    /**
     * Gets stock information
     *
     * @param label
     * @return string
     */
    public String getStockInfo(String label) {
        By info = By.xpath(String.format("//span[.='%s']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(info).getAttribute("textContent");
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
