public class SudokuInput{
    public static void main(String[] args){
        int[][] x = {
            {5,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},    //The Problem
            {0,0,0,0,0,0,0,3,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,9,0,0,0,0,0},
            {0,0,7,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,8,0},
            {0,0,0,0,0,0,6,0,0},
            {0,0,0,0,0,0,0,0,0},
        };
        SudokuCSP sudoku = new SudokuCSP(x);
        if(sudoku.isTrue()){
            sudoku.print();
        }
        else
            System.err.println("Simulation Failed");
    }
}