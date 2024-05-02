package org.example.util;

import org.example.config.GameConfig;
import org.example.models.GameMatrix;
import org.example.models.MatrixCell;
import org.example.models.Probability;
import org.example.models.Symbol;

import java.util.*;

public class GameMatrixGenerator {

    private final GameConfig config;
    private final Random random;

    public GameMatrixGenerator(GameConfig config) {
        this.config = config;
        this.random = new Random();
    }

    public GameMatrix generateMatrix() {
        GameMatrix matrix = new GameMatrix();
        matrix.setRows(config.getRows());
        matrix.setColumns(config.getColumns());
        matrix.setMatrix(new MatrixCell[config.getRows()][config.getColumns()]);

        for (int i = 0; i < config.getRows(); i++) {
            for (int j = 0; j < config.getColumns(); j++) {
                matrix.getMatrix()[i][j] = new MatrixCell(i, j, getRandomSymbol(i, j));
            }
        }
        return matrix;
    }

    private Symbol getRandomSymbol(int rowIndex, int columnIndex) {
        List<String> symbolList = new ArrayList<>();

        Probability probability = config.getProbabilities();
        if (probability != null) {
            List<Probability.SymbolProbability> standardSymbols = probability.getStandard_symbols();
            if (standardSymbols != null && !standardSymbols.isEmpty()) {
                for (Probability.SymbolProbability symbolProbability : standardSymbols) {
                    if (symbolProbability.getRow() == rowIndex && symbolProbability.getColumn() == columnIndex) {
                        symbolProbability.getSymbols().forEach((symbol, prob) -> {
                            for (int i = 0; i < prob; i++) {
                                symbolList.add(symbol);
                            }
                        });
                        break;
                    }
                }
            }
        }

        if (symbolList.isEmpty()) {
            if (probability != null) {
                List<Probability.SymbolProbability> standardSymbols = probability.getStandard_symbols();
                if (standardSymbols != null) {
                    standardSymbols.forEach(symbolProbability -> symbolProbability.getSymbols().forEach((symbol, prob) -> {
                        for (int i = 0; i < prob; i++) {
                            symbolList.add(symbol);
                        }
                    }));
                }

                if(probability.getBonus_symbols() != null) {
                    Map<String, Integer> bonusSymbols = probability.getBonus_symbols().get("symbols");
                    if (bonusSymbols != null) {
                        bonusSymbols.forEach((symbol, prob) -> {
                            for (int i = 0; i < prob; i++) {
                                symbolList.add(symbol);
                            }
                        });
                    }
                }
            }

            if (symbolList.isEmpty()) {
                symbolList.addAll(config.getSymbols().keySet());
            }
        }

        String symbolName = symbolList.get(random.nextInt(symbolList.size()));
        Symbol symbolConfig = config.getSymbols().get(symbolName);

        return new Symbol(symbolName, symbolConfig.getReward_multiplier(), symbolConfig.getType(), symbolConfig.getImpact(), symbolConfig.getExtra());
    }

}
