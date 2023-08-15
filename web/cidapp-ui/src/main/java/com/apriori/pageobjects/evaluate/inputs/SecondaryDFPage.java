package com.apriori.pageobjects.evaluate.inputs;

import com.apriori.PageUtils;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.pageobjects.common.InputsController;
import com.apriori.pageobjects.common.ModalDialogController;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class SecondaryDFPage extends LoadableComponent<SecondaryDFPage> {

    @FindBy(css = ".secondary-digital-factory-select-form")
    private WebElement secondaryForm;

    @FindBy(css = ".select-field.digital-factory-select-field .apriori-select")
    private WebElement machiningDropdown;

    @FindBy(css = ".switch .placeholder-left")
    private WebElement yesButton;

    @FindBy(xpath = "//span[.='No']")
    private WebElement noButton;

    @FindBy(css = "[data-testid='primary-button']")
    private WebElement submitButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;

    public SecondaryDFPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.inputsController = new InputsController(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(secondaryForm);
    }

    /**
     * Select yes/no
     *
     * @param choice - the choice
     * @return current page object
     */
    public SecondaryDFPage usePrimaryDF(String choice) {
        if (choice.equals("Yes")) {
            pageUtils.waitForElementAndClick(yesButton);
        } else {
            pageUtils.waitForElementAndClick(noButton);
        }
        return this;
    }

    /**
     * Selects the machining dropdown
     *
     * @param digitalFactory - the digital factory
     * @return current page object
     */
    public SecondaryDFPage selectDropdown(String secondaryProcess, DigitalFactoryEnum digitalFactory) {
        WebElement machiningDropdown = pageUtils.waitForElementToAppear(driver.findElement(By.cssSelector(String.format("[id='qa-%s-select-field'] .apriori-select", secondaryProcess))));
        inputsController.selectInputsDropdown(machiningDropdown, digitalFactory.getDigitalFactory());
        return this;
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(submitButton, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
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
