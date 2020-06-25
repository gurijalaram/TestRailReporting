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

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_button-92'] > button > span:nth-of-type(3)")
    private WebElement nextBtn;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_textbox-148'] > table > tbody > tr > td > input")
    private WebElement inputName;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_textarea-152'] > div > textarea")
    private WebElement inputDescription;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-154'] > div > div")
    private WebElement connectorDropdown;

    @FindBy(css = "div[class^='ss-content ss-'][class$=' ss-open'] > div > input[type='search']")
    private WebElement connectorDropdownSearch;


    private WebDriver driver;
    private PageUtils pageUtils;

    public NewEditWorkflow (WebDriver driver){
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load(){
    }

    @Override
    protected void isLoaded(){
        pageUtils.waitForElementToAppear(nextBtn);
    }

    /**
     * Click Next btn
     *
     */
    public NewEditWorkflow clickNextBtn(){
        nextBtn.click();
        return this;
    }

    public NewEditWorkflow inputWorkflowName(String workflowName){
        inputName.sendKeys(workflowName);
        return new NewEditWorkflow(driver);
    }

    public NewEditWorkflow selectConnector(String connectorName) {
        connectorDropdown.click();
        pageUtils.waitForElementAndClick(connectorDropdownSearch);
        connectorDropdownSearch.sendKeys(connectorName);
        By connectorToClick = By.xpath(String.format("//div[contains(text(), '%s')]", connectorName));
        driver.findElement(connectorToClick).click();
        return this;
    }
}
