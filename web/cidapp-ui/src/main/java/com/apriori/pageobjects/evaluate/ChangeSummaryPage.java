package com.apriori.pageobjects.evaluate;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ModalDialogController;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ChangeSummaryPage extends LoadableComponent<CostHistoryPage> {
    @FindBy(css = "div[role='tooltip']")
    private WebElement changeSummary;

    @FindBy(css = "div[role='tooltip'] div div div div div:nth-of-type(2) h3")
    private WebElement leftColHeader;

    @FindBy(css = "div[role='tooltip'] div div div div div:nth-of-type(3) h3")
    private WebElement rightColHeader;

    @FindBy(id = "qa-change-summary-column-1-Annual Volume")
    private WebElement leftColAnnualVolume;

    @FindBy(id = "qa-change-summary-column-2-Annual Volume")
    private WebElement rightColAnnualVolume;

    @FindBy(id = "qa-change-summary-column-1-Batch Size")
    private WebElement leftColBatchSize;

    @FindBy(id = "qa-change-summary-column-2-Batch Size")
    private WebElement rightColBatchSize;

    @FindBy(id = "qa-change-summary-column-1-Machining Mode")
    private WebElement leftColMachiningMode;

    @FindBy(id = "qa-change-summary-column-2-Machining Mode")
    private WebElement rightColMachiningMode;

    @FindBy(id = "qa-change-summary-column-1-Process Group")
    private WebElement leftColProcessGroup;

    @FindBy(id = "qa-change-summary-column-2-Process Group")
    private WebElement rightColProcessGroup;

    @FindBy(id = "qa-change-summary-column-1-Years")
    private WebElement leftColYears;

    @FindBy(id = "qa-change-summary-column-2-Years")
    private WebElement rightColYears;

    @FindBy(id = "qa-change-summary-column-1-Primary Digital Factory=Secondary Digital Factory")
    private WebElement leftColPGMatches;

    @FindBy(id = "qa-change-summary-column-2-Primary Digital Factory=Secondary Digital Factory")
    private WebElement rightColPGMatches;

    @FindBy(id = "qa-change-summary-column-1-Digital Factory")
    private WebElement leftColDigitalFactory;

    @FindBy(id = "qa-change-summary-column-2-Digital Factory")
    private WebElement rightColDigitalFactory;

    //Placeholders for future ones
//    @FindBy(id = "qa-change-summary-column-1-")
//    private WebElement leftCol;
//
//    @FindBy(id = "qa-change-summary-column-2-")
//    private WebElement rightCol;

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
        pageUtils.waitForElementToAppear(leftColHeader);
    }

    /**
     * Get Left Column Header Text
     *
     * @return - String of Left Column Header Text
     */
    public String leftColHeader() {
        return leftColHeader.getText();
    }

    /**
     * Get Left Column Header Text
     *
     * @return - String of Left Column Header Text
     */
    public String rightColHeader() {
        return rightColHeader.getText();
    }
}
