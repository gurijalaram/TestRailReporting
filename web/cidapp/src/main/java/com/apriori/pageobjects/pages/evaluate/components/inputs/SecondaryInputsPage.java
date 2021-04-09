package com.apriori.pageobjects.pages.evaluate.components.inputs;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.common.SecondaryInputsController;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecondaryInputsPage extends LoadableComponent<SecondaryInputsPage> {

    private static final Logger logger = LoggerFactory.getLogger(SecondaryInputsPage.class);

    @FindBy(css = ".inputs-container input[name='batchSize']")
    private WebElement batchSizeInput;

    @FindBy(css = ".inputs-container [data-icon='chevron-down']")
    private WebElement secondaryVpeDrodown;

    private WebDriver driver;
    private PageUtils pageUtils;
    private SecondaryInputsController secondaryInputsController;
    private ModalDialogController modalDialogController;

    public SecondaryInputsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.secondaryInputsController = new SecondaryInputsController(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(batchSizeInput);
    }

    /**
     * Enters the batch size
     *
     * @param batchSize - the batch size
     * @return current page object
     */
    public SecondaryInputsPage enterBatchSize(String batchSize) {
        secondaryInputsController.enterBatchSize(batchSizeInput, batchSize);
        return this;
    }

    /**
     * Selects the secondary vpe dropdown
     *
     * @param secondaryVpe - the secondary vpe
     * @return current page object
     */
    public SecondaryInputsPage selectSecondaryVpe(String secondaryVpe) {
        secondaryInputsController.selectSecondaryVpe(secondaryVpeDrodown, secondaryVpe);
        return this;
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
     * Cost
     *
     * @return current page object
     */
    public <T> T cost(Class<T> klass) {
        return modalDialogController.cost(klass);
    }
}
