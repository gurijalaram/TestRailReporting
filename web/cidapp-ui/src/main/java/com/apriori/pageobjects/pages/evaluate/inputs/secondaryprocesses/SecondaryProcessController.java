package com.apriori.pageobjects.pages.evaluate.inputs.secondaryprocesses;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SecondaryProcessController {

    @FindBy(xpath = "//input[@placeholder='Search...']")
    private WebElement searchInput;

    @FindBy(css = ".selected-preview .pill-box")
    private WebElement selectedPreviewItems;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public SecondaryProcessController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Selects both the process type and process name of the secondary process
     *
     * @param processType - accepts a comma separated list of type string
     * @param processName - the process name
     * @return current page object
     */
    public SecondaryProcessController selectSecondaryProcess(String processType, String processName) {
        findProcessTypeAndName(processType, processName).click();
        return this;
    }

    /**
     * Finds the process type and name
     *
     * @param processType - the process type
     * @param processName - the process name
     * @return webelement
     */
    private WebElement findProcessTypeAndName(String processType, String processName) {
        return selectProcessType(processType).findProcessName(processName);
    }

    /**
     * Selects the secondary types dropdowns in the process tree
     *
     * @param processTypes - the secondary process type
     * @return current page object
     */
    private SecondaryProcessController selectProcessType(String processTypes) {
        Arrays.stream(Stream.of(processTypes)
            .map(processType -> processType.split(","))
            .collect(Collectors.toList())
            .get(0))
            .forEach(process -> {
                By secondaryProcess = By.xpath(String.format("//span[.='%s']/ancestor::span//button", process.trim()));
                pageUtils.scrollWithJavaScript(pageUtils.waitForElementToAppear(secondaryProcess), true).click();
            });
        return this;
    }

    /**
     * Select the secondary process checkbox
     *
     * @param processName - the secondary process
     * @return current page object
     */
    private WebElement findProcessName(String processName) {
        By processBox = By.xpath(String.format("//span[.='%s']/ancestor::span//label", processName.trim()));
        return pageUtils.scrollWithJavaScript(driver.findElement(processBox), true);
    }

    /**
     * Enter search input
     *
     * @param searchTerm - search term
     * @return current page object
     */
    public SecondaryProcessController search(String searchTerm) {
        pageUtils.waitForElementAppear(searchInput).clear();
        searchInput.sendKeys(searchTerm);
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
}
