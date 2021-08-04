package com.apriori.pageobjects.pages.login;

import com.apriori.pageobjects.navtoolbars.NavigationBar;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElectronicsDataCollectionPage extends NavigationBar {

    private static final Logger logger = LoggerFactory.getLogger(ElectronicsDataCollectionPage.class);

    @FindBy(css = "[id='qa-uploaded-bill-of-materials'] .title-left")
    private WebElement uploadedBillOfMaterials;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ElectronicsDataCollectionPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    /**
     * Gets the Uploaded Bill Of Materials value on the page
     *
     * @return String
     */
    public String getUploadedBillOfMaterials() {
        return pageUtils.waitForElementToAppear(uploadedBillOfMaterials).getAttribute("textContent");
    }
}
