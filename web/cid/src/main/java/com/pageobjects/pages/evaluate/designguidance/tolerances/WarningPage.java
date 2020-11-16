package com.pageobjects.pages.evaluate.designguidance.tolerances;

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

public class WarningPage extends LoadableComponent<WarningPage> {

    private final Logger logger = LoggerFactory.getLogger(WarningPage.class);

    @FindBy(css = "div[data-ap-comp='genericDialog'] .modal-content")
    private WebElement dialog;

    @FindBy(css = "div[data-ap-scope='genericDialog'] .gwt-Label")
    private WebElement warningText;

    @FindBy(css = "div[data-ap-scope='genericDialog'] button.btn.btn-primary")
    private WebElement okButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public WarningPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(dialog);
    }

    /**
     * Gets the text in the warning dialog
     *
     * @return webelement as string
     */
    public String getWarningText() {
        return pageUtils.waitForElementToAppear(warningText).getText();
    }

    /**
     * Selects the ok button
     *
     * @return new page object
     */
    public <T> T ok(Class<T> className) {
        pageUtils.waitForElementAndClick(okButton);
        return PageFactory.initElements(driver, className);
    }
}
