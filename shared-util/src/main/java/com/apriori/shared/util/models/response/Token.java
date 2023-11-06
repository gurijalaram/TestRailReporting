package com.apriori.shared.util.models.response;

import com.apriori.shared.util.annotations.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(location = "TokenSchema.json")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Token {
    private String token;
}
