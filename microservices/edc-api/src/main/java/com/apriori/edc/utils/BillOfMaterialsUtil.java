package com.apriori.edc.utils;

import com.apriori.edc.enums.EDCAPIEnum;
import com.apriori.edc.models.response.bill.of.materials.BillOfMaterialsItemsResponse;
import com.apriori.edc.models.response.bill.of.materials.BillOfMaterialsResponse;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.MultiPartFiles;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.http.utils.TestUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import org.apache.http.HttpStatus;

import java.util.List;

public class BillOfMaterialsUtil extends TestUtil {
    public UserCredentials currentUser = UserUtil.getUser();

    /**
     * Create Bill of Materials
     *
     * @param fileName - the file name
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> postBillOfMaterials(String fileName) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.BILL_OF_MATERIALS, BillOfMaterialsResponse.class)
                .multiPartFiles(new MultiPartFiles()
                    .use("multiPartFile", FileResourceUtil.getResourceAsFile(fileName)))
                .queryParams(new QueryParams()
                    .use("type", "pcba"))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Create Bill of Materials
     *
     * @param fileName - the file name
     * @return response object
     */
    // TODO z: fix threads
    public static ResponseWrapper<BillOfMaterialsResponse> postBillOfMaterialsWithToken(String fileName, String token) {
        final RequestEntity requestEntity =
            new RequestEntity().endpoint(EDCAPIEnum.BILL_OF_MATERIALS).returnType(BillOfMaterialsResponse.class)
                .token(token)
                .multiPartFiles(new MultiPartFiles()
                    .use("multiPartFile", FileResourceUtil.getResourceAsFile(fileName)))
                .queryParams(new QueryParams()
                    .use("type", "pcba"))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).postMultipart();
    }


    /**
     * Delete Bill of Materials by Identity
     *
     * @param billOfMaterialsId - Bill of Material Id
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> deleteBillOfMaterialById(String billOfMaterialsId) {
        RequestEntity requestEntity = genericRequest(billOfMaterialsId, EDCAPIEnum.BILL_OF_MATERIALS_BY_IDENTITY, null)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Get All Bill of Materials
     *
     * @return response object
     */
    protected static List<BillOfMaterialsResponse> getAllBillOfMaterials() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.BILL_OF_MATERIALS, BillOfMaterialsItemsResponse.class)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BillOfMaterialsItemsResponse> getAllResponse = HTTPRequest.build(requestEntity).get();

        return getAllResponse.getResponseEntity().getItems();
    }

    /**
     * Delete a Bill of Materials in UI tests
     *
     * @param billOfMaterialsId - the bill of material identity
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> deleteBillOfMaterialByIdUi(final String billOfMaterialsId, UserCredentials currentUser) {
        RequestEntity requestEntity = genericRequest(billOfMaterialsId, EDCAPIEnum.BILL_OF_MATERIALS_BY_IDENTITY, null)
            .token(currentUser.getToken());

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Get Bill of Materials by Id
     *
     * @param identity - the identity
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> getBillOfMaterialById(String identity) {
        return getBillOfMaterialById(identity, BillOfMaterialsResponse.class, HttpStatus.SC_OK);
    }

    /**
     * Get Bill of Materials by Id
     *
     * @param klass    - the klass
     * @param identity - the identity
     * @return response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> getBillOfMaterialById(String identity, Class klass, Integer expectedResponseCode) {
        RequestEntity requestEntity = genericRequest(identity, EDCAPIEnum.BILL_OF_MATERIALS_BY_IDENTITY, klass)
            .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Post Export a BOM as a CSV file
     *
     * @param billOfMaterialsId - the bill of material identity
     * @return - response object
     */
    public static ResponseWrapper<BillOfMaterialsResponse> postExportBomAsCsvFile(String billOfMaterialsId) {
        RequestEntity requestEntity = genericRequest(billOfMaterialsId, EDCAPIEnum.BILL_OF_MATERIALS_BY_IDENTITY_TO_EXPORT, null)
            .expectedResponseCode(HttpStatus.SC_OK);

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
