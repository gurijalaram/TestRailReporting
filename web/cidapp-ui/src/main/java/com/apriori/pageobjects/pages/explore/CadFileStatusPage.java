package com.apriori.pageobjects.pages.explore;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

/**
 * @author cfrith
 */

@Slf4j
public class CadFileStatusPage extends LoadableComponent<CadFileStatusPage> {

    @FindBy(css = "[data-testid='close-button']")
    private WebElement closeButton;

    @FindBy(css = ".modal-body .mb-3")
    private WebElement uploadStatusText;

    @FindBy(xpath = "//button[(text()='Successes')]")
    private WebElement successesTab;

    @FindBy(xpath = "//button[(text()='Failures')]")
    private WebElement failuresTab;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CadFileStatusPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
        //Don't really need to do anything here
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Successes tab is not displayed", pageUtils.waitForElementToAppear(successesTab).isDisplayed());;
        assertTrue("Failures tab is not displayed", pageUtils.waitForElementToAppear(failuresTab).isDisplayed());;
    }

    public void openSuccessesTab() {
        pageUtils.waitForElementAndClick(successesTab);
    }

    /**
     * Close the import modal dialog
     *
     * @return new page object
     */
    public ExplorePage clickClose() {
        pageUtils.waitForElementAndClick(closeButton);
        return new ExplorePage(driver);
    }

    /**
     * This gets the message of the import status
     *
     * @return - string
     */
    public String getImportMessage() {
        return pageUtils.waitForElementToAppear(uploadStatusText).getText();
    }

    /**
     * Open specified file from Successes tab
     *
     * @param filename - The filename of the item to open
     *
     * @return - Evaluate Page Object
     */
    public EvaluatePage openFile(String filename) {
        openSuccessesTab();
        WebElement fileLink = driver.findElement(By.xpath("//a[contains(text(),'" + filename + "')]"));
        pageUtils.waitForElementAndClick(fileLink);
        return new EvaluatePage(driver);
    }
}
