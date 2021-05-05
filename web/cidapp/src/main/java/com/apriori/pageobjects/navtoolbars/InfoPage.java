package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.common.StatusIcon;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.StatusIconEnum;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class InfoPage extends LoadableComponent<InfoPage> {

    private static final Logger logger = LoggerFactory.getLogger(InfoPage.class);

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

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;
    private StatusIcon statusIcon;

    public InfoPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.statusIcon = new StatusIcon(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(statusLabel);
    }

    /**
     * Uses type ahead to input the status
     *
     * @param status - the status
     * @return current page object
     */
    public InfoPage inputStatus(String status) {
        pageUtils.typeAheadInput(statusDropdown, statusInput, status);
        return this;
    }

    /**
     * Uses type ahead to input the cost maturity
     *
     * @param costMaturity - the cost maturity
     * @return current page object
     */
    public InfoPage inputCostMaturity(String costMaturity) {
        pageUtils.typeAheadInput(costMaturityDropdown, costMaturityInput, costMaturity);
        return this;
    }

    /**
     * Input description
     *
     * @param description - the description
     * @return current page object
     */
    public InfoPage inputDescription(String description) {
        pageUtils.waitForElementAndClick(descriptionInput);
        descriptionInput.clear();
        descriptionInput.sendKeys(description);
        return this;
    }

    /**
     * Input notes
     *
     * @param notes - the notes
     * @return current page object
     */
    public InfoPage inputNotes(String notes) {
        pageUtils.waitForElementAndClick(notesInput);
        notesInput.clear();
        notesInput.sendKeys(notes);
        return this;
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
        return statusIcon.isIconDisplayed(icon);
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(klass);
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
