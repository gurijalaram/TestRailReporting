package com.apriori.vds.entity.response.customizations;

import com.apriori.annotations.Schema;
import com.apriori.bcs.entity.response.ProcessGroup;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.vds.entity.response.custom.attributes.CustomAttribute;
import com.apriori.vds.entity.response.digital.factories.DigitalFactory;
import com.apriori.vds.entity.response.process.group.site.variable.SiteVariable;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "Customization.json")
@Data
@JsonRootName(value = "response")
public class Customization {
    private List<CustomAttribute> customAttributes;
    private String customerIdentity;
    private List<DigitalFactory> digitalFactories;
    private List<ExchangeRate> exchangeRates;
    private String identity;
    private List<ProcessGroup> processGroups;
    private List<SiteVariable> siteVariables;
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
