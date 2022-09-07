package com.apriori.pageobjects.pages.evaluate.inputs;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.enums.NewCostingLabelEnum;
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

    @FindBy(css = ".MuiCheckbox-colorPrimary")
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

    /**
     * Clicks the Let aPriori Decide check box
     *
     * @return - current page object
     */
    public RoutingSelectionPage selectLetAprioriDecide() {
        getPageUtils().waitForElementAndClick(checkBox);
        return this;
    }

    /**
     * Select routing preference by name
     *
     * @param preference - routing preference
     * @return - current page object
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

    /**
     * Get cost label from a specific routing preference
     *
     * @param routingPreference - the routing preference
     * @param label             - cost label
     * @return - Boolean
     */
    public boolean isCostLabel(String routingPreference, NewCostingLabelEnum label) {
        By byCostLabel = By.xpath(String.format("//h3[text()='%s']/parent::div//div[.='%s']", routingPreference, label.getCostingText()));
        WebElement costLabel = getDriver().findElement(byCostLabel);
        return getPageUtils().textPresentInElement(costLabel, label.getCostingText());
    }
}
