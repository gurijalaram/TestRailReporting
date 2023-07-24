package com.apriori.pageobjects.pages.evaluate;

import com.apriori.EagerPageComponent;
import com.apriori.pageobjects.common.ModalDialogController;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class SetInputStatusPage extends EagerPageComponent<SetInputStatusPage> {

    @FindBy(css = ".scenario-group-operations-success-message")
    private WebElement setInputMessage;

    private ModalDialogController modalDialogController;

    /**
     * Initializes a new instance of this object.
     *  @param driver The web driver that the page exists on.
     *
     */
    public SetInputStatusPage(WebDriver driver) {
        super(driver, log);
        this.modalDialogController = new ModalDialogController(driver);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(setInputMessage);
    }

    /**
     * This get the message after costing is completed
     *
     * @return - string
     */
    public String getSetInputMessage() {
        return getPageUtils().waitForElementToAppear(setInputMessage).getText();
    }

    /**
     * Close
     *
     * @return generic page object
     */
    public <T> T close(Class<T> klass) {
        return modalDialogController.close(klass);
    }
}
