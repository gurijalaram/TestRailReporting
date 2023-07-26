package com.apriori.bcs.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "VPESchema.json")
public class CustomerVPE extends Pagination {
    private List<CustomerVPE> items;
    private String identity;
    private String createdBy;
    private String updatedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String subjectIdentity;
    private List<String> permissions;
    private String customerIdentity;
    private Boolean active;
    private Integer annualVolume;
    private Number batchesPerYear;
    private String description;
    private String location;
    private String materialCatalogVpeIdentity;
    private String name;
    private String ownerType;
    private Number productionLife;
    private String productVersion;
    private String revision;
    private String useType;
    private String vpeType;
    private String defaultMaterialName;
    private ProcessGroupAssociations processGroupAssociations;
}
