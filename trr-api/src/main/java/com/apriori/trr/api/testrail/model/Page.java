package com.apriori.trr.api.testrail.model;

import com.apriori.trr.api.testrail.internal.PageDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PageDeserializer.class)
public class Page<T> {

    public int offset;

    public int limit;

    public int size;

    public Links links;

    public T objects;
}
