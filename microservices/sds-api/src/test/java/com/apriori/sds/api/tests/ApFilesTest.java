package com.apriori.sds.api.tests;

import com.apriori.sds.api.enums.SDSAPIEnum;
import com.apriori.sds.api.models.response.PostComponentResponse;
import com.apriori.sds.api.util.SDSTestUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.MultiPartFiles;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

// TODO : currently not working endpoint, should be added
@ExtendWith(TestRulesAPI.class)
public class ApFilesTest  extends SDSTestUtil {

    @Test
    @TestRail(id = {8417})
    @Description("Import an existing aP File resulting in the creation/modification of components, scenarios and/or iterations. ")
    @Disabled
    public void testPostApFile() {
        final RequestEntity requestEntity =
            requestEntityUtil.init(SDSAPIEnum.POST_AP_FILES, null)
                .multiPartFiles(new MultiPartFiles()
                    .use("data", new File("C:\\Users\\vzarovnyi\\Downloads\\test.Initial (1).ap"))
                );
        ResponseWrapper<PostComponentResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
    }

}
