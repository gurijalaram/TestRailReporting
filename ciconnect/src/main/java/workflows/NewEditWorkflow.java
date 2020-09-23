package workflows;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewEditWorkflow extends LoadableComponent<NewEditWorkflow> {

    private final Logger logger = LoggerFactory.getLogger(Schedule.class);

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_button-92'] > button") //> span:nth-of-type(3)
    private WebElement detailsNextBtn;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_button-104'] > button > span:nth-of-type(3)")
    private WebElement queryNextBtn;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_button-108'] > button > span:nth-of-type(3)")
    private WebElement costingInputsNextBtn;

    @FindBy(xpath = "//div[@id='root_pagemashupcontainer-1_navigation-83-popup_button-92']/button[@disabled='']")
    private WebElement disabledNextBtn;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_textbox-148'] > table > tbody > tr > td > input")
    private WebElement inputName;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_textarea-152'] > div > textarea")
    private WebElement inputDescription;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-154'] > div > div")
    private WebElement connectorDropdown;

    @FindBy(css = "div[class^='ss-content ss-'][class$=' ss-open'] > div > input[type='search']")
    private WebElement connectorDropdownSearch;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_button-43'] > button")
    private WebElement saveButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public NewEditWorkflow(WebDriver driver) {
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
    protected void isLoaded() {
        //pageUtils.waitForElementToAppear(nextBtn);
    }

    /**
     * Click Next btn on Details tab of New Edit Workflow Modal
     */
    public NewEditWorkflow clickDetailsNextBtn() {
        pageUtils.waitForElementAndClick(detailsNextBtn);
        return this;
    }

    /**
     * Click Next btn on Query tab of New Edit Workflow Modal
     */
    public NewEditWorkflow clickQueryNextBtn() {
        pageUtils.waitForElementAndClick(queryNextBtn);
        return this;
    }

    /**
     * Click Next btn on Query tab of New Edit Workflow Modal
     */
    public NewEditWorkflow clickCostingInputsNextBtn() {
        pageUtils.waitForElementAndClick(costingInputsNextBtn);
        return this;
    }

    /**
     * Input Workflow name
     *
     * @param workflowName - Workflow name to be entered
     * @return NewEditWorkflow page object
     */
    public NewEditWorkflow inputWorkflowName(String workflowName) {
        pageUtils.waitForElementToAppear(detailsNextBtn);
        inputName.sendKeys(workflowName);
        return new NewEditWorkflow(driver);
    }

    /**
     * Select connector
     *
     * @param connectorName - Name of connector to be selected
     * @return NewEditWorkflow page object
     */
    public NewEditWorkflow selectConnector(String connectorName) throws InterruptedException {
        Thread.sleep(1000);

        pageUtils.waitForElementAndClick(connectorDropdown);

        By connectorToClick = By.xpath(String.format("//div[contains(text(), '%s')]", connectorName));
        pageUtils.waitForElementAndClick(connectorToClick);
        return new NewEditWorkflow(driver);
    }

    /**
     * Select query CI Connect field
     *
     * @param ruleNumber - Rule number, posistion of rule in list
     * @param fieldName  - Field name, CI Connect field to be used in query
     * @return NewEditWorkflow page object
     */
    public NewEditWorkflow selectQueryCIConnectField(Integer ruleNumber, String fieldName) {
        pageUtils.waitForElementToAppear(By.xpath(String.format(
            "//select[@name='root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_%s_filter']", ruleNumber.toString())));
        Select queryField = new Select(driver.findElement(By.xpath(String.format(
            "//select[@name='root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_%s_filter']", ruleNumber.toString()))));
        queryField.selectByVisibleText(fieldName);
        return new NewEditWorkflow(driver);
    }

    /**
     * Enter query argument
     *
     * @param ruleNumber   - Rule number, position of rule in list of query rules
     * @param ruleArgument - argument to be entered
     * @return NewEditWorkflow page object
     */
    public NewEditWorkflow enterQueryArgument(Integer ruleNumber, String ruleArgument) {
        WebElement inputField = driver.findElement(By.xpath(String.format(
            "//input[@name='root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_%s_value_0']", ruleNumber.toString())));
        pageUtils.waitForElementAndClick(inputField);
        pageUtils.clearInput(inputField);
        inputField.sendKeys(ruleArgument);
        return new NewEditWorkflow(driver);
    }

    /**
     * Click save button on New Edit Workflow modal
     *
     * @return new GenericWorkflow page object
     */
    public Schedule clickSaveButton() {
        pageUtils.waitForElementAndClick(saveButton);
        return new Schedule(driver);
    }

}