package com.apriori.pageobjects.pages.login;

import com.apriori.pageobjects.navtoolbars.NavigationBar;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.RightClickOptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

    @FindBy(id = "qa-summary")
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
        getPageUtils().waitFor(1500);
        String getNumber = getPageUtils().waitForElementAppear(numberOfLoadedBOMs).getText();
        return Integer.parseInt(getNumber.substring(getNumber.indexOf("of ") + 3));
    }

    /**
     * right click on specified BOM on the list and choose one option
     *
     * @return current page object
     */
    public ElectronicsDataCollectionPage rightClickOnSpecifiedBomAndChooseOption(String bomId, RightClickOptionEnum option) {
        setPagination();
        getPageUtils().waitFor(1000);
        By xpath = By.xpath(String.format("//a[contains(@href,'%s')]", bomId));
        WebElement bomElement = getDriver().findElement(xpath);
        rightClick(getPageUtils().waitForElementToAppear(bomElement));

        By xpath2 = By.xpath(String.format("//span[contains(.,'%s')]", option.getOption()));
        WebElement optionChosen = bomElement.findElement(xpath2);

        getPageUtils().waitForElementToAppear(optionChosen).click();
        if (option.equals(RightClickOptionEnum.DELETE)) {
            getPageUtils().waitForElementToAppear(deleteButton).click();
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
        rightClick(getPageUtils().waitForElementToAppear(firstBOMOnTheList));

        WebElement  firstDivElement = getDriver().findElement(By.xpath("//div[@class = 'panel-body']/div[1]"));
        WebElement  firstLinkElement = getDriver().findElement(By.xpath("//div[@class = 'panel-body']/a[1]"));
        String bomID = firstLinkElement.getAttribute("id");
        String filePath = downloadPath + bomID + ".csv";
        FileResourceUtil.deleteIfExistsLocalFile(filePath);
        By optionXpath = By.xpath(String.format("//span[contains(.,'%s')]", option.getOption()));
        WebElement optionChosen = firstDivElement.findElement(optionXpath);

        getPageUtils().waitForElementToAppear(optionChosen).click();
        if (option.equals(RightClickOptionEnum.DELETE)) {
            getPageUtils().waitForElementToAppear(deleteButton).click();
        }

        getPageUtils().waitFor(1000);

        return bomID;
    }

    /**
     * right click on element
     *
     * @return void
     */
    public void rightClick(WebElement element) {
        Actions actions = new Actions(getDriver());
        actions.contextClick(element).perform();
    }
}
