package utils;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.properties.PropertiesContext;

import entity.response.PlmCsrfToken;
import entity.response.PlmPartResponse;
import enums.PlmApiEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
public class PlmApiTestUtil {

    /**
     * Get PLM CSRF Token
     *
     * @return PlmCsrfToken class object
     */
    public static PlmCsrfToken getPlmCsrfToken() {
        RequestEntity requestEntity = RequestEntityUtil.init(PlmApiEnum.PLM_WC_CSRF_TOKEN, PlmCsrfToken.class)
            .headers(new HashMap<String, String>() {
                {
                    put("Authorization", "Basic " + PropertiesContext.get("ci-connect.${ci-connect.agent_type}.host_token"));
                }
            }).expectedResponseCode(HttpStatus.SC_OK);
        return (PlmCsrfToken) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get Part Information using PLM Product Management Parts API.
     *
     * @param plmPartId    Plm Part ID (Example : OR:wt.part.WTPart:17375584)
     * @param plmCsrfToken plf Csrf Token
     * @return PlmPartResponse
     */
    public static PlmPartResponse getPlmPart(String plmPartId, String plmCsrfToken) {
        RequestEntity requestEntity = RequestEntityUtil.init(PlmApiEnum.PLM_WC_PROD_MGMT_PARTS, PlmPartResponse.class)
            .inlineVariables(plmPartId)
            .headers(new HashMap<String, String>() {
                {
                    put("Authorization", "Basic " + PropertiesContext.get("ci-connect.${ci-connect.agent_type}.host_token"));
                    put("CSRF_NONCE", plmCsrfToken);
                }
            }).expectedResponseCode(HttpStatus.SC_OK);
        PlmPartResponse plmPartResponse = (PlmPartResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
        if (plmPartResponse == null) {
            throw new NullPointerException(String.format("PART NOT FOUND IN PLM SYSTEM WITH PART ID: (%s)", plmPartId));
        }
        return plmPartResponse;
    }
}
