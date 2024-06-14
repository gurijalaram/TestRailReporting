package com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults;

import com.apriori.cic.ui.pageobjects.CICBasePage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.pageobjects.workflows.schedule.notifications.NotificationsPart;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Publish Results tab during workflow creation or modification
 * Navigate to Write Fields Tab or Attach Report tab
 */
@Slf4j
public class PublishResultsPart extends CICBasePage {

    protected static final String PUBLISH_RESULTS_TAB = "//div[@tab-number='5']";

    @FindBy(css = "div.tabsv2-tab.enabled.selected[tab-value=write-fields]")
    protected WebElement selectedWriteFieldsTab;

    @FindBy(xpath = PUBLISH_RESULTS_TAB + "//div[@title='Write Fields']")
    protected WebElement writeFieldsTab;

    @FindBy(xpath = PUBLISH_RESULTS_TAB + "//div[@title='Attach Report']")
    protected WebElement attachReportTab;

    @FindBy(xpath = PUBLISH_RESULTS_TAB + "//div/button/span[.='Save']")
    private WebElement saveButton;

    @FindBy(xpath = PUBLISH_RESULTS_TAB + "//div/button/span[.='Previous']")
    private WebElement previousButton;

    @FindBy(xpath = PUBLISH_RESULTS_TAB + "//div/button/span[.='Cancel']")
    private WebElement cancelButton;

    public PublishResultsPart(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * select Writer Fields tab
     *
     * @return WriteFieldsTab
     */
    public WriteFieldsTab selectWriteFieldsTab() {
        if (!this.writeFieldsTab.isEnabled()) {
            pageUtils.waitForElementAndClick(writeFieldsTab);
        }
        return new WriteFieldsTab(driver);
    }

    /**
     * Select Attach Report tab in publish results part
     *
     * @return AttachReportTab
     */
    public PRAttachReportTab selectAttachReportTab() {
        if (this.attachReportTab.isEnabled()) {
            pageUtils.waitForElementAndClick(attachReportTab);
        }
        pageUtils.waitForElementToBeClickable(getSaveButtonElement());
        return new PRAttachReportTab(driver);
    }

    /**
     * click save button in publish results part
     *
     * @return WorkflowHome
     */
    public WorkflowHome clickSaveButton() {
        pageUtils.waitForElementToBeClickable(getSaveButtonElement());
        pageUtils.waitForElementAndClick(getSaveButtonElement());

        return new WorkflowHome(driver);
    }

    /**
     * click cancel button in publish results part
     *
     * @return WorkflowHome
     */
    public WorkflowHome clickCancelButton() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new WorkflowHome(driver);
    }

    /**
     * click previous button in publish results part
     *
     * @return NotificationsPart
     */
    public NotificationsPart clickPreviousButton() {
        pageUtils.waitForElementAndClick(previousButton);
        return new NotificationsPart(driver);
    }

    /**
     * click save button in publish results part
     *
     * @return WebElement
     */
    public WebElement getSaveButtonElement() {
        pageUtils.waitForElementToAppear(saveButton);
        return saveButton.findElement(By.xpath(".."));
    }

    /**
     * get previous button element in publish results part
     *
     * @return WebElement
     */
    public WebElement getPreviousButtonElement() {
        pageUtils.waitForElementToAppear(previousButton);
        return previousButton.findElement(By.xpath(".."));
    }
}
