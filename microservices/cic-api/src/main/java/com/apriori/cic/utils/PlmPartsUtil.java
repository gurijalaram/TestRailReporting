package com.apriori.cic.utils;

import com.apriori.cic.enums.PlmPartDataType;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.reader.file.InitFileData;
import com.apriori.reader.file.part.PartData;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class PlmPartsUtil {
    private static ConcurrentLinkedQueue<PartData> plmPartsQueue;

    public PlmPartsUtil() {
        plmPartsQueue = new InitFileData().initRows(PartData.class,
            FileResourceUtil.getCloudFile(ProcessGroupEnum.RESOURCES, "plm-test-parts.csv"));
    }

    /**
     * get plm part data from plm test parts csv file from cloud
     *
     * @return PartData
     */
    public PartData getPlmPartData() {
        PartData partData = plmPartsQueue.poll();
        plmPartsQueue.add(partData);
        log.info(String.format("Received PART for tests fileName:%s processGroup:%s ", partData.getFilename(), partData.getProcessGroup()));
        return partData;
    }

    /**
     * get plm parts from cloud
     *
     * @param numOfParts
     * @return list<PartData>
     */
    public List<PartData> getPlmPartsFromCloud(Integer numOfParts) {
        return IntStream.range(0, numOfParts)
            .mapToObj(i -> getPlmPartData())
            .collect(Collectors.toList());
    }

    /**
     * get plm part data from plm test parts csv file from cloud
     *
     * @return PartData
     */
    public PartData getPlmPartData(PlmPartDataType plmPartDataType) {
        PartData partData = plmPartsQueue.poll();
        try {
            while (true) {
                if (partData.getPlmMapped().equals(plmPartDataType.getPlmPartDataType())) {
                    return partData;
                }
                partData = plmPartsQueue.poll();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("MATCHED PLM PART NOT FOUND IN DATA FILE!!");
        }
    }

    /**
     * get plm part data from plm test parts csv file from cloud
     *
     * @return PartData
     */
    public List<PartData> getPlmPartData(PlmPartDataType plmPartDataType, Integer numOfParts) {
        List<PartData> partDataList;
        partDataList =  IntStream.range(0, numOfParts)
            .mapToObj(i -> getPlmPartData(plmPartDataType))
            .collect(Collectors.toList());

        if (partDataList == null) {
            throw new IllegalArgumentException("FAILED TO RETRIEVE THE DATA FROM AWS");
        }
        return partDataList;
    }
}
