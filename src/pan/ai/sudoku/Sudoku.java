package pan.ai.sudoku;

import java.util.ArrayList;

public class Sudoku
{
    CSP b;
    BackTrackingSolver s;
    boolean fixed[][];
    public int[][]brd;
    public Sudoku(){
    	fixed=new boolean[9][9];
    	brd=new int[9][9];
        int[][] adj=new int[81][20];
        int[] t=new int[81];
        int nx,ny;
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                for(int k=0;k<9;k++){
                    if(k!=i) adj[index(i,j)][t[index(i,j)]++]=index(k,j);
                    if(k!=j) adj[index(i,j)][t[index(i,j)]++]=index(i,k);
                }
                nx=i/3;
                ny=j/3;
                nx*=3;
                ny*=3;
                if(nx!=i){
                    if(ny!=j) adj[index(i,j)][t[index(i,j)]++]=index(nx,ny);
                    if(ny+1!=j) adj[index(i,j)][t[index(i,j)]++]=index(nx,ny+1);
                    if(ny+2!=j) adj[index(i,j)][t[index(i,j)]++]=index(nx,ny+2);
                }
                if(nx+1!=i){
                    if(ny!=j) adj[index(i,j)][t[index(i,j)]++]=index(nx+1,ny);
                    if(ny+1!=j) adj[index(i,j)][t[index(i,j)]++]=index(nx+1,ny+1);
                    if(ny+2!=j) adj[index(i,j)][t[index(i,j)]++]=index(nx+1,ny+2);
                }
                if(nx+2!=i){
                    if(ny!=j) adj[index(i,j)][t[index(i,j)]++]=index(nx+2,ny);
                    if(ny+1!=j) adj[index(i,j)][t[index(i,j)]++]=index(nx+2,ny+1);
                    if(ny+2!=j) adj[index(i,j)][t[index(i,j)]++]=index(nx+2,ny+2);
                }
            }
        }
        ConstraintType[][] ct=new ConstraintType[81][20];
        for(int i=0;i<ct.length;i++)
            for(int j=0;j<ct[i].length;j++)
                ct[i][j]=ConstraintType.UNEQUAL;
        b=new CSP(adj,ct,9);
        b.initialise();
        s=new BackTrackingSolver(b,20000);
    }
    public void fix(int i,int j,int x){
    	fixed[i][j]=true;
        int v=index(i,j);
        s.fix(v,x-1);
    }
    public boolean fixed(int i,int j){
    	return fixed[i][j];
    }
    public int get(int i,int j){
        return b.value[index(i,j)]+1;
    }
    public boolean trySolve(int ns,int nm){
        for(int i=0;i<81;i++)
            b.value[i]=-1;
        boolean ans=s.solve(ns,nm);
        return ans;
    }
    void print(){
    	for(int i=0;i<9;i++){
    		for(int j=0;j<9;j++)
    			System.out.print(get(j,i));
    		System.out.println();
    	}
    }
    public void clear(){
    	for(int i=0;i<9;i++)
    		for(int j=0;j<9;j++)
    			fixed[i][j]=false;
    	s.initialise(b);
    }
    public void generate(int n,int d,int nd){
    	long start=System.currentTimeMillis();
    	clear();
    	Sudoku s=new Sudoku();
    	for(int i=0;i<9;i++)
    		s.fix(i,(int)(Math.random()*9),i+1);
    	s.trySolve(1,100);
    	s.print();
    	boolean fill[][]=new boolean[9][9];
    	for(int i=0;i<9;i++)for(int j=0;j<9;j++){
    		brd[i][j]=s.get(i,j);fill[i][j]=true;
    		if(brd[i][j]==0){
    			generate(n,d,nd);
    			return;
    		}
    	}
    	ArrayList<Integer> rnd=new ArrayList<Integer>(81);
    	for(int i=0;i<81;i++)
    		rnd.add(i);
    	int perm[]=new int[81];
    	for(int i=0;i<81;i++)
    		perm[i]=rnd.remove((int)(Math.random()*rnd.size()));
    	for(int i=0;i<n;i++){
    		int x=perm[i]/9;
    		int y=perm[i]%9;
    		fill[x][y]=false;
    		if(i>9){
    			s.clear();
    			for(int j=0;j<9;j++)
    				for(int k=0;k<9;k++)
    					if(fill[j][k])
    						s.fix(j, k, brd[j][k]);
    			if(s.trySolve(2,d))
    				fill[x][y]=true;
    		}
    	}
    	
    	/*
    	int r=(int)(Math.random()*3);
    	int c=(int)(Math.random()*3);
    	int lim=Math.min(100, n);
    	for(int i=0;i<lim;i++){
    		int x,y;
    		if(n>100&&i<9){
    			x=r*3+i/3;
    			y=c*3+i%3;
    		}else{
    			x=(int)(Math.random()*9);
    			y=(int)(Math.random()*9);
    		}
    		if(!fill[x][y]){
    			x=(int)(Math.random()*9);
    			y=(int)(Math.random()*9);
    		}
    		if(!fill[x][y])
    			continue;
    		fill[x][y]=false;
    		if(i>9){
    			s.clear();
    			for(int k=0;k<9;k++)for(int l=0;l<9;l++)
    				if(fill[k][l])
    					s.fix(k,l,brd[k][l]);
    			if(s.trySolve(2,d)){
    				fill[x][y]=true;
    			}
    		}
    	}
    	if(n>100){
    		for(int i=0;i<9;i++)
    			for(int j=0;j<9;j++)
    				if(fill[i][j]){
    					fill[i][j]=false;
    					s.clear();
    	    			for(int k=0;k<9;k++)for(int l=0;l<9;l++)
    	    				if(fill[k][l])
    	    					s.fix(k,l,brd[k][l]);
    	    			if(s.trySolve(2,d)){
    	    				fill[i][j]=true;
    	    			}
    				}
    	}*/
    	int nf=0;
    	for(int i=0;i<9;i++)
    		for(int j=0;j<9;j++)
    			if(fill[i][j]){
    				fix(i,j,brd[i][j]);
    				nf++;
    			}
    	start=System.currentTimeMillis()-start;
    	System.out.println("Took "+start+" millis with "+nf+" clues");
    }
    public final int index(int i,int j){
        return i*9+j;
    }
}
