package com.apriori.pageobjects.pages.evaluate.inputs;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class RoutingSelectionPage extends EagerPageComponent<RoutingSelectionPage> {

    @FindBy(css = "svg[data-icon='code-branch']")
    private WebElement selectRouting;

    @FindBy(css = "svg[data-testid='CheckBoxIcon']")
    private WebElement checkBox;

    @FindBy(xpath = "//div[@id='styled-routings-list']/following-sibling::div//button[.='Submit']")
    private WebElement submit;

    private ModalDialogController modalDialogController = new ModalDialogController(getDriver());

    public RoutingSelectionPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(selectRouting);
    }

    public RoutingSelectionPage selectLetAprioriDecide() {
        By byCheckBox = By.xpath("//input[@class]/parent::span/parent::div[@class]//span//input[@value='true']");
        getPageUtils().waitForElementToAppear(byCheckBox);
        getPageUtils().waitForElementAndClick(byCheckBox);
        return this;
    }

    /**
     * Select routing preference by name
     *
     * @param preference - routing preference
     * @return - this page object
     */
    public RoutingSelectionPage selectRoutingPreferenceByName(String preference) {
        By byRoutingPreference = By.xpath(String.format("//h3[text()='%s']/parent::div/following-sibling::div/button", preference));
        getPageUtils().waitForElementAndClick(byRoutingPreference);
        return this;
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancelButton(klass);
    }

    /**
     * Select the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submitButton(submit, klass);
    }

    /**
     * Checks if button is enabled
     *
     * @return boolean
     */
    public boolean isSubmitButtonEnabled() {
        return getPageUtils().isElementEnabled(submit);
    }
}
