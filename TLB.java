public class TLB
{
    int tlb[][];
    int pointer;
    
    int size;
    
    TLB(int size)
    {
        this.size = size;
        tlb = new int[size][4];
        pointer = 0;
        
        for(int x[] : tlb)
            for(int y : x)
                y = -1;
    }
    
    //Checks if TLB is full
    boolean isFull() {return pointer==size;}
    
    //Adds new entry
    int add(int pgNum, int frameNum, int clock, char act)
    {
        int old;
        
        //If TLB is not full
        if(!isFull())
        {
            tlb[pointer][0] = pgNum;
            tlb[pointer][1] = frameNum;
            tlb[pointer][2] = clock;
            tlb[pointer][0] = act=='W' ? 1 : 0;;
            
            pointer++;
            
            return -1;
        }
        
        //If Page Table is full, replace using LRU policy.
        int lruID = 0;
        
        for(int i = 1; i<size; i++)
            if(tlb[i][1]<tlb[lruID][1])
                lruID = i;
        
        old = tlb[lruID][0];
        
        tlb[lruID][0] = pgNum;
        tlb[lruID][1] = clock;
        tlb[lruID][2] = act=='W' ? 1 : 0;
        
        return lruID;
    }
    
    //Checks TLB for given entry
    int contains(int pgNum, int clock, char act)
    {
        for(int i = 0; i < size; i++)
            if(tlb[i][0]==pgNum)
                return tlb[i][0];
        
        return -1;
    }
}