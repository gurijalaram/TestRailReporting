package com.apriori.pageobjects.common;

import com.apriori.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class InputsController {

    private WebDriver driver;
    private PageUtils pageUtils;

    public InputsController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Enters the annual volume
     *
     * @param annualVolumeInput - the annual volume input
     * @param annualVolume      - the annual volume
     * @return current page object
     */
    public InputsController enterAnnualVolume(WebElement annualVolumeInput, String annualVolume) {
        pageUtils.clearValueOfElement(annualVolumeInput);
        annualVolumeInput.sendKeys(annualVolume);
        annualVolumeInput.sendKeys(Keys.TAB);
        return this;
    }

    /**
     * Selects the vpe dropdown
     *
     * @param digitalFactoryDropdown - the vpe dropdown
     * @param root                   - the bottom level of the locator. this is the page the element is located on eg. can be in a modal dialog
     * @param value                  - the value
     * @return current page object
     */
    public InputsController selectInputsDropdown(WebElement digitalFactoryDropdown, String root, String value) {
        pageUtils.typeAheadSelect(digitalFactoryDropdown, root, value);
        return this;
    }

    /**
     * Selects the Digital factory dropdown
     *
     * @param digitalFactoryDropdown - the digital factory dropdown
     * @param value                  - the value
     * @return current page object
     */
    public InputsController selectInputsDropdown(WebElement digitalFactoryDropdown, String value) {
        pageUtils.typeAheadSelect(digitalFactoryDropdown, value);
        return this;
    }

    /**
     * Enters the years of annual volume
     *
     * @param productionLifeInput - the production life input
     * @param productionLife      - the years
     * @return current page object
     */
    public InputsController enterAnnualYears(WebElement productionLifeInput, String productionLife) {
        pageUtils.clearValueOfElement(productionLifeInput);
        productionLifeInput.sendKeys(productionLife);
        productionLifeInput.sendKeys(Keys.TAB);
        return this;
    }

    /**
     * Opens the secondary processes page
     *
     * @param secondaryProcessesPencil - secondary process pencil icon
     * @return new page object
     */
    public InputsController openSecondaryProcesses(WebElement secondaryProcessesPencil) {
        pageUtils.waitForElementAndClick(secondaryProcessesPencil);
        return this;
    }

    /**
     * Enters the batch size
     *
     * @param batchSizeInput - the batch size input
     * @param batchSize      - the batch size
     * @return current page object
     */
    public InputsController enterBatchSize(WebElement batchSizeInput, String batchSize) {
        pageUtils.clearValueOfElement(batchSizeInput);
        batchSizeInput.sendKeys(batchSize);
        batchSizeInput.sendKeys(Keys.TAB);
        return this;
    }

    /**
     * Gets list of secondary processes
     *
     * @param secondaryProcesses - list of secondary processes
     * @return list of string
     */
    public List<String> getSecondaryProcesses(WebElement secondaryProcesses) {
        return Arrays.stream(secondaryProcesses.getText().split("\n")).collect(Collectors.toList());
    }

    /**
     * Opens the material selector table
     *
     * @param materialsPencil - material pencil icon
     * @return new page object
     */
    public InputsController openMaterialSelectorTable(WebElement materialsPencil) {
        pageUtils.waitForElementAndClick(materialsPencil);
        return this;
    }

    /**
     * Opens the source component selector table
     *
     * @param sourceComponentPencil - Source Model pencil icon
     * @return new page object
     */
    public InputsController openSourceModelSelectorTable(WebElement sourceComponentPencil) {
        pageUtils.waitForElementAndClick(sourceComponentPencil);
        return this;
    }

    /**
     * Gets list of digital factory
     *
     * @return list as string
     */
    public List<String> getListOfDigitalFactory(WebElement digitalFactoryList, String filterOption) {
        return Arrays.stream(digitalFactoryList.getText().split("")).filter(x -> !x.equalsIgnoreCase(filterOption)).collect(Collectors.toList());
    }

    /**
     * Selects the Do not machine this part checkbox
     * @param machineCheckbox - the machine checkbox webelement
     * @return current page object
     */
    public InputsController selectMachineOptionsCheckbox(WebElement machineCheckbox) {
        pageUtils.waitForElementAndClick(machineCheckbox);
        return this;
    }

    /**
     * Checks if the machine part checkbox is displayed
     * @param machineCheckbox - the machine checkbox webelement
     * @return boolean
     */
    public boolean isMachineOptionsCheckboxDisplayed(WebElement machineCheckbox) {
        return pageUtils.isElementDisplayed(machineCheckbox);
    }

    /**
     * Checks if the machine part checkbox is selected
     * @param checkBoxInput - the machine checkbox webelement
     * @return boolean
     */
    public boolean isMachineOptionsCheckboxSelected(WebElement checkBoxInput) {
        return pageUtils.isCheckboxSelected(checkBoxInput);
    }

    /**
     * Checks if the select source model button is enabled
     * @param sourceComponentPencil - Source Model pencil icon
     * @return boolean
     */
    public boolean isSelectSourceButtonEnabled(WebElement sourceComponentPencil) {
        return pageUtils.isElementEnabled(sourceComponentPencil);
    }

    /**
     * Closes the source model invalid message
     *
     * @param  closeButton
     * @return new page object
     */
    public InputsController closeMessagePanel(WebElement closeButton) {
        pageUtils.waitForElementAndClick(closeButton);
        return this;
    }
}
