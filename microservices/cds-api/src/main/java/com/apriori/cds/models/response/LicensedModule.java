package com.apriori.cds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateDeserializer_yyyyMMdd;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(location = "LicenseModuleResponseSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LicensedModule {
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String name;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateDeserializer_yyyyMMdd.class)
    private LocalDate expiresAt;
    private String licenseSignature;
}
