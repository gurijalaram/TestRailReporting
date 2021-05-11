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

    private static final Logger logger = LoggerFactory.getLogger(ConfigurePage.class);

    @FindBy(xpath = "//label[.='Number of sticky columns']/ancestor::div//div[contains(@class,'apriori-select')]")
    private WebElement stickyDropdown;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public ConfigurePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
        pageUtils.javaScriptClick(driver.findElement(By.xpath(String.format("button[value='%s']", value))));
        return this;
    }

    /**
     * Move the column
     *
     * @param direction - the direction
     * @return current page object
     */
    public ConfigurePage moveColumn(DirectionEnum direction) {
        By arrow = By.cssSelector(String.format("[data-icon='angle-%s']", direction.getDirection()));
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
        By byColumn = By.xpath(String.format("//div[@class='checkbox-icon']/following-sibling::div[.='%s']", columnName));
        pageUtils.waitForElementAndClick(byColumn);
        return this;
    }

    public ConfigurePage moveToTop(String columnName) {
        By byColumn = By.xpath(String.format("//div[@class='checkbox-icon']/following-sibling::div[.='%s']", columnName));
        pageUtils.waitForElementAndClick(byColumn);

        By byArrow = By.cssSelector(String.format("[data-icon='angle-%s']", DirectionEnum.UP.getDirection()));
        do {
            pageUtils.waitForElementAndClick(byArrow);
        } while (!driver.findElement(byArrow).isEnabled());
        return this;
    }

    public ConfigurePage moveToBottom(String columnName) {
        By byColumn = By.xpath(String.format("//div[@class='checkbox-icon']/following-sibling::div[.='%s']", columnName));
        pageUtils.waitForElementAndClick(byColumn);

        By byArrow = By.cssSelector(String.format("[data-icon='angle-%s']", DirectionEnum.DOWN.getDirection()));
        do {
            pageUtils.waitForElementAndClick(byArrow);
        } while (!driver.findElement(byArrow).isEnabled());
        return this;
    }

    public ConfigurePage sortColumn() {
        By byColumn = driver.findElement(By.xpath("//div[.='Process Group']")).findElement(By.cssSelector("svg[data-icon='sort-up']"));
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
