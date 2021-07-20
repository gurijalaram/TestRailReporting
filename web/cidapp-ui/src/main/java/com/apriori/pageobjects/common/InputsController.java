package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;

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
     * Selects the pg dropdown
     *
     * @param processGroupDropdown - the process group dropdown
     * @param processGroup         - the process group
     * @return current page object
     */
    public InputsController selectProcessGroup(WebElement processGroupDropdown, ProcessGroupEnum processGroup) {
        pageUtils.typeAheadSelect(processGroupDropdown, processGroup.getProcessGroup());
        return this;
    }

    /**
     * Enters the annual volume
     *
     * @param annualVolumeInput - the annual volume input
     * @param annualVolume      - the annual volume
     * @return current page object
     */
    public InputsController enterAnnualVolume(WebElement annualVolumeInput, String annualVolume) {
        annualVolumeInput.clear();
        annualVolumeInput.sendKeys(Keys.DELETE);
        annualVolumeInput.sendKeys(annualVolume);
        return this;
    }

    /**
     * Selects the vpe dropdown
     *
     * @param digitalFactoryDropdown - the vpe dropdown
     * @param digitalFactory         - the vpe
     * @return current page object
     */
    public InputsController selectDigitalFactory(WebElement digitalFactoryDropdown, DigitalFactoryEnum digitalFactory) {
        pageUtils.typeAheadSelect(digitalFactoryDropdown, digitalFactory.getDigitalFactory());
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
        productionLifeInput.clear();
        productionLifeInput.sendKeys(Keys.DELETE);
        productionLifeInput.sendKeys(productionLife);
        return this;
    }

    /**
     * Opens the secondary processes page
     *
     * @param secondaryProcessesPencil - secondary process pencil icon
     * @return new page object
     */
    public InputsController openMachiningProcesses(WebElement secondaryProcessesPencil) {
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
        batchSizeInput.clear();
        batchSizeInput.sendKeys(Keys.DELETE);
        batchSizeInput.sendKeys(batchSize);
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
        return Arrays.stream(digitalFactoryList.getText().split("\n")).filter(x -> !x.equalsIgnoreCase(filterOption)).collect(Collectors.toList());
    }
}
