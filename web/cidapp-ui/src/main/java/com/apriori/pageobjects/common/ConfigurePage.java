package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import com.utils.ColumnsEnum;
import com.utils.DirectionEnum;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurePage extends LoadableComponent<ConfigurePage> {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurePage.class);

    @FindBy(xpath = "//label[.='Number of sticky columns']/ancestor::div//div[contains(@class,'apriori-select')]")
    private WebElement stickyDropdown;

    @FindBy(css = ".apriori-card.medium-card.shuttle-box-list.card")
    private List<WebElement> columnList;

    @FindBy(xpath = "//div[@class='modal-content']//button[@class='btn btn-primary'][.='Submit']")
    private WebElement submitButton;

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
        pageUtils.waitForElementToAppear(stickyDropdown);
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
    public ConfigurePage selectColumn(ColumnsEnum columnName) {
        By byColumn = By.xpath(String.format("//div[@class='checkbox-icon']/following-sibling::div[.='%s']", columnName.getColumns()));
        pageUtils.waitForElementAndClick(byColumn);
        return this;
    }

    /**
     * Moves column to top
     *
     * @param columnName - the column name
     * @return current page object
     */
    public ConfigurePage moveToTop(ColumnsEnum columnName) {
        moveColumn(columnName, DirectionEnum.UP);
        return this;
    }

    /**
     * Moves column to bottom
     *
     * @param columnName - the column name
     * @return current page object
     */
    public ConfigurePage moveToBottom(ColumnsEnum columnName) {
        moveColumn(columnName, DirectionEnum.DOWN);
        return this;
    }

    /**
     * Moves the column
     *
     * @param columnName - the column name
     * @param direction  - the direction
     */
    private void moveColumn(ColumnsEnum columnName, DirectionEnum direction) {
        selectColumn(columnName);
        By byArrow = By.cssSelector(String.format("[data-icon='angle-%s']", direction.getDirection()));

        while (!driver.findElement(By.xpath(String.format("//*[name()='svg' and @data-icon='angle-%s']/..", direction.getDirection()))).getAttribute("class").contains("disabled")) {
            pageUtils.waitForElementAndClick(byArrow);
        }
    }

    /**
     * Gets choices list
     *
     * @return list string
     */
    public List<String> getChoicesList() {
        return Stream.of(columnList.get(0).getAttribute("innerText").split("\n")).filter(x -> !x.contains("Choices".toUpperCase())).collect(Collectors.toList());
    }

    /**
     * Gets chosen list
     *
     * @return list string
     */
    public List<String> getChosenList() {
        return Stream.of(columnList.get(1).getAttribute("innerText").split("\n")).filter(x -> !x.contains("Chosen".toUpperCase())).collect(Collectors.toList());
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        pageUtils.waitForElementAndClick(submitButton);
        return PageFactory.initElements(driver, klass);
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
