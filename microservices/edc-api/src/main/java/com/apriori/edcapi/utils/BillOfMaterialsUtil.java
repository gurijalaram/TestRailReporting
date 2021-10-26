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

    private static String bomIdentity;

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

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, HTTPRequest.build(requestEntity).post().getStatusCode());

        return (BillOfMaterialsResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Delete Bill of Materials by Identity
     *
     * @param billOfMaterialsId - Bill of Material Id
     */
    public static void deleteBillOfMaterialById(String billOfMaterialsId) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                .inlineVariables(billOfMaterialsId);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTPRequest.build(requestEntity).delete().getStatusCode());
    }

    /**
     * Get the first Bill of Material
     *
     * @return Bill of Materials Response instance
     */
    protected static String getFirstBillOfMaterials() {
        List<BillOfMaterialsResponse> billOfMaterialResponses =  getAllBillOfMaterials();

        return billOfMaterialResponses.get(0).getIdentity();
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

    /**
     * Get Bill of Materials by Id
     *
     * @param identity - the identity
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> getBillOfMaterialById(String identity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS_BY_IDENTITY, BillOfMaterialsResponse.class)
            .inlineVariables(identity);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTPRequest.build(requestEntity).get().getStatusCode());

        return HTTPRequest.build(requestEntity).get();
    }

    public static String getBomIdentity() {
        if (bomIdentity == null) {
            bomIdentity = getAllBillOfMaterials().get(0).getIdentity();
        }
        return bomIdentity;
    }
}
