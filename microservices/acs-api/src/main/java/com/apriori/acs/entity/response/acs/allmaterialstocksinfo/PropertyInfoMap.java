package com.apriori.acs.entity.response.acs.allmaterialstocksinfo;

import com.apriori.acs.entity.response.acs.genericclasses.GenericExtendedPropertyInfoItem;

import lombok.Data;

@Data
public class PropertyInfoMap {
    private GenericExtendedPropertyInfoItem costPerKG;
    private GenericExtendedPropertyInfoItem hardnessSystem;
    private GenericExtendedPropertyInfoItem thickness;
    private GenericExtendedPropertyInfoItem length;
    private GenericExtendedPropertyInfoItem description;
    private GenericExtendedPropertyInfoItem materialStockCarbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem baseCostPerUnit;
    private GenericExtendedPropertyInfoItem hardness;
    private GenericExtendedPropertyInfoItem formName;
    private GenericExtendedPropertyInfoItem name;
    private GenericExtendedPropertyInfoItem width;
    private GenericExtendedPropertyInfoItem sourceName;
    private GenericExtendedPropertyInfoItem costUnits;
    private GenericExtendedPropertyInfoItem dataSource;
    private GenericExtendedPropertyInfoItem costPerUnit;
}
