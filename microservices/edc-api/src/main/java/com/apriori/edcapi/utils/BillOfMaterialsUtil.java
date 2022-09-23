package com.apriori.edcapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterialsItemsResponse;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterialsResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

import java.util.List;

public class BillOfMaterialsUtil extends TestUtil {

    /**
     * Create Bill of Materials
     *
     * @param fileName - the file name
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> postBillOfMaterials(String fileName) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.POST_BILL_OF_MATERIALS, BillOfMaterialsResponse.class)
                .multiPartFiles(new MultiPartFiles()
                    .use("multiPartFile", FileResourceUtil.getResourceAsFile(fileName)))
                .queryParams(new QueryParams()
                    .use("type", "pcba"));

        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Delete Bill of Materials by Identity
     *
     * @param billOfMaterialsId - Bill of Material Id
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> deleteBillOfMaterialById(String billOfMaterialsId) {
        RequestEntity requestEntity = genericRequest(billOfMaterialsId, EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Get All Bill of Materials
     *
     * @return response object
     */
    protected static List<BillOfMaterialsResponse> getAllBillOfMaterials() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS, BillOfMaterialsItemsResponse.class);

        ResponseWrapper<BillOfMaterialsItemsResponse> getAllResponse = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, getAllResponse.getStatusCode());

        return getAllResponse.getResponseEntity().getItems();
    }

    /**
     * Delete a Bill of Materials in UI tests
     *
     * @param billOfMaterialsId - the bill of material identity
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> deleteBillOfMaterialByIdUi(final String billOfMaterialsId) {
        RequestEntity requestEntity = genericRequest(billOfMaterialsId, EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
            .token(new AuthorizationUtil().getTokenAsString());

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Get Bill of Materials by Id
     *
     * @param identity - the identity
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> getBillOfMaterialById(String identity) {
        return getBillOfMaterialById(identity, BillOfMaterialsResponse.class);
    }

    /**
     * Get Bill of Materials by Id
     *
     * @param klass    - the klass
     * @param identity - the identity
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> getBillOfMaterialById(String identity, Class klass) {
        RequestEntity requestEntity = genericRequest(identity, EDCAPIEnum.GET_BILL_OF_MATERIALS_BY_IDENTITY, klass);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Post Export a BOM as a CSV file
     *
     * @param billOfMaterialsId - the bill of material identity
     * @return - response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> postExportBomAsCsvFile(String billOfMaterialsId) {
        RequestEntity requestEntity = genericRequest(billOfMaterialsId, EDCAPIEnum.POST_BILL_OF_MATERIALS_IDENTITY_TO_EXPORT, null);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * This is a generic request
     *
     * @param identity   - the identity
     * @param edcApiEnum - the EDCApiEnum
     * @return response object
     */
    protected static RequestEntity genericRequest(String identity, EDCAPIEnum edcApiEnum, Class klass) {
        return RequestEntityUtil.init(edcApiEnum, klass)
            .inlineVariables(identity);
    }
}
