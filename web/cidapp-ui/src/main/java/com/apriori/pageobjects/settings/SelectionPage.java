package com.apriori.pageobjects.settings;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ModalDialogController;

import com.utils.ColourEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class SelectionPage extends LoadableComponent<SelectionPage> {

    @FindBy(css = ".user-preferences [type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//button[.='Selection']")
    private WebElement selectionTab;

    @FindBy(css = ".flexbox-fix input")
    private WebElement colourInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;
    private SettingsNavigation settingsNavigation;

    public SelectionPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.settingsNavigation = new SettingsNavigation(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(selectionTab.getAttribute("class").contains("active"), "Selection tab is not active");
    }

    /**
     * Go to production default tab
     *
     * @return new page object
     */
    public ProductionDefaultsPage goToProductionTab() {
        return settingsNavigation.goToProductionTab();
    }

    /**
     * Go to tolerances default tab
     *
     * @return new page object
     */
    public ToleranceDefaultsPage goToToleranceTab() {
        return settingsNavigation.goToToleranceTab();
    }

    /**
     * Go to display default tab
     *
     * @return new page object
     */
    public DisplayPreferencesPage goToDisplayTab() {
        return settingsNavigation.goToDisplayTab();
    }

    /**
     * Selects the colour
     *
     * @param colour - the colour
     * @return current page object
     */
    public SelectionPage selectColour(ColourEnum colour) {
        By byColour = By.cssSelector(String.format("[title='%s']", colour.getColour()));
        pageUtils.waitForElementAndClick(byColour);
        return this;
    }

    /**
     * Checks the colour
     * eg. assertThat(selectionPage.isColour(ColourEnum.AMBER), is(true));
     *
     * @return boolean
     */
    public boolean isColour(ColourEnum colour) {
        return pageUtils.waitForElementToAppear(colourInput).getAttribute("value").equals(colour.getColour().toUpperCase());
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(submitButton, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}
