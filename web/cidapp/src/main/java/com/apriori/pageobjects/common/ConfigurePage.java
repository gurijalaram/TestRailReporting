package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurePage extends LoadableComponent<ConfigurePage> {

    private final Logger LOGGER = LoggerFactory.getLogger(ConfigurePage.class);

    @FindBy(xpath = "//label[.='Number of sticky columns']/ancestor::div//div[contains(@class,'apriori-select')]")
    private WebElement stickyDropdown;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public ConfigurePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(stickyDropdown);
    }

    /**
     * Set sticky column
     *
     * @param value - the number of sticky columns
     * @return current page object
     */
    public ConfigurePage setStickyColumn(String value) {
        pageUtils.waitForElementAndClick(stickyDropdown);
        By columnNo = By.cssSelector(String.format("button[value='%s']", value));
        pageUtils.waitForElementAndClick(columnNo);
        return this;
    }

    /**
     * Move the column
     *
     * @param direction - the direction
     * @return current page object
     */
    public ConfigurePage moveColumn(String direction) {
        By arrow = By.cssSelector(String.format("[data-icon='angle-%s']", direction));
        pageUtils.waitForElementAndClick(arrow);
        return this;
    }

    /**
     * Select the column
     *
     * @param columnName - the column
     * @return current page object
     */
    public ConfigurePage selectColumn(String columnName) {
        By column = By.xpath(String.format("//div[@class='checkbox-icon']/following-sibling::div[.='%s']", columnName));
        pageUtils.waitForElementAndClick(column);
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
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}