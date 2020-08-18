package newcustomer.users;

import com.apriori.utils.PageUtils;

import customeradmin.NavToolbar;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ImportPage extends LoadableComponent<ImportPage> {

    private final Logger logger = LoggerFactory.getLogger(ImportPage.class);

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    @FindBy(xpath = "//button[.='Load']")
    private WebElement loadButton;

    @FindBy(xpath = "//button[.='Refresh']")
    private WebElement refreshButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;

    public ImportPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.navToolbar = new NavToolbar(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(loadButton);
    }

    /**
     * Import File
     *
     * @param filePath - the file path
     * @return current page object
     */
    public ImportPage importFile(File filePath) {
        fileInput.sendKeys(filePath.getAbsolutePath());
        return this;
    }
}
