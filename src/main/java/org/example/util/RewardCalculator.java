package org.example.util;

import org.example.config.GameConfig;
import org.example.models.GameMatrix;
import org.example.models.Result;
import org.example.models.Symbol;
import org.example.models.WinCombination;

import java.util.*;

public class RewardCalculator {

    private final GameConfig gameConfig;

    public RewardCalculator(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public void calculateReward(Result result, GameMatrix matrix, double betAmount) {
        double totalReward = 0;
        Set<String> checkedSymbols = new HashSet<>();
        Set<String> appliedBonuses = new HashSet<>();
        Map<String, List<String>> appliedWinningCombinations = new HashMap<>();

        Map<String, WinCombination> winCombinations = gameConfig.getWin_combinations();
        
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                Symbol symbol = matrix.getMatrix()[i][j].getSymbol();
                if (symbol != null && !checkedSymbols.contains(symbol.getName())) {
                    List<String> sortedWinCombinations = new ArrayList<>(winCombinations.keySet());
                    sortedWinCombinations.sort((s1, s2) -> {
                        int count1 = winCombinations.get(s1).getCount();
                        int count2 = winCombinations.get(s2).getCount();
                        return Integer.compare(count2, count1);
                    });
                    
                    for (String winCombinationName : sortedWinCombinations) {
                        WinCombination winCombination = winCombinations.get(winCombinationName);
                        if (checkWinCondition(matrix, winCombination, i, j)) {
                            appliedWinningCombinations.put(symbol.getName(), Collections.singletonList(winCombinationName));
                            checkedSymbols.add(symbol.getName()); 
                            if (!Objects.isNull(symbol.getReward_multiplier())) {
                                totalReward += betAmount * winCombination.getReward_multiplier() * symbol.getReward_multiplier();
                            } else if (!Objects.isNull(symbol.getExtra())) {
                                totalReward += betAmount * winCombination.getReward_multiplier() + symbol.getExtra();
                            }
                            break; 
                        }
                    }
                }
            }
        }

        if (totalReward > 0) {
            applyBonuses(totalReward, appliedBonuses, matrix);
        }

        result.setReward(totalReward);
        result.setApplied_winning_combinations(appliedWinningCombinations);
        result.setApplied_bonus_symbol(new ArrayList<>(appliedBonuses));
    }


    private boolean checkWinCondition(GameMatrix matrix, WinCombination winComb, int row, int col) {
        String startingSymbolName = matrix.getMatrix()[row][col].getSymbol().getName();
        String startingSymbolType = matrix.getMatrix()[row][col].getSymbol().getType();
        if (startingSymbolName == null || !"standard".equals(startingSymbolType)) {
            return false;  
        }
        
        if (!"same_symbols".equals(winComb.getGroup())) {
            return false; 
        }
        
        int count = countSymbols(matrix, startingSymbolName);
        return count >= winComb.getCount();
    }

    private int countSymbols(GameMatrix matrix, String symbol) {
        int count = 0;
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                if (matrix.getMatrix()[i][j].getSymbol().getName().equals(symbol)) {
                    count++;
                }
            }
        }
        return count;
    }

    private void applyBonuses(double totalReward, Set<String> appliedBonuses, GameMatrix matrix) {
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                Symbol symbol = matrix.getMatrix()[i][j].getSymbol();
                if (symbol != null && "bonus".equals(symbol.getType())) {
                    switch (symbol.getImpact()) {
                        case "multiply_reward":
                            totalReward *= symbol.getReward_multiplier();
                            appliedBonuses.add(symbol.getName());
                            break;
                        case "extra_bonus":
                            totalReward += symbol.getExtra();
                            appliedBonuses.add(symbol.getName());
                            break;
                        case "miss":
                            break;
                        default:
                            System.out.println("Unknown bonus type: " + symbol.getImpact());
                    }
                }
            }
        }
    }
}
