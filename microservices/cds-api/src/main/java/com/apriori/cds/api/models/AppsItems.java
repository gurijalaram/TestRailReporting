package com.apriori.cds.api.models;

import com.apriori.shared.util.annotations.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "AppsItemsSchema.json")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppsItems {
    List<Apps> appsList;
}
