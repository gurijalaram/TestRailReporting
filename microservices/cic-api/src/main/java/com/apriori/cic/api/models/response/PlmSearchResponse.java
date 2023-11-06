package com.apriori.cic.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "PlmSearchSchema.json")
public class PlmSearchResponse {
    private List<PlmSearchPart> items;
}