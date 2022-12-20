package com.apriori.acs.entity.response.acs.tolerancepolicydefaults;

import com.apriori.acs.entity.response.acs.genericclasses.GenericPropertyInfoItem;

import lombok.Data;

@Data
public class PropertyInfoItem extends GenericPropertyInfoItem {
    private String supportedSerializedType;
}
