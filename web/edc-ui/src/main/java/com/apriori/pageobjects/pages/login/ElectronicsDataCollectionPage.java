package com.apriori.pageobjects.pages.login;

import com.apriori.pageobjects.navtoolbars.NavigationBar;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;

@Slf4j
public class ElectronicsDataCollectionPage extends NavigationBar {

    @FindBy(css = "[id='qa-uploaded-bill-of-materials'] .title-left")
    private WebElement uploadedBillOfMaterials;

    @FindBy(css = ".paginator .left")
    private WebElement paginatorDropdown;

    @FindBy(xpath = "//button[.='20']")
    private WebElement paginator;

    public ElectronicsDataCollectionPage(WebDriver driver) {
        super(driver, log);
    }

    /**
     * Gets the Uploaded Bill Of Materials value on the page
     *
     * @return String
     */
    public String getUploadedBillOfMaterials() {
        return getPageUtils().waitForElementToAppear(uploadedBillOfMaterials).getAttribute("textContent");
    }

    /**
     * Upload the resource file
     *
     * @param resourceFile resource file
     * @return new page object
     */
    public FileUploadPage uploadComponent(File resourceFile) {
        return new FileUploadPage(getDriver()).inputComponentDetails(resourceFile);
    }

    /**
     * Sets pagination to by default
     *
     * @return current page object
     */
    public ElectronicsDataCollectionPage setPagination() {
        getPageUtils().waitForElementAndClick(paginatorDropdown);
        getPageUtils().waitForElementAppear(paginator);
        getPageUtils().javaScriptClick(paginator);
        return this;
    }
}
