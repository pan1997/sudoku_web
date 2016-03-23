package pan.ai.sudoku;
public class CSP
{
    int[] value;
    boolean[][] domain;
    int[] sizeofDomain;
    int[] minHeap;
    int[] pos;
    int nunassigned;
    CSP(int[][] ad,ConstraintType[][] ct,int nd)
    {
        adj=ad;
        type=ct;
        value=new int[ad.length];
        sizeofDomain=new int[value.length];
        minHeap=new int[value.length];
        domain=new boolean[value.length][nd];
        pos=new int[value.length];
    }
    void initialise()
    {
        nunassigned=value.length;
        for(int i=0;i<value.length;i++)
        {
            value[i]=-1;
            for(int j=0;j<domain[i].length;j++)
                domain[i][j]=true;
            sizeofDomain[i]=domain[i].length;
            pos[i]=i;
            minHeap[i]=i;
        }
        for(int i=nunassigned/2;i>=0;i--)
            hf(i);
    }
    void hf(int i)
    {
        int l,r,t;
        while(i<nunassigned)
        {
            l=(i<<1)+1;
            r=l+1;
            if(l<nunassigned&&sizeofDomain[minHeap[l]]<sizeofDomain[minHeap[i]])
                t=l;
            else
                t=i;
            if(r<nunassigned&&sizeofDomain[minHeap[r]]<sizeofDomain[minHeap[t]])
                t=r;
            if(t==i)
                return;
            l=minHeap[t];
            minHeap[t]=minHeap[i];
            minHeap[i]=l;
            pos[minHeap[i]]=i;
            pos[minHeap[t]]=t;
            i=t;
        }
    }
    void hb(int i)
    {
        int p,t;
        while(i>0)
        {
            p=(i-1)>>1;
            if(sizeofDomain[minHeap[p]]<=sizeofDomain[minHeap[i]])
                return;
            t=minHeap[p];
            minHeap[p]=minHeap[i];
            minHeap[i]=t;
            pos[minHeap[i]]=i;
            pos[minHeap[p]]=p;
            i=p;
        }
    }
    int getVarWithSmallestDomain()
    {
        int x=minHeap[0];
        minHeap[0]=minHeap[--nunassigned];
        pos[x]=-1;
        pos[minHeap[0]]=0;
        hf(0);
        return x;
    }
    void putBackUnassigned(int v)
    {
        pos[v]=nunassigned;
        minHeap[nunassigned]=v;
        hb(nunassigned);
        nunassigned++;
    }
    void removeFromDomain(int v,int x)
    {
        domain[v][x]=false;
        sizeofDomain[v]--;
        hb(pos[v]);
    }
    void addToDomain(int v,int x)
    {
        domain[v][x]=true;
        sizeofDomain[v]++;
        hf(pos[v]);
    }
    int sizeofSmallestDomain()
    {
        return sizeofDomain[minHeap[0]];
    }
    void print()
    {
        for(int i=0;i<value.length;i++)
            System.out.println(value[i]);
    }
    int[][] adj;
    ConstraintType[][] type;
}
