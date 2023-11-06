package com.apriori.shared.util.exceptions;

import com.apriori.shared.util.testconfig.Browser;

public class BrowserNotSupportedException extends IllegalStateException {
    public BrowserNotSupportedException(Browser browser) {
        super("Browser '{}' not supported " + browser);
    }
}
