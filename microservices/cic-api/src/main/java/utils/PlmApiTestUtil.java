package utils;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.properties.PropertiesContext;

import entity.request.PlmFieldDefinitions;
import entity.response.PlmCsrfToken;
import entity.response.PlmPartResponse;
import entity.response.PlmSearchPart;
import entity.response.PlmSearchResponse;
import enums.PlmApiEnum;
import enums.PlmPartsSearch;
import enums.PlmWCType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
public class PlmApiTestUtil {

    private PlmPartResponse plmPartResponse;
    private PlmCsrfToken plmCsrfToken;

    /**
     * Get Part Information using PLM Product Management Parts API.
     *
     * @param plmPartId Plm Part ID (Example : OR:wt.part.WTPart:17375584)
     * @return PlmApiTestUtil
     */
    public PlmApiTestUtil getPartInfoFromPlm(String plmPartId) {
        RequestEntity requestEntity = RequestEntityUtil.init(PlmApiEnum.PLM_WC_PROD_MGMT_PARTS, PlmPartResponse.class)
            .inlineVariables(plmPartId)
            .headers(setupHeader())
            .expectedResponseCode(HttpStatus.SC_OK);
        plmPartResponse = (PlmPartResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
        if (plmPartResponse == null) {
            throw new NullPointerException(String.format("PART NOT FOUND IN PLM SYSTEM WITH PART ID: (%s)", plmPartId));
        }
        return this;
    }

    /**
     * Update the attribute value in plm system
     *
     * @param plmPartId            plm part number
     * @param plmFieldDataToUpdate PlmFieldDefinitions
     * @param updateNote           updated Note
     * @return PlmPartResponse
     */
    public PlmPartResponse updatePartInfoToPlm(String plmPartId, PlmFieldDefinitions plmFieldDataToUpdate, String updateNote) {
        return plmCsrfToken()
            .getPartInfoFromPlm(plmPartId)
            .plmPartCheckout(updateNote)
            .patchPartToPlm(plmFieldDataToUpdate)
            .plmPartCheckin(updateNote)
            .getPlmPartResponse();
    }


    /**
     * get Csrf Token
     *
     * @return PlmCsrfToken
     */
    public PlmCsrfToken getCsrfToken() {
        return this.plmCsrfToken;
    }

    /**
     * getter for PlmPartResponse
     *
     * @return PlmPartResponse
     */
    public PlmPartResponse getPlmPartResponse() {
        return plmPartResponse;
    }

    /**
     * Get PLM CSRF Token
     *
     * @return PlmApiTestUtil class object
     */
    public PlmApiTestUtil plmCsrfToken() {
        RequestEntity requestEntity = RequestEntityUtil.init(PlmApiEnum.PLM_WC_CSRF_TOKEN, PlmCsrfToken.class)
            .headers(new HashMap<String, String>() {
                {
                    put("Authorization", "Basic " + PropertiesContext.get("ci-connect.${ci-connect.agent_type}.host_token"));
                }
            }).expectedResponseCode(HttpStatus.SC_OK);
        plmCsrfToken = (PlmCsrfToken) HTTPRequest.build(requestEntity).get().getResponseEntity();
        if (plmCsrfToken == null) {
            throw new NullPointerException("FAILED TO GET CSRF NONCE TOKEN!!");
        }
        return this;
    }

    /**
     * search and return single Plm Windchill Part
     *
     * @param plmPartNumber
     * @return PlmPart response class
     */
    @SneakyThrows
    public PlmSearchPart getPlmPartByPartNumber(String plmPartNumber) {
        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(), plmPartNumber))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchResponse plmParts = CicApiTestUtil.searchPlmWindChillParts(searchFilter);
        PlmSearchPart plmPart;
        if (plmParts.getItems().size() > 1) {
            plmPart = plmParts
                .getItems()
                .get(new Random().nextInt(plmParts.getItems().size()));
        } else {
            plmPart = plmParts.getItems().get(0);
        }
        if (plmPart == null) {
            throw new IllegalArgumentException(String.format("PARTS NOT FOUND IN PLM SYSTEM WITH PART NUMBER >>%s<<", plmPartNumber));
        }
        return plmPart;
    }

    /**
     * update part information back to plm system
     *
     * @param plmFieldData
     * @return current class object
     */
    private PlmApiTestUtil patchPartToPlm(PlmFieldDefinitions plmFieldData) {
        RequestEntity requestEntity = RequestEntityUtil.init(PlmApiEnum.PLM_WC_PROD_MGMT_PARTS, PlmPartResponse.class)
            .inlineVariables(plmPartResponse.getVersionID())
            .headers(setupHeader())
            .body(plmFieldData)
            .expectedResponseCode(HttpStatus.SC_OK);
        plmPartResponse = (PlmPartResponse) HTTPRequest.build(requestEntity).patch().getResponseEntity();
        if (plmPartResponse == null) {
            throw new NullPointerException(String.format("PART NOT FOUND IN PLM SYSTEM TO UPDATE WITH PART ID: (%s)", plmPartResponse.getId()));
        }
        return this;
    }

    /**
     * Plm API to check-in the part updated.
     *
     * @param checkInNote
     * @return current class object
     */
    private PlmApiTestUtil plmPartCheckin(String checkInNote) {
        RequestEntity requestEntity = RequestEntityUtil.init(PlmApiEnum.PLM_WC_PROD_MGMT_PART_CHECKIN, PlmPartResponse.class)
            .inlineVariables(plmPartResponse.getVersionID())
            .headers(setupHeader())
            .customBody(String.format("{\"CheckInNote\" : \"%s\"}", checkInNote))
            .expectedResponseCode(HttpStatus.SC_OK);
        plmPartResponse = (PlmPartResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
        if (plmPartResponse == null & !plmPartResponse.getCheckoutState().contains("in")) {
            throw new NullPointerException(String.format("PART NOT CHECKED-IN FROM PLM SYSTEM WITH PART ID: (%s)", plmPartResponse.getId()));
        }
        return this;
    }

    /**
     * Plm APi to checkout the part for update
     *
     * @param note checkout nate
     * @return current class object
     */
    private PlmApiTestUtil plmPartCheckout(String note) {
        if (plmPartResponse.getCheckoutState().contains("out")) {
            plmPartCheckin(note);
        }
        RequestEntity requestEntity = RequestEntityUtil.init(PlmApiEnum.PLM_WC_PROD_MGMT_PART_CHECKOUT, PlmPartResponse.class)
            .inlineVariables((plmPartResponse.getVersionID() == null) ? plmPartResponse.getId() : plmPartResponse.getVersionID())
            .headers(setupHeader())
            .customBody(String.format("{\"CheckOutNote\" : \"%s\"}", note))
            .expectedResponseCode(HttpStatus.SC_OK);

        plmPartResponse = (PlmPartResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();

        if (plmPartResponse == null & !plmPartResponse.getCheckoutState().contains("out")) {
            throw new RuntimeException(String.format("PART NOT CHECKED-OUT FROM PLM SYSTEM WITH PART ID: (%s)", plmPartResponse.getId()));
        }
        return this;
    }

    /**
     * Get encoded string for plm api authorization
     *
     * @return encoded credentials
     */
    private String getPlmEncodedCredential() {
        String plmCredential = PropertiesContext.get("ci-connect.windchill.username") + ":" + PropertiesContext.get("ci-connect.windchill.password");
        return Base64.getEncoder().encodeToString(plmCredential.getBytes());
    }

    /**
     * setup header for plm api
     *
     * @return header map.
     */
    private Map<String, String> setupHeader() {
        return new HashMap<String, String>() {
            {
                put("Authorization", "Basic " + PropertiesContext.get("ci-connect.${ci-connect.agent_type}.host_token"));
                put("CSRF_NONCE", plmCsrfToken.getNonceValue());
            }
        };
    }
}
