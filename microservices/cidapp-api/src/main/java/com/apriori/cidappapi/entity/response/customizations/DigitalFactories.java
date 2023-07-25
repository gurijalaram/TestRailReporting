package com.apriori.cidappapi.entity.response.customizations;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DigitalFactories {
    private Boolean active;
    private Integer annualVolume;
    private String baseVpeIdentity;
    private Integer batchesPerYear;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByName;
    private String currencyCode;
    private String customerIdentity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedByName;
    private String description;
    private String identity;
    private String location;
    private String machinesVpeIdentity;
    private String materialCatalogVpeIdentity;
    private String name;
    private String ownerType;
    private List<String> permissions;
    private ProcessGroupAssociations processGroupAssociations;
    private String productVersion;
    private Integer productionLife;
    private String revision;
    private String subjectIdentity;
    private String toolShopVpeIdentity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String updatedByName;
    private String useType;
    private String vpeType;
}
