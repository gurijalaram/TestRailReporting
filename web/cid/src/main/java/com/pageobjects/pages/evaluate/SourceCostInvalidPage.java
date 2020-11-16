package com.pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceCostInvalidPage extends LoadableComponent<SourceCostInvalidPage> {

    private final Logger logger = LoggerFactory.getLogger(SourceCostInvalidPage.class);

    @FindBy(css = "div[data-ap-comp='additionalOrderInputs'] .modal-content")
    private WebElement modalDialog;

    @FindBy(css = "div[data-ap-comp='additionalOrderInputDialogMessage']")
    private WebElement invalidText;

    @FindBy(xpath = "//div[@data-ap-comp='additionalOrderInputs']//button[.='Continue'] ")
    private WebElement continueButton;

    @FindBy(xpath = "//div[@data-ap-comp='additionalOrderInputs']//button[.='Fix Source'] ")
    private WebElement fixSourceButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public SourceCostInvalidPage(WebDriver driver) {
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
        pageUtils.waitForElementAppear(modalDialog);
    }

    /**
     * Gets the text from the modal dialog
     * @return string
     */
    public String getCostInvalidText() {
        return pageUtils.waitForElementToAppear(invalidText).getText();
    }

    /**
     * Selects the continue button
     * @return new page object
     */
    public EvaluatePage selectContinue() {
        pageUtils.waitForElementAndClick(continueButton);
        return new EvaluatePage(driver);
    }

    /**
     * Selects the fix source button
     * @return new page object
     */
    public EvaluatePage selectFixSource() {
        pageUtils.waitForElementAndClick(fixSourceButton);
        pageUtils.windowHandler(1);
        return new EvaluatePage(driver);
    }
}
