package com.apriori.pageobjects.pages.evaluate.components.inputs;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.InputsController;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.inputs.CustomPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ComponentCustomPage extends LoadableComponent<CustomPage> {

    @FindBy(xpath = "//div[@id='modal-body']//button[.='Custom']")
    private WebElement customTab;

    @FindBy(xpath = "//div[@id='modal-body']//button[.='Advanced']")
    private WebElement advancedTab;

    @FindBy(xpath = "//div[@id='modal-body']//button[.='Basic']")
    private WebElement basicTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;

    public ComponentCustomPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.inputsController = new InputsController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {
        //Don't really need to do anything here
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Custom was not selected", customTab.getAttribute("class").contains("active"));
    }

    /**
     * Opens basic tab
     *
     * @return new page object
     */
    public ComponentBasicPage goToBasicTab() {
        pageUtils.waitForElementAndClick(basicTab);
        return new ComponentBasicPage(driver);
    }

    /**
     * Opens advanced tab
     *
     * @return new page object
     */
    public ComponentAdvancedPage goToAdvancedTab() {
        pageUtils.waitForElementAndClick(advancedTab);
        return new ComponentAdvancedPage(driver);
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
     * Apply & Cost
     *
     * @return generic page object
     */
    public <T> T applyAndCost(Class<T> klass) {
        return modalDialogController.applyCost(klass);
    }
}
