package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinCombination {
    private Double reward_multiplier;
    private Integer count;
    private String group;
    private String when;
}