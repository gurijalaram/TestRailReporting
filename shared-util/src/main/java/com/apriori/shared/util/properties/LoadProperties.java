package com.apriori.shared.util.properties;

import com.apriori.shared.util.http.utils.FileResourceUtil;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * @author cfrith
 */
public class LoadProperties {
    /**
     * Loads the file containing the project run id
     *
     * @return int
     */
    @SneakyThrows
    public static Properties loadProperties(String fileName) {
        String file = Optional.ofNullable(System.getProperty(fileName)).orElse(fileName);
        Properties properties = new Properties();
        InputStream is = FileResourceUtil.getResourceFileStream(file + ".properties");
        properties.load(is);
        return properties;
    }
}
