package com.apriori;

import org.openqa.selenium.WebDriver;

public interface DriverManager<T> {
    WebDriver createDriver();

    T getOptions();
}
