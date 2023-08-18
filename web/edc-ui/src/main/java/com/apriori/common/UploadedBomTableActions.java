package com.apriori.common;

import com.apriori.PageUtils;
import com.apriori.pageobjects.login.MatchedPartPage;
import com.apriori.pageobjects.login.UploadedFilePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class UploadedBomTableActions extends LoadableComponent<UploadedBomTableActions> {

    @FindBy(id = "add-button")
    private WebElement addPartButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public UploadedBomTableActions(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(addPartButton);
    }

    /**
     * Clear part selection
     *
     *  @param element - clear selection button
     * @return new page object
     */
    public UploadedFilePage clearPartSelection(WebElement element) {
        pageUtils.waitForElementAndClick(element);
        return new UploadedFilePage(driver);
    }

    /**
     * Selects part for export
     *
     *  @param element - select part button
     * @return new page object
     */
    public MatchedPartPage selectPartForExport(WebElement element) {
        pageUtils.waitForElementAndClick(element);
        return new MatchedPartPage(driver);
    }

    /**
     * Add a Custom part
     *
     * @param element - the add button
     * @return
     */
    public AddCustomPartPage addCustomPart(WebElement element) {
        pageUtils.waitForElementAndClick(element);
        return new AddCustomPartPage(driver);
    }

    /**
     * Edit the selected BOM
     *
     *  @param element - the edit button
     * @return new page object
     */
    public EditBomPage editSelectedBom(WebElement element) {
        pageUtils.waitForElementAndClick(element);
        return new EditBomPage(driver);
    }
}
