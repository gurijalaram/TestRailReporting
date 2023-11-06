package com.apriori.cid.ui.pageobjects.evaluate.inputs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.web.app.util.PageUtils;

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

    @FindBy(css = ".secondary-process-selector [type='submit']")
    private WebElement submitButton;

    @FindBy(css = ".description .content")
    private WebElement description;

    @FindBy(css = ".process-selector-details [value='default']")
    private WebElement opDefaultButton;

    @FindBy(css = ".process-selector-details [value='user']")
    private WebElement opOverrideButton;

    @FindBy(css = ".process-selector-details [type='number']")
    private WebElement opOverrideInput;

    @FindBy(css = ".process-selector-details [value='defaultNoMasking']")
    private WebElement maskedDefault;

    @FindBy(css = ".process-selector-details [value='userOverride']")
    private WebElement maskedUser;

    @FindBy(css = ".process-selector-details [value='wholePart']")
    private WebElement fractionDefault;

    @FindBy(css = ".process-selector-details [value='threadedHoles']")
    private WebElement maskedFeaturesDefault;

    @FindBy(css = ".process-selector-details [value='productionBatchSize']")
    private WebElement batchSizeDefault;

    @FindBy(css = ".process-selector-details [value='auto']")
    private WebElement compPaintPartDefault;

    @FindBy(xpath = "//h6[text()='Number of Components Per Load Bar']/..//input[@value='auto']")
    private WebElement compLoadBarDefault;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;
    private PsoController psoController;

    public SecondaryProcessesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.psoController = new PsoController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertEquals("Secondary Processes", title.getAttribute("textContent"), "Secondary Processes page was not displayed");
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
        assertTrue(driver.findElement(byTabName).getAttribute("class").contains("active"), "Correct tab was not selected");
    }

    /**
     * Expands the secondary process tree
     *
     * @param processTypes - the secondary process types
     * @return current page object
     */
    public SecondaryProcessesPage expandSecondaryProcessTree(String processTypes) {
        Arrays.stream(Stream.of(processTypes)
                .map(processType -> processType.split(","))
                .collect(Collectors.toList())
                .get(0))
            .forEach(process -> {
                By secondaryProcess = By.xpath(String.format("//span[.='%s']/ancestor::span", process.trim()));
                pageUtils.scrollWithJavaScript(pageUtils.waitForElementToAppear(secondaryProcess), true);

                By dropdown = By.xpath(String.format("//span[.='%s']/ancestor::span//button", process.trim()));
                if (pageUtils.isElementPresent(dropdown)) {
                    pageUtils.waitForElementAndClick(dropdown);
                }
            });
        return this;
    }

    /**
     * Selects the secondary process types in the process tree
     *
     * @param process - the process
     * @return current page object
     */
    public SecondaryProcessesPage selectSecondaryProcess(String process) {
        highlightSecondaryProcess(process);
        By byProcess = By.xpath(String.format("//span[.='%s']/ancestor::span//label", process.trim()));
        pageUtils.waitForElementAndClick(byProcess);
        return this;
    }

    /**
     * Select secondary process checkbox
     *
     * @param process - the process
     * @return current page object
     */
    public SecondaryProcessesPage highlightSecondaryProcess(String process) {
        By byProcess = By.xpath(String.format("//span[.='%s']/ancestor::span[@role]", process.trim()));
        pageUtils.waitForElementAndClick(byProcess);
        return this;
    }

    /**
     * Select the secondary process checkbox
     *
     * @param process - the process
     * @return true/false
     */
    public boolean isCheckboxSelected(String process) {
        By byProcess = with(By.cssSelector("svg"))
            .near(By.xpath(String.format("//span[.='%s']/ancestor::span", process.trim())));
        return driver.findElement(byProcess).getDomAttribute("data-icon").equals("check-square");
    }

    /**
     * Get list of selected items
     *
     * @return list of string
     */
    public List<String> getSelectedPreviewList() {
        return Arrays.stream(selectedPreviewItems.getText().split("")).collect(Collectors.toList());
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
     * Selects options default
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectOptionsDefault() {
        pageUtils.waitForElementAndClick(opDefaultButton);
        return this;
    }

    /**
     * Inputs option override
     *
     * @param value -the value
     * @return current page object
     */
    public SecondaryProcessesPage inputOptionsOverride(String value) {
        psoController.inputOverrideValue(opOverrideButton, opOverrideInput, value);
        return this;
    }

    /**
     * Get options
     *
     * @return string
     */
    public double getOptions() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(opOverrideInput).getAttribute("value"));
    }

    /**
     * Selects masked feature
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectNumberOfMasking() {
        pageUtils.waitForElementAndClick(maskedDefault);
        return this;
    }

    /**
     * Inputs masked feature
     *
     * @param value -the value
     * @return current page object
     */
    public SecondaryProcessesPage inputMaskingOverride(String value) {
        psoController.inputOverrideValue(maskedUser, psoController.inputLocator("Masking"), value);
        return this;
    }

    /**
     * Get masked feature
     *
     * @return string
     */
    public double getMasking() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(psoController.inputLocator("Masking")).getAttribute("value"));
    }

    /**
     * Selects fraction painted
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectFractionPainted() {
        pageUtils.waitForElementAndClick(fractionDefault);
        return this;
    }

    /**
     * Inputs fraction override
     *
     * @param value -the value
     * @return current page object
     */
    public SecondaryProcessesPage inputFractionOverride(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("What Fraction of Component is Painted?", "user"),
            psoController.inputLocator("What Fraction of Component is Painted?"), value);
        return this;
    }

    /**
     * Inputs fraction part area
     *
     * @param value -the value
     * @return current page object
     */
    public SecondaryProcessesPage inputFractionPartArea(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Fraction of Part Area that is Powder Coated", "user"),
            psoController.inputLocator("Fraction of Part Area that is Powder Coated"), value);
        return this;
    }

    /**
     * Selects masked features
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectMaskedFeatures() {
        pageUtils.waitForElementAndClick(maskedFeaturesDefault);
        return this;
    }

    /**
     * Inputs masking
     *
     * @param value - the value
     * @return current page object
     */
    public SecondaryProcessesPage inputMasking(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Masking", "userOverride"), psoController.inputLocator("Masking"), value);
        return this;
    }

    /**
     * Selects no masking
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectNoMasking() {
        pageUtils.waitForElementAndClick(psoController.buildLocator("Number of Masked Features", "none"));
        return this;
    }

    /**
     * Input masked feature
     *
     * @param value - the value
     * @return current page object
     */
    public SecondaryProcessesPage inputMaskedFeatures(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Number of Masked Features", "user"), psoController.inputLocator("Number of Masked Features"), value);
        return this;
    }

    /**
     * Selects batch size
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectBatchSizeDefault() {
        pageUtils.waitForElementAndClick(batchSizeDefault);
        return this;
    }

    /**
     * Inputs batch size
     *
     * @param value - the value
     * @return current page object
     */
    public SecondaryProcessesPage inputBatchSizeOverride(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Painted Batch Size", "user"), psoController.inputLocator("Painted Batch Size"), value);
        return this;
    }

    /**
     * Select components per paint cart
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectCompPaintCart() {
        pageUtils.waitForElementAndClick(compPaintPartDefault);
        return this;
    }

    /**
     * Inputs component per paint cart
     *
     * @param value - the value
     * @return current page object
     */
    public SecondaryProcessesPage inputCompPaintCart(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Number of Components Per Paint Cart", "user"), psoController.inputLocator("Number of Components Per Paint Cart"), value);
        return this;
    }

    /**
     * Select components per load bar
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectCompLoadBar() {
        pageUtils.waitForElementAndClick(compLoadBarDefault);
        return this;
    }

    /**
     * Inputs component per load bar
     *
     * @param value - the value
     * @return current page object
     */
    public SecondaryProcessesPage inputCompLoadBar(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Number of Components Per Load Bar", "user"), psoController.inputLocator("Number of Components Per Load Bar"), value);
        return this;
    }

    /**
     * Gets value for overridden PSO
     *
     * @param pso - the pso
     * @return double
     */
    public double getOverriddenPso(String pso) {
        return psoController.getOverriddenPso(pso);
    }

    /**
     * Enter search input
     *
     * @param searchTerm - search term
     * @return current page object
     */
    public SecondaryProcessesPage search(String searchTerm) {
        pageUtils.clearValueOfElement(searchInput);
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
        return modalDialogController.submit(submitButton, klass);
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
