package utils;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableUtils {
    private PageUtils pageUtils;
    private WebDriver driver;

    public TableUtils(WebDriver driver) {
        this.pageUtils = new PageUtils(driver);
        this.driver = driver;
    }

    /**
     * Determine if the actual list contains all of the items in the expected list
     *
     * @param expectedList The list of expected values
     * @param actualList The list of actual values
     * @return True if the actual list contains all items in the expected list
     */
    public boolean actualListContainsAllItems(List<String> expectedList, List<String> actualList) {
        for (String expected : expectedList) {
            if (!actualList.contains(expected)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Find an item in a table by name
     *
     * @param table The table to search
     * @param name The name to search for
     * @return The webelement if found
     */
    public WebElement findTableItemByName(WebElement table, String name) {
        pageUtils.waitForElementToBeClickable(table);
        List<WebElement> rows =
                table.findElements(By.tagName("tr"))
                .stream()
                .skip(1)
                .filter(user -> user.findElements(By.tagName("td")).get(0).getText().equalsIgnoreCase(name))
                .collect(Collectors.toList());
        if (rows.size() > 0) {
            return rows.get(0);
        } else {
            return null;
        }
    }

    /**
     * Return the number of rows in a table
     *
     * @param table The target table
     * @return number of rows
     */
    public Integer getRowCount(WebElement table) {
        pageUtils.waitForElementToBeClickable(table);
        List<WebElement> rows =
                table.findElements(By.tagName("tr"))
                        .stream()
                        .skip(1)
                        .collect(Collectors.toList());
        return  rows.size();
    }

    /**
     * Return the index of a workflow
     *
     * @param table table to search
     * @param name workflow name
     * @return The row number of the specified
     */
    public Integer getTableItemIndex(WebElement table, String name) {
        pageUtils.waitForElementToBeClickable(table);
        List<WebElement> rows =
                table.findElements(By.tagName("tr"))
                        .stream()
                        .skip(1)
                        .collect(Collectors.toList());

        if (rows.size() <= 0) {
            return null;
        }
        int idx = 0;

        for (WebElement row : rows) {
            if (row.findElements(By.tagName("td")).get(0).getText().equalsIgnoreCase(name)) {
                return idx;
            }

            idx++;
        }

        return -1;
    }

    /**
     * Returns the number of displayed rows in a table (skipping the header row)
     * @param table
     * @return
     */
    public Integer numberOfRows(WebElement table) {
        return table.findElements(By.tagName("tr")).size() - 1;
    }

    /**
     * Find an item in a table by name
     *
     * @param table The table to search
     * @param rowNumber The row number to select
     * @return The webelement if found
     */
    public WebElement selectRowByIndex(WebElement table, int rowNumber) {
        int rowId = rowNumber + 1;
        String cssRow = String.format("tr:nth-child(%d)",rowId);
        WebElement row = table.findElement(By.cssSelector(cssRow));
        pageUtils.waitForElementAndClick(row);
        return row;
    }



    /**
     * Select a row by item name
     *
     * @param table  The table to search
     * @param name The item name to search for
     */
    public  void selectRowByName(WebElement table, String name) {
        findTableItemByName(table, name).click();
    }

    /**
     * Check if a item exisits in the table. Search by item name
     *
     * @param table  The table to search
     * @param name The workflow name to check for
     * @return True if the item exists in the table
     */
    public Boolean itemExistsInTable(WebElement table, String name) {
        WebElement item = findTableItemByName(table, name);
        if (item != null) {
            return true;
        }

        return false;
    }

    /**
     * Return a tables headers
     *
     * @param tableHeaders The header row of a table
     * @return List of headers
     */
    public List<String> getTableHeaders(WebElement tableHeaders) {
        pageUtils.waitForElementToBeClickable(tableHeaders);
        List<String> headers = new ArrayList<>();
        tableHeaders.findElements(By.tagName("td"))
                .forEach(column -> headers.add(column.getText()));
        return headers;
    }

    /**
     * Returns the specified column header element
     *
     * @param tableHeaders Header row to search through
     * @param columnHeader The column header element to return
     * @return Column Header element
     */
    public WebElement getColumnHeader(WebElement tableHeaders, String columnHeader) {
        pageUtils.waitForElementToBeClickable(tableHeaders);
        List<WebElement> columns = tableHeaders.findElements(By.tagName("td"));
        for (WebElement column : columns) {
            if (column.getText().equalsIgnoreCase(columnHeader)) {
                return column;
            }
        }
        return null;
    }
}
