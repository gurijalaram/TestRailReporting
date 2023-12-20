package com.apriori.dfs.api.models.response;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(location = "ProcessGroupSchema.json")
public class ProcessGroup {
    private Boolean supportedByCloudApplications;
    private Boolean supportsAssemblies;
    private Boolean supportsParts;
    private String identity;
    private Integer annualVolume;
    private Integer batchesPerYear;
    private String currencyCode;
    private String customerIdentity;
    private String description;
    private String location;
    private String name;
    private List<String> processGroups;
    private Integer productionLife;
    private String revision;
    private String type;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
}
