package main.java.http.builder.service;

import main.java.http.builder.common.entity.RequestEntity;
import main.java.http.builder.common.entity.UserAuthenticationEntity;
import main.java.http.builder.dao.ConnectionManager;
import main.java.utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//INFO z: we may add new functionality in a feature.
// for now it's working like:
//    requestService.customizeRequest().set().set().commitChanges().useNewFunctionalityInThisService().connect()
public class RequestInitService {

    private RequestEntity requestEntity;

    public static RequestInitService build(RequestEntity requestEntity) {
        return new RequestInitService(requestEntity);
    }

    private RequestInitService(RequestEntity requestEntity) {
        requestEntity.setRequestInitService(this);
        this.requestEntity = requestEntity;
    }

    public RequestEntity customizeRequest() {
        return requestEntity;
    }

    public ConnectionManager<?> connect() {
        return new ConnectionManager<>(this.requestEntity, this.requestEntity.getReturnType());
    }
}
