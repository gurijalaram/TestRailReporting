package com.apriori.pageobjects.pages.explore;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class EditScenarioStatusPage extends EagerPageComponent<EditScenarioStatusPage> {

    @FindBy(css = ".scenario-group-operations-success-message")
    private WebElement editScenarioMessage;

    private ModalDialogController modalDialogController;

    /**
     * Initializes a new instance of this object.
     *  @param driver The web driver that the page exists on.
     *
     */
    public EditScenarioStatusPage(WebDriver driver) {
        super(driver, log);
        this.modalDialogController = new ModalDialogController(driver);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(editScenarioMessage);
    }

    /**
     * This get the message after upload is completed
     *
     * @return - string
     */
    public String getEditScenarioMessage() {
        return getPageUtils().waitForElementToAppear(editScenarioMessage).getText();

    }

    /**
     * Close the import modal dialog
     *
     * @param klass - the class
     * @param <T>   - the generic page object
     * @return generic page object
     */
    public <T> T close(Class<T> klass) {
        return modalDialogController.close(klass);
    }

    /**
     * clicks the link text
     *
     * @return - new page object
     */
    public EvaluatePage clickHere() {
        getPageUtils().javaScriptClick(getDriver().findElement(By.linkText("here")));
        return new EvaluatePage(getDriver());
    }
}
