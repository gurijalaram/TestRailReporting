package com.apriori.pageobjects.login;

import com.apriori.http.utils.FileResourceUtil;
import com.apriori.pageobjects.navtoolbars.NavigationBar;

import com.utils.RightClickOptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.nio.file.Paths;

@Slf4j
public class ElectronicsDataCollectionPage extends NavigationBar {

    @FindBy(css = "[id='qa-uploaded-bill-of-materials'] .title-left")
    private WebElement uploadedBillOfMaterials;

    @FindBy(css = ".paginator .left")
    private WebElement paginatorDropdown;

    @FindBy(xpath = "//button[.='20']")
    private WebElement paginator;

    @FindBy(xpath = "//span[contains(.,'1 - 10')]")
    private WebElement numberOfLoadedBOMs;

    @FindBy(xpath = "//button[@class = 'btn btn-danger']")
    private WebElement deleteButton;

    @FindBy(xpath = "//div[@class = 'panel-body']/a[1]")
    private WebElement firstBOMOnTheList;

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

    /**
     * gets number of loaded BOMs
     *
     * @return int
     */
    public int getNumberOfLoadedBOMs() {
        String getNumber = getPageUtils().waitForElementAppear(numberOfLoadedBOMs).getText();
        return Integer.parseInt(getNumber.substring(getNumber.indexOf("of ") + 3));
    }

    /**
     * gets number of loaded BOMs - check for expected number
     * @param expectedNumber - check for expected amount of BOMs
     *
     * @return boolean
     */
    public int getNumberOfLoadedBOMs(int expectedNumber) {
        By xpath = By.xpath(String.format("//span[contains(.,'of %s')]", expectedNumber));
        WebElement element = getPageUtils().waitForElementToAppear(xpath);
        String getNumber = element.getText();
        return Integer.parseInt(getNumber.substring(getNumber.indexOf("of ") + 3));
    }

    /**
     * right click on specified BOM on the list and choose one option
     *
     * @return current page object
     */
    public ElectronicsDataCollectionPage rightClickOnSpecifiedBomAndChooseOption(String bomId, RightClickOptionEnum option) {
        setPagination();
        By xpath = By.xpath(String.format("//a[contains(@href,'%s')]", bomId));
        WebElement bomElement = getPageUtils().waitForElementToAppear(xpath);
        getPageUtils().rightClick(getPageUtils().waitForElementToAppear(bomElement));

        By xpath2 = By.xpath(String.format("//span[contains(.,'%s')]", option.getOption()));
        WebElement optionChosen = bomElement.findElement(xpath2);

        getPageUtils().waitForElementAndClick(optionChosen);
        if (option.equals(RightClickOptionEnum.DELETE)) {
            getPageUtils().waitForElementAndClick(deleteButton);
        }
        return this;
    }

    /**
     * right click on first BOM on the list and choose one option
     *
     * @return String - BOM ID
     */

    public String rightClickOnFirstBomAndChooseOption(RightClickOptionEnum option) {
        String downloadPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;
        getPageUtils().rightClick(getPageUtils().waitForElementToAppear(firstBOMOnTheList));

        WebElement  firstDivElement = getDriver().findElement(By.xpath("//div[@class = 'panel-body']/div[1]"));
        WebElement  firstLinkElement = getDriver().findElement(By.xpath("//div[@class = 'panel-body']/a[1]"));
        String bomID = firstLinkElement.getAttribute("id");
        String filePath = downloadPath + bomID + ".csv";
        FileResourceUtil.deleteFileWhenAppears(Paths.get(filePath),1);
        By optionXpath = By.xpath(String.format("//span[contains(.,'%s')]", option.getOption()));
        WebElement optionChosen = firstDivElement.findElement(optionXpath);

        getPageUtils().waitForElementToAppear(optionChosen).click();
        if (option.equals(RightClickOptionEnum.DELETE)) {
            getPageUtils().waitForElementToAppear(deleteButton).click();
        }
        return bomID;
    }
}
