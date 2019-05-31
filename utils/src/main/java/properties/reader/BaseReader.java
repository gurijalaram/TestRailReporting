package main.java.properties.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author kpatel
 */
public class BaseReader {

    Properties properties;

    public BaseReader(String fileName) {
        try (InputStream fileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            properties = new Properties();
            properties.load(fileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
