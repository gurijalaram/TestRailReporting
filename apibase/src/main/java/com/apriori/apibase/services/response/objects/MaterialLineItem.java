package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "MaterialLineItemSchema.json")
@Data
public class MaterialLineItem {
    @JsonProperty
    private String identity;
    @JsonProperty
    //@JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    //@JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    @JsonProperty
    //@JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime deletedAt;
    @JsonProperty
    private String deletedBy;
    @JsonProperty
    private String costingStatus;
    @JsonProperty
    private String status;
    @JsonProperty
    private String customerPartNumber;
    @JsonProperty
    private String manufacturerPartNumber;
    @JsonProperty
    private String manufacturer;
    @JsonProperty
    private String quantity;
    @JsonProperty
    private Integer level;
    @JsonProperty("parts")
    private List<MaterialPart> materialParts;
}
