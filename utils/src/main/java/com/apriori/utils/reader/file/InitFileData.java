package com.apriori.utils.reader.file;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.constants.CommonConstants;

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
 * The users list file name, is declared by users.csv.file property.
 * users.csv.file: the name of csv file with users list by path: resources/{@link CommonConstants#environment} folder
 * (if users are absent, return default user with:
 * - username:{@link CommonConstants#DEFAULT_USER_NAME}
 * - password:{@link CommonConstants#DEFAULT_PASSWORD}
 * - accessLevel:{@link CommonConstants#DEFAULT_ACCESS_LEVEL}
 * )
 *
 * @author vzarovnyi
 */
@Slf4j
public class InitFileData {

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
            logError("Error when initializing rows. \n" + e.getMessage());
            return null;
        }

        return rows;
    }

    private static void logError(String errorText) {
        log.error(errorText);
    }
}
