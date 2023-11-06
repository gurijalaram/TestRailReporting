package com.apriori.acs.api.models.response.acs.genericclasses;

import lombok.Data;

@Data
public class GenericExtendedPropertyInfoItem extends GenericPropertyInfoItem {
    private String supportedSerializedType;
}
