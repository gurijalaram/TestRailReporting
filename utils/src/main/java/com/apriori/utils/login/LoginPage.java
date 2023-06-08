package com.apriori.utils.login;

import com.apriori.utils.reader.file.user.UserCredentials;

public interface LoginPage {
    /**
     * Interface method that login pages implement to perform login
     *
     * @param userCredentials - users
     * @param klass - class to return an instance of
     * @return returns an instance of specified class
     * @param <T> - specified class which to return
     */
    <T> T performLogin(final UserCredentials userCredentials, Class<T> klass);
}
