package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.common.ModalDialogController;
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

public class InfoPage extends LoadableComponent<InfoPage> {

    private static final Logger logger = LoggerFactory.getLogger(InfoPage.class);

    @FindBy(xpath = "//label[.='Status']")
    private WebElement statusLabel;

    @FindBy(css = "textarea[name='description']")
    private WebElement descriptionInput;

    @FindBy(css = "textarea[name='notes']")
    private WebElement notesInput;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public InfoPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(statusLabel);
    }

    /**
     * Uses type ahead to input info for any section
     *
     * @param label - the label
     * @param value - the value
     * @return current page object
     */
    public InfoPage typeAheadInSection(String label, String value) {
        String labelLocator = "//label[.='%']/..//div[contains(@class,'apriori-select')]";
        WebElement labelDropdown = driver.findElement(By.xpath(String.format(labelLocator, label)));
        WebElement valueInput = driver.findElement(By.xpath(String.format(labelLocator.concat("//input"), label)));
        pageUtils.typeAheadInput(labelDropdown, valueInput, value);
        return this;
    }

    /**
     * Input description
     *
     * @param description - the description
     * @return current page object
     */
    public InfoPage inputDescription(String description) {
        pageUtils.waitForElementAndClick(descriptionInput);
        descriptionInput.clear();
        descriptionInput.sendKeys(description);
        return this;
    }

    /**
     * Input notes
     *
     * @param notes - the notes
     * @return current page object
     */
    public InfoPage inputNotes(String notes) {
        pageUtils.waitForElementAndClick(notesInput);
        notesInput.clear();
        notesInput.sendKeys(notes);
        return this;
    }

    /**
     * Get description
     *
     * @return string
     */
    public String getDescription() {
        return descriptionInput.getAttribute("textContent");
    }

    /**
     * Get notes
     *
     * @return string
     */
    public String getNotes() {
        return notesInput.getAttribute("textContent");
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}
