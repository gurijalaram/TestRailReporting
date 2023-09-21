package com.apriori.pageobjects.connectors;

import com.apriori.pageobjects.CICBasePage;
import com.apriori.pageobjects.home.CIConnectHome;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ConnectorMappings extends CICBasePage {

    @FindBy(xpath = "//div[@class='apriori-stepper']//div[.='Mappings']")
    protected WebElement connectorMappingsTab;

    @FindBy(xpath = "//div[@tab-number='2']//button[.='Save']")
    protected WebElement saveBtn;

    @FindBy(xpath = "//div[@tab-number='2']//button[.='Previous']")
    protected WebElement previousBtn;

    @FindBy(css = "div[title='Standard Fields']")
    protected WebElement standardFieldsTab;

    @FindBy(css = "div[title='Additional PLM Fields']")
    protected WebElement additionalPlmFieldsTab;

    protected static final String cssColumnSelector = "div[class*='cic-input']";
    protected static final String cssTextboxSelector = "input[type='text']";
    protected static WebElement selectedRow;

    public ConnectorMappings(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
    }

    /**
     * Click Standard Fields Tab in connector mappings section
     *
     * @return StandardFields object
     */
    public StandardFields selectStandardFieldsTab() {
        pageUtils.waitForElementAndClick(standardFieldsTab);
        return new StandardFields(driver);
    }

    /**
     * click on Additional PLM Tab
     *
     * @return AdditionalPlmFields object
     */
    public AdditionalPlmFields selectAdditionalPlmFieldsTab() {
        pageUtils.waitForElementAndClick(additionalPlmFieldsTab);
        return new AdditionalPlmFields(driver);
    }

    /**
     * click save button connector details page
     *
     * @return CIConnectHome
     */
    public CIConnectHome clickSaveBtn() {
        pageUtils.waitForElementAndClick(saveBtn);
        return new CIConnectHome(driver);
    }

    /**
     * click save button connector details page
     *
     * @return CIConnectHome
     */
    public ConnectorDetails clickPreviousBtn() {
        pageUtils.waitForElementAndClick(previousBtn);
        return new ConnectorDetails(driver);
    }
}
