package com.apriori.pageobjects.common;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class PartsAndAssemblyDetailsController extends EagerPageComponent<PartsAndAssemblyDetailsController> {


    public PartsAndAssemblyDetailsController(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {


    }

    /**
     * Gets scenario result card fields
     *
     * @return list of string
     */

    public List<String> getScenarioResultCardFields(String cardName) {
        return Stream.of(getDriver().findElement(By.xpath("//h2[text()='" + cardName + "']//following::div[starts-with(@class,'MuiCollapse-root MuiCollapse-vertical')]")).getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

}