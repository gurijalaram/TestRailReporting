package com.apriori.reader.file;

import com.apriori.FileResourceUtil;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Work with csv file.
 * Contain functionality to map data from CSV file
 *
 * @author vzarovnyi
 */
@Slf4j
public class InitFileData {

    public static List<String> initRows(String filePath) {
        InputStream usersListStream = FileResourceUtil.getResourceFileStream(filePath);

        if (fileNotExist(usersListStream)) {
            logError("File with rows was not found. File path:" + filePath);
            return null;
        }

        return parseDataToLineList(usersListStream);
    }

    private static boolean fileNotExist(InputStream usersListStream) {
        return usersListStream == null;
    }

    private static List<String> parseDataToLineList(InputStream usersFileStream) {
        List<String> rows = new LinkedList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(usersFileStream))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                rows.add(line);
            }
        } catch (IOException e) {
            logError("Error when initializing rows. " + e.getMessage());
            return null;
        }

        return rows;
    }

    private static void logError(String errorText) {
        log.error(errorText);
    }

    /**
     * Map data into Object from CSV file
     *
     * @param mapperType - Object type to map
     * @param fileToRead - CSV file with data to map
     * @param <T>
     * @return thread safe queue
     */
    @SneakyThrows
    //TODO z: should be updated for users util too, as a separate PR
    public <T> ConcurrentLinkedQueue<T> initRows(Class<T> mapperType, File fileToRead) {
        CsvSchema orderLineSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<T> orderLines = csvMapper.readerFor(mapperType)
            .with(orderLineSchema)
            .readValues(fileToRead);

        ConcurrentLinkedQueue<T> elementsQueue = new ConcurrentLinkedQueue<>();
        orderLines.forEachRemaining(elementsQueue::add);

        return elementsQueue;

    }
}
