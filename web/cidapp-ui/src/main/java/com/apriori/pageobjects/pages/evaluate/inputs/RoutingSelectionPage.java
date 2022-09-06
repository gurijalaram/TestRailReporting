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

    @FindBy(css = "[data-testid='select-routing-btn-%s']")
    private WebElement routingSelection;

    private ModalDialogController modalDialogController = new ModalDialogController(getDriver());

    public RoutingSelectionPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(selectRouting);
    }

    public RoutingSelectionPage selectLetAprioriDecide() {
        By byCheckBox = By.xpath("//input[@class]/parent::span/parent::div[@class]//span//input[@value='false']");
        getPageUtils().waitForElementToAppear(byCheckBox);
        getPageUtils().waitForElementAndClick(byCheckBox);
        return this;
    }

    public RoutingSelectionPage selectPlasma(int indexNumber) {
        By byRoutingIndex = By.cssSelector(String.format("[data-testid='select-routing-btn-%s']", indexNumber - 1));
        getPageUtils().waitForElementAndClick(byRoutingIndex);
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
        return modalDialogController.submitButton(klass);
    }
}
