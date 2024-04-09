package com.apriori.cir.api.utils;

import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Schema(location = "InputControlsUpdatedPayload.json")
@Data
public class UpdatedInputControlsRootItem {
    public ArrayList<InputControlState> inputControlState;
}