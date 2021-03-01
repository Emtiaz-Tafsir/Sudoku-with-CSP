import java.util.Scanner;
import java.util.Arrays;
import java.io.File;

public class TestSudokuAI{
    public static void main(String[] args){
        
        Scanner sc = null;
        try{
            sc = new Scanner(new File("sudoku.csv"));
        }
        catch(Exception e){
            System.err.println("Error Reading file");
        }
        sc.nextLine();
        
        int[][] x = new int[9][9];
        int[][] y = new int[9][9];
        int t = 0;
        int t_a = 0;
        int f = 0;
        int tot = 0;
        
        while(sc.hasNext()){
            
            String[] s = sc.nextLine().split(",");
            char[] in = s[0].toCharArray();
            char[] out = s[1].toCharArray();
            
            for(int i=0,k=0; i<9; i++){
                for(int j=0; j<9; j++,k++){
                    x[i][j] = (int)in[k]-48;
                    y[i][j] = (int)out[k]-48;
                }
            }
            
            SudokuCSP csp = new SudokuCSP(x);
            if(csp.isTrue()){
                if(evaluate(x,y)) t++;
                if(csp.check()) t_a++;
                else f++;
            }
            else f++;
            tot++;
        }
        
        System.out.println("Total: "+tot);
        System.out.println("Matched: "+t);
        System.out.println("Unmatched: "+f);
        System.out.println("Absolute: "+t_a);
    }
    
    public static boolean evaluate(int[][] a, int[][] b){
        boolean res = true;
        for(int i=0; i<9; i++)
            res = res && Arrays.equals(a[i],b[i]);
        return res;
    }
}