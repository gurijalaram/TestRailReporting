package com.apriori.edc.api.utils;

import com.apriori.edc.api.enums.EDCAPIEnum;
import com.apriori.edc.api.models.request.PartsRequest;
import com.apriori.edc.api.models.response.parts.Parts;
import com.apriori.edc.api.models.response.parts.PartsResponse;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.json.JsonManager;

import org.apache.http.HttpStatus;

import java.util.List;

public class PartsUtil extends TestUtil {

    /**
     * This method has a json file to input info for the parts body
     *
     * @return response object
     */
    private static PartsRequest partsInfoBody() {
        return JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreatePartData.json").getPath(), PartsRequest.class);
    }

    /**
     * List the parts in a line item matching a specified query
     *
     * @param bomIdentity      - bill of material identity
     * @param lineItemIdentity - line of item identity
     * @return response object
     */
    public PartsResponse getAllPartsInLineItem(String bomIdentity, String lineItemIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.BILL_OF_MATERIALS_LINE_ITEMS_PARTS, PartsResponse.class)
                .inlineVariables(bomIdentity, lineItemIdentity)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<PartsResponse> getAllPartsResponse = HTTPRequest.build(requestEntity).get();

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
            RequestEntityUtil.init(EDCAPIEnum.BILL_OF_MATERIALS_LINE_ITEMS_PARTS, Parts.class)
                .inlineVariables(bomIdentity, lineItemIdentity)
                .body(partsInfoBody())
                .expectedResponseCode(HttpStatus.SC_CREATED);

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
            RequestEntityUtil.init(EDCAPIEnum.BILL_OF_MATERIALS_LINE_ITEMS_PARTS_FOR_UPDATE, Parts.class)
                .inlineVariables(bomIdentity, lineItemIdentity, partIdentity)
                .body(partsInfoBody())
                .expectedResponseCode(HttpStatus.SC_OK);

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
            RequestEntityUtil.init(EDCAPIEnum.BILL_OF_MATERIALS_LINE_ITEMS_PARTS_FOR_EXPORT, null)
                .inlineVariables(bomIdentity, lineItemIdentity, partIdentity)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Method to select Part or Parts to cost
     *
     * @param bomIdentity      - the bom identity
     * @param lineItemIdentity - the line item identity
     * @param partIdentity     - the part identity
     * @return parts response object
     */
    public ResponseWrapper<Parts> postSelectPartsToCost(String bomIdentity, String lineItemIdentity, List<String> partIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.BILL_OF_MATERIALS_LINE_ITEMS_PARTS_FOR_COST, null)
                .inlineVariables(bomIdentity, lineItemIdentity)
                .body(partIdentity)
                .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).post();
    }
}