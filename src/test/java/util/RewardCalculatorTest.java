package util;

import org.example.config.GameConfig;
import org.example.models.GameMatrix;
import org.example.models.Symbol;
import org.example.util.RewardCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

import org.example.models.*;

import java.util.*;

import static org.mockito.Mockito.*;

public class RewardCalculatorTest {

    private RewardCalculator rewardCalculator;
    private GameConfig gameConfig;

    @BeforeEach
    void setUp() {
        gameConfig = mock(GameConfig.class);
        rewardCalculator = new RewardCalculator(gameConfig);
    }

    @Test
    void testCalculateReward_WithNoWinningCombinations() {
        GameMatrix matrix = createSpecificMatrix();

        Result result = mock(Result.class);

        Map<String, WinCombination> winCombinations = Collections.emptyMap();
        when(gameConfig.getWin_combinations()).thenReturn(winCombinations);

        rewardCalculator.calculateReward(result, matrix, 100);

        verify(result).setReward(0);
    }

    @Test
    void testCalculateReward_WithWinningCombination() {
        GameMatrix matrix = createSpecificMatrix();

        Symbol winningSymbol = new Symbol("A", 2.0, "standard", "", 0.0);
        matrix.getMatrix()[0][0] = new MatrixCell(0, 0, winningSymbol);

        Result result = mock(Result.class);

        Map<String, WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("winCombination1", new WinCombination(2.5, 3, "same_symbols", "same_symbols"));
        when(gameConfig.getWin_combinations()).thenReturn(winCombinations);

        rewardCalculator.calculateReward(result, matrix, 100);

        verify(result).setReward(500);
        Map<String, List<String>> expectedWinningCombinations = Collections.singletonMap("A", Collections.singletonList("winCombination1"));
        verify(result).setApplied_winning_combinations(expectedWinningCombinations);
    }

    @Test
    void testCalculateReward_WithBonusSymbols() {
        GameMatrix matrix = createSpecificMatrix();

        Symbol bonusSymbol = new Symbol("10x", 10.0, "bonus", "multiply_reward", 0.0);
        matrix.getMatrix()[0][0] = new MatrixCell(0, 0, bonusSymbol);

        Result result = mock(Result.class);

        Map<String, WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("winCombination1", new WinCombination(2.5, 3, "same_symbols", "same_symbols"));
        when(gameConfig.getWin_combinations()).thenReturn(winCombinations);

        rewardCalculator.calculateReward(result, matrix, 100);

        verify(result).setReward(250);
        List<String> expectedAppliedBonuses = Collections.singletonList("10x");
        verify(result).setApplied_bonus_symbol(expectedAppliedBonuses);
    }

    private GameMatrix createSpecificMatrix() {
        GameMatrix matrix = new GameMatrix();
        matrix.setRows(3);
        matrix.setColumns(3);
        MatrixCell[][] matrixCells = new MatrixCell[3][3];

        matrixCells[0][0] = new MatrixCell(0, 0, new Symbol("A", 1.0, "standard", "", 0.0));
        matrixCells[0][1] = new MatrixCell(0, 1, new Symbol("A", 1.0, "standard", "", 0.0));
        matrixCells[0][2] = new MatrixCell(0, 2, new Symbol("B", 1.0, "standard", "", 0.0));

        matrixCells[1][0] = new MatrixCell(1, 0, new Symbol("A", 1.0, "standard", "", 0.0));
        matrixCells[1][1] = new MatrixCell(1, 1, new Symbol("C", 1.0, "standard", "", 0.0));
        matrixCells[1][2] = new MatrixCell(1, 2, new Symbol("D", 1.0, "standard", "", 0.0));

        matrixCells[2][0] = new MatrixCell(2, 0, new Symbol("A", 1.0, "standard", "", 0.0));
        matrixCells[2][1] = new MatrixCell(2, 1, new Symbol("10x", 10.0, "bonus", "multiply_reward", 0.0));
        matrixCells[2][2] = new MatrixCell(2, 2, new Symbol("E", 1.0, "standard", "", 0.0));

        matrix.setMatrix(matrixCells);
        return matrix;
    }
}

