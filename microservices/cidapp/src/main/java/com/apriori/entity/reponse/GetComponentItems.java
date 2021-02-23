package com.apriori.entity.reponse;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cidapp/GetComponentResponse.json")
public class GetComponentItems {
    private GetComponentItems response;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByName;
    private String customerIdentity;
    private String componentName;
    private String componentType;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;

    public GetComponentItems getResponse() {
        return response;
    }

    public String getIdentity() {
        return identity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getComponentType() {
        return componentType;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
