package com.apriori.pageobjects.navtoolbars;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public class CompareTabToolbar extends MainNavigationBar {

    public CompareTabToolbar(WebDriver driver) {
        super(driver, log);
    }
}
