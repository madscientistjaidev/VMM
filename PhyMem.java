public class PhyMem
{
    int pm[][];
    byte data[][];
    int pointer;
    int size;
    
    //Checks if memory is full
    boolean isFull() {return pointer==size;}
    
    PhyMem(int size)
    {
        this.size = size;
        pm = new int[size][3];
        data = new byte[size][256];
    }
    
    //Adds new frame to memory
    int add(int pgNum, int clock, char act)
    {
        int old;
        
        //If memory is not full
        if(pointer!=size)
        {
            pm[pointer][0] = pgNum;
            pm[pointer][1] = clock;
            pm[pointer][2] = act=='W' ? 1 : 0;
            pointer++;
            return pointer;
        }
        
        //If memory is full, replace LRU frame
        int lruID = 0;
        
        for(int i = 1; i<size; i++)
            if(pm[i][2]<pm[lruID][2])
                lruID = i;
        
        old = pm[lruID][0];
        pm[lruID][0] = pgNum;
        pm[lruID][1] = clock;
        pm[lruID][2] = act=='W' ? 1 : 0;
        
        return old;
    }
    
    //Checks if memory contains given frame.
    int contains(int pgNum, int clock, char act)
    {
        for(int i = 0; i <size; i++)
            if(pm[i][0]==pgNum)
            {
                pm[i][1] = clock;
                pm[i][2] = act=='W' ? 1 : 0;
                return i;
            }

        return -1;
    }
}