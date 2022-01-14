package com.apriori.utils.token;

import com.apriori.utils.http.enums.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(location = "common/TokenSchema.json")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Token {
    private String token;
}
