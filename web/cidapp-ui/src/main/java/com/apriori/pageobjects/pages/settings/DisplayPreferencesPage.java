package com.apriori.pageobjects.pages.settings;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisplayPreferencesPage extends LoadableComponent<DisplayPreferencesPage> {

    private static final Logger logger = LoggerFactory.getLogger(DisplayPreferencesPage.class);

    @FindBy(xpath = "//button[.='Display Preferences']")
    private WebElement displayTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public DisplayPreferencesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(displayTab);
    }

    /**
     * Set dropdown for any field
     *
     * @param dropdown - the dropdown
     * @param value    - the value
     * @return current page object
     */
    public DisplayPreferencesPage setDropdown(String dropdown, String value) {
        By theDropdown = By.xpath(String.format("//label[.='%s']/following-sibling::div[contains(@class,'apriori-select form-control')]", dropdown));
        pageUtils.waitForElementAndClick(theDropdown);
        By theValue = By.xpath(String.format("//button[.='%s']", value));
        pageUtils.scrollWithJavaScript(driver.findElement(theValue), true).click();
        return this;
    }

    /**
     * Uses type ahead to input info for any section
     *
     * @param label - the label
     * @param value - the value
     * @return current page object
     */
    public DisplayPreferencesPage typeAheadInSection(String label, String value) {
        String labelLocator = "//label[.='%s']/following-sibling::div[contains(@class,'apriori-select')]";
        WebElement labelDropdown = driver.findElement(By.xpath(String.format(labelLocator, label)));
        WebElement valueInput = driver.findElement(By.xpath(String.format(labelLocator.concat("//input"), label)));

        pageUtils.typeAheadInput(labelDropdown, valueInput, value);
        return this;
    }

    /**
     * Set system selection
     *
     * @param system - the metric system
     * @return current page object
     */
    public DisplayPreferencesPage setSystem(String system) {
        By theSystem = By.xpath(String.format("//input[@value='%s']", system));
        pageUtils.waitForElementAndClick(theSystem);
        return this;
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
    public EvaluatePage cancel() {
        return modalDialogController.cancel(EvaluatePage.class);
    }
}