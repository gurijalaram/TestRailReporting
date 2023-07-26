package com.apriori.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

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
     * @param table      - the table
     * @param column     - the column
     * @param rowLocator - the locator of the cell
     * @return string
     */
    public String columnDetails(String table, String column, String rowLocator) {
        String[] columns = driver.findElement(By.xpath("//div[@data-ap-comp='" + table + "']//thead")).getAttribute("innerText").split("");
        String[] cells = driver.findElement(By.xpath(rowLocator)).getAttribute("innerText").split("");

        String[] filteredCells = Arrays.stream(cells).filter(cell -> !cell.equals("\t") && !cell.equals("\t\t")).toArray(String[]::new);

        IntStream.range(0, columns.length).filter(headerIndex -> IntStream.range(0, filteredCells.length).anyMatch(rowIndex -> headerIndex == rowIndex)).forEach(headerIndex -> map.put(columns[headerIndex], filteredCells[headerIndex]));
        return map.get(column);
    }
}
