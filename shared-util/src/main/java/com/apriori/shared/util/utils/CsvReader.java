package com.apriori.shared.util.utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

public class CsvReader {

    /**
     * Reads a csv file
     *
     * @param path      - the path to the file
     * @param separator - the separator for the columns
     * @param klass     - the class
     * @param <T>       - the generic type
     * @return generic list
     */
    public <T> List<T> csvReader(String path, char separator, Class<T> klass) {
        Reader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        CsvToBean<T> csvReader = new CsvToBeanBuilder<T>(reader)
            .withType(klass)
            .withSeparator(separator)
            .withIgnoreLeadingWhiteSpace(true)
            .withIgnoreEmptyLine(true)
            .build();

        return csvReader.parse();
    }
}
