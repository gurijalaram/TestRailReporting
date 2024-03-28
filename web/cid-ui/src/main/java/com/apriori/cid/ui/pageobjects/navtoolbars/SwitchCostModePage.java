package com.apriori.cid.ui.pageobjects.navtoolbars;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class SwitchCostModePage extends LoadableComponent<SwitchCostModePage> {

    @FindBy(xpath = "//h2[.='Are you sure?']")
    private WebElement switchCostModeHeader;

    @FindBy(css = "button[data-testid='primary-button']")
    private WebElement continueButton;

    @FindBy(css = "button[data-testid='secondary-button']")
    private WebElement cancelButton;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public SwitchCostModePage(WebDriver driver) {
        this.driver = driver;
        this.modalDialogController = new ModalDialogController(driver);
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(pageUtils.waitForElementToAppear(switchCostModeHeader).getAttribute("textContent").equalsIgnoreCase("Delete"), "Delete modal is not displayed");
    }

    /**
     * Selects the Continue button
     *
     * @return Evaluate Page PO
     */
    public EvaluatePage clickContinue() {
        pageUtils.waitForElementAndClick(continueButton);
        return new EvaluatePage(driver);
    }

    /**
     * Get the text of the Continue button
     *
     * @return - String of the text used in the Continue button
     */
    public String continueButtonText() {
        pageUtils.waitForElementToAppear(continueButton);
        return continueButton.getText();
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public EvaluatePage clickCancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new EvaluatePage(driver);
    }

    /**
     * Clicks the x button to close the modal
     *
     * @return generic page object
     */
    public EvaluatePage closeDialog() {
        return modalDialogController.closeDialog(EvaluatePage.class);
    }
}
