package com.apriori.utils.reader.file.part;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.part.service.PartCommonService;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * Get user functionality.
 * Has reference to {@link CommonConstants#environment}.properties file
 * reference properties:
 * - different.users
 * - if true: will return each time new user
 * - if false: will return each time single user
 * - users.csv.file: the name of csv file with users list from resources/{@link CommonConstants#environment} folder
 * (if users are absent, return default user with:
 * - username:{@link CommonConstants#DEFAULT_USER_NAME}
 * - password:{@link CommonConstants#DEFAULT_PASSWORD}
 * )
 * <p>
 * Users list is global for two Collections:
 * - security users collection
 * - common users collection
 * <p>
 * Each collection has a copy of this list and after getting the user, this user will be pushed to the end of queue
 * Example:
 * security collection - user1, user2, user3
 * common collection - user1, user2, user3
 * <p>
 * after getting the security user
 * <p>
 * security collection - user2, user3, user1
 * common collection - user1, user2, user3
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
