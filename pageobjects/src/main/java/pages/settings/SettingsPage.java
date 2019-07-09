package main.java.pages.settings;

import main.java.pages.explore.ExplorePage;
import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsPage extends LoadableComponent<SettingsPage> {

    private Logger logger = LoggerFactory.getLogger(SettingsPage.class);

    @FindBy(css = ".modal-title")
    private WebElement dialogTitle;

    @FindBy(css = "a[href='#tolerancePolicyTab']")
    private WebElement tolerancesButton;

    @FindBy(css = "select[data-ap-field='unitSystem']")
    private WebElement unitsDropdown;

    @FindBy(css = ".btn.btn-primary")
    private WebElement saveButton;

    @FindBy(css = ".btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public SettingsPage(WebDriver driver) {
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
     * Opens tolerances tab
     * @return new page object
     */
    public ToleranceSettingsPage openTolerancesTab() {
        tolerancesButton.click();
        return new ToleranceSettingsPage(driver);
    }

    public SettingsPage changeDisplayUnits(String units) {
        pageUtils.selectDropdownOption(unitsDropdown, units);
        return this;
    }

    /**
     * Selects the save button
     * @return new page object
     */
    public ExplorePage save() {
        saveButton.click();
        return new ExplorePage(driver);
    }

    /**
     * Selects the cancel button
     * @return new page object
     */
    public ExplorePage cancel() {
        cancelButton.click();
        return new ExplorePage(driver);
    }
}
