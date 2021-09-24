package com.apriori.pageobjects.pages.login;

import com.apriori.pageobjects.common.EditBomPage;
import com.apriori.pageobjects.common.UploadedBomTableActions;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class MatchedPartPage extends LoadableComponent<MatchedPartPage> {

    @FindBy(css = ".part-card")
    private WebElement fileMatch;

    @FindBy(id = "edit-button")
    private WebElement editButton;

    @FindBy(css = "[data-icon='plus']")
    private WebElement addButton;

    @FindBy(css = "p:nth-child(5) > span.title")
    private WebElement pinCount;

    private WebDriver driver;
    private PageUtils pageUtils;

    private UploadedBomTableActions uploadedBomTableActions;

    public MatchedPartPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.uploadedBomTableActions = new UploadedBomTableActions(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(addButton);
    }

    /**
     * Highlight the selected file
     *
     * @return current page object
     */
    public MatchedPartPage highlightItem() {
        pageUtils.waitForElementAndClick(fileMatch);
        return this;
    }

    /**
     * Edit the selected BOM
     *
     * @return new page object
     */
    public EditBomPage editSelectedBom() {
        return uploadedBomTableActions.editSelectedBom(editButton);
    }

    public String getPinCountHeaderText() {
        return pageUtils.waitForElementToAppear(pinCount).getAttribute("textContent");
    }

    /**
     * Gets the BOM Identity
     *
     * @return String
     */
    public String getBillOfMaterialsId() {
        String currentUrl = driver.getCurrentUrl();
        int billOfMaterialsIdPosition = 4;
        String[] currMatArray = currentUrl.split("/");
        return currMatArray[billOfMaterialsIdPosition];
    }
}
