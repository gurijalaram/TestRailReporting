package pageobjects.navtoolbars;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.pages.explore.FileUploadPage;

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
     * Collective method to upload a file then select OK
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @param className    - the class name
     * @return new page object
     */
    public <T> T uploadComponentAndOk(String scenarioName, File filePath, Class<T> className) {
        return uploadComponent(scenarioName, filePath).selectUploadButton(className);
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
        return uploadComponent(scenarioName, filePath).selectCancelButton(className);
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
}
