package com.apriori;

import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.MultiPartFiles;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.sds.enums.SDSAPIEnum;
import com.apriori.sds.models.response.PostComponentResponse;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

// TODO z: currently not working endpoint, should be added
public class ApFilesTest  extends SDSTestUtil {

    @Test
    @TestRail(id = {8417})
    @Description("Import an existing aP File resulting in the creation/modification of components, scenarios and/or iterations. ")
    @Disabled
    public void testPostApFile() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_AP_FILES, null)
                .multiPartFiles(new MultiPartFiles()
                    .use("data", new File("C:\\Users\\vzarovnyi\\Downloads\\test.Initial (1).ap"))
                );
        ResponseWrapper<PostComponentResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
    }

}
