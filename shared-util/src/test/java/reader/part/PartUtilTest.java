package reader.part;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.apriori.enums.AuthUserContextEnum;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Users;
import com.apriori.reader.file.part.PartData;
import com.apriori.reader.file.part.PartUtil;

import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class PartUtilTest {


    // TODO z: remove this test
    @Test
    public void getHttpRequest() {
        RequestEntity requestEntity = RequestEntityUtil.init(AuthUserContextEnum.GET_AUTH_USER_CONTEXT, Users.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper response = HTTPRequest.build(requestEntity).get();
    }

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
