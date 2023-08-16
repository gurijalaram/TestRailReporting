package com.apriori.navtoolbars;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public class EvaluateTabToolbar extends MainNavigationBar {

    public EvaluateTabToolbar(WebDriver driver) {
        super(driver, log);
    }
}
