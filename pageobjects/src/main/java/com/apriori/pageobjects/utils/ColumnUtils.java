package com.apriori.pageobjects.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cfrith
 */

public class ColumnUtils {

    private WebDriver driver;

    public ColumnUtils(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Maps the column header to the cell value based on the webElement
     *
     * @param column    - the column
     * @param issueType - the issue type
     * @return string
     */
    public String columnDetails(String issueType, String column, String tableLocator) {
        String[] columns = driver.findElement(By.xpath("//div[@data-ap-comp='" + tableLocator + "']//thead")).getAttribute("innerText").split("\n");
        String[] cells = driver.findElement(By.xpath("//div[@data-ap-comp='" + tableLocator + "']//td[contains(text(),'" + issueType + " ')]/ancestor::tr")).getAttribute("innerText").split("\n");

        Map<String, String> columnDetails = new HashMap<>();

        for (int headerIndex = 0; headerIndex < columns.length; headerIndex++) {
            for (int rowIndex = 0; rowIndex < cells.length; rowIndex++) {
                if (headerIndex == rowIndex) {
                    columnDetails.put(columns[headerIndex], cells[headerIndex]);
                    break;
                }
            }
        }
        return columnDetails.get(column);
    }
}
