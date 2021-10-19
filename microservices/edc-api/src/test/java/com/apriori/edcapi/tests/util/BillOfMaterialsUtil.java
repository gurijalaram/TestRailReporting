package com.apriori.edcapi.tests.util;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterialsItems;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterialsResponse;
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

    protected static BillOfMaterialsResponse createBillOfMaterials(String fileName) {

        final RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.POST_BILL_OF_MATERIALS, BillOfMaterialsResponse.class)
                .multiPartFiles(new MultiPartFiles()
                    .use("multiPartFile", FileResourceUtil.getResourceAsFile(fileName)))
                .formParams(new FormParams()
                    .use("type","pcba"));

        ResponseWrapper<BillOfMaterialsResponse> createBillOfMaterialsResponse = HTTPRequest.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, createBillOfMaterialsResponse.getStatusCode());

        return createBillOfMaterialsResponse.getResponseEntity();
    }

    public static void deleteBomById() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                .inlineVariables(getFirstBillOfMaterials().getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTPRequest.build(requestEntity).delete().getStatusCode());
    }

    protected static BillOfMaterialsResponse getFirstBillOfMaterials() {
        List<BillOfMaterialsResponse> billOfMaterialResponses =  getAllBillOfMaterials();

        return billOfMaterialResponses.get(0);
    }

    protected static List<BillOfMaterialsResponse> getAllBillOfMaterials() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS, BillOfMaterialsItems.class);

        ResponseWrapper<BillOfMaterialsItems> responseWrapper = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity()
            .getItems();
    }
}
