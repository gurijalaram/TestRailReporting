package com.apriori.pageobjects.pages.explore;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
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

    @FindBy(xpath = "//button[(text()='Successes')]")
    private WebElement successesTab;

    @FindBy(xpath = "//button[(text()='Failures')]")
    private WebElement failuresTab;

    @FindBy(xpath = "//button[(text()='Successes')]//span")
    private WebElement numberOfSuccesses;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public CadFileStatusPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
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
        assertTrue("Successes tab is not displayed", pageUtils.waitForElementToAppear(successesTab).isDisplayed());
        assertTrue("Failures tab is not displayed", pageUtils.waitForElementToAppear(failuresTab).isDisplayed());
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
     * This gets the number of successes
     *
     * @return - int
     */
    public int getNumberOfSuccesses() {
        return Integer.parseInt(pageUtils.waitForElementToAppear(numberOfSuccesses).getAttribute("textContent"));
    }

    /**
     * Clicks the x button to close the modal
     *
     * @return generic page object
     */
    public <T> T closeDialog(Class<T> klass) {
        return modalDialogController.closeDialog(klass);
    }
}
