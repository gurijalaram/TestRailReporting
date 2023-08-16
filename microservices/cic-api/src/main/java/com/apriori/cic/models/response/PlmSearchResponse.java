package com.apriori.cic.models.response;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "PlmSearchSchema.json")
public class PlmSearchResponse {
    private List<PlmSearchPart> items;
}