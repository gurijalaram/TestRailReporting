package com.apriori.apibase.services.cis.apicalls;

import com.apriori.apibase.services.cis.objects.Part;
import com.apriori.apibase.services.cis.objects.PartCosting;
import com.apriori.apibase.services.cis.objects.Parts;
import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PartResources extends CisBase {

    private static final String endpointParts = "parts";
    private static final String endpointPartsWithIdentity = "parts/%s";

    public static <T> ResponseWrapper<T> getParts() {
        String url = String.format(getCisUrl(), endpointParts);
        return GenericRequestUtil.get(
                RequestEntity.init(url, Parts.class),
                new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> getPartRepresentation() {
        String url = String.format(getCisUrl(), String.format(endpointPartsWithIdentity, Constants.getCisPartIdentity()));
        return GenericRequestUtil.get(
                RequestEntity.init(url, Part.class),
                new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> getPartCosting() {
        String url = String.format(getCisUrl(), String.format(endpointPartsWithIdentity, Constants.getCisPartIdentity()).concat(
                "/results"));
        return GenericRequestUtil.get(
                RequestEntity.init(url, PartCosting.class),
                new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> createNewPart(Object obj) {
        NewPartRequest npr = (NewPartRequest)obj;
        String url = String.format(getCisUrl(), endpointParts);

        Map<String, String> headers = new HashMap<String, String>();
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


        return GenericRequestUtil.postMultipart(requestEntity, new RequestAreaApi());
    }
}