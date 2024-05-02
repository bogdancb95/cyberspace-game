package org.example;

import org.example.config.GameConfig;
import org.example.models.GameMatrix;
import org.example.models.Result;
import org.example.util.GameMatrixGenerator;
import org.example.util.RewardCalculator;
import org.jetbrains.annotations.NotNull;

import static org.example.config.GameConfig.loadFromJson;

public class Main {
    public static void main(@NotNull String[] args) {
        if (args.length < 4 || !args[0].equals("--config") || !args[2].equals("--betting-amount")) {
            System.out.println("Usage: java -jar <your-jar-file> --config <config-file> --betting-amount <amount>");
            return;
        }

        String configFile = args[1];
        double bettingAmount = Double.parseDouble(args[3]);
        if (bettingAmount < 0) {
            System.out.println("Betting amount must be a positive number");
            return;
        }
        
        Result result = new Result();
        
        GameConfig gameConfig = loadFromJson(configFile);

        GameMatrixGenerator matrixGenerator = new GameMatrixGenerator(gameConfig);
        GameMatrix gameMatrix = matrixGenerator.generateMatrix();
        result.setMatrix(gameMatrix.getMatrix());
        
        RewardCalculator rewardCalculator = new RewardCalculator(gameConfig);
        rewardCalculator.calculateReward(result, gameMatrix, bettingAmount);
        
        System.out.println(result);
    }
}