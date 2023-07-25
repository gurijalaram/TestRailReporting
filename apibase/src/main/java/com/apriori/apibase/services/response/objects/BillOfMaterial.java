package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "BillOfMaterialSchema.json")
@Data
public class BillOfMaterial {
    @JsonProperty
    private Integer numberOfLineItemsCannotCost;
    @JsonProperty
    private Integer numberOfLineItemsNotCosted;
    @JsonProperty
    private Integer numberOfLineItemsCosted;
    @JsonProperty
    //@JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonProperty
    //@JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private String identity;
    @JsonProperty
    private String name;
    @JsonProperty
    private Integer numberOfLineItemsReadyForExport;
    @JsonProperty
    private Integer totalNumberOfLineItems;
    @JsonProperty
    private Integer numberOfLineItemsNoPartsMatched;
    @JsonProperty
    private Integer numberOfLineItemsIncomplete;
    @JsonProperty
    private String filename;
    @JsonProperty
    private String type;
}
