package com.apriori.cid.ui.pageobjects.navtoolbars;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.cid.ui.pageobjects.common.StatusIcon;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

/**
 * @author cfrith
 */

@Slf4j
public class InfoPage extends LoadableComponent<InfoPage> {

    @FindBy(xpath = "//label[.='Status']")
    private WebElement statusLabel;

    @FindBy(css = "[id='qa-scenario-info-form-status-select'] .apriori-select")
    private WebElement statusDropdown;

    @FindBy(css = "[id='qa-scenario-info-form-status-select'] input")
    private WebElement statusInput;

    @FindBy(css = "[id='qa-scenario-info-form-cost-maturity-select'] .apriori-select")
    private WebElement costMaturityDropdown;

    @FindBy(css = "[id='qa-scenario-info-form-cost-maturity-select'] input")
    private WebElement costMaturityInput;

    @FindBy(css = "textarea[name='description']")
    private WebElement descriptionInput;

    @FindBy(css = "textarea[name='notes']")
    private WebElement notesInput;

    @FindBy(css = "[id='qa-scenario-info-form-status-select'] [id]")
    private WebElement statusText;

    @FindBy(css = "[id='qa-scenario-info-form-cost-maturity-select'] [id]")
    private WebElement costMaturityText;

    @FindBy(css = "[role='dialog'] button[type='submit']")
    private WebElement submitButton;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;
    private StatusIcon statusIcon;

    public InfoPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.statusIcon = new StatusIcon(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertEquals("Scenario Info & Notes", pageUtils.waitForElementToAppear(statusLabel).getAttribute("textContent"), "Scenario Notes dialog is not displayed");
    }

    /**
     * Uses type ahead to input the status
     *
     * @param status - the status
     * @return current page object
     */
    public InfoPage selectStatus(String status) {
        pageUtils.typeAheadSelect(statusDropdown, "qa-scenario-info-form-status-select", status);
        return this;
    }

    /**
     * Uses type ahead to input the cost maturity
     *
     * @param costMaturity - the cost maturity
     * @return current page object
     */
    public InfoPage inputCostMaturity(String costMaturity) {
        pageUtils.typeAheadSelect(costMaturityDropdown, "qa-scenario-info-form-cost-maturity-select", costMaturity);
        return this;
    }

    /**
     * Input description
     *
     * @param description - the description
     * @return current page object
     */
    public InfoPage inputDescription(String description) {
        inputFields(descriptionInput, description);
        return this;
    }

    /**
     * Edits the scenario notes
     *
     * @param description - the description notes
     * @return current page object
     */
    public InfoPage editDescription(String description) {
        inputFields(descriptionInput, description);
        return this;
    }

    /**
     * Input notes
     *
     * @param notes - the notes
     * @return current page object
     */
    public InfoPage inputNotes(String notes) {
        inputFields(notesInput, notes);
        return this;
    }

    /**
     * Edits the scenario notes
     *
     * @param notes - the scenario notes
     * @return current page object
     */
    public InfoPage editNotes(String notes) {
        inputFields(notesInput, notes);
        return this;
    }

    /**
     * Input fields
     *
     * @param element     - the element
     * @param description - the description
     */
    private void inputFields(WebElement element, String description) {
        pageUtils.waitForElementAndClick(element);
        pageUtils.clearValueOfElement(element);
        element.sendKeys(description);
    }

    /**
     * Get description
     *
     * @return string
     */
    public String getDescription() {
        return descriptionInput.getAttribute("textContent");
    }

    /**
     * Get notes
     *
     * @return string
     */
    public String getNotes() {
        return notesInput.getAttribute("textContent");
    }

    /**
     * Checks icon is displayed
     *
     * @param icon - the icon
     * @return true/false
     */
    public boolean isIconDisplayed(StatusIconEnum icon) {
        By iconStatus = By.cssSelector(String.format("[id='modal-body'] [data-icon='%s']", icon.getStatusIcon()));
        return pageUtils.waitForElementToAppear(iconStatus).isDisplayed();
    }

    /**
     * Gets scenario info
     *
     * @param label    - the label
     * @param assignee - the assignee
     * @return string
     */
    public boolean isScenarioInfo(String label, String assignee) {
        By byLabel = By.xpath(String.format("//span[.='%s']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(byLabel).getAttribute("textContent").equals(assignee);
    }

    /**
     * Gets status
     *
     * @return string
     */
    public String getStatus() {
        return pageUtils.waitForElementToAppear(statusText).getAttribute("textContent");
    }

    /**
     * Gets cost maturity
     *
     * @return string
     */
    public String getCostMaturity() {
        return pageUtils.waitForElementToAppear(costMaturityText).getAttribute("textContent");
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(submitButton, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}
