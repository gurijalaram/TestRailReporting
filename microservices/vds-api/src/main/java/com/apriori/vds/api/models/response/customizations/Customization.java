package com.apriori.vds.api.models.response.customizations;

import com.apriori.bcs.api.models.response.ProcessGroup;
import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;
import com.apriori.vds.api.models.response.custom.attributes.CustomAttribute;
import com.apriori.vds.api.models.response.digital.factories.DigitalFactory;
import com.apriori.vds.api.models.response.process.group.site.variable.SiteVariable;

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
