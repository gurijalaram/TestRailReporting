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

import java.util.List;

public class NewEditWorkflow extends LoadableComponent<NewEditWorkflow> {

    private final Logger logger = LoggerFactory.getLogger(Schedule.class);

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_button-92'] > button") //> span:nth-of-type(3)
    private WebElement nextBtn;

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

    @FindBy(css = "div[id=''")


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
        pageUtils.waitForElementToAppear(nextBtn);
    }

    /**
     * Click Next btn
     */
    public NewEditWorkflow clickNextBtn() {
        pageUtils.waitForElementNotDisplayed(disabledNextBtn, 1);
        nextBtn.click();
        return this;
    }

    /**
     * Input Workflow name
     *
     * @param workflowName - Workflow name to be entered
     * @return NewEditWorkflow page object
     */
    public NewEditWorkflow inputWorkflowName(String workflowName) {
        inputName.sendKeys(workflowName);
        return new NewEditWorkflow(driver);
    }

    /**
     * Select connector
     *
     * @param connectorName - Name of connector to be selected
     * @return NewEditWorkflow page object
     */
    public NewEditWorkflow selectConnector(String connectorName) {
        connectorDropdown.click();
        pageUtils.waitForElementAndClick(connectorDropdownSearch);
        connectorDropdownSearch.sendKeys(connectorName);
        By connectorToClick = By.xpath(String.format("//div[contains(text(), '%s')]", connectorName));
        driver.findElement(connectorToClick).click();
        return this;
    }

    /**
     * Select query CI Connect field
     *
     * @param ruleNumber - Rule number, posistion of rule in list
     * @param fieldName - Field name, CI Connect field to be used in query
     * @return NewEditWorkflow page object
     */
    public NewEditWorkflow selectQueryCIConnectField(Integer ruleNumber, String fieldName) {
        pageUtils.waitForElementToAppear(By.xpath(String.format("//select[@name='root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_%s_filter']", ruleNumber.toString())));
        Select queryField = new Select(driver.findElement(By.xpath(String.format("//select[@name='root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_%s_filter']", ruleNumber.toString()))));
        queryField.selectByVisibleText(fieldName);
        return new NewEditWorkflow(driver);
    }
}
