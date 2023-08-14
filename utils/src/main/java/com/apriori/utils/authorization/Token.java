package com.apriori.utils.authorization;



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
