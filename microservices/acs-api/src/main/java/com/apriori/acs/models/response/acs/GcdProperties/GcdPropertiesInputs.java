package com.apriori.acs.models.response.acs.GcdProperties;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GcdPropertiesInputs {
    private List<GcdPropertiesGroupItemsInputs> groupItems;
}