package com.apriori.cidappapi.utils;

import com.apriori.cidappapi.enums.CidAppAPIEnum;
import com.apriori.cidappapi.models.response.PeopleResponse;
import com.apriori.cidappapi.models.response.PersonResponse;
import com.apriori.cus.enums.CusAppAPIEnum;
import com.apriori.cus.models.response.User;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;

public class PeopleUtil {

    /**
     * GET current user
     *
     * @param userCredentials - the user credentials
     * @return user object
     */
    public User getCurrentUser(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CusAppAPIEnum.CURRENT_USER, User.class)
            .token(userCredentials.getToken());

        ResponseWrapper<User> userResponse = HTTPRequest.build(requestEntity).get();
        return userResponse.getResponseEntity();
    }

    /**
     * GET current person
     *
     * @param userCredentials - the user credentials
     * @return person object
     */
    public PersonResponse getCurrentPerson(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CidAppAPIEnum.CURRENT_PERSON, PeopleResponse.class)
            .token(userCredentials.getToken())
            .inlineVariables(userCredentials.getUsername());

        ResponseWrapper<PeopleResponse> peopleResponse = HTTPRequest.build(requestEntity).get();
        return peopleResponse.getResponseEntity().getItems().get(0);
    }
}
