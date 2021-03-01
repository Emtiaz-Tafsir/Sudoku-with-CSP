import java.util.ArrayList;
import java.util.Collections;           //Space Complexity =  n3 + 2n2 + 3n = O(n3)
public class SudokuCSP{                 //Time Complexity  = 9n4 + 5n3 + n2 = O(n4)
    int rem;
    int[][] prob;
    int size;                           //storing for later use;
    int unit;                           //unit-> squareroot of dimention, helps calculate small boxes
    ArrayList<ArrayList<Integer>> rd;   //rd-> ordered list of Domains(per row)
    Object[] ridx;                      //ridx-> array of row domains by index
    ArrayList<ArrayList<Integer>> cd;   //cd-> ordered list of Domains(per column)
    Object[] cidx;                      //cidx-> array of column domains by index
    Object[] bidx;                      //bidx-> array of unit box domains by index
    boolean result = false;
    
    public SudokuCSP(){
        prob = new int[9][9];
        init();
    }
    
    public SudokuCSP(int[][] p){
        prob = p;
        init();
    }
    
    public void init(){
        
        size = prob.length;
        unit = (int)Math.sqrt(size);
        
        if(!check()){
            System.err.println("Caused by input violation");
            return;
        }
        
        rd = new ArrayList<ArrayList<Integer>>(size);
        ridx = new Object[size];
        cd = new ArrayList<ArrayList<Integer>>(size);
        cidx = new Object[size];
        bidx = new Object[size];
        rem = size*size;               //rem-> remaining boxes to be filled
        
        for(int i=0; i<size; i++){
            
            ArrayList<Integer> rde = new ArrayList<Integer>(size+1);
            ArrayList<Integer> cde = new ArrayList<Integer>(size+1);
            ArrayList<Integer> bde = new ArrayList<Integer>(size+1);
            
            for(int j=1; j<=size; j++){
                rde.add(j);
                cde.add(j);            //adding all allowable numbers to the domains
                bde.add(j);
            }
            
            for(int j=0; j<size; j++){
                if(prob[i][j]!=0) rde.remove((Integer)prob[i][j]);   //removing ones that already exists
                if(prob[j][i]!=0){ cde.remove((Integer)prob[j][i]);
                    rem--;
                }
            }
            
            int rBase = (i/unit)*unit;  //box index function
            int cBase = (i%unit)*unit;
            for(int j=0; j<size; j++){
                int row = rBase+j/unit;
                int col = cBase+j%unit;
                if(prob[row][col]!=0) bde.remove((Integer)prob[row][col]);
            }
            
            Collections.shuffle(rde);
            Collections.shuffle(cde);
            Collections.shuffle(bde);
            
            rde.add(0,size+(i+1));      //first element indicates index added with size
            cde.add(0,size+(i+1));
            bde.add(0,size+(i+1));
            ridx[i] = rde;
            cidx[i] = cde;
            addList(rd,rde);            //addList method sorts elements by size at insertion
            addList(cd,cde);
            bidx[i] = bde;              //no need to sort unit boxes as position can be found from row and column only
        }
//        System.out.println("Input:");
//        print();
//        System.out.print("Output:");
        result = solve();
//        if(result){
//            System.out.println("Success!\n");
//            print();
//        }
//        else
//            System.out.println("Failed!\n");
    }
    
    private boolean solve(){
        if(rem==0) return true;
        
        int[] pos = getVarPos();
        if(pos==null) return false;
        
        ArrayList<Integer> dom = getDomain(pos);
        if(dom.size()==0) return false;
        
        while(dom.size()!=0){
            Integer e = dom.remove(0);
            prob[pos[0]][pos[1]] = e;
            
            ArrayList<Integer> r = (ArrayList<Integer>)ridx[pos[0]];
            ArrayList<Integer> c = (ArrayList<Integer>)cidx[pos[1]];
            ArrayList<Integer> b = (ArrayList<Integer>)bidx[pos[2]];
            
            r.remove(e);
            c.remove(e);
            b.remove(e);
            updateList(r,c);            //sorts the Lists again after updating domain
            rem--;
            
            boolean res = solve();      //recursive call
            
            if(res) return true;
            
            r.add(e);
            c.add(e);
            b.add(e);
            if(!rd.contains(r)) rd.add(r);
            if(!cd.contains(c)) cd.add(c);
            rem++;
        }
        
        prob[pos[0]][pos[1]] = 0;
        return false;
    }
    
