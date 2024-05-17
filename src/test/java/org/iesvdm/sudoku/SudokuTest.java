package org.iesvdm.sudoku;

//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SudokuTest {

    @Test
    void failTest() {
        Sudoku sudoku = new Sudoku();
        sudoku.fillBoardBasedInCluesRandomlySolvable();
        //sudoku.fillBoardBasedInCluesRandomly();
        sudoku.printBoard();
    }

    @Test
    void fillBoardRandomly(){
        Sudoku sudoku = new Sudoku();
        sudoku.fillBoardRandomly();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertThat(sudoku.getBoard()[i][j]).isBetween(0,10);
            }
        }
    }

    @Test
    void fillBoardBasedInCluesRandomly(){
        Sudoku sudoku = new Sudoku();
        sudoku.fillBoardRandomly();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertThat(sudoku.getBoard()[i][j]).isBetween(0,9);
            }
        }
    }

    @Test
    void fillBoardBasedInCluesRandomlySolvable(){
        Sudoku sudoku = new Sudoku();
        sudoku.setNumClues(80);
        sudoku.fillBoardBasedInCluesRandomlySolvable();
        assertThat(sudoku.solveBoard()).isTrue();
    }

    @Test
    void fillBoardSolvable(){
        Sudoku sudoku = new Sudoku();
        sudoku.fillBoardSolvable();
        assertThat(sudoku.solveBoard()).isTrue();
    }

    @Test
    void fillBoardUnsolvable() {
        Sudoku sudoku = new Sudoku();
        sudoku.fillBoardUnsolvable();
        assertThat(sudoku.solveBoard()).isFalse();
    }

    @Test
    void copyBoard(){
        Sudoku sudoku = new Sudoku();
        sudoku.fillBoardRandomly();
        Sudoku sudoku1 = new Sudoku();
        sudoku1.copyBoard(sudoku.getBoard());
        assertThat(sudoku.getBoard()).isEqualTo(sudoku1.getBoard());
    }

    @Test
    void printBoard(){
        //Given
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        Sudoku sudoku = new Sudoku();
        sudoku.setGridSize(3);
        sudoku.setBoard(new int[3][3]);
        sudoku.printBoard();
        assertThat(outputStreamCaptor.toString()).isNotEmpty();
    }

    @Test
    void isNumberInRow(){
        Sudoku sudoku = new Sudoku();
        int numFalse = 10;
        int numTrue = 5;
        sudoku.setGridSize(2);
        int [][] board = {{5,0},{5,0}};
        sudoku.setBoard(board);
        assertThat(sudoku.isNumberInRow(numTrue,1)).isTrue();
        assertThat(sudoku.isNumberInRow(numFalse,0)).isFalse();
    }

    @Test
    void isNumberInColumn(){
        Sudoku sudoku = new Sudoku();
        int numFalse = 10;
        int numTrue = 5;
        sudoku.setGridSize(2);
        int [][] board = {{0,5},{0,2}};
        sudoku.setBoard(board);
        assertThat(sudoku.isNumberInColumn(numTrue,1)).isTrue();
        assertThat(sudoku.isNumberInColumn(numFalse,0)).isFalse();
    }

    @Test
    void isNumberInBox(){
        Sudoku sudoku = new Sudoku();
        int numFalse = 10;
        int numTrue = 5;
        sudoku.setGridSize(2);
        int [][] board = {{0,5},{0,2}};
        sudoku.setBoard(board);
        assertThat(sudoku.isNumberInBox(numTrue,0,0)).isTrue();
    }

    @Test
    void isValidPlacement(){
        Sudoku sudoku = new Sudoku();
        int numFalse = 7;
        sudoku.setGridSize(5);
        sudoku.fillBoardRandomly();
        assertThat(sudoku.isValidPlacement(numFalse,0,0)).isTrue();
    }
    @Test
    void solveBoard(){
        Sudoku sudoku = new Sudoku();
        int [][] board = {{1,3,2},{2,2,3},{2,3,1}};
        sudoku.setGridSize(3);
        sudoku.setBoard(board);
        //Error le estoy dando un sudoku no solucionable y me imprime que esta solucionado.
        assertThat(sudoku.solveBoard()).isFalse();
    }

}
