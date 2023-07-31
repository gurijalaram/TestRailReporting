package reader.part;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.apriori.reader.file.part.PartData;
import com.apriori.reader.file.part.PartUtil;

import org.junit.Assert;
import org.junit.Test;

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

        Assert.assertEquals("Files should be the same", partData.getFile(), partDataToCompare.getFile());
    }

    @Test
    public void testGetDifferentPartWithFile() {
        System.setProperty("global_different_parts", "true");
        PartData partData = PartUtil.getPartDataWithFile();
        PartData partDataToCompare = PartUtil.getPartDataWithFile();

        assertNotEquals(partData.getFile(), partDataToCompare.getFile(), "Parts should be different");
    }
}
