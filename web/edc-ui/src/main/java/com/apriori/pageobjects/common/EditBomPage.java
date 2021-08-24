package com.apriori.pageobjects.common;

import com.apriori.pageobjects.pages.login.BillOfMaterialsPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class EditBomPage {

    @FindBy(css = ".modal-footer .disabled")
    private WebElement disabledSaveButton;

    @FindBy(id = "mount-type")
    private WebElement mountTypeInput;

    @FindBy(id = "pin-count")
    private WebElement pinCountInput;

    @FindBy(css = ".modal-footer .btn-outline-primary")
    private WebElement saveButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public EditBomPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    public boolean isSaveButtonEnabled() {
        return pageUtils.waitForElementToAppear(disabledSaveButton).isEnabled();
    }

    public EditBomPage enterMountType(String testMountTypeData) {
        mountTypeInput.clear();
        mountTypeInput.sendKeys(testMountTypeData);
        return this;
    }

    public EditBomPage enterPinCount(String testPinCountData) {
        pinCountInput.clear();
        pinCountInput.sendKeys(testPinCountData);
        return this;
    }

    public BillOfMaterialsPage clickSave() {
        pageUtils.waitForElementAndClick(saveButton);
        return new BillOfMaterialsPage(driver);
    }
}
