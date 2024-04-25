package com.apriori.shared.util.models.response;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "InstallationSchema.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Installation {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String customerIdentity;
    private String deploymentIdentity;
    private String name;
    private String cloudReference;
    private String description;
    private Boolean active;
    private Boolean highMem;
    private String apVersion;
    private String url;
    private String realm;
    private String s3Bucket;
    private String tenantGroup;
    private String tenant;
    private String clientId;
    private String clientSecret;
    private String cidGlobalKey;
    private String cidApiId;
    private String cidApiSecret;
    private List<Application> applications;
    private String region;
    private Features features;
}
