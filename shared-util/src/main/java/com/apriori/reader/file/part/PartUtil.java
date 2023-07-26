package com.apriori.reader.file.part;

import com.apriori.FileResourceUtil;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.reader.file.part.service.PartCommonService;

import lombok.extern.slf4j.Slf4j;

/**
 * Get part functionality.
 * reference properties:
 * - different.parts
 * - if true: will return each time new part
 * - if false: will return each time the same part
 * - parts file is located on S3 bucket the
 * After getting the part from Collection, this part will be pushed to the end of the queue
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
    public static PartData getPartDataWithFile() {
        PartData partData = PartCommonService.getPartData();
        partData.setFile(
            FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(partData.getProcessGroup()), partData.getFilename())
        );
        logInfo(partData);
        return partData;
    }

    private static void logInfo(PartData partData) {
        log.info(String.format("Received PART for tests fileName:%s processGroup:%s ", partData.getFilename(), partData.getProcessGroup()));
    }

}
