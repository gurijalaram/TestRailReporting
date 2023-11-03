package com.apriori.shared.util.tests.reader.part;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.file.part.PartUtil;

import org.junit.jupiter.api.Test;

public class PartUtilTest {

    @Test
    public void testGetTheSamePartData() {
        System.setProperty("global_different_parts", "false");
        PartData partData = PartUtil.getPartData();
        PartData partDataToCompare = PartUtil.getPartData();

        assertNotEquals(partData, partDataToCompare, "Parts should be the same");
    }

    @Test
    public void testGetTheDifferentPartData() {
        System.setProperty("global_different_parts", "true");
        PartData partData = PartUtil.getPartData();
        PartData partDataToCompare = PartUtil.getPartData();

        assertNotEquals(partData, partDataToCompare, "Parts should be different");
    }

    @Test
    public void testGetTheSamePartWithFile() {
        System.setProperty("global_different_parts", "false");
        PartData partData = PartUtil.getPartDataWithFile();
        PartData partDataToCompare = PartUtil.getPartDataWithFile();

        assertEquals(partData.getFile(), partDataToCompare.getFile(), "Files should be the same");
    }

    @Test
    public void testGetDifferentPartWithFile() {
        System.setProperty("global_different_parts", "true");
        PartData partData = PartUtil.getPartDataWithFile();
        PartData partDataToCompare = PartUtil.getPartDataWithFile();

        assertNotEquals(partData.getFile(), partDataToCompare.getFile(), "Parts should be different");
    }
}
