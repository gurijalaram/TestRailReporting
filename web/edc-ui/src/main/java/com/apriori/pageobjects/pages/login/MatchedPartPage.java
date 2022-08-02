package com.apriori.pageobjects.pages.login;

import com.apriori.pageobjects.common.EditBomPage;
import com.apriori.pageobjects.common.UploadedBomTableActions;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class MatchedPartPage extends EagerPageComponent<MatchedPartPage> {

    @FindBy(css = ".part-card")
    private WebElement fileMatch;

    @FindBy(id = "edit-button")
    private WebElement editButton;

    @FindBy(css = "[data-icon='plus']")
    private WebElement addButton;

    @FindBy(css = "p:nth-child(5) > span.title")
    private WebElement pinCount;

    private UploadedBomTableActions uploadedBomTableActions = new UploadedBomTableActions(getDriver());

    public MatchedPartPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(addButton);
    }

    /**
     * Highlight the selected file
     *
     * @return current page object
     */
    public MatchedPartPage highlightItem() {
        getPageUtils().waitForElementAndClick(fileMatch);
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

    /**
     * Pin count text
     *
     * @return String
     */
    public String getPinCountHeaderText() {
        return getPageUtils().waitForElementToAppear(pinCount).getAttribute("textContent");
    }
}
