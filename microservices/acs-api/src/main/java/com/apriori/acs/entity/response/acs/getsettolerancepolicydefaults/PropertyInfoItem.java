package com.apriori.acs.entity.response.acs.getsettolerancepolicydefaults;

import lombok.Data;

@Data
public class PropertyInfoItem {
    private String name;
    private String unitTypeName;
    private String supportedSerializedType;
}