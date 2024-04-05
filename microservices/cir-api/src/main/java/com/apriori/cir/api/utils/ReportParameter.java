package com.apriori.cir.api.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReportParameter {
    public List<UpdatedInputControlsPayloadInputsItem> reportParameter = new ArrayList<>();
}
