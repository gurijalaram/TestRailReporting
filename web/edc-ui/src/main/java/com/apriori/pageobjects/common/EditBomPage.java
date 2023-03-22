package com.apriori.pageobjects.common;

import com.apriori.pageobjects.pages.login.MatchedPartPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class EditBomPage extends EagerPageComponent<EditBomPage> {

    @FindBy(css = ".modal-footer .disabled")
    private WebElement disabledSaveButton;

    @FindBy(id = "mount-type")
    private WebElement mountTypeInput;

    @FindBy(id = "pin-count")
    private WebElement pinCountInput;

    @FindBy(css = ".modal-title .bill-of-materials-type")
    private WebElement pcbaLogo;

    @FindBy(id = "average-cost")
    private WebElement averageCost;

    @FindBy(id = "manufacturer-part-number")
    private WebElement partNumber;

    @FindBy(css = "input[class='is-invalid form-control'][name='mountType']")
    private WebElement mountTypeError;

    @FindBy(xpath = "//div[@class='modal-footer'] //button[.='Save']")
    private WebElement saveButton;

    @FindBy(css = "input[type='radio'][name='mountType']")
    private List<WebElement> mountTypeRadioButtons;

    @FindBy(css = "[class='input-radio'] label")
    private List<WebElement> mountTypeButtonsText;

    private DialogController dialogController = new DialogController(getDriver());
    private PageUtils pageUtils;

    public EditBomPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(pcbaLogo);
    }

    /**
     * Check if the save button is enabled
     *
     * @return boolean
     */
    public boolean isSaveButtonEnabled() {
        return getPageUtils().waitForElementToAppear(saveButton).isEnabled();
    }

    /**
     * Check if the disabled save button is displayed
     *
     * @return boolean
     */
    public boolean isSaveButtonDisabledDisplayed() {
        return getPageUtils().waitForElementToAppear(saveButton)
            .getAttribute("class")
            .contains("disabled");
    }

    /**
     * Check if particular error on the edit BOM  page is displayed
     *
     * @return boolean
     */
    public boolean isErrorMessageDisplayed(String textDisplayed) {
        By errorMessage =
            By.xpath(String.format("//div[contains(.,'%s')][@class = 'invalid-feedback']", textDisplayed));
        return getPageUtils().isElementDisplayed(errorMessage);
    }

    /**
     * Check if mount type warning message is displayed
     *
     * @return boolean
     */
    public boolean isMountTypeWarnMsgDisplayed() {
        return getPageUtils().waitForElementToAppear(mountTypeError).isDisplayed();
    }

    /**
     * get list of radio buttons labels for mount type
     *
     *
     * @return list of label names for radio buttons for mount type
     */
    public List<String> getMountTypeBtnLabels() {

        List<String> radioListText = mountTypeButtonsText
            .stream()
            .map(i -> i.getText()).collect(Collectors.toList());
        return radioListText;
    }

    /**
     * get the mount type radio button options
     *
     *
     * @return int - size of elements' list
     */

    public int getMountTypeButtonsSize() {
        return mountTypeRadioButtons.size();
    }

    /**
     * Selects the radio button for Surface Mount and Through Hole
     *
     * @param mountType - the mount type to select
     * @return current page object
     */
    public EditBomPage selectMountType(String mountType) {
        getPageUtils().waitForElementAndClick(By.xpath(String.format("//div[@class='input-radio']//label[text()='%s']", mountType)));
        return this;
    }

    /**
     * Selects the radio button for Other Mount type
     *
     * @param mountTypeData - mount type data
     * @return current page object
     */
    public EditBomPage selectOtherMountType(String mountTypeData) {
        getPageUtils().waitForElementAndClick(By.xpath("//div[@class='input-radio']//label[text()='Other:']"));
        getPageUtils().clearValueOfElement(mountTypeInput);
        mountTypeInput.sendKeys(mountTypeData);
        return this;
    }

    /**
     * Selects Mount Type - old version - introducing while we do not have on qa-test version with mount type radio buttons
     *
     * @param mountTypeData - mount type data
     * @return current page object
     */

    public EditBomPage enterMountTypeOldVer(String mountTypeData) {
        getPageUtils().clearValueOfElement(mountTypeInput);
        mountTypeInput.sendKeys(mountTypeData);
        return this;
    }

    /**
     * Enter the Pin Count
     *
     * @param testPinCountData - test pin count data
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
     * @param testAverageCostData - test average cost data
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
     * @param testPartNumberData - test part number data
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
        return new MatchedPartPage(getDriver());
    }

    /**
     * Click the cancel button
     *
     * @return new page object
     */
    public MatchedPartPage clickCancel() {
        dialogController.cancel();
        return new MatchedPartPage(getDriver());
    }
}
