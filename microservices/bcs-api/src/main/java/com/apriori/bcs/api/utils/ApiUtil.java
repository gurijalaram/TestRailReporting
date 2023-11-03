package com.apriori.bcs.api.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ApiUtil {

    /*
    The getPropertyValue method uses reflection to retrieve any Property on any Api response
    For example, if you wanted to get the identity value for a customer, you would pass in an instance
    of the customer POJO, the customer class and the getter signature, which would like be getIdentity.
     */
    public static String getPropertyValue(Object obj, Class klass, String method) throws IllegalAccessException,
        NoSuchMethodException, InvocationTargetException {
        Class[] emptyParameterSet = {};
        Method getId = klass.getDeclaredMethod(method, emptyParameterSet);
        Object value = getId.invoke(obj);
        return (String) value;
    }

}