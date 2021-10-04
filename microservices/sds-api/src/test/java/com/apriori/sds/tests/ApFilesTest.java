package com.apriori.sds.tests;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.PostComponentResponse;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

// TODO z: currently not working endpoint, should be added
public class ApFilesTest  extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"8417"})
    @Description("Import an existing aP File resulting in the creation/modification of components, scenarios and/or iterations. ")
    @Ignore
    public void testPostApFile() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_AP_FILES, null)
                .multiPartFiles(new MultiPartFiles()
                    .use("data", new File("C:\\Users\\vzarovnyi\\Downloads\\test.Initial (1).ap"))
                );
        ResponseWrapper<PostComponentResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
    }

}
