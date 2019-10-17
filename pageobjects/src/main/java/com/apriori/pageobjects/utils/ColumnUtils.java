package com.apriori.pageobjects.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cfrith
 */

public class ColumnUtils {

    private WebDriver driver;

    Map<String, String> map = new HashMap<>();

    public ColumnUtils(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Maps the column header to the cell value based on the webElement
     *
     * @param table       - the table
     * @param column      - the column
     * @param cellLocator - the locator of the cell
     * @return string
     */
    public String columnDetails(String table, String column, String cellLocator) {
        String[] columns = driver.findElement(By.xpath("//div[@data-ap-comp='" + table + "']//thead")).getAttribute("innerText").split("\n");
        String[] cells = driver.findElement(By.xpath(cellLocator)).getAttribute("innerText").split("\n");

        String[] filteredCells = Arrays.stream(cells).filter(cell -> !cell.equals("\t\t")).toArray(String[]::new);

        for (int headerIndex = 0; headerIndex < columns.length; headerIndex++) {
            for (int rowIndex = 0; rowIndex < filteredCells.length; rowIndex++) {
                if (headerIndex == rowIndex) {
                    map.put(columns[headerIndex], filteredCells[headerIndex]);
                    break;
                }
            }
        }
        return map.get(column);
    }
}
