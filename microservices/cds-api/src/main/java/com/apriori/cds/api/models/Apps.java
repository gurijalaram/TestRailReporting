package com.apriori.cds.api.models;

import com.apriori.cds.api.enums.AppAccessControlsEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apps {
    AppAccessControlsEnum appAccessControlsEnum;
    String environment;
}
