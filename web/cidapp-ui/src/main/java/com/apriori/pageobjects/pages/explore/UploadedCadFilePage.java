package com.apriori.pageobjects.pages.explore;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class UploadedCadFilePage extends EagerPageComponent<UploadedCadFilePage> {

    @FindBy(xpath = "//button[contains(text(), 'Close')]")
    private WebElement closeButton;

    @FindBy(id = "qa-file-uploaded-successfully-message")
    private WebElement message;

    /**
     * Initializes a new instance of this object.
     *  @param driver The web driver that the page exists on.
     *
     */
    public UploadedCadFilePage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(closeButton);
    }

    /**
     * This close the modal
     *
     * @return new page object
     */
    public ExplorePage close() {
        getPageUtils().waitForElementAndClick(closeButton);
        return new ExplorePage(getDriver());
    }

    /**
     * This get the message after upload is completed
     *
     * @return - string
     */
    public String getImportMessage() {
        return getPageUtils().waitForElementToAppear(message).getText();
    }
}
