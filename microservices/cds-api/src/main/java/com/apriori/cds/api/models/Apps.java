package com.apriori.cds.api.models;

import com.apriori.cds.api.enums.AppAccessControlsEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apps {
    String deployment;
    List<AppAccessControlsEnum> applications;
}
