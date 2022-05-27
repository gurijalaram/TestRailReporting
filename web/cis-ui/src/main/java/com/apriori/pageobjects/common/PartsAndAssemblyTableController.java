package com.apriori.pageobjects.common;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class PartsAndAssemblyTableController extends EagerPageComponent<PartsAndAssemblyTableController> {

    @FindBy(css = "div.MuiDataGrid-columnHeaders.css-qw65j7")
    private WebElement tableHeaders;

    @FindBy(css = "div.MuiDataGrid-pinnedColumnHeaders")
    private WebElement pinnedTableHeaders;

    public PartsAndAssemblyTableController(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {


    }

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public List<String> getTableHeaders() {
        return Stream.of(tableHeaders.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets pinned table headers
     *
     * @return list of string
     */
    public List<String> getPinnedTableHeaders() {
        return Stream.of(pinnedTableHeaders.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }
}