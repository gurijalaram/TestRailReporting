package com.apriori.pageobjects.pages.evaluate.inputs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.ProcessOptionsController;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SecondaryProcessesPage extends LoadableComponent<SecondaryProcessesPage> {

    @FindBy(css = ".modal-title")
    private WebElement title;

    @FindBy(xpath = "//input[@placeholder='Search...']")
    private WebElement searchInput;

    @FindBy(css = ".selected-preview .pill-box")
    private WebElement selectedPreviewItems;

    @FindBy(css = ".description .content")
    private WebElement description;

    @FindBy(css = ".process-selector-details [value='default']")
    private WebElement defaultValueButton;

    @FindBy(css = ".process-selector-details [value='user']")
    private WebElement overrideButton;

    @FindBy(css = ".process-setup-option-form-group [type='number']")
    private WebElement overrideInput;

    @FindBy(css = "[value='defaultNoMasking']")
    private WebElement maskingButton;

    @FindBy(css = ".process-selector-details [value='userOverride']")
    private WebElement maskingFeaturesButton;

    @FindBy(xpath = ".process-selector-details [type='number']")
    private WebElement maskingInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;
    private ProcessOptionsController processOptionsController;

    public SecondaryProcessesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.processOptionsController = new ProcessOptionsController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertEquals("Secondary Processes page was not displayed", "Secondary Processes", title.getAttribute("textContent"));
    }

    /**
     * Go to surface treatment tab
     *
     * @return current page object
     */
    public SecondaryProcessesPage goToSurfaceTreatmentTab() {
        goToTab("Surface Treatment");
        return this;
    }

    /**
     * Go to machining tab
     *
     * @return current page object
     */
    public SecondaryProcessesPage goToMachiningTab() {
        goToTab("Machining");
        return this;
    }

    /**
     * Go to heat treament tab
     *
     * @return current page object
     */
    public SecondaryProcessesPage goToHeatTreatmentTab() {
        goToTab("Heat Treatment");
        return this;
    }

    /**
     * Go to other secondary processes tab
     *
     * @return current page object
     */
    public SecondaryProcessesPage goToOtherSecProcessesTab() {
        goToTab("Other Secondary Processes");
        return this;
    }

    /**
     * Go to other secondary processes tab
     *
     * @return current page object
     */
    public SecondaryProcessesPage goToCastingDieTab() {
        goToTab("Casting - Die");
        return this;
    }

    /**
     * Go to tab
     *
     * @param tabName - the tab name
     */
    private void goToTab(String tabName) {
        By byTabName = By.xpath(String.format("//button[contains(text(),'%s')]", tabName));
        pageUtils.waitForElementAndClick(byTabName);
        assertTrue("Correct tab was not selected", driver.findElement(byTabName).getAttribute("class").contains("active"));
    }

    /**
     * Selects the secondary types dropdowns in the process tree
     *
     * @param processTypes - the secondary process type
     * @return current page object
     */
    public SecondaryProcessesPage selectSecondaryProcess(String processTypes) {
        Arrays.stream(Stream.of(processTypes)
            .map(processType -> processType.split(","))
            .collect(Collectors.toList())
            .get(0))
            .forEach(process -> {
                By secondaryProcess = By.xpath(String.format("//span[.='%s']/ancestor::span", process.trim()));
                pageUtils.scrollWithJavaScript(pageUtils.waitForElementToAppear(secondaryProcess), true);

                // TODO: 22/07/2021 cn - find a more efficient way of doing this
                By dropdown = By.xpath(String.format("//span[.='%s']/ancestor::span//label", process.trim()));
                if (pageUtils.isElementPresent(dropdown)) {
                    pageUtils.waitForElementAndClick(dropdown);
                }

                By label = By.xpath(String.format("//span[.='%s']/ancestor::span//button", process.trim()));
                if (pageUtils.isElementPresent(label)) {
                    pageUtils.waitForElementAndClick(label);
                }
            });
        return this;
    }

    /**
     * Get list of selected items
     *
     * @return list of string
     */
    public List<String> getSelectedPreviewList() {
        return Arrays.stream(selectedPreviewItems.getText().split("\n")).collect(Collectors.toList());
    }

    /**
     * Get number of selected processes
     *
     * @return
     */
    public int getNoOfSelected() {
        By amounts = By.cssSelector("div[class='selected-amount'] span");
        String[] amount = pageUtils.waitForElementToAppear(amounts).getText().split("of");
        return Integer.parseInt(amount[0].trim());
    }

    /**
     * Gets description
     *
     * @return string
     */
    public String getDescription() {
        return pageUtils.waitForElementToAppear(description).getAttribute("textContent");
    }

    /**
     * Selects default value radio button
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectDefaultValue() {
        processOptionsController.selectDefaultValue(defaultValueButton);
        return this;
    }

    /**
     * Selects override radio button
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectOverride() {
        processOptionsController.selectOverride(overrideButton);
        return this;
    }

    /**
     * Inputs override value
     *
     * @param value - the value
     * @return current page object
     */
    public SecondaryProcessesPage inputOverride(String value) {
        processOptionsController.inputOverride(overrideInput, value);
        return this;
    }

    /**
     * Select masking
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectMasking() {
        processOptionsController.selectMasking(maskingButton);
        return this;
    }

    /**
     * Select masked features
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectMaskedFeatures() {
        processOptionsController.selectMaskedFeatures(maskingFeaturesButton);
        return this;
    }

    /**
     * Select masked input
     *
     * @param value        - the value
     * @return current page object
     */
    public SecondaryProcessesPage inputMaskedFeatures(String value) {
        processOptionsController.inputMaskedFeatures(maskingInput, value);
        return this;
    }

    /**
     * Gets masking input
     * @return string
     */
    public String getMaskedFeatures() {
        return processOptionsController.getMaskedFeatures(maskingInput);
    }

    /**
     * Enter search input
     *
     * @param searchTerm - search term
     * @return current page object
     */
    public SecondaryProcessesPage search(String searchTerm) {
        pageUtils.waitForElementAppear(searchInput).clear();
        searchInput.sendKeys(searchTerm);
        return this;
    }

    /**
     * Expand all
     *
     * @return current page object
     */
    public SecondaryProcessesPage expandAll() {
        modalDialogController.expandAll();
        return this;
    }

    /**
     * Collapse all
     *
     * @return current page object
     */
    public SecondaryProcessesPage collapseAll() {
        modalDialogController.collapseAll();
        return this;
    }

    /**
     * Deselect all
     *
     * @return current page object
     */
    public SecondaryProcessesPage deselectAll() {
        modalDialogController.deselectAll();
        return this;
    }

    /**
     * Reset
     *
     * @return current page object
     */
    public SecondaryProcessesPage reset() {
        modalDialogController.reset();
        return this;
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
    public EvaluatePage cancel() {
        return modalDialogController.cancel(EvaluatePage.class);
    }
}
