package com.apriori.utils.reader.file.part;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.part.service.PartCommonService;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * Get part functionality.
 * reference properties:
 * - different.parts
 * - if true: will return each time new part
 * - if false: will return each time the same part
 * - parts file is located on S3 bucket the
 *  After getting the part from Collection, this part will be pushed to the end of the queue
 * Example:
 * collection - part1, part2, part3
 * <p>
 * after getting the part
 * <p>
 * collection - part2, part3, part1
 *
 * @author vzarovnyi
 */
@Slf4j
public class PartUtil {


    /**
     * Return part data information from part file
     *
     * @return PartData
     */
    public static PartData getPartData() {
        PartData partData = PartCommonService.getPartData();
        logInfo(partData);
        return partData;
    }

    /**
     * Return part file copied into temp folder from S3 bucket
     *
     * @return File
     */
    public static File getPartFile() {
        PartData partData = PartCommonService.getPartData();
        logInfo(partData);
        return FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(partData.getProcessGroup()), partData.getFileName());
    }

    private static void logInfo(PartData partData) {
        log.info(String.format("Received PART for tests fileName:%s processGroup:%s ", partData.getFileName(), partData.getProcessGroup()));
    }

}
