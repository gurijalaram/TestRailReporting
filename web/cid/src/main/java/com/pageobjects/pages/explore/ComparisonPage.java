package com.pageobjects.pages.explore;

import com.apriori.utils.PageUtils;

import com.pageobjects.toolbars.CompareHeader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ComparisonPage extends CompareHeader {

    private final Logger LOGGER = LoggerFactory.getLogger(ComparisonPage.class);

    @FindBy(css = "h3.modal-title")
    private WebElement modalDialog;

    @FindBy(css = "input[data-ap-field='name']")
    private WebElement nameInput;

    @FindBy(css = "textarea[data-ap-field='description']")
    private WebElement descriptionInput;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement saveButton;

    @FindBy(css = "button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ComparisonPage(WebDriver driver) {
        super(driver);
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
        pageUtils.waitForElementToAppear(modalDialog);
    }

    /**
     * Enters the comparison name
     *
     * @param comparisonName - the comparison name
     * @return current page object
     */
    public ComparisonPage enterComparisonName(String comparisonName) {
        nameInput.sendKeys(comparisonName);
        return this;
    }

    /**
     * Enters the comparison description
     *
     * @param comparisonDescription - the comparison description
     * @return current page object
     */
    public ComparisonPage enterComparisonDescription(String comparisonDescription) {
        descriptionInput.sendKeys(comparisonDescription);
        return this;
    }

    /**
     * Selects the save button
     *
     * @param className - the class the method should return
     * @param <T>       - the return type
     * @return new page object
     */
    public <T> T save(Class<T> className) {
        pageUtils.waitForElementAndClick(saveButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ExplorePage cancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new ExplorePage(driver);
    }
}
