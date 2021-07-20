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
public class SurfaceTreatmentProcessesPage extends LoadableComponent<SurfaceTreatmentProcessesPage> {

    @FindBy(xpath = "//button[contains(text(),'Surface Treatment')]")
    private WebElement surfaceTreatmentTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;
    private SecondaryProcessController secondaryProcessController;

    public SurfaceTreatmentProcessesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.secondaryProcessController = new SecondaryProcessController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Heat Treatment tab was not selected", surfaceTreatmentTab.getAttribute("class").contains("active"));
    }

    /**
     * Selects both the process type and process name of the secondary process
     *
     * @param processType - accepts a comma separated list of type string
     * @param processName - the process name
     * @return current page object
     */
    public SurfaceTreatmentProcessesPage selectSecondaryProcess(String processType, String processName) {
        secondaryProcessController.selectSecondaryProcess(processType, processName);
        return this;
    }


    /**
     * Enter search input
     *
     * @param searchTerm - search term
     * @return current page object
     */
    public SurfaceTreatmentProcessesPage search(String searchTerm) {
        secondaryProcessController.search(searchTerm);
        return this;
    }

    /**
     * Get list of selected items
     * @return list of string
     */
    public List<String> getSelectedPreviewList() {
        return secondaryProcessController.getSelectedPreviewList();
    }

    /**
     * Expand all
     *
     * @return current page object
     */
    public SurfaceTreatmentProcessesPage expandAll() {
        modalDialogController.expandAll();
        return this;
    }

    /**
     * Collapse all
     *
     * @return current page object
     */
    public SurfaceTreatmentProcessesPage collapseAll() {
        modalDialogController.collapseAll();
        return this;
    }

    /**
     * Deselect all
     *
     * @return current page object
     */
    public SurfaceTreatmentProcessesPage deselectAll() {
        modalDialogController.deselectAll();
        return this;
    }

    /**
     * Reset
     *
     * @return current page object
     */
    public SurfaceTreatmentProcessesPage reset() {
        modalDialogController.reset();
        return this;
    }

    /**
     * Get number of selected processes
     *
     * @return
     */
    public String getNoOfSelected() {
        return secondaryProcessController.getNoOfSelected();
    }

    /**
     * Switches to new tab
     *
     * @param tab   - the tab name
     * @param klass - the class
     * @param <T>   - the object
     * @return - generic page object
     */
    public <T> T switchTab(String tab, Class<T> klass) {
        return modalDialogController.switchTab(tab, klass);
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
