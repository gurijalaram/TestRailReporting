package com.apriori.util.test.reader.part;

import com.apriori.utils.reader.file.part.PartUtil;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class PartUtilTest {

    @Test
    public void testGetTheSamePart() {
        System.setProperty("global_different_parts", "false");
        File partFile = PartUtil.getPartFile();
        File partFileToCompare = PartUtil.getPartFile();

        Assert.assertEquals("Parts should be the same", partFile.getName(), partFileToCompare.getName());
    }

    @Test
    public void testGetDifferentPart() {
        System.setProperty("global_different_parts", "true");
        File partFile = PartUtil.getPartFile();
        File partFileToCompare = PartUtil.getPartFile();

        Assert.assertNotEquals("Parts should be different", partFile.getName(), partFileToCompare.getName());
    }
}
