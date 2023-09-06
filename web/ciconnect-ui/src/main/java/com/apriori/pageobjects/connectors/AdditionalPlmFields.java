package com.apriori.pageobjects.connectors;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Slf4j
public class AdditionalPlmFields extends ConnectorMappings {

    public AdditionalPlmFields(WebDriver driver) {
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
        if (pageUtils.isElementDisplayed(statusMessageLbl)) {
            throw new RuntimeException(statusMessageLbl.getText());
        }
    }

    private List<WebElement> getStandardFieldsRows() {
        return driver.findElements(By.cssSelector("div[tab-number='1'] div[class^='BMCollectionViewCellWrapper']"));
    }
}
