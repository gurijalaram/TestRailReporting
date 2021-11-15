package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.navtoolbars.EvaluateTabToolbar;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public class EvaluatePage extends EagerPageComponent<EvaluatePage> {

    private EvaluateTabToolbar evaluateTabToolbar;

    public EvaluatePage(WebDriver driver) {
        super(driver, log);
        evaluateTabToolbar =  new EvaluateTabToolbar(driver);

    }

    @Override
    protected void isLoaded() throws Error {

    }
}
