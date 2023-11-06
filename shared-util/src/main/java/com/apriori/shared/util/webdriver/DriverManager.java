package com.apriori.shared.util.webdriver;

import org.openqa.selenium.WebDriver;

public interface DriverManager<T> {
    WebDriver createDriver();

    T getOptions();
}
