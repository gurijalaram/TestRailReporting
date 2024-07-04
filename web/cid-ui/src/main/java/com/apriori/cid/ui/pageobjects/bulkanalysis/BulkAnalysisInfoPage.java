package com.apriori.cid.ui.pageobjects.bulkanalysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class BulkAnalysisInfoPage extends LoadableComponent<BulkAnalysisInfoPage> {

    @FindBy(css = "[role='dialog'] h2")
    private WebElement headerText;

    @FindBy(css = "[id='name']")
    private WebElement nameInput;

    @FindBy(css = "[id='description']")
    private WebElement descriptionInput;

    private ModalDialogController modalDialogController;
    private PageUtils pageUtils;
    private WebDriver driver;

    public BulkAnalysisInfoPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {
        //don't need to do anything here
    }

    @Override
    protected void isLoaded() throws Error {
        assertEquals("Bulk Analysis Info", pageUtils.waitForElementToAppear(headerText).getAttribute("textContent"), "Bulk analysis info modal was not displayed");
    }

    /**
     * Enters bulk analysis name
     *
     * @param name - the name
     * @return current page object
     */
    public BulkAnalysisInfoPage enterBulkAnalysisName(String name) {
        pageUtils.waitForElementAndClick(nameInput);
        pageUtils.clearValueOfElement(nameInput);
        nameInput.sendKeys(name);
        return this;
    }

    /**
     * Enter bulk analysis description
     *
     * @param description - the description
     * @return current page object
     */
    public BulkAnalysisInfoPage enterBulkAnalysisDescription(String description) {
        pageUtils.waitForElementAndClick(descriptionInput);
        pageUtils.clearValueOfElement(descriptionInput);
        descriptionInput.sendKeys(description);
        return this;
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public WorksheetsExplorePage clickCancel() {
        return modalDialogController.cancel(WorksheetsExplorePage.class);
    }

    /**
     * Select the save button
     *
     * @return generic page object
     */
    public BulkAnalysisPage clickSave() {
        return modalDialogController.save(BulkAnalysisPage.class);
    }
}
