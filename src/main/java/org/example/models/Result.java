package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private MatrixCell[][] matrix;
    private double reward;
    private Map<String, List<String>> applied_winning_combinations;
    private List<String> applied_bonus_symbol;

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder("{\n");
        
        resultString.append("\t\"matrix\": [\n");
        for (MatrixCell[] row : matrix) {
            resultString.append("\t\t[").append(Arrays.stream(row).map(cell -> "\"" + cell.getSymbol().getName() + "\"").collect(Collectors.joining(", "))).append("],\n");
        }
        resultString.deleteCharAt(resultString.length() - 2);
        resultString.append("\t],\n");
        
        resultString.append("\t\"reward\": ").append(reward).append(",\n");
        
        if(reward > 0) {

            resultString.append("\t\"applied_winning_combinations\": {\n");
            for (Map.Entry<String, List<String>> entry : applied_winning_combinations.entrySet()) {
                resultString.append("\t\t\"").append(entry.getKey()).append("\": ").append(entry.getValue()).append(",\n");
            }
            if (!applied_winning_combinations.isEmpty()) {
                resultString.deleteCharAt(resultString.length() - 2);
            }
            resultString.append("\t},\n");

            resultString.append("\t\"applied_bonus_symbol\": ").append(applied_bonus_symbol).append("\n}");
        } else {
            resultString.append("}");
        }

        return resultString.toString();
    }

}
