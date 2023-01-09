package com.apriori.pageobjects.navtoolbars;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DeletePage extends LoadableComponent<DeletePage> {
    private static final Logger logger = LoggerFactory.getLogger(DeletePage.class);

    @FindBy(xpath = "//h1[.='Delete']")
    private WebElement deleteHeader;

    @FindBy(css = "[role='dialog'] li")
    private List<WebElement> componentScenarioNames;

    @FindBy(css = "[data-testid = 'primary-button']")
    private WebElement deleteButton;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public DeletePage(WebDriver driver) {
        this.driver = driver;
        this.modalDialogController = new ModalDialogController(driver);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Delete modal is not displayed", pageUtils.waitForElementToAppear(deleteHeader).getAttribute("textContent").equalsIgnoreCase("Delete"));
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
