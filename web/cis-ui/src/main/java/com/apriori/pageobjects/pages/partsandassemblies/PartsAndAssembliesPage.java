package com.apriori.pageobjects.pages.partsandassemblies;

import com.apriori.pageobjects.common.PartsAndAssemblyTableController;
import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import java.util.List;

@Slf4j
public class PartsAndAssembliesPage extends EagerPageComponent<PartsAndAssembliesPage> {

    @FindBy(xpath = "//div[@Class='MuiDataGrid-row']")
    private List<WebElement> tableRow;

    @FindBy(xpath = "//div[@class='MuiDataGrid-row Mui-selected']")
    private List<WebElement> selectedCheckboxes;

    public PartsAndAssembliesPage(WebDriver driver) {

        this(driver, log);
    }

    private PageUtils pageUtils;
    private WebDriver driver;
    private PartsAndAssemblyTableController partsAndAssemblyTableController;

    public PartsAndAssembliesPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.partsAndAssemblyTableController = new PartsAndAssemblyTableController(driver);
        PageFactory.initElements(driver, this);

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public  List<String> getTableHeaders() {
        return partsAndAssemblyTableController.getTableHeaders();
    }

    /**
     * Method to wait until loading complete
     */
    public void waitForTableLoad() {
        pageUtils.waitForElementsToNotAppear(By.xpath("//span[@role='progressbar']"),1);
    }

    /**
     * Get the checkboxes status
     *
     * @return String
     */
    public String getComponentCheckBoxStatus() {
        if (tableRow.size() > 0) {
            for (int i = 0;i <= tableRow.size();i++) {
                getPageUtils().waitForElementToAppear(driver.findElement(By.xpath("//div[@data-rowindex='" + i + "']//span[starts-with(@class,'MuiCheckbox-root')]"))).click();
            }
        }
        return getPageUtils().waitForElementsToAppear(selectedCheckboxes).get(tableRow.size()).getAttribute("aria-selected");
    }
}