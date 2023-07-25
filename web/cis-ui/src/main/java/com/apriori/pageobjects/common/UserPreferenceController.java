package com.apriori.pageobjects.common;

import com.apriori.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class UserPreferenceController extends EagerPageComponent<UserPreferenceController> {


    public UserPreferenceController(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Gets items in user preference
     *
     * @return list of string
     */
    public List<String> getUserPreferenceItems() {
        return Stream.of(getDriver().findElement(By.xpath("//div[@aria-orientation='vertical']")).getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets items in display preference
     *
     * @return list of string
     */
    public List<String> getDisplayPreferenceItems() {
        return Stream.of(getDriver().findElement(By.xpath("//div[starts-with(@id,'vertical-tabpanel')]")).getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }
}