package com.apriori.cid.api.utils;

import com.apriori.bcm.api.enums.CusAppAPIEnum;
import com.apriori.cid.api.enums.CidAppAPIEnum;
import com.apriori.cid.api.models.response.PeopleResponse;
import com.apriori.cid.api.models.response.PersonResponse;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.User;

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
