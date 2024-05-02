package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Probability {
    private List<SymbolProbability> standard_symbols;
    private Map<String, Map<String, Integer>> bonus_symbols;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SymbolProbability {
        private int column;
        private int row;
        private Map<String, Integer> symbols;
    }

}