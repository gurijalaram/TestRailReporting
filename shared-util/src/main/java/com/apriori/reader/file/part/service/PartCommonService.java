package com.apriori.reader.file.part.service;

import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.InitFileData;
import com.apriori.reader.file.part.PartData;

import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Contain logic to work with common users.
 *
 * @author vzarovnyi
 */
@Slf4j
public class PartCommonService {

    private static ConcurrentLinkedQueue<PartData> partsQueue = new InitFileData().initRows(PartData.class,
        FileResourceUtil.getCloudFile(ProcessGroupEnum.RESOURCES, "css-test-parts.csv")
    );

    private static PartData globalPart;

    /**
     * Return single user
     * if different.users is false
     * else each time return unique user
     *
     * @throws NoSuchElementException if the iteration has no more elements
     */
    public static PartData getPartData() {
        return PropertiesContext.get("global.different_parts").equals("true") ? getNewPart() : getGlobalPart();
    }

    private static PartData getNewPart() {
        PartData partData = partsQueue.poll();
        partsQueue.add(partData);

        return partData;
    }

    private static PartData getGlobalPart() {
        if (globalPart != null) {
            return globalPart;
        }

        globalPart = getNewPart();
        return globalPart;
    }
}
