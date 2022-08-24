package com.apriori.edcapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.request.PartsRequest;
import com.apriori.edcapi.entity.response.parts.Parts;
import com.apriori.edcapi.entity.response.parts.PartsResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import org.apache.http.HttpStatus;

public class PartsUtil extends TestUtil {

    /**
     * List the parts in a line item matching a specified query
     *
     * @param bomIdentity      - bill of material identity
     * @param lineItemIdentity - line of item identity
     * @return response object
     */
    public PartsResponse getAllPartsInLineItem(String bomIdentity, String lineItemIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS_LINE_ITEMS_PARTS, PartsResponse.class)
                .inlineVariables(bomIdentity, lineItemIdentity);

        ResponseWrapper<PartsResponse> getAllPartsResponse = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, getAllPartsResponse.getStatusCode());

        return getAllPartsResponse.getResponseEntity();
    }

    /**
     * Post new part to the Line item
     *
     * @param bomIdentity      - the bom identity
     * @param lineItemIdentity - the line item identity
     * @return parts response object
     */
    public ResponseWrapper<Parts> postNewPartToLineItem(String bomIdentity, String lineItemIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.POST_BILL_OF_MATERIALS_LINE_ITEMS_PARTS, Parts.class)
                .inlineVariables(bomIdentity, lineItemIdentity)
                .body(partsInfoBody());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Patch update a part
     *
     * @param bomIdentity      - the bom identity
     * @param lineItemIdentity - the line item identity
     * @param partIdentity     - the part identity
     * @return parts response object
     */
    public ResponseWrapper<Parts> patchUpdatePart(String bomIdentity, String lineItemIdentity, String partIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.PATCH_BILL_OF_MATERIALS_LINE_ITEMS_PARTS, Parts.class)
                .inlineVariables(bomIdentity, lineItemIdentity, partIdentity)
                .body(partsInfoBody());

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Select a part for export
     *
     * @param bomIdentity      - the bom identity
     * @param lineItemIdentity - the line item identity
     * @param partIdentity     - the part identity
     * @return parts response object
     */
    public ResponseWrapper<Parts> postSelectPartForExport(String bomIdentity, String lineItemIdentity, String partIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.POST_BILL_OF_MATERIALS_LINE_ITEMS_PARTS_FOR_EXPORT, null)
                .inlineVariables(bomIdentity, lineItemIdentity, partIdentity);

        return HTTPRequest.build(requestEntity).post();
    }

    public ResponseWrapper<Parts> postCostParts(String bomIdentity, String lineItemIdentity, String partIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.POST_BILL_OF_MATERIALS_LINE_ITEMS_PARTS_FOR_COST, null)
                .inlineVariables(bomIdentity, lineItemIdentity)
                .body(partIdentity);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * This method has a json file to input info for the parts body
     *
     * @return response object
     */
    private PartsRequest partsInfoBody() {
        return JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "CreatePartData.json"
            ).getPath(), PartsRequest.class);
    }
}
