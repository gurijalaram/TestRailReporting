package com.apriori.authorization.response;

import com.apriori.annotations.Schema;

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
