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
// TODO: 12/06/2024 cn - fix this class name
public class Apps {
    public String deployment;
    public List<AppAccessControlsEnum> applications;
}
