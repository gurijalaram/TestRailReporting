package com.apriori.cic.ui.pageobjects.connectors;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cic.ui.enums.ConnectorListHeaders;
import com.apriori.cic.ui.enums.SortedOrderType;
import com.apriori.cic.ui.pageobjects.CICBasePage;
import com.apriori.cic.ui.pageobjects.workflows.history.ModalDialog;
import com.apriori.cic.ui.utils.Constants;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Connectors page
 */
@Slf4j
public class ConnectorsPage extends CICBasePage {

    @FindBy(css = "div[class$='cic-table'] table.obj")
    private WebElement connectorListTable;

    @FindBy(css = "div[class$='cic-table'] table.hdr")
    private WebElement connectorHeaderListTable;

    @FindBy(css = "div.xhdr td:nth-of-type(4)")
    private WebElement nameHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(5)")
    private WebElement descriptionHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(6)")
    private WebElement typeHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(8)")
    private WebElement connectionStatusHeader;

    @FindBy(xpath = "//button[.='New']")
    private WebElement newConnectorBtn;

    @FindBy(xpath = "//button[.='Delete']")
    private WebElement deleteConnectorBtn;

    @FindBy(xpath = "//button[.='Edit']")
    private WebElement editConnectorBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-31 > button")
    private WebElement refreshConnectorStatusBtn;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_label-8'] > span")
    private WebElement connectorsLabel;

    public ConnectorsPage(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.waitForElementToAppear(newConnectorBtn);

    }

    /**
     * Select workflow in table
     *
     * @param connectorName - name of workflow to select
     * @return new Schedule page object
     */
    public ConnectorsPage selectConnector(String connectorName) {
        pageUtils.waitForElementAppear(connectorListTable);
        Integer columnIndex = tableUtils.getColumnIndx(connectorHeaderListTable, ConnectorListHeaders.NAME.getColumnName().toString());
        tableUtils.selectRowByName(connectorListTable, connectorName, columnIndex);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return new ConnectorsPage(driver);
    }

    /**
     * Select workflow in table
     *
     * @param connectorName - name of workflow to select
     * @return new Schedule page object
     */
    public Boolean isConnectorExist(String connectorName) {
        Boolean isConnectorDeleted = true;
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        int attempts = 0;
        while (attempts < 10) {
            try {
                isConnectorDeleted = tableUtils.itemExistsInTable(connectorListTable, connectorName);
                break;
            } catch (StaleElementReferenceException e) {
                log.info(e.getMessage());
            }
            attempts++;
        }
        return isConnectorDeleted;
    }

    /**
     * click New button in connectors home page
     *
     * @return ConnectorDetails object
     */
    public ConnectorDetails clickNewBtn() {
        pageUtils.waitForElementAndClick(newConnectorBtn);
        return new ConnectorDetails(driver);
    }

    /**
     * click delete button in connectors home page
     *
     * @return current class object
     */
    public ConnectorsPage clickDeleteBtn() {
        pageUtils.waitForElementAndClick(deleteConnectorBtn);
        return this;
    }

    /**
     * click edit button in connectors home page
     *
     * @return ConnectorDetails object
     */
    public ConnectorDetails clickEditBtn() {
        pageUtils.waitForElementAndClick(editConnectorBtn);
        return new ConnectorDetails(driver);
    }

    /**
     * click refresh button
     *
     * @return ConnectorDetails page object
     */
    public ConnectorsPage clickRefreshBtn() {
        pageUtils.waitForElementAndClick(getRefreshConnectorStatusBtn());
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * click delete button model dialog confirmation pop up window
     *
     * @return current class object
     */
    public ConnectorsPage clickConfirmAlertDelete() {
        new ModalDialog(driver).clickDeleteButton();
        return this;
    }

    /**
     * Get connectors page label text
     *
     * @return String
     */
    public String getConnectorText() {
        pageUtils.waitForElementToAppear(connectorsLabel);
        return connectorsLabel.getText();
    }

    /**
     * Get connectors page Edit button
     *
     * @return WebElement
     */
    public WebElement getEditConnectorBtn() {
        return editConnectorBtn;
    }

    /**
     * Get connectors page Delete button
     *
     * @return WebElement
     */
    public WebElement getDeleteConnectorBtn() {
        return deleteConnectorBtn;
    }

    /**
     * Get connectors page New button
     *
     * @return WebElement
     */
    public WebElement getNewConnectorBtn() {
        return newConnectorBtn;
    }

    /**
     * Get connectors page refresh button
     *
     * @return WebElement
     */
    public WebElement getRefreshConnectorStatusBtn() {
        return driver.findElement(with(By.xpath("//button//span[@class='widget-button-text']")).toLeftOf(By.xpath("//button[.='Delete']")));
    }

    /**
     * validates workflow is in sorted order by workflow name
     *
     * @param columnName  - ConnectorListColumns enum
     * @param sortedBy    - SortedOrderType (Ascending or descending)
     * @param columnValue - Connector name
     * @return - true or false
     */
    public Boolean isConnectorListIsSorted(ConnectorListHeaders columnHeader, SortedOrderType sortedBy) {
        Boolean isInSortedOrder = false;
        WebElement webElement = tableUtils.getColumnHeader(connectorHeaderListTable, columnHeader.getColumnName().toString());
        Integer columnIndex = tableUtils.getColumnIndx(connectorHeaderListTable, columnHeader.getColumnName().toString());
        pageUtils.waitForElementAndClick(webElement);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.waitForElementToBeClickable(connectorHeaderListTable);
        switch (sortedBy.toString()) {
            case "DESCENDING":
                if (!webElement.getAttribute("class").equals("dhxgrid_sort_desc_col")) {
                    pageUtils.waitForElementAndClick(webElement);
                    pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
                    pageUtils.waitForElementToBeClickable(connectorHeaderListTable);
                }
                isInSortedOrder = webElement.getAttribute("class").equals("dhxgrid_sort_asc_col");
                break;
            default:
                if (!webElement.getAttribute("class").equals("dhxgrid_sort_asc_col")) {
                    pageUtils.waitForElementAndClick(webElement);
                    pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
                    pageUtils.waitForElementToBeClickable(connectorHeaderListTable);
                }
                isInSortedOrder = webElement.getAttribute("class").equals("dhxgrid_sort_desc_col");
        }
        return isInSortedOrder;
    }
}
