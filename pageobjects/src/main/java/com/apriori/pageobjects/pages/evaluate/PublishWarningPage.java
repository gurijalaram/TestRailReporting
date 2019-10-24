package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.utils.PageUtils;
import com.apriori.utils.enums.WorkspaceEnum;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishWarningPage extends LoadableComponent<PublishWarningPage> {

    private final Logger logger = LoggerFactory.getLogger(PublishWarningPage.class);

    @FindBy(css = ".modal-title")
    private WebElement dialogTitle;

    @FindBy(css = "input[data-ap-field='scenarioName']")
    private WebElement inputField;

    @FindBy(css = "button.btn-primary")
    private WebElement continueButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PublishWarningPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(dialogTitle);
    }

    /**
     * Enter scenario name
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public PublishWarningPage enterNewScenarioName(String scenarioName) {
        inputField.clear();
        inputField.sendKeys(scenarioName);
        return this;
    }

    /**
     * Selects the publish button
     *
     * @return new page object
     */
    public ExplorePage selectContinueButton() {
        continueButton.click();
        return new ExplorePage(driver).selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace());
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public EvaluatePage selectCancelButton() {
        cancelButton.click();
        return new EvaluatePage(driver);
    }
}
