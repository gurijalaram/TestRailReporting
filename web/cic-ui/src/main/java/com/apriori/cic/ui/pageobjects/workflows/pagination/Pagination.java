package com.apriori.cic.ui.pageobjects.workflows.pagination;

import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Pagination to handle pages in footer in workflow and view history page
 */
public class Pagination extends WorkflowHome {

    private static final Logger logger = LoggerFactory.getLogger(Pagination.class);

    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div.dhx_toolbar_btn.dhxtoolbar_btn_over > img")
    private WebElement paginatorIconBtn;

    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div")
    private WebElement paginationBar;


    public Pagination(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    public void selectNoOfRowsToDisplay(int rowsPerPage) {
        pageUtils.waitForElementAndClick(paginatorIconBtn);
        this.findPaginationElementAndClick(rowsPerPage);
    }

    /**
     * Finds the maximum page size button and clicks on it
     *
     * @param size Current maximum rows per page setting
     * @return Workflow page
     */
    public void findPaginationElementAndClick(int size) {
        List<WebElement> paginationFields = paginationBar.findElements(By.cssSelector("div"));
        paginationFields.stream().filter(field -> field.getText().equalsIgnoreCase(String.format("%d rows per page", size))).findFirst().ifPresent(field -> pageUtils.waitForElementAndClick(field));
    }
}
