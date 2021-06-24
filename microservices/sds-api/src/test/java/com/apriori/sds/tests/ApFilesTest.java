package com.apriori.sds.tests;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.PostComponentRequest;
import com.apriori.sds.entity.response.PostComponentResponse;
import com.apriori.sds.util.SDSRequestEntityUtil;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.EncodedFileUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import org.junit.Test;

import java.io.File;

public class ApFilesTest {//extends SDSTestUtil {


    //"mode": "formdata",
    //							"formdata": [
    //								{
    //									"key": "data",
    //									"type": "file",
    //									"src": "/Users/andy/Documents/Work/aPriori/aP Files/single-part.ap"
    //								},
    //								{
    //									"key": "createdBy",
    //									"value": "{{user_identity}}",
    //									"type": "text"
    //								}
    //							]
    @Test
    public void testPostApFile() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_AP_FILES, null)
                .multiPartFiles(new MultiPartFiles()
                    .use("data", new File("C:\\Users\\vzarovnyi\\Downloads\\test.Initial (1).ap"))
                )
                ;
//                .formParams(new FormParams().use("key", "data")
//                .use("type", "file")
//                .use("src", "C:\\Users\\vzarovnyi\\Downloads\\test.Initial.ap")
//                );

//                .body("component", PostComponentRequest.builder().filename(componentName)
//                    .scenarioName(scenarioName)
//                    .override(false)
//                    .fileContents(EncodedFileUtil.encodeFileFromCloudToBase64Binary(componentName, processGroup))
//                    .build());

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTP2Request.build(requestEntity).post();
    }
}
