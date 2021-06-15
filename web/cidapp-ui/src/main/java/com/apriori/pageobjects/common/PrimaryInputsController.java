package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PrimaryInputsController {

    private static final Logger logger = LoggerFactory.getLogger(PrimaryInputsController.class);

    @FindBy(css = "div[id='qa-process-group-select-field'] .apriori-select")
    private WebElement pgDropdown;

    @FindBy(css = "div[id='qa-digital-factory-select-field'] .apriori-select")
    private WebElement digitalFactoryDropdown;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PrimaryInputsController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Selects the pg dropdown
     *
     * @param processGroupDropdown - the process group dropdown
     * @param processGroup         - the process group
     * @return current page object
     */
    public PrimaryInputsController selectProcessGroup(WebElement processGroupDropdown, String processGroup) {
        pageUtils.typeAheadSelect(processGroupDropdown, processGroup);
        return this;
    }

    /**
     * Enters the annual volume
     *
     * @param annualVolumeInput - the annual volume input
     * @param annualVolume      - the annual volume
     * @return current page object
     */
    public PrimaryInputsController enterAnnualVolume(WebElement annualVolumeInput, String annualVolume) {
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
    public PrimaryInputsController selectDigitalFactory(WebElement digitalFactoryDropdown, String digitalFactory) {
        pageUtils.typeAheadSelect(digitalFactoryDropdown, digitalFactory);
        return this;
    }

    /**
     * Enters the years of annual volume
     *
     * @param productionLifeInput - the production life input
     * @param productionLife      - the years
     * @return current page object
     */
    public PrimaryInputsController enterAnnualYears(WebElement productionLifeInput, String productionLife) {
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
    public PrimaryInputsController openSecondaryProcesses(WebElement secondaryProcessesPencil) {
        pageUtils.waitForElementAndClick(secondaryProcessesPencil);
        return this;
    }

    /**
     * Gets list of secondary processes
     *
     * @param secondaryProcesses - list of secondary processes
     * @return list of string
     */
    public List<String> getSecondaryProcesses(List<WebElement> secondaryProcesses) {
        return secondaryProcesses.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Opens the material selector table
     *
     * @param materialsPencil - material pencil icon
     * @return new page object
     */
    public PrimaryInputsController openMaterialSelectorTable(WebElement materialsPencil) {
        pageUtils.waitForElementAndClick(materialsPencil);
        return this;
    }

    /**
     * Opens the source component selector table
     *
     * @param sourceComponentPencil - Source Model pencil icon
     * @return new page object
     */
    public PrimaryInputsController openSourceModelSelectorTable(WebElement sourceComponentPencil) {
        pageUtils.waitForElementAndClick(sourceComponentPencil);
        return this;
    }
}
