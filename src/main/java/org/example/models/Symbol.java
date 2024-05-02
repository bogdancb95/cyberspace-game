package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Symbol {
    private String name;
    private Double reward_multiplier;
    private String type;
    private String impact;
    private Double extra;
}
