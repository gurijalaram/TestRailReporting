package com.apriori.qms.entity.response.component;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonRootName("response")
@Schema(location = "ComponentResponseSchema.json")
public class ComponentResponse {
    public String identity;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    public LocalDateTime createdAt;
    public String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    public LocalDateTime updatedAt;
    public String updatedBy;
    public String customerIdentity;
    public String componentName;
    public String componentType;
    public String filename;
    public String configurationName;
}