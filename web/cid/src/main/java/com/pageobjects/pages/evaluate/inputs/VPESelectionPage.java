package com.pageobjects.pages.evaluate.inputs;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class VPESelectionPage extends LoadableComponent<VPESelectionPage> {

    private static final Logger logger = LoggerFactory.getLogger(VPESelectionPage.class);

    @FindBy(css = "div[data-ap-comp='vpeSelection'] .modal-content")
    private WebElement modalTitle;

    @FindBy(css = "input[data-ap-field='usePrimaryAsDefault']")
    private WebElement vpeTickBox;

    @FindBy(css = "div[data-ap-comp='vpeSelection'] select[data-ap-field='primaryVpeName']")
    private WebElement forgingDropdown;

    @FindBy(css = "div[data-ap-comp='vpeSelection'] select[data-ap-field='Machining']")
    private WebElement machiningDropdown;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement saveButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement closeButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public VPESelectionPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(modalTitle);
    }

    /**
     * Select the vpe box
     *
     * @return current page object
     */
    public VPESelectionPage selectVPEBox() {
        vpeTickBox.click();
        return this;
    }

    /**
     * Selects forging
     *
     * @param vpe - the vpe
     * @return current page object
     */
    public VPESelectionPage selectForging(String vpe) {
        new Select(forgingDropdown).selectByVisibleText(vpe);
        return this;
    }

    /**
     * Selects machining
     *
     * @param vpe - the vpe
     * @return current page object
     */
    public VPESelectionPage selectMachining(String vpe) {
        new Select(machiningDropdown).selectByVisibleText(vpe);
        return this;
    }

    /**
     * Selects the save changes button
     *
     * @return new page object
     */
    public MoreInputsPage saveChanges() {
        pageUtils.waitForElementAndClick(saveButton);
        return new MoreInputsPage(driver);
    }

    /**
     * Selects the close button
     *
     * @return new page object
     */
    public MoreInputsPage close() {
        pageUtils.waitForElementAndClick(closeButton);
        return new MoreInputsPage(driver);
    }

    /**
     * Check use primary VPE as default is selected
     *
     * @return current page object
     */
    public String isUsePrimaryVPESelected(String attribute) {
        return pageUtils.waitForElementToAppear(vpeTickBox).getAttribute(attribute);
    }
}