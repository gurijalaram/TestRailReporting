package com.apriori.web.app.util.login;

import com.apriori.shared.util.file.user.UserCredentials;

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

    <T> T performLogin(String username, String password, Class<T> klass);

    void performLoginVoid(String username, String password);

    void failedLoginEmptyFieldsNoReturn();

    <T> T forgottenPassword(Class<T> klass);

    <T> T submitEmailForgotPwd(String username, Class<T> klass);

    String getLoginTitle();

    String getLoginErrorMessage();

    boolean isLogoDisplayed();

    String getWelcomeText();

    <T> T privacyPolicy(Class<T> klass);
}
