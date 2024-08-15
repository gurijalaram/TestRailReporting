package com.apriori.trr.api.testrail.internal;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Factory to create instances of {@link URLConnection}.
 */
public class UrlConnectionFactory {

    /**
     * Get URL connection.
     *
     * @param url the URL to connect to
     * @return the open connection
     * @throws IOException if URL is malformed or if there's an IO error
     */
    public URLConnection getUrlConnection(final String url) throws IOException {
        return new URL(url).openConnection();
    }

}
