package com.apriori.pageobjects.evaluate.inputs;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.EagerPageComponent;
import com.apriori.pageobjects.common.ModalDialogController;

import com.utils.ComparisonDeltaEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class RoutingSelectionPage extends EagerPageComponent<RoutingSelectionPage> {

    @FindBy(css = "[role='dialog'] .dialog-title")
    private WebElement dialogTitle;

    @FindBy(css = ".MuiCheckbox-colorPrimary")
    private WebElement checkBox;

    @FindBy(xpath = "//div[@id='styled-routings-list']/following-sibling::div//button[.='Submit']")
    private WebElement submit;

    @FindBy(css = ".MuiChip-label [data-testid='logo']")
    private WebElement aPLogo;

    @FindBy(css = "[role='dialog'] h3")
    private List<WebElement> routingNames;

    private ModalDialogController modalDialogController = new ModalDialogController(getDriver());

    public RoutingSelectionPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(getPageUtils().waitForElementToAppear(dialogTitle).getAttribute("textContent").contains("Select Routing"), "Select Routing page is not displayed");
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
     * Get cost preferences from a specific routing preference
     *
     * @param routingPreference - the routing preference
     * @return - Boolean
     */

    public List<String> getRoutingStates(String routingPreference) {
        return getPageUtils().waitForElementsToAppear(byCostStatus(routingPreference)).stream().map(o -> o.getAttribute("textContent")).collect(Collectors.toList());
    }

    /**
     * verify the cost difference
     *
     * @param routingPreference - the routing preference
     * @param value             - the value
     * @return - Boolean
     */
    public boolean isCostDifference(String routingPreference, String value) {
        return getPageUtils().textPresentInElement(getDriver().findElement(
            By.xpath(String.format("//h3[text()='%s']/parent::div//following-sibling::span[.='%s']//span", routingPreference, value))), value);
    }

    /**
     * Get the value of the cost difference
     *
     * @param routingPreference - the routing preference
     * @return double
     */
    public double getCostDifferenceValue(String routingPreference) {
        By value = By.xpath(String.format("//h3[text()='%s']/parent::div", routingPreference));
        return Double.parseDouble(getPageUtils().waitForElementToAppear(value).getAttribute("textContent")
            .replaceAll("-", "0").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Get the value of the cost status
     *
     * @param routingPreference - the routing preference
     * @return string
     */
    public String getCostStatusValue(String routingPreference) {
        By value = byCostStatus(routingPreference);
        return getPageUtils().waitForElementToAppear(value).getAttribute("textContent");
    }

    /**
     * Get by routing preference and cost status
     *
     * @param routingPreference - the routing preference
     * @return by
     */
    private By byCostStatus(String routingPreference) {
        return By.xpath(String.format("//h3[text()='%s']/..//span[contains(@class,'MuiChip-label MuiChip-labelSmall')]", routingPreference));
    }

    /**
     * verify the delta arrow key
     *
     * @param routingPreference - the routing preference
     * @param value             -the value
     * @return - Boolean
     */
    public boolean isDeltaArrowKey(String routingPreference, ComparisonDeltaEnum value) {
        return getPageUtils().isElementDisplayed(getDriver().findElement(
            By.xpath(String.format("//h3[text()='%s']/parent::div//*[@data-icon='%s']", routingPreference, value.getDelta()))));
    }

    /**
     * verify the presence of the aPriori logo in a row
     *
     * @param routingPreference - the routing preference
     * @return - Boolean
     */
    public boolean isAprioriLogoDisplayed(String routingPreference) {
        return getPageUtils().isElementDisplayed(getDriver().findElement(
            By.xpath(String.format("//h3[text()='%s']/parent::div//*[@data-testid='logo']", routingPreference))));
    }

    /**
     * Check if user tile is displayed
     *
     * @param routingPreference - the routing preference
     * @return true/false
     */
    public boolean isUserTileDisplayed(String routingPreference) {
        By byUserTile = with(By.cssSelector("[data-icon='user-check']"))
            .below(By.xpath(String.format("//h3[text()='%s']", routingPreference)));
        return getPageUtils().isElementDisplayed(byUserTile);
    }

    /**
     * Gets the text of the button
     *
     * @param routingPreference - the routing preference
     * @return string
     */
    public String getSelectionStatus(String routingPreference) {
        By byButton = with(By.xpath("//button"))
            .toRightOf(By.xpath(String.format("//h3[text()='%s']", routingPreference)));
        return getDriver().findElement(byButton).getAttribute("textContent");
    }

    /**
     * @return the list of available routings
     */
    public List<String> getAvailableRoutings() {
        return getPageUtils().waitForElementsToAppear(routingNames).stream().map(routingName -> routingName.getAttribute("textContent")).collect(Collectors.toList());
    }
}
