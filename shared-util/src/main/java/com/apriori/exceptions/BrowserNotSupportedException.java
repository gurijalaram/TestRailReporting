package com.apriori.exceptions;

import com.apriori.testconfig.Browser;

public class BrowserNotSupportedException extends IllegalStateException {
    public BrowserNotSupportedException(Browser browser) {
        super("Browser '{}' not supported " + browser);
    }
}
