package com.apriori.cis.utils;

import com.apriori.apibase.services.cis.CisUtils;
import com.apriori.apibase.services.cis.objects.Part;
import com.apriori.apibase.services.cis.objects.PartCosting;
import com.apriori.apibase.services.cis.objects.Parts;
import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PartResources extends CisBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PartResources.class);

    private static final String endpointParts = "parts";
    private static final String endpointPartsWithIdentity = "parts/%s";
    APIAuthentication apiAuthentication = new APIAuthentication();

    public static <T> ResponseWrapper<T> getParts() {
        String url = String.format(getCisUrl(), endpointParts);
        return GenericRequestUtil.get(
            RequestEntity.init(url, Parts.class),
            new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> getPartRepresentation(String identity) {
        String url = String.format(getCisUrl(), String.format(endpointPartsWithIdentity, identity));
        return GenericRequestUtil.get(
            RequestEntity.init(url, Part.class),
            new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> getPartCosting(String partIdentity) {
        String url = String.format(getCisUrl(), String.format(endpointPartsWithIdentity, partIdentity).concat(
            "/results"));
        return GenericRequestUtil.get(
            RequestEntity.init(url, PartCosting.class),
            new RequestAreaApi()
        );
    }

    public static Part createNewPart(Object obj) {
        NewPartRequest npr = (NewPartRequest) obj;
        String url = String.format(getCisUrl(), endpointParts);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data");
        RequestEntity requestEntity = RequestEntity.init(url, Part.class)
            .setHeaders(headers)
            .setMultiPartFiles(new MultiPartFiles()
                .use("data", FileResourceUtil.getLocalResourceFile(npr.getFilename()))
            )
            .setFormParams(new FormParams()
                .use("filename", npr.getFilename())
                .use("externalId", String.format(npr.getExternalId(), System.currentTimeMillis()))
                .use("AnnualVolume", npr.getAnnualVolume().toString())
                .use("BatchSize", npr.getBatchSize().toString())
                .use("Description", npr.getDescription())
                .use("PinnedRouting", npr.getPinnedRouting())
                .use("ProcessGroup", npr.getProcessGroup())
                .use("ProductionLife", npr.getProductionLife().toString())
                .use("ScenarioName", npr.getScenarioName())
                .use("Udas", npr.getUdas())
                .use("VpeName", npr.getVpeName())
                .use("MaterialName", npr.getMaterialName()));


        return (Part) GenericRequestUtil.postMultipart(requestEntity, new RequestAreaApi()).getResponseEntity();
    }

    public static Boolean isPartComplete(String partIdentity) {
        Object partDetails;
        Boolean isPartComplete = false;
        int count = 0;
        while (count <= 18) {
            partDetails = PartResources.getPartRepresentation(partIdentity).getResponseEntity();
            isPartComplete = CisUtils.pollState(partDetails, Part.class);
            count += 1;
        }

        return isPartComplete;
    }
}