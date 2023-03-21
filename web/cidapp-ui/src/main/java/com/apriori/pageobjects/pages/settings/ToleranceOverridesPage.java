package com.apriori.pageobjects.pages.settings;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import com.utils.OverridesEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ToleranceOverridesPage extends LoadableComponent<ToleranceOverridesPage> {

    @FindBy(xpath = "//h2[.='Geometric Tolerances']")
    private WebElement sectionHeader;

    @FindBy(css = ".tolerance-overrides-form [type='Submit']")
    private WebElement submitButton;

    @FindBy(css = ".tolerance-overrides-form [type='button']")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public ToleranceOverridesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Geometric Tolerances header is not displayed", pageUtils.isElementDisplayed(sectionHeader));
    }

    /**
     * Input override value
     *
     * @param label - the label
     * @param value - the value
     * @return current page object
     */
    public ToleranceOverridesPage inputOverride(OverridesEnum label, String value) {
        WebElement override = pageUtils.waitForElementToAppear(By.cssSelector(String.format("[name='%s']", label.getOverrides())));
        pageUtils.clearValueOfElement(override);
        override.sendKeys(value);
        return this;
    }

    /**
     * Gets tolerance override value
     * eg. assertThat(toleranceOverridesPage.getToleranceOverride(OverridesEnum.TOTAL_RUNOUT), is(equalTo(3.0)));
     *
     * @param label - the label
     * @return double
     */
    public double getToleranceOverride(OverridesEnum label) {
        By byOverride = By.cssSelector(String.format("[name='%s']", label.getOverrides()));
        return Double.parseDouble(pageUtils.waitForElementToAppear(byOverride).getAttribute("value"));
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public ToleranceDefaultsPage submit() {
        pageUtils.waitForElementAndClick(submitButton);
        return new ToleranceDefaultsPage(driver);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, klass);
    }
}
