package com.apriori.utils.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author kpatel
 */
public class BaseReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseReader.class);
    Properties properties;

    public BaseReader(String fileName) {
        try (InputStream fileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            properties = new Properties();
            properties.load(fileStream);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
