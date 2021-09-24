package com.apriori.pageobjects.pages.login;

import com.apriori.pageobjects.navtoolbars.NavigationBar;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;

@Slf4j
public class ElectronicsDataCollectionPage extends NavigationBar {

    @FindBy(css = "[id='qa-uploaded-bill-of-materials'] .title-left")
    private WebElement uploadedBillOfMaterials;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ElectronicsDataCollectionPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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

    /**
     * Upload the resource file
     *
     * @param resourceFile resource file
     * @return new page object
     */
    public FileUploadPage uploadComponent(File resourceFile) {
        return new FileUploadPage(driver).inputComponentDetails(resourceFile);
    }
}
