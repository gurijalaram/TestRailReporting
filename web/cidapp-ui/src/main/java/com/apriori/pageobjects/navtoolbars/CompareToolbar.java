package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.pages.compare.CompareExplorePage;
import com.apriori.pageobjects.pages.explore.FileUploadPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CompareToolbar extends MainNavBar {

    private static final Logger logger = LoggerFactory.getLogger(CompareToolbar.class);

    @FindBy(id = "qa-sub-header-modify-button")
    private WebElement modifyButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CompareToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementAppear(modifyButton);
    }

    /**
     * Selects the file dropdown and enters file details
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @return new page object
     */
    public FileUploadPage uploadComponent(String scenarioName, File filePath) {
        return new ExploreToolbar(driver).uploadComponent(scenarioName, filePath);
    }

    /**
     * Create new scenario
     *
     * @return new page object
     */
    public ScenarioPage createScenario() {
        return new ExploreToolbar(driver).createScenario();
    }

    /**
     * Modify a scenario
     *
     * @return new page object
     */
    public CompareExplorePage modify() {
        pageUtils.waitForElementAndClick(modifyButton);
        return new CompareExplorePage(driver);
    }
}