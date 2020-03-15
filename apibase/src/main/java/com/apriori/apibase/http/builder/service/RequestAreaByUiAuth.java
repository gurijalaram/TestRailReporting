package com.apriori.apibase.http.builder.service;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.interfaces.RequestArea;
import com.apriori.apibase.utils.ResponseWrapper;
import com.apriori.apibase.utils.WebDriverUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestAreaByUiAuth implements RequestArea {

    @Override
    public <T> ResponseWrapper<T> get(RequestEntity requestEntity) {
        return RequestInitService.build(authValidation(requestEntity))
                .connect()
                .get();
    }

    @Override
    public <T> ResponseWrapper<T> post(RequestEntity requestEntity) {
        return RequestInitService.build(authValidation(requestEntity))
                .connect()
                .post();
    }

    @Override
    public <T> ResponseWrapper<T> postMultipart(RequestEntity requestEntity) {
        return RequestInitService.build(authValidation(requestEntity))
                .connect()
                .postMultiPart();
    }

    @Override
    public <T> ResponseWrapper<T> put(RequestEntity requestEntity) {
        return RequestInitService.build(authValidation(requestEntity))
                .connect()
                .put();
    }

    @Override
    public <T> ResponseWrapper<T> delete(RequestEntity requestEntity) {
        return RequestInitService.build(authValidation(requestEntity))
                .connect()
                .delete();
    }

    @Override
    public <T> ResponseWrapper<T> patch(RequestEntity requestEntity) {
        return RequestInitService.build(authValidation(requestEntity))
                .connect()
                .patch();
    }


    private RequestEntity authValidation(RequestEntity requestEntity) {
        if(requestEntity.getHeaders() == null || requestEntity.getHeaders().size() == 0) {
            requestEntity.setHeaders(doUiAuth(requestEntity));
        }

        return requestEntity;
    }

    private Map<String, String> doUiAuth(RequestEntity requestEntity) {

        String token = new WebDriverUtils()
                .getToken(requestEntity.getUserAuthenticationEntity().getEmailAddress(),
                        requestEntity.getUserAuthenticationEntity().getPassword()
                );

        return new HashMap<String, String>() {{
            put("Authorization", "Bearer " + token);
//            put("ap-cloud-context", "EDC");
        }};
    }
}
