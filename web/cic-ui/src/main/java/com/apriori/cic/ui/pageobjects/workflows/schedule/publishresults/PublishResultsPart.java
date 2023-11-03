package com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults;

import com.apriori.cic.ui.pageobjects.CICBasePage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Publish Results tab during workflow creation or modification
 * Navigate to Write Fields Tab or Attach Report tab
 */
public class PublishResultsPart extends CICBasePage {

    private static final Logger logger = LoggerFactory.getLogger(PublishResultsPart.class);

    public static final String PARENT_ELEMENT = "div[id^='root_pagemashupcontainer-1_navigation-']";

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_tabsv2-330'] > div.tab-content > div.tabsv2-viewport > div > div > div[class^='tabsv2-tab'][tab-value='write-fields']")
    protected WebElement writeFieldsTab;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_tabsv2-330'] > div.tab-content > div.tabsv2-viewport > div > div > div[class^='tabsv2-tab'][tab-value='attach-plm-report']")
    protected WebElement attachReportTab;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_button-288'] > button")
    protected WebElement saveButton;



    protected String reportConfigRootElementsRowsCss = "div[class='BMCollectionViewCellWrapper'] div[class='BMCollectionViewCell BMCollectionViewCellHoverable'][id^='CIC_ReportConfigurationCell_MU-']";


    public PublishResultsPart(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
        return new PRAttachReportTab(driver);
    }

    /**
     * click save button in publish results part
     *
     * @return WorkflowHome
     */
    public WorkflowHome clickSaveButton() {
        pageUtils.waitForElementToBeClickable(saveButton);
        pageUtils.waitForElementAndClick(saveButton);

        return new WorkflowHome(driver);
    }
}
