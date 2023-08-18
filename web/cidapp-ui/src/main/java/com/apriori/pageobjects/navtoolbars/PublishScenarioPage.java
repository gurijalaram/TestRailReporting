package com.apriori.pageobjects.navtoolbars;

import com.apriori.EagerPageComponent;
import com.apriori.pageobjects.common.ModalDialogController;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class PublishScenarioPage extends EagerPageComponent<PublishScenarioPage> {

    @FindBy(css = ".apriori-alert")
    private WebElement publishScenarioAlert;

    @FindBy(css = "[id='modal-body'] h5")
    private WebElement unpublishedScenarioAlert;

    private ModalDialogController modalDialogController;

    public PublishScenarioPage(WebDriver driver) {
        super(driver, log);
        this.modalDialogController = new ModalDialogController(driver);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(publishScenarioAlert);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }

    /**
     * Select the Try Again button
     *
     * @return generic page object
     */
    public <T> T tryAgain(Class<T> klass) {
        return modalDialogController.tryAgain(klass);
    }

    /**
     * Gets the Publish Scenario alert
     *
     * @return - String
     */
    public String getPublishScenarioAlert() {
        return getPageUtils().waitForElementToAppear(publishScenarioAlert).getText();
    }

    /**
     * Gets the Unpublished scenario message
     *
     * @return - string
     */
    public String getUnpublishedAlert() {
        return getPageUtils().waitForElementToAppear(unpublishedScenarioAlert).getText();
    }
}
