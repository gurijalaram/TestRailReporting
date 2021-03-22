package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PrimaryInputsController {

    private static final Logger logger = LoggerFactory.getLogger(PrimaryInputsController.class);

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
     * @param processGroup - the process group
     * @return current page object
     */
    public PrimaryInputsController selectProcessGroup(WebElement processGroupDropdown, String processGroup) {
        pageUtils.waitForElementAndClick(processGroupDropdown);
        By group = By.cssSelector(String.format("button[value='%s']", processGroup));
        pageUtils.scrollWithJavaScript(driver.findElement(group), true).click();
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
     * @param vpeDropdown - the vpe dropdown
     * @param vpe         - the vpe
     * @return current page object
     */
    public PrimaryInputsController selectVPE(WebElement vpeDropdown, String vpe) {
        pageUtils.waitForElementAndClick(vpeDropdown);
        By vp = By.cssSelector(String.format("button[value='%s']", vpe));
        pageUtils.scrollWithJavaScript(driver.findElement(vp), true).click();
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
}