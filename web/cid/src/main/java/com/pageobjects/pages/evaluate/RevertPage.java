package com.pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

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

public class RevertPage extends LoadableComponent<RevertPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(RevertPage.class);

    @FindBy(css = "[data-ap-comp='revertScenario'] .modal-title")
    private WebElement dialogTitle;

    @FindBy(css = "[data-ap-comp='descriptionLabel']")
    private WebElement revertInfo;

    @FindBy(css = "[data-ap-comp='revertScenario'] .btn.btn-primary")
    private WebElement revertButton;

    @FindBy(css = "[data-ap-comp='revertScenario'] .btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public RevertPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(dialogTitle);
    }

    /**
     * Gets the info in the revert dialog
     *
     * @return revert info as string
     */
    public String getRevertInfo() {
        return revertInfo.getText();
    }

    /**
     * Selects the revert button
     *
     * @return new Evaluate Page
     */
    public EvaluatePage revertScenario() {
        pageUtils.waitForElementAndClick(revertButton);
        return new EvaluatePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @param className - the class the method should return
     * @param <T>       - the return type
     * @return generic page object
     */
    public <T> T cancel(Class<T> className) {
        cancelButton.click();
        return PageFactory.initElements(driver, className);
    }
}