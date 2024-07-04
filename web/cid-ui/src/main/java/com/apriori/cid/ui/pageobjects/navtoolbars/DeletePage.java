package com.apriori.cid.ui.pageobjects.navtoolbars;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DeletePage extends LoadableComponent<DeletePage> {

    @FindBy(xpath = "//h1[.='Delete']")
    private WebElement deleteHeader;

    @FindBy(css = "[role='dialog'] li")
    private List<WebElement> componentScenarioNames;

    @FindBy(css = "[data-testid = 'delete-button']")
    private WebElement deleteButton;

    @FindBy(css = "[role='dialog']")
    private  WebElement deleteText;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    // FIXME: 02/07/2024 cn - this should really be an abstract class with other delete functionality extending it eg. delete in explore page is different from bulk analysis
    public DeletePage(WebDriver driver) {
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
        assertTrue(pageUtils.waitForElementToAppear(deleteHeader).getAttribute("textContent").equalsIgnoreCase("Delete"), "Delete modal is not displayed");
    }

    /**
     * Gets scenario name in delete text
     *
     * @return string
     */
    public List<String> getScenarioNames() {
        return componentScenarioNames.stream().map(o -> o.getAttribute("textContent")).collect(Collectors.toList());
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T clickDelete(Class<T> klass) {
        return modalDialogController.delete(deleteButton, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }

    /**
     * Select the remove scenarios button
     *
     * @return generic page object
     */
    public <T> T removeScenarios(Class<T> klass) {
        return modalDialogController.removeScenarios(klass);
    }

    /**
     * Gets text from delete dialog
     * @return string
     */
    public String getDeleteText() {
        return pageUtils.waitForElementAppear(deleteText).getAttribute("textContent");
    }

    /**
     * Clicks the x button to close the modal
     *
     * @return generic page object
     */
    public <T> T closeDialog(Class<T> klass) {
        return modalDialogController.closeDialog(klass);
    }

    /**
     * Clicks the close button
     *
     * @return generic page object
     */
    public <T> T clickClose(Class<T> klass) {
        return modalDialogController.close(klass);
    }
}
