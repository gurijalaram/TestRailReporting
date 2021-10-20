package com.apriori.edcapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.BillOfMaterialsItemsResponse;
import com.apriori.edcapi.entity.response.BillOfMaterialsResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

import java.util.List;

public class BillOfMaterialsUtil extends TestUtil {

    protected static BillOfMaterialsResponse createBillOfMaterials(String fileName, String type) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.POST_BILL_OF_MATERIALS, BillOfMaterialsResponse.class)
                .multiPartFiles(new MultiPartFiles()
                    .use("multiPartFile", FileResourceUtil.getResourceAsFile(fileName)))
                .formParams(new FormParams()
                    .use("type",type));

        ResponseWrapper<BillOfMaterialsResponse> createBillOfMaterialsResponse = HTTPRequest.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, createBillOfMaterialsResponse.getStatusCode());

        return createBillOfMaterialsResponse.getResponseEntity();
    }

    public static ResponseWrapper<Object> deleteBillOfMaterialById(final String identity) {
        RequestEntity requestEntity =
                RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                        .inlineVariables(identity).token(new JwtTokenUtil().retrieveJwtToken());

        return HTTPRequest.build(requestEntity).delete();
    }

    protected static BillOfMaterialsResponse getFirstBillOfMaterials() {
        List<BillOfMaterialsResponse> billOfMaterialResponses =  getAllBillOfMaterials();

        return billOfMaterialResponses.get(0);
    }

    protected static List<BillOfMaterialsResponse> getAllBillOfMaterials() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS, BillOfMaterialsItemsResponse.class);

        ResponseWrapper<BillOfMaterialsItemsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity()
            .getItems();
    }
}
