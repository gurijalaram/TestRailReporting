package com.apriori.edcapi.entity.response.bill.of.materials;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(location = "BillOfMaterialsResponse.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillOfMaterials {

    private String createdBy;
    private String deletedBy;
    private String filename;
    private String identity;
    private String name;
    private Integer numberOfLineItemsIncomplete;
    private Integer numberOfLineItemsNoPartsMatched;
    private Integer numberOfLineItemsReadyForExport;
    private Integer totalNumberOfLineItems;
    private String type;
    private String updatedBy;

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
