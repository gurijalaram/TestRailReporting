package com.apriori.cia.ui.pageobjects.manage;

import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class NewScenarioExportPopup extends LoadableComponent<NewScenarioExportPopup> {


    @FindBy(xpath = "//div[@class='DTE_Header_Content']")
    private WebElement titleText;

    @FindBy(xpath = "//input[@id='DTE_Field_name']")
    private WebElement setNameInput;

    @FindBy(xpath = "//input[@id='DTE_Field_description']")
    private WebElement descriptionInput;

    @FindBy(xpath = "//input[@id='DTE_Field_scenarioKey-masterName']")
    private WebElement namePartInput;

    @FindBy(xpath = "//input[@id='DTE_Field_scenarioKey-stateName']")
    private WebElement scenarioNameInput;

    @FindBy(xpath = "//div[@id='DTE_Field_schedule.onceDateTime']//span[@class='input-group-addon']")
    private WebElement dataTimeCalendarButton;

    @FindBy(xpath = "//button[@class='btn btn btn-primary']")
    private WebElement createdButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public NewScenarioExportPopup(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() {
        pageUtils.waitForElementToAppear(titleText);
        pageUtils.waitForElementAppear(createdButton);
    }

    /**
     * Double click on calendar button to insert current time
     *
     * @return NewScenarioExportPopup
     */
    public NewScenarioExportPopup doubleClickCalendarButton() {
        pageUtils.waitForElementAndClick(dataTimeCalendarButton);
        pageUtils.waitForElementAndClick(dataTimeCalendarButton);
        return this;
    }

    /**
     * Insert value for Set Name field
     *
     * @return NewScenarioExportPopup
     */
    public NewScenarioExportPopup insertSetNameValue(final String setNameValue) {
        return setInputField(setNameInput, setNameValue);
    }

    /**
     * Insert value for Description field
     *
     * @return NewScenarioExportPopup
     */
    public NewScenarioExportPopup insertDescriptionValue(final String descriptionValue) {
        return setInputField(descriptionInput, descriptionValue);
    }

    /**
     * Insert value for Name/Part # field
     *
     * @return NewScenarioExportPopup
     */
    public NewScenarioExportPopup insertNamePartValue(final String namePartValue) {
        return setInputField(namePartInput, namePartValue);
    }

    /**
     * Insert value for Name/Part # field
     *
     * @return NewScenarioExportPopup
     */
    public NewScenarioExportPopup insertScenarioName(final String scenarioNameValue) {
        return setInputField(scenarioNameInput, scenarioNameValue);
    }



    /**
     * Click create button
     *
     * @return ScenarioExport popup
     */
    public ScenarioExport clickCreate() {
        pageUtils.waitForElementAndClick(createdButton);
        return PageFactory.initElements(driver, ScenarioExport.class);
    }


    /**
     * Wait input is available and insert value
     *
     * @param webElementToInsert
     * @param valueToInsert
     * @return
     */
    private NewScenarioExportPopup setInputField(final WebElement webElementToInsert, final String valueToInsert) {
        pageUtils.waitForElementToBeClickable(webElementToInsert);
        webElementToInsert.sendKeys(valueToInsert);
        return this;
    }
}
