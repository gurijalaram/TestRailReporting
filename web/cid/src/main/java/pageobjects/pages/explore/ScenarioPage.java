package pageobjects.pages.explore;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.pages.evaluate.EvaluatePage;

/**
 * @author cfrith
 */

public class ScenarioPage extends LoadableComponent<ScenarioPage> {

    private final Logger logger = LoggerFactory.getLogger(ScenarioPage.class);

    @FindBy(css = "h3.modal-title")
    private WebElement modalDialog;

    @FindBy(css = "input[data-ap-field='name']")
    private WebElement nameInput;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement saveButton;

    @FindBy(css = "button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ScenarioPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(modalDialog);
    }

    /**
     * Enter scenario name
     *
     * @param scenarioName - the scenario name
     * @return curent page object
     */
    public ScenarioPage enterScenarioName(String scenarioName) {
        pageUtils.clearInput(nameInput);
        nameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public <T> T save(Class <T> className) {
        pageUtils.waitForElementAndClick(saveButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ExplorePage cancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new ExplorePage(driver);
    }
}
