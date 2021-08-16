package com.apriori.pageobjects.pages.evaluate.inputs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
    private WebElement opDefaultButton;

    @FindBy(css = ".process-selector-details [value='user']")
    private WebElement opOverrideButton;

    @FindBy(css = ".process-selector-details [type='number']")
    private WebElement opOverrideInput;

    @FindBy(css = ".process-selector-details [value='defaultNoMasking']")
    private WebElement maskedDefault;

    @FindBy(css = ".process-selector-details [value='userOverride']")
    private WebElement maskedUser;

    @FindBy(css = ".process-selector-details [type='number']")
    private WebElement maskedInput;

    @FindBy(css = ".process-selector-details [value='wholePart']")
    private WebElement fractionDefault;

    @FindBy(xpath = "//h6[contains(text(),'What Fraction of Component is Painted?')]/..//input[@value='user']")
    private WebElement fractionUser;

    @FindBy(css = ".process-selector-details [type='number']")
    private WebElement fractionInput;

    @FindBy(css = ".process-selector-details [value='threadedHoles']")
    private WebElement maskedFeaturesDefault;

    @FindBy(css = ".process-selector-details [value='none']")
    private WebElement noMasking;

    @FindBy(xpath = "//h6[contains(text(),'Number of Masked Features')]/..//input[@value='user']")
    private WebElement maskedFeatureUser;

    @FindBy(xpath = "//h6[contains(text(),'Number of Masked Features')]/..//input[@type='number']")
    private WebElement maskedFeatureInput;

    @FindBy(css = ".process-selector-details [value='productionBatchSize']")
    private WebElement batchSizeDefault;

    @FindBy(xpath = "//h6[contains(text(),'Painted Batch Size')]/..//input[@value='user']")
    private WebElement batchSizeUser;

    @FindBy(xpath = "//h6[contains(text(),'Painted Batch Size')]/..//input[@type='number']")
    private WebElement batchSizeInput;

    @FindBy(css = ".process-selector-details [value='auto']")
    private WebElement compPaintPartDefault;

    @FindBy(xpath = "//h6[contains(text(),'Number of Components Per Paint Cart')]/..//input[@value='user']")
    private WebElement paintCartUser;

    @FindBy(xpath = "//h6[contains(text(),'Number of Components Per Paint Cart')]/..//input[@type='number']")
    private WebElement paintCartInput;

    @FindBy(xpath = "//h6[contains(text(),'Number of Components Per Load Bar')]/..//input[@value='auto']")
    private WebElement compLoadBarDefault;

    @FindBy(xpath = "//h6[contains(text(),'Number of Components Per Load Bar')]/..//input[@value='user']")
    private WebElement compLoadBarUser;

    @FindBy(xpath = "//h6[contains(text(),'Number of Components Per Load Bar')]/..//input[@type='number']")
    private WebElement compLoadBarInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public SecondaryProcessesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
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
     * @param processTypes - the secondary process types
     * @param process      - the process
     * @return current page object
     */
    public SecondaryProcessesPage selectSecondaryProcess(String processTypes, String process) {
        expandSecondaryProcessTree(processTypes);
        checkSecondaryProcessBox(process);
        By byProcess = By.xpath(String.format("//span[.='%s']/ancestor::span[@role]", process.trim()));
        pageUtils.waitForElementAndClick(byProcess);
        return this;
    }

    /**
     * Selects the secondary process types in the process tree
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectSecondaryProcess(String process) {
        By byProcess = By.xpath(String.format("//span[.='%s']/ancestor::span[@role]", process.trim()));
        pageUtils.waitForElementAndClick(byProcess);
        return this;
    }

    /**
     * Select secondary process checkbox
     * @param process - the process
     * @return current page object
     */
    public SecondaryProcessesPage checkSecondaryProcessBox(String process) {
        By byProcess = By.xpath(String.format("//span[.='%s']/ancestor::span//label", process.trim()));
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
        By byProcess = By.xpath(String.format("//span[.='%s']/ancestor::span//label//input", process.trim()));
        return driver.findElement(byProcess).getAttribute("checked").equals("true");
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
        pageUtils.waitForElementAndClick(opOverrideButton);
        opOverrideInput.clear();
        opOverrideInput.sendKeys(value);
        return this;
    }

    /**
     * Get options
     *
     * @return string
     */
    public double getOptions() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(opOverrideInput).getAttribute("textContent"));
    }

    /**
     * Selects masked feature
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectMasking() {
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
        pageUtils.waitForElementAndClick(maskedUser);
        maskedInput.clear();
        maskedInput.sendKeys(value);
        return this;
    }

    /**
     * Get masked feature
     *
     * @return string
     */
    public double getMasking() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(maskedInput).getAttribute("textContent"));
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
     * Inputs masked feature
     *
     * @param value -the value
     * @return current page object
     */
    public SecondaryProcessesPage inputFractionOverride(String value) {
        pageUtils.waitForElementAndClick(fractionUser);
        fractionInput.clear();
        fractionInput.sendKeys(value);
        return this;
    }

    /**
     * Get masked feature
     *
     * @return string
     */
    public double getFractionPainted() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(fractionInput).getAttribute("textContent"));
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
     * Selects no masking
     *
     * @return current page object
     */
    public SecondaryProcessesPage selectNoMasking() {
        pageUtils.waitForElementAndClick(noMasking);
        return this;
    }

    /**
     * Input masked feature
     *
     * @param value - the value
     * @return current page object
     */
    public SecondaryProcessesPage inputMaskedFeatures(String value) {
        pageUtils.waitForElementAndClick(maskedFeatureUser);
        maskedFeatureInput.clear();
        maskedFeatureInput.sendKeys(value);
        return this;
    }

    /**
     * Gets masked feature
     *
     * @return double
     */
    public double getMaskedFeatures() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(maskedFeatureInput).getAttribute("textContent"));
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
        pageUtils.waitForElementAndClick(batchSizeUser);
        batchSizeInput.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
        batchSizeInput.sendKeys(value);
        return this;
    }

    /**
     * Gets batch size
     *
     * @return double
     */
    public double getBatchSize() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(batchSizeInput).getAttribute("textContent"));
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
        pageUtils.waitForElementAndClick(paintCartUser);
        paintCartInput.clear();
        paintCartInput.sendKeys(value);
        return this;
    }

    /**
     * Gets component per paint cart
     *
     * @return double
     */
    public double getComponentsPaintCart() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(paintCartInput).getAttribute("textContent"));
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
        pageUtils.waitForElementAndClick(compLoadBarUser);
        compLoadBarInput.clear();
        compLoadBarInput.sendKeys(value);
        return this;
    }

    /**
     * Gets component per paint cart
     *
     * @return double
     */
    public double getComponentsLoadBar() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(compLoadBarInput).getAttribute("textContent"));
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
