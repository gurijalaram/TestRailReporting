package com.apriori.util.test.reader.part;


import com.apriori.utils.reader.file.part.PartUtil;

import org.junit.Assert;
import org.junit.Test;

public class PartUtilTest {

    @Test
    public void testGetTheSamePartData() {
        System.setProperty("global_different_parts", "false");
        PartData partData = PartUtil.getPartData();
        PartData partDataToCompare = PartUtil.getPartData();

        Assert.assertEquals("Parts should be the same", partData, partDataToCompare);
    }

    @Test
    public void testGetTheDifferentPartData() {
        System.setProperty("global_different_parts", "true");
        PartData partData = PartUtil.getPartData();
        PartData partDataToCompare = PartUtil.getPartData();

        Assert.assertNotEquals("Parts should be different", partData, partDataToCompare);
    }

    @Test
    public void testGetTheSamePartWithFile() {
        System.setProperty("global_different_parts", "false");
        PartData partData = PartUtil.getPartDataWithFile();
        PartData partDataToCompare = PartUtil.getPartDataWithFile();

        Assert.assertEquals("Files should be the same", partData.getFile(), partDataToCompare.getFile());
    }

    @Test
    public void testGetDifferentPartWithFile() {
        System.setProperty("global_different_parts", "true");
        PartData partData = PartUtil.getPartDataWithFile();
        PartData partDataToCompare = PartUtil.getPartDataWithFile();

        Assert.assertNotEquals("Parts should be different", partData.getFile(), partDataToCompare.getFile());
    }
}
