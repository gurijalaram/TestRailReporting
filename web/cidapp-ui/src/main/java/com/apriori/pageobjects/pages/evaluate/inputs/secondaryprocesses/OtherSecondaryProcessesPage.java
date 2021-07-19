package com.apriori.pageobjects.pages.evaluate.inputs.secondaryprocesses;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;

@Slf4j
public class OtherSecondaryProcessesPage extends LoadableComponent<OtherSecondaryProcessesPage> {

    @FindBy(xpath = "//button[contains(text(),'Other Secondary Processes')]")
    private WebElement otherSecProcessTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;
    private SecondaryProcessesController secondaryProcessesController;

    public OtherSecondaryProcessesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.secondaryProcessesController = new SecondaryProcessesController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Machining tab was not selected", otherSecProcessTab.getAttribute("class").contains("active"));
    }

    /**
     * Selects both the process type and process name of the secondary process
     *
     * @param processType - accepts a comma separated list of type string
     * @param processName - the process name
     * @return current page object
     */
    public OtherSecondaryProcessesPage selectSecondaryProcess(String processType, String processName) {
        secondaryProcessesController.selectSecondaryProcess(processType, processName);
        return this;
    }

    /**
     * Enter search input
     *
     * @param searchTerm - search term
     * @return current page object
     */
    public OtherSecondaryProcessesPage search(String searchTerm) {
        secondaryProcessesController.search(searchTerm);
        return this;
    }

    /**
     * Get list of selected items
     * @return list of string
     */
    public List<String> getSelectedPreviewList() {
        return secondaryProcessesController.getSelectedPreviewList();
    }

    /**
     * Expand all
     *
     * @return current page object
     */
    public OtherSecondaryProcessesPage expandAll() {
        secondaryProcessesController.expandAll();
        return this;
    }

    /**
     * Collapse all
     *
     * @return current page object
     */
    public OtherSecondaryProcessesPage collapseAll() {
        secondaryProcessesController.collapseAll();
        return this;
    }

    /**
     * Get number of selected processes
     *
     * @return
     */
    public String getNoOfSelected() {
        return secondaryProcessesController.getNoOfSelected();
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(klass);
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
