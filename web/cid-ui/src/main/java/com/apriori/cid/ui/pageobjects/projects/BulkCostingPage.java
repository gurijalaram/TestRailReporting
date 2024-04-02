package com.apriori.cid.ui.pageobjects.projects;


import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;

public class BulkCostingPage extends LoadableComponent<BulkCostingPage> {
    @FindBy(xpath = "//button[contains(@class,'bcm-sub-header-delete-button')]")
    private WebElement deleteButton;
    @FindBy(xpath = "//button[contains(@type,'submit')][contains(.,'Confirm')]")
    private WebElement confirmDeleteButton;
    private PageUtils pageUtils;
    private WebDriver driver;

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        driver.getCurrentUrl().contains("/bulk-analysis");
    }

    public BulkCostingPage(WebDriver driver) {
        super();
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * verify if the list of worksheet is present in the bulk analysis page
     * @return boolean
     */
    public boolean isListOfWorksheetsPresent() {
        List<WebElement> listOfWorksheetItems =
            pageUtils.waitForElementsToAppear(By.xpath("//div[@data-testid = 'table-body']/div"));
        return !(listOfWorksheetItems.isEmpty());
    }

    /**
     * select and delete worksheet
     * @param nameBulkAnalysis - name of the worksheet
     * @return page object
     */
    public BulkCostingPage selectAndDeleteSpecificBulkAnalysis(String nameBulkAnalysis) {
        String xpath = "//div[contains(@class,'table-row')][contains(.,'" + nameBulkAnalysis + "')]/descendant::span";
        WebElement checkBox2 = pageUtils.waitForElementToBeClickable(By.xpath(xpath));
        pageUtils.waitForElementAndClick(checkBox2);
        pageUtils.waitForElementAndClick(deleteButton);
        pageUtils.waitForElementAndClick(confirmDeleteButton);
        return this;
    }

    public boolean isWorksheetIsPresent(String worksheetName) {
        pageUtils.waitFor(1000);
        return driver.getPageSource().contains("Expected text");
    }
}
