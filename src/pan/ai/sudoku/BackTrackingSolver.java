package pan.ai.sudoku;
public class BackTrackingSolver
{
    CSP problem;
    int[][] stack;
    int stackpos;
    int n;
    BackTrackingSolver(CSP c,int s)
    {
        problem=c;
        stack=new int[s][3];
    }
    void initialise(CSP c)
    {
        problem=c;
        problem.initialise();
    }
    void fix(int v,int c)
    {
        for(int i=0;i<problem.domain[v].length;i++)
            if(problem.domain[v][i])
            {
                if(i!=c)
                    problem.removeFromDomain(v,i);
            }
            else if(i==c) problem.addToDomain(v,c);
        problem.value[v]=c;
    }
    boolean solve(int nSols,int nMax)
    {
        stackpos=0;
        int v;
        out:for(n=0;n<nMax;n++)
        {
            if(problem.nunassigned==0){
            	nSols--;
            	if(nSols<=0)
                	return true;
            }
            if(problem.sizeofSmallestDomain()==0||problem.nunassigned==0)
            {
                for(;;)
                {
                    stackpos--;
                    if(stackpos==-1) return false;
                    if(stack[stackpos][2]==1)
                    {
                        problem.addToDomain(stack[stackpos][0],stack[stackpos][1]);
                    }
                    else if(stack[stackpos][2]==0)
                    {
                        v=stack[stackpos][0];
                        if(problem.sizeofDomain[v]>1)for(problem.value[v]++;problem.value[v]<problem.domain[v].length;problem.value[v]++)
                            if(problem.domain[v][problem.value[v]])
                            {
                                stackpos++;
                                FC(v,problem.value[v]);
                                continue out;
                            }
                        problem.value[v]=-1;
                        problem.putBackUnassigned(v);
                    }
                }
            }
            else
            {
                v=problem.getVarWithSmallestDomain();
                problem.value[v]=0;
                while(!problem.domain[v][problem.value[v]]) problem.value[v]++;
                stack[stackpos][0]=v;
                stack[stackpos][1]=problem.value[v];
                stack[stackpos++][2]=0;
                FC(v,problem.value[v]);
            }
        }
        return false;
    }
    void removeFromDomain(int v,int x)
    {
        problem.removeFromDomain(v,x);
        stack[stackpos][0]=v;
        stack[stackpos][1]=x;
        stack[stackpos++][2]=1;
    }
    void FC(int v,int x)
    {
        for(int i=0;i<problem.adj[v].length;i++)
            if(problem.value[problem.adj[v][i]]==-1)
                switch(problem.type[v][i])
                {
                    case UNEQUAL:
                        if(problem.domain[problem.adj[v][i]][x])
                        {
                            removeFromDomain(problem.adj[v][i],x);
                            if(problem.sizeofSmallestDomain()==0) return;
                        }
                        continue;
                    case LESS_THAN_OR_EQUAL:
                        for(int j=0;j<x;j++) if(problem.domain[problem.adj[v][i]][j])
                        {
                            removeFromDomain(problem.adj[v][i],j);
                            if(problem.sizeofSmallestDomain()==0) return;
                        }
                        continue;
                    case LESS_THAN:
                        for(int j=0;j<=x;j++) if(problem.domain[problem.adj[v][i]][j])
                        {
                            removeFromDomain(problem.adj[v][i],j);
                            if(problem.sizeofSmallestDomain()==0) return;
                        }
                        continue;
                    case GREATER_THAN:
                        for(int j=x;j<problem.domain[problem.adj[v][i]].length;j++) if(problem.domain[problem.adj[v][i]][j])
                        {
                            removeFromDomain(problem.adj[v][i],j);
                            if(problem.sizeofSmallestDomain()==0) return;
                        }
                        continue;
                    case GREATER_THAN_OR_EQUAL:
                        for(int j=x+1;j<problem.domain[problem.adj[v][i]].length;j++) if(problem.domain[problem.adj[v][i]][j])
                        {
                            removeFromDomain(problem.adj[v][i],j);
                            if(problem.sizeofSmallestDomain()==0) return;
                        }
                        continue;
                    case EQUAL:
                        for(int j=0;j<problem.domain[problem.adj[v][i]].length;j++) if(problem.domain[problem.adj[v][i]][j]&&j!=x)
                        {
                            removeFromDomain(problem.adj[v][i],j);
                            if(problem.sizeofSmallestDomain()==0) return;
                        }
                        continue;
                        
                }
    }
}
