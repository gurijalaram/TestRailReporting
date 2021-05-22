package com.pageobjects.pages.compare;

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

public class SaveAsPage extends LoadableComponent<SaveAsPage> {

    private static final Logger logger = LoggerFactory.getLogger(SaveAsPage.class);

    @FindBy(css = "input[data-ap-field='name']")
    private WebElement nameInput;

    @FindBy(css = "textarea[data-ap-field='description']")
    private WebElement descriptionInput;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement createButton;

    @FindBy(css = "button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public SaveAsPage(WebDriver driver) {
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

    }

    /**
     * Enters the name
     *
     * @param name - the name
     * @return current page object
     */
    public SaveAsPage inputName(String name) {
        pageUtils.waitForElementToAppear(nameInput).clear();
        nameInput.sendKeys(name);
        return this;
    }

    /**
     * Enters the description
     *
     * @param description - the description
     * @return current page object
     */
    public SaveAsPage inputDescription(String description) {
        descriptionInput.clear();
        descriptionInput.sendKeys(description);
        return this;
    }

    /**
     * Selects the create button
     *
     * @return new page object
     */
    public ComparePage selectCreateButton() {
        createButton.click();
        return new ComparePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ComparePage selectCancelButton() {
        cancelButton.click();
        return new ComparePage(driver);
    }
}
