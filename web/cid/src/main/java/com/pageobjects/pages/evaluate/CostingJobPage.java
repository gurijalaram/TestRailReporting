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

public class CostingJobPage extends LoadableComponent<CostingJobPage> {

    private static final Logger logger = LoggerFactory.getLogger(CostingJobPage.class);

    @FindBy(css = "div[data-ap-comp='additionalOrderInputs'] .modal-content")
    private WebElement dialog;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement dialogCostButton;

    @FindBy(css = "div[data-ap-comp='additionalOrderInputs'] button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CostingJobPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(dialog);
        pageUtils.waitForElementAppear(dialogCostButton);
    }

    /**
     * Selects the cost button
     *
     * @return current page object
     */
    public CostingJobPage selectCostButton() {
        pageUtils.waitForElementAndClick(dialogCostButton);
        return this;
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public EvaluatePage cancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new EvaluatePage(driver);
    }
}
