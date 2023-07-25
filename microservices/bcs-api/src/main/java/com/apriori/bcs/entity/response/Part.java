package com.apriori.bcs.entity.response;

import com.apriori.database.dto.BCSPartBenchmarkingDTO;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.reader.file.part.PartData;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonRootName("response")
@Schema(location = "PartResponseSchema.json")
public class Part {
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String updatedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String customerIdentity;
    private String state;
    private String partType;
    private String externalId;
    private Boolean requireWatchpointReport;
    private String errors;
    private String url;
    private String partName;
    private String costingResult;
    private String message;
    private String description;


    /**
     * Add metrics to existing PartData object
     * @param partData
     */
    public void convertToBCSPartBenchData(PartData partData) {
        partData.setStartTime(this.createdAt);
        partData.setIdentity(this.getIdentity());
        partData.setPartName(this.getPartName());
    }

    /**
     * Add metrics and return new PartData object
     * @return BCSPartBenchmarkingDTO
     */
    public BCSPartBenchmarkingDTO convertToBCSPartBenchData() {
        return BCSPartBenchmarkingDTO.builder()
            .startTime(this.createdAt)
            .identity(this.getIdentity())
            .partName(this.getPartName())
            .build();
    }
}
