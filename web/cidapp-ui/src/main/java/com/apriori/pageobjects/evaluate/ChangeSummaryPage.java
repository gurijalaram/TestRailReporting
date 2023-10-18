package com.apriori.pageobjects.evaluate;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ModalDialogController;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ChangeSummaryPage extends LoadableComponent<CostHistoryPage> {
    @FindBy(css = "div[role='presentation']")
    private WebElement changeSummary;

    @FindBy(xpath = "(//div[@role='presentation']//h3)[2]")
    private WebElement changedFromHeader;

    @FindBy(xpath = "(//div[@role='presentation']//h3)[3]")
    private WebElement changedToHeader;

    private String changedValueID = "qa-change-summary-column-%1$d-%2$s";

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public ChangeSummaryPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(changeSummary);
        pageUtils.waitForElementToAppear(changedFromHeader);
    }

    /**
     * Get Left Column Header Text
     *
     * @return - String of Left Column Header Text
     */
    public String changedFromHeader() {
        return changedFromHeader.getText();
    }

    /**
     * Get Left Column Header Text
     *
     * @return - String of Left Column Header Text
     */
    public String changedToHeader() {
        return changedToHeader.getText();
    }

    /**
     * Get value for a specified value changed from
     *
     * @param changedInput - String of name of changed input
     *
     * @return - String of displayed change
     */
    public String getChangedFrom(String changedInput) {
        By locator = By.id(String.format(changedValueID, 1, changedInput));
        return driver.findElement(locator).getText();
    }

    /**
     * Get value for a specified value changed from
     *
     * @param changedInput - String of name of changed input
     *
     * @return - String of displayed change
     */
    public String getChangedTo(String changedInput) {
        By locator = By.id(String.format(changedValueID, 2, changedInput));
        return driver.findElement(locator).getText();
    }

    /**
     * Close the change summary pop up
     *
     * @return - Cost History PO
     */
    public CostHistoryPage close() {
        pageUtils.mouseMoveWithOffsets(changedFromHeader, 0, 100);
        return new CostHistoryPage(driver);
    }
}
