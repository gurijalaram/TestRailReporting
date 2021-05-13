package com.apriori.vds.entity.response.customizations;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "vds/CustomizationsItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomizationsItems extends Pagination {
    private List<Customization> items;
}
