package com.apriori.pageobjects.common;

import com.apriori.pageobjects.pages.login.UploadedFilePage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class UploadedBomTableActions {

    @FindBy(id = "clear-button")
    private WebElement clearSelectionButton;

    @FindBy(id = "edit-button")
    private  WebElement editButton;

    @FindBy(id = "select-button")
    private WebElement selectPartButton;

    @FindBy(id = "add-button")
    private WebElement addPartButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public UploadedBomTableActions (WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clear part selection
     *
     * @return new page object
     */
    public UploadedFilePage clearPartSelection () {
        pageUtils.waitForElementAndClick(clearSelectionButton);
        return new UploadedFilePage(driver);
    }

    /**
     * Edit the selected BOM
     *
     * @return new page object
     */
    public EditBomPage editSelectedBom () {
        pageUtils.waitForElementAndClick(editButton);
        return new EditBomPage(driver);
    }

    /**
     * Selects part for export
     *
     * @return new page object
     */
    public void selectPartForExport () {
        pageUtils.waitForElementAndClick(selectPartButton);
    }

    public AddCustomPartPage addCustomPart () {
        pageUtils.waitForElementAndClick(addPartButton);
        return new AddCustomPartPage(driver);
    }
}
