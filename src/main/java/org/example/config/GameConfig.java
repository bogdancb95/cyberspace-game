package org.example.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.models.Probability;
import org.example.models.Symbol;
import org.example.models.WinCombination;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameConfig {
    private int columns;
    private int rows;
    private Map<String, Symbol> symbols;
    private Probability probabilities;
    private Map<String, WinCombination> win_combinations;

    public static GameConfig loadFromJson(String path) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(new File(path), GameConfig.class);
        } catch (IOException e) {
            System.err.println("Error loading game configuration: " + e.getMessage());
            return null;
        }
    }
}
