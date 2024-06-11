package com.apriori.cds.api.utils;

import com.apriori.cds.api.enums.ApplicationEnum;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Applications;

import org.apache.http.HttpStatus;

public class ApplicationUtil {
    /**
     * Calls an API with GET verb
     *
     * @param applicationCloudReference - the application cloud reference
     * @return string
     */
    public String getApplicationIdentity(ApplicationEnum applicationCloudReference) {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(CDSAPIEnum.APPLICATIONS, Applications.class)
            .queryParams(new QueryParams().use("cloudReference[EQ]", applicationCloudReference.getApplication()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Applications> response = HTTPRequest.build(requestEntity).get();
        return response.getResponseEntity().getItems().stream().findFirst().get().getIdentity();
    }
}
