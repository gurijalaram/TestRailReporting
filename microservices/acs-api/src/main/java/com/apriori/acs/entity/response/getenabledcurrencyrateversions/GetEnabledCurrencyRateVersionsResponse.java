package com.apriori.acs.entity.response.getenabledcurrencyrateversions;

import com.apriori.utils.http.enums.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GetEnabledCurrencyRateVersionsResponse.json")
public class GetEnabledCurrencyRateVersionsResponse {
    private List<CurrencyRateVersionItem> items;
}
