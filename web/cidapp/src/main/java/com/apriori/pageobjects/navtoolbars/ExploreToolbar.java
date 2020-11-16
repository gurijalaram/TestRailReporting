package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.pages.explore.FileUploadPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author cfrith
 */

public class ExploreToolbar extends MainNavBar {

    private final Logger LOGGER = LoggerFactory.getLogger(ExploreToolbar.class);

    @FindBy(xpath = "//button[.='New']")
    private WebElement newButton;

    @FindBy(xpath = "//button[.='Component']")
    private WebElement componentButton;

    @FindBy(xpath = "//button[.='Publish']")
    private WebElement publishButton;

    @FindBy(xpath = "//button[.='Revert']")
    private WebElement revertButton;

    @FindBy(xpath = "//button[.='Delete']")
    private WebElement deleteButton;

    @FindBy(xpath = "//button[.='Actions']")
    private WebElement actionsButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ExploreToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementAppear(newButton);
        pageUtils.waitForElementAppear(publishButton);
        pageUtils.waitForElementAppear(actionsButton);
    }

    /**
     * Checks delete button is displayed
     *
     * @return visibility of button
     */
    public boolean isDeleteButtonPresent() {
        return deleteButton.isDisplayed();
    }

    /**
     * Collective method to upload a file then select Submit
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @param klass    - the class name
     * @return new page object
     */
    public <T> T uploadComponentAndSubmit(String scenarioName, File filePath, Class<T> klass) {
        return uploadComponent(scenarioName, filePath).submit(klass);
    }

    /**
     * Collective method to upload a file then select Cancel
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @param className    - the class name
     * @return new page object
     */
    public <T> T uploadComponentAndCancel(String scenarioName, File filePath, Class<T> className) {
        return uploadComponent(scenarioName, filePath).cancel(className);
    }

    /**
     * Selects the file dropdown and enters file details
     *
     * @param scenarioName - the name of the scenario
     * @param filePath      - location of the file
     * @return new page object
     */
    public FileUploadPage uploadComponent(String scenarioName, File filePath) {
        pageUtils.waitForElementAndClick(newButton);
        pageUtils.waitForElementAndClick(componentButton);
        return new FileUploadPage(driver).inputComponentDetails(scenarioName, filePath);
    }

    /**
     * Publish the scenario
     *
     * @return new page object
     */
    public <T> T publishScenario(Class<T> className) {
        pageUtils.waitForElementAndClick(publishButton);
        return PageFactory.initElements(driver, className);
    }
}
