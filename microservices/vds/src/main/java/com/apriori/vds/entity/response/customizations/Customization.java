package com.apriori.vds.entity.response.customizations;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;
import com.apriori.vds.entity.response.access.control.AccessControlPermission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Schema(location = "vds/CustomizationResponse.json")
@Data
@JsonRootName(value = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customization {
    private String attributes;
    private String createdBy;
    private String customerIdentity;
    private String deletedBy;
    private String description;
    private String groupId;
    private String identity;
    private List<String> members;
    private String name;
    private String parentGroupIdentity;
    private List<AccessControlPermission> permissions;
    private Boolean systemGroup;
    private String type;
    private String updatedBy;


    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private String createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private String deletedAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private String updatedAt;
}



//"customAttributes": [],
//  "customerIdentity": "string",
//  "digitalFactories": [],
//  "exchangeRates": [],
//  "identity": "string",
//  "processGroups": [],
//  "siteVariables": []






//"customAttributes": [
//    {
//      "createdAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "createdBy": "string",
//      "customerIdentity": "string",
//      "defaultValue": "string",
//      "deletedAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "deletedBy": "string",
//      "displayName": "string",
//      "identity": "string",
//      "multiSelect": true,
//      "name": "string",
//      "options": [
//        "string"
//      ],
//      "ordinal": 0,
//      "precision": 0,
//      "requiredAttributeType": "NEVER",
//      "type": "DATE",
//      "updatedAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "updatedBy": "string"
//    }
//  ],
//  "customerIdentity": "string",
//  "digitalFactories": [
//    {
//      "active": true,
//      "annualVolume": 0,
//      "baseVpeIdentity": "string",
//      "batchesPerYear": 0,
//      "createdAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "createdBy": "string",
//      "currencyCode": "string",
//      "customerIdentity": "string",
//      "deletedAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "deletedBy": "string",
//      "description": "string",
//      "identity": "string",
//      "location": "string",
//      "machinesVpeIdentity": "string",
//      "materialCatalogVpeIdentity": "string",
//      "name": "string",
//      "ownerType": "PUBLIC",
//      "permissions": [
//        "CREATE"
//      ],
//      "processGroupAssociations": {
//        "additionalProp1": {
//          "additionalProp1": {},
//          "additionalProp2": {},
//          "additionalProp3": {}
//        },
//        "additionalProp2": {
//          "additionalProp1": {},
//          "additionalProp2": {},
//          "additionalProp3": {}
//        },
//        "additionalProp3": {
//          "additionalProp1": {},
//          "additionalProp2": {},
//          "additionalProp3": {}
//        }
//      },
//      "productVersion": "string",
//      "productionLife": 0,
//      "revision": "string",
//      "subjectIdentity": "string",
//      "toolShopVpeIdentity": "string",
//      "updatedAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "updatedBy": "string",
//      "useType": "COSTING",
//      "vpeType": "string"
//    }
//  ],
//  "exchangeRates": [
//    {
//      "active": true,
//      "createdAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "createdBy": "string",
//      "currencyCode": "string",
//      "customerIdentity": "string",
//      "deletedAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "deletedBy": "string",
//      "description": "string",
//      "identity": "string",
//      "rate": 0,
//      "updatedAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "updatedBy": "string"
//    }
//  ],
//  "identity": "string",
//  "processGroups": [
//    {
//      "assemblySupported": true,
//      "cidSupported": true,
//      "createdAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "createdBy": "string",
//      "defaultVpeIdentity": "string",
//      "defaultVpeName": "string",
//      "deletedAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "deletedBy": "string",
//      "description": "string",
//      "identity": "string",
//      "name": "string",
//      "partSupported": true,
//      "secondaryProcessGroup": true,
//      "updatedAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "updatedBy": "string"
//    }
//  ],
//  "siteVariables": [
//    {
//      "createdAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "createdBy": "string",
//      "customerIdentity": "string",
//      "deletedAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "deletedBy": "string",
//      "identity": "string",
//      "name": "string",
//      "notes": "string",
//      "type": "BOOLEAN",
//      "updatedAt": "yyyy-MM-dd'T'HH:mm'Z'",
//      "updatedBy": "string",
//      "value": "string"
//    }
//  ]