package util;

import org.example.config.GameConfig;
import org.example.models.GameMatrix;
import org.example.models.Probability;
import org.example.models.Symbol;
import org.example.util.GameMatrixGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class GameMatrixGeneratorTest {

    @Mock
    private GameConfig config;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateMatrix() {
        Map<String, Symbol> symbols = new HashMap<>();
        symbols.put("A", new Symbol("A", 1.0, "standard", "", 0.0));
        symbols.put("B", new Symbol("B", 1.0, "standard", "", 0.0));

        Map<String, Symbol> bonusSymbols = new HashMap<>();
        bonusSymbols.put("10x", new Symbol("10x", 10.0, "bonus", "multiply_reward", 0.0));

        Map<String, Symbol> mergedSymbols = new HashMap<>();
        mergedSymbols.putAll(symbols);
        mergedSymbols.putAll(bonusSymbols);

        Map<String, Integer> probabilityBonusSymbols = new HashMap<>();
        probabilityBonusSymbols.put("10x", 1);

        List<Probability.SymbolProbability> standardSymbols = new ArrayList<>();
        standardSymbols.add(new Probability.SymbolProbability(0, 0, Map.of("A", 1)));
        standardSymbols.add(new Probability.SymbolProbability(0, 1, Map.of("B", 1)));

        Probability probability = new Probability();
        probability.setStandard_symbols(standardSymbols);
        probability.setBonus_symbols(Map.of("symbols", probabilityBonusSymbols));

        when(config.getRows()).thenReturn(3);
        when(config.getColumns()).thenReturn(3);
        when(config.getProbabilities()).thenReturn(probability);
        when(config.getSymbols()).thenReturn(mergedSymbols);

        GameMatrixGenerator generator = new GameMatrixGenerator(config);

        GameMatrix matrix = generator.generateMatrix();

        assertNotNull(matrix);
    }

}
