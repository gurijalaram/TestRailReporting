package com.apriori.pageobjects.common;

import com.apriori.pageobjects.pages.login.MatchedPartPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class EditBomPage extends LoadableComponent<EditBomPage> {

    @FindBy(css = ".modal-footer .disabled")
    private WebElement disabledSaveButton;

    @FindBy(id = "mount-type")
    private WebElement mountTypeInput;

    @FindBy(id = "pin-count")
    private WebElement pinCountInput;

    @FindBy(css = ".modal-footer .btn-outline-primary")
    private WebElement saveButton;

    @FindBy(css = ".modal-title .bill-of-materials-type")
    private WebElement pcbaLogo;

    @FindBy(id = "average-cost")
    private WebElement averageCost;

    @FindBy(id = "manufacturer-part-number")
    private WebElement partNumber;

    private PageUtils pageUtils;
    private WebDriver driver;
    private DialogController dialogController;

    public EditBomPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.dialogController = new DialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(pcbaLogo);
    }

    /**
     * Check if the save button is enabled
     *
     * @return boolean
     */
    public boolean isSaveButtonEnabled() {
        return pageUtils.waitForElementToAppear(disabledSaveButton).isEnabled();
    }

    /**
     * Enter the Mount Type
     *
     * @param testMountTypeData
     * @return current page object
     */
    public EditBomPage enterMountType(String testMountTypeData) {
        mountTypeInput.clear();
        mountTypeInput.sendKeys(testMountTypeData);
        return this;
    }

    /**
     * Enter the Pin Count
     *
     * @param testPinCountData
     * @return current page object
     */
    public EditBomPage enterPinCount(String testPinCountData) {
        pinCountInput.clear();
        pinCountInput.sendKeys(testPinCountData);
        return this;
    }

    /**
     * Enter the Average cost
     *
     * @param testAverageCostData
     * @return current page object
     */
    public EditBomPage enterAverageCost(String testAverageCostData) {
        averageCost.clear();
        averageCost.sendKeys(testAverageCostData);
        return this;
    }

    /**
     * Enter the Manufacturer part number
     *
     * @param testPartNumberData
     * @return current page object
     */
    public EditBomPage enterManufacturerPartNumber(String testPartNumberData) {
        partNumber.clear();
        partNumber.sendKeys(testPartNumberData);
        return this;
    }

    /**
     * Click on the Save button
     *
     * @return new page object
     */
    public MatchedPartPage clickSave() {
        dialogController.save();
        return new MatchedPartPage(driver);
    }

    /**
     * Click the cancel button
     *
     * @return new page object
     */
    public MatchedPartPage clickCancel() {
        dialogController.cancel();
        return new MatchedPartPage(driver);
    }
}
