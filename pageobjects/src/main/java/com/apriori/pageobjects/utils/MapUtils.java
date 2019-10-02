package com.apriori.pageobjects.utils;

import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    private Map<String, WebElement> map = new HashMap<>();

    public MapUtils(Map<String, WebElement> populateMap) {
        populateMap.entrySet().forEach(maps -> map.put(maps.getKey(), maps.getValue()));
    }

    public WebElement getLocatorFromMap(String locator) {
        return getMap().get(locator);
    }

    private Map<String, WebElement> getMap() {
        if (map.isEmpty()) {
            throw new AssertionError("Map must be populated");
        }
        return map;
    }
}
