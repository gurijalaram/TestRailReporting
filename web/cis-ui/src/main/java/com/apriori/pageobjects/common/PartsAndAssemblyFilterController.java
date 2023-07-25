package com.apriori.pageobjects.common;

import com.apriori.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class PartsAndAssemblyFilterController extends EagerPageComponent<PartsAndAssemblyFilterController> {

    @FindBy(id = "menu-")
    private WebElement operationList;

    public PartsAndAssemblyFilterController(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Gets filter operations
     *
     * @return list of string
     */
    public List<String> getOperationList() {
        return Stream.of(operationList.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }
}