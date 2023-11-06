package com.apriori.vds.api.models.response.digital.factories;

import com.apriori.bcs.api.models.response.ProcessGroupAssociations;
import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(location = "DigitalFactoryResponse.json")
@Data
@JsonRootName(value = "response")
public class DigitalFactory {
    private Boolean active;
    private Integer annualVolume;
    private String baseVpeIdentity;
    private Double batchesPerYear;
    private String createdBy;
    private String currencyCode;
    private String customerIdentity;
    private String deletedBy;
    private String description;
    private String identity;
    private String location;
    private String machinesVpeIdentity;
    private String materialCatalogVpeIdentity;
    private String name;
    private String ownerType;
    private String[] permissions;
    private ProcessGroupAssociations processGroupAssociations;
    private String productVersion;
    private Double productionLife;
    private String revision;
    private String subjectIdentity;
    private String toolShopVpeIdentity;
    private String updatedBy;
    private String useType;
    private String vpeType;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime deletedAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
}
