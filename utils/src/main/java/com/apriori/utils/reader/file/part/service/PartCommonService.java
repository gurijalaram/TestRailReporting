package com.apriori.utils.reader.file.part.service;







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
