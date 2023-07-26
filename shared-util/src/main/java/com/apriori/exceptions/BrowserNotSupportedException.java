package com.apriori.exceptions;

import com.apriori.Browser;

public class BrowserNotSupportedException extends IllegalStateException {
    public BrowserNotSupportedException(Browser browser) {
        super("Browser '{}' not supported " + browser);
    }
}
