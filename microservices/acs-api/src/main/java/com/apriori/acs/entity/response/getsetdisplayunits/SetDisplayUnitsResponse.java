package com.apriori.acs.entity.response.getsetdisplayunits;

import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Data
@Schema(location = "acs/SetDisplayUnitsResponse.json")
public class SetDisplayUnitsResponse {
    private String resourceCreated;
}
