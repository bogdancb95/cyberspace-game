package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMatrix {
    private int rows;
    private int columns;
    private MatrixCell[][] matrix;
}