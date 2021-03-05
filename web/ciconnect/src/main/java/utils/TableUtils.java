package utils;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
     * Find an item in a table by name
     *
     * @param table The table to search
     * @param name The name to search for
     * @return The webelement if found
     */
    public WebElement findTableItemByName(WebElement table, String name) {
        pageUtils.waitForElementToBeClickable(table);
        List<WebElement> rows = table.findElements(By.tagName("tr"))
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
}
