package com.apriori.pageobjects.navtoolbars;

import com.apriori.PageUtils;
import com.apriori.pageobjects.compare.ModifyComparisonPage;
import com.apriori.pageobjects.compare.SaveComparisonPage;
import com.apriori.pageobjects.explore.ImportCadFilePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;

@Slf4j
public class CompareToolbar extends MainNavBar {

    @FindBy(id = "qa-sub-header-new-comparison")
    private WebElement newButton;

    @FindBy(id = "qa-sub-header-modify-button")
    private WebElement modifyButton;

    @FindBy(css = "div[id='qa-sub-header-save-as-button'] button")
    private WebElement saveButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CompareToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(newButton);
    }

    /**
     * Selects the file dropdown and enters file details
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @return new page object
     */
    public ImportCadFilePage uploadComponent(String scenarioName, File filePath) {
        return new ExploreToolbar(driver).importCadFile()
            .inputComponentDetails(scenarioName, filePath);
    }

    /**
     * Copies a scenario
     *
     * @return new page object
     */
    public ScenarioPage copyScenario() {
        return new ExploreToolbar(driver).copyScenario();
    }

    /**
     * Modify a scenario
     *
     * @return new page object
     */
    public ModifyComparisonPage modify() {
        pageUtils.waitForElementAndClick(modifyButton);
        return new ModifyComparisonPage(driver);
    }

    /**
     * Save new Comparison
     * To be used when the comparison is new and will require input of a unique name
     *
     * @return Save Comparison Page Object
     */
    public SaveComparisonPage saveNew() {
        pageUtils.waitForElementAndClick(saveButton);
        return new SaveComparisonPage(driver);
    }

    /**
     * Save changes to Comparison
     * To be used when the comparison already exists
     *
     * @return Compare Page Object
     */
    public SaveComparisonPage saveChanges() {
        pageUtils.waitForElementAndClick(saveButton);
        return new SaveComparisonPage(driver);
    }

    /**
     * Check enabled state of Save button
     *
     * @return Boolean
     */
    public Boolean saveButtonEnabled() {
        return pageUtils.isElementEnabled(saveButton);
    }
}
