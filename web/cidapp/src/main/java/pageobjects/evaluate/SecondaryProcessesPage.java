package pageobjects.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.common.ModalDialogController;

public class SecondaryProcessesPage extends LoadableComponent<SecondaryProcessesPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(SecondaryProcessesPage.class);

    @FindBy(xpath = "//div[normalize-space(@class)='tree selectable']")
    private WebElement processTree;

    @FindBy(xpath = "//input[@placeholder='Search...']")
    private WebElement searchInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public SecondaryProcessesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(processTree);
    }

    /**
     * Selects both the process type and process name of the secondary process
     *
     * @param processType - accepts a comma separated list of type string
     * @param processName - the process name
     * @return current page object
     */
    public SecondaryProcessesPage selectSecondaryProcess(String processType, String processName) {
        findProcessTypeAndName(processType, processName).click();
        return this;
    }

    /**
     * Finds the process type and name
     *
     * @param processType - the process type
     * @param processName - the process name
     * @return webelement
     */
    private WebElement findProcessTypeAndName(String processType, String processName) {
        return selectProcessType(processType).findProcessName(processName);
    }

    /**
     * Selects the secondary types dropdowns in the process tree
     *
     * @param processType - the secondary process type
     * @return current page object
     */
    private SecondaryProcessesPage selectProcessType(String processType) {
        String[] processTypes = processType.split(",");

        for (String process : processTypes) {
            By secondaryProcess = By.xpath(String.format("//span[.='%s']/ancestor::span//button", process.trim()));
            pageUtils.scrollWithJavaScript(driver.findElement(secondaryProcess), true).click();
        }
        return this;
    }

    /**
     * Select the secondary process checkbox
     *
     * @param processName - the secondary process
     * @return current page object
     */
    private WebElement findProcessName(String processName) {
        By processBox = By.xpath(String.format("//span[.='%s']/ancestor::span//label", processName.trim()));
        return pageUtils.scrollWithJavaScript(driver.findElement(processBox), true);
    }

    /**
     * Enter search input
     * @param searchTerm - search term
     * @return current page object
     */
    public SecondaryProcessesPage search(String searchTerm) {
        pageUtils.waitForElementAppear(searchInput).clear();
        searchInput.sendKeys(searchTerm);
        return this;
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public EvaluatePage submit() {
        return modalDialogController.submit(EvaluatePage.class);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public EvaluatePage cancel() {
        return modalDialogController.cancel(EvaluatePage.class);
    }
}
