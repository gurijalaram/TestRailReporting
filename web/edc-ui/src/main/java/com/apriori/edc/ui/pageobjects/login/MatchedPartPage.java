package com.apriori.edc.ui.pageobjects.login;

import com.apriori.edc.ui.common.EditBomPage;
import com.apriori.edc.ui.common.UploadedBomTableActions;
import com.apriori.web.app.util.EagerPageComponent;

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

    @FindBy(xpath = "//span[.='Pin Count']")
    private WebElement pinCount;

    @FindBy(xpath = "//span[.='Mount Type']")
    private WebElement mountType;

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

    /**
     * Mount Type text
     *
     * @return String
     */
    public String getMountTypeHeaderText() {
        return getPageUtils().waitForElementToAppear(mountType).getAttribute("textContent");
    }
}
