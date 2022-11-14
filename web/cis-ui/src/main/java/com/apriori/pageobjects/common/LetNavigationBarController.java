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
public class LetNavigationBarController extends EagerPageComponent<LetNavigationBarController> {


    public LetNavigationBarController(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Gets items in Navigation Panel Sections
     *
     * @return list of string
     */
    public List<String> getItemsOfSections(String section) {
        return Stream.of(getDriver().findElement(By.xpath("//span[text()='" + section + "']//..")).getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }
}