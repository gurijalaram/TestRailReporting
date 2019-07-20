package main.java.pages.explore;

import main.java.header.CompareHeader;
import main.java.pages.compare.ComparePage;
import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComparisonPage extends CompareHeader {

    private final Logger logger = LoggerFactory.getLogger(ComparisonPage.class);

    @FindBy(css = "h3.modal-title")
    private WebElement modalDialog;

    @FindBy(css = "input[data-ap-field='name']")
    private WebElement nameInput;

    @FindBy(css = "textarea[data-ap-field='description']")
    private WebElement descriptionInput;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement saveButton;

    @FindBy(css = "button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ComparisonPage(WebDriver driver) {
        super(driver);
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
     * Enters the comparison name
     * @param comparisonName - the comparison name
     * @return current page object
     */
    public ComparisonPage enterComparisonName(String comparisonName) {
        nameInput.sendKeys(comparisonName);
        return this;
    }

    /**
     * Enters the comparison description
     * @param comparisonDescription - the comparison description
     * @return current page object
     */
    public ComparisonPage enterComparisonDescription(String comparisonDescription) {
        descriptionInput.sendKeys(comparisonDescription);
        return this;
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public ComparePage save() {
        saveButton.click();
        return new ComparePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ExplorePage cancel() {
        cancelButton.click();
        return new ExplorePage(driver);
    }
}
