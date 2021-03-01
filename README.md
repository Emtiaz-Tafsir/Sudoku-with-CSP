# Description
  An AI that is able to solve upto 16x16 sudoku problems using CSP algorithm
 

# File Details
  
  SudokuCSP.java             --> Contains the CSP Algorithm that is used to solve Sudoku problems
  
  SudokuInput.java           --> Used for solving custom test case scenarios through matrix array
  
  TestSudokuAI.java          --> Used for testing mutiple test cases present in the csv file and generate accuracy by matching with solutions provided
  
  sudoku.csv                 --> Contains 10000 9x9 sudoku problems and their solutions
  
  Sudoku CSP pseudocode.docx --> Contains detailed explaination of the algorithm used with pseudocode


# Remarks
  The bigger the dimension of the problem the longer it takes for the AI to solve it. It solves 9x9 problems really fast but takes a fair amount of time for solving 16x16 problems because the complexity of Sudoku problems increase exponentially with linear increase of dimention. I have not tried bigger dimention than 16x16, but i believe in a well equipped computer system, this program can solve those problems with ease
