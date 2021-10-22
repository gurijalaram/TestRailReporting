package com.apriori.edcapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterialsItemsResponse;
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

    /**
     * Create Bill of Materials
     *
     * @param fileName - the file name
     * @return Bill of Materials Response instance
     */
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

    /**
     * Delete Bill of Materials by Identity
     *
     * @param billOfMaterialsId
     */
    public static void deleteBillOfMaterialById(String billOfMaterialsId) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                .inlineVariables(getFirstBillOfMaterials().getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTPRequest.build(requestEntity).delete().getStatusCode());
    }

    /**
     * Get the first Bill of Material
     *
     * @return Bill of Materials Response instance
     */
    protected static BillOfMaterialsResponse getFirstBillOfMaterials() {
        List<BillOfMaterialsResponse> billOfMaterialResponses =  getAllBillOfMaterials();

        return billOfMaterialResponses.get(0);
    }

    /**
     * Get All Bill of Materials
     *
     * @return bill of materials object
     */
    protected static List<BillOfMaterialsResponse> getAllBillOfMaterials() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS, BillOfMaterialsItemsResponse.class);

        ResponseWrapper<BillOfMaterialsItemsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity()
            .getItems();
    }

    /**
     * Delete a Bill of Materials in UI tests
     *
     * @param identity - the identity
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> deleteBillOfMaterialByIdUi(final String identity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                .inlineVariables(identity).token(new JwtTokenUtil().retrieveJwtToken());

        return HTTPRequest.build(requestEntity).delete();
    }
}
