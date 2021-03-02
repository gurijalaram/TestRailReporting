package com.apriori.utils;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.entity.reponse.PostComponentResponse;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

public class CidAppTestUtil {

    private static String token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
        Constants.getCidServiceHost(),
        HttpStatus.SC_CREATED,
        Constants.getCidTokenUsername(),
        Constants.getCidTokenEmail(),
        Constants.getCidTokenIssuer(),
        Constants.getCidTokenSubject());

    /**
     * Adds a new component
     *
     * @param url          - the url
     * @param scenarioName - the scenario name
     * @param partName     - the part name
     * @return responsewrapper
     */
    public ResponseWrapper<PostComponentResponse> postComponents(String url, String scenarioName, String processGroup, String partName) {
        RequestEntity requestEntity = RequestEntity.init(url, PostComponentResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(processGroup), partName)))
            .setFormParams(new FormParams().use("filename", partName)
                .use("override", "false")
                .use("scenarioName", scenarioName));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * Find components for the current user matching a specified query.
     *
     * @param url   - the url
     * @param klass - the class
     * @return responsewrapper
     */
    public <T> ResponseWrapper<T> getComponents(String url, Class klass) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.get(requestEntity, new RequestAreaApi());
    }
}
