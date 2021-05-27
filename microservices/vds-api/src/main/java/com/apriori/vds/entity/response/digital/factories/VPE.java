package com.apriori.vds.entity.response.digital.factories;

import com.apriori.bcs.entity.response.ProcessGroupAssociations;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(location = "VPEResponse.json")
@Data
@JsonRootName(value = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VPE {
    private Boolean active;
    private Integer annualVolume;
    private String baseVpeIdentity;
    private Integer batchesPerYear;
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
    private Integer productionLife;
    private String revision;
    private String subjectIdentity;
    private String toolShopVpeIdentity;
    private String updatedBy;
    private String useType;
    private String vpeType;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
}
