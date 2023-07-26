package com.apriori;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class EventCapture implements WebDriverListener {
    /**
     * This class can be used for implementing extra debugging on any selenium actions.  An example is given below.
     */

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        WebDriverListener.super.beforeSendKeys(element, keysToSend);
        log.debug("Typing in Element: " + element + " Keys: " + Arrays.stream(keysToSend).collect(Collectors.toList()));
    }
}
