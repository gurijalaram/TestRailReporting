package com.apriori.cid.ui.pageobjects.explore;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.web.app.util.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class EditScenarioStatusPage extends EagerPageComponent<EditScenarioStatusPage> {

    @FindBy(css = ".scenario-group-operations-success-message")
    private WebElement editScenarioMessage;

    @FindBy(css = "[data-testid='alert-messaging'] div")
    private WebElement editScenarioErrorMessage;

    @FindBy(xpath = "//button[.='Close']")
    private WebElement closeButton;

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
        getPageUtils().waitForElementToAppear(closeButton);
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
     * This gets the error message after editing fails
     *
     * @return - string
     */
    public String getEditScenarioErrorMessage() {
        return getPageUtils().waitForElementToAppear(editScenarioErrorMessage).getText();
    }

    /**
     * close the Edit scenario status modal dialog
     *
     * @return current page object
     */
    public <T> T close(Class<T> klass) {
        getPageUtils().waitForElementAndClick(closeButton);
        return PageFactory.initElements(getDriver(), klass);
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