 /* getVarPos() method returns the index of the variable with the most constraints  */
    private int[] getVarPos(){
        int iter1 = (rd.size()<cd.size()) ? rd.size() : cd.size();
        int iter2 = (rd.size()>cd.size()) ? rd.size() : cd.size();
        for(int i=0; i<iter1; i++){
            for(int j=0; j<iter2; j++){
                int r,c,k;
                if(i==j){
                    k = (i<rd.size()) ? i:rd.size()-1;
                    r = rd.get(k).get(0)-size-1;
                    k = (i<cd.size()) ? i:cd.size()-1;
                    c = cd.get(k).get(0)-size-1;
                    if(prob[r][c]==0){
                        int b = (r/unit)*unit+c/unit;
                        int[] res = {r,c,b};
                        return res;
                    }
                    continue;
                }
                int l;
                r = (i<rd.size()) ? i:rd.size()-1;
                c = (j<cd.size()) ? j:cd.size()-1;
                k = (j<rd.size()) ? j:rd.size()-1;
                l = (i<cd.size()) ? i:cd.size()-1;
                if(rd.get(r).size()+cd.get(c).size()<=rd.get(k).size()+cd.get(l).size()){
                    r = rd.get(r).get(0)-size-1;
                    c = cd.get(c).get(0)-size-1;
                    if(prob[r][c]==0){
                        int b = (r/unit)*unit+c/unit;
                        int[] res = {r,c,b};
                        return res;
                    }
                }
                r = rd.get(k).get(0)-size-1;
                c = cd.get(l).get(0)-size-1;
                if(prob[r][c]==0){
                    int b = (r/unit)*unit+c/unit;
                    int[] res = {r,c,b};
                    return res;
                }
            }
        }
        return null;
    }
    
 /* getDomain() method returns the intersection of row, column and unit box domains */
    private ArrayList<Integer> getDomain(int[] p){
        ArrayList<Integer> r = (ArrayList<Integer>)ridx[p[0]];
        ArrayList<Integer> c = (ArrayList<Integer>)cidx[p[1]];
        ArrayList<Integer> b = (ArrayList<Integer>)bidx[p[2]];
        ArrayList<Integer> dom;
        if(r.size()>c.size()){
            dom = c;
            c = r; 
            r = dom;
        }
        if(r.size()>b.size()){
            dom = b;
            b = r; 
            r = dom;
        }
        dom = new ArrayList<Integer>();
        for(int i=1; i<r.size(); i++){
            Integer x = r.get(i);
            if(c.contains(x) && b.contains(x)) dom.add(x);
        }
        return dom;
    }
    
 /* sorts the Lists again after updating domain */
    private void updateList(ArrayList<Integer> r, ArrayList<Integer> c){
        int ir = rd.indexOf(r);
        int ic = cd.indexOf(c);
        if(r.size()==1) rd.remove(ir);
        else{
            if(ir!=0){
                if(rd.get(ir-1).size()>r.size()){
                    for(int i=ir-1; i>=0; i--){
                        if(rd.get(i).size()<=r.size()){
                            rd.remove(ir);
                            rd.add(i+1,r);
                            break;
                        }
                    }
                }
            }
        }
        if(c.size()==1) cd.remove(ic);
        else{
            if(ic!=0){
                if(cd.get(ic-1).size()>c.size()){
                    for(int i=ic-1; i>=0; i--){
                        if(cd.get(i).size()<=c.size()){
                            cd.remove(ic);
                            cd.add(i+1,c);
                            break;
                        }
                    }
                }
            }
        }
    }
    
 /* addList method sorts elements by size at insertion */
    private void addList(ArrayList<ArrayList<Integer>> p, ArrayList<Integer> e){  
        if(p.isEmpty() || p.get(p.size()-1).size()<=e.size()){
            p.add(e);
            return;
        }
        for(int i=p.size()-1; i>=0; i--){
            if(p.get(i).size() <= e.size()){
                p.add(i+1, e);
                return;
            }
        }
        p.add(0,e);
    }
    
    public void print(){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++)
                System.out.print(prob[i][j]+" ");
            System.out.println();
        }
        System.out.println();
    }
    
 /* Validates the solution */
    public boolean check(){
        for(int j=0; j<size; j++){
            int[] v1 = new int[size];
            int[] v2 = new int[size];
            for(int k=0; k<size; k++){
                try{
                    v1[prob[j][k]-1]++;
                }catch(Exception e){}
                try{
                    v2[prob[k][j]-1]++;
                }catch(Exception e){}
                
            }
            
            for(int k=0; k<size; k++){
                if(v1[k]>1){
                    System.err.println("Duplicate number: "+(k+1)+" found in "+(j+1)+"th row"); 
                    return false;
                }
                if(v2[k]>1){
                    System.err.println("Duplicate number: "+(k+1)+" found in "+(j+1)+"th column"); 
                    return false;
                }
            }
        }
        for(int k=0; k<size; k++){
            int[] v3 = new int[size];
            int rBase = (k/unit)*unit;
            int cBase = (k%unit)*unit;
            for(int j=0; j<size; j++){
                int row = rBase+j/unit;
                int col = cBase+j%unit;
                try{
                    v3[prob[row][col]-1]++;
                }catch(Exception e){}
            }
            for(int j=0; j<size; j++){
                if(v3[j]>1){
                    System.err.println("Duplicate number: "+(j+1)+" found in "+(k+1)+"th box");
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isTrue(){
        return result;
    }
}