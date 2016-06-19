public class PageTable
{
    int table[][];
    int pointer;
    
    PageTable()
    {
        //First column = page number
        //Second column = clock (for LRU)
        //Third column = dirty bit
        table = new int[256][3];
        
        pointer = 0; //Address of first empty location
        
        for(int x[] : table)
            for(int y : x)
                y = -1;
    }
    
    //Adds new page to table.
    int add(int pgNum, int clock, char act)
    {
        //If Page Table is not full, add new page.
        if(pointer!=256)
        {
            table[pointer][0] = pgNum;
            table[pointer][1] = clock;
            table[pointer][2] = act=='W' ? 1 : 0;
            pointer++;
            return -1;
        }
        
        //If Page Table is full, find LRU page
        int lruID = 0;
        int old;
        for(int i = 1; i<256; i++)
            if(table[i][1]<table[lruID][1])
                lruID = i;
        
        old = table[lruID][0];
        
        //Replace LRU page with new page
        table[lruID][0] = pgNum;
        table[lruID][1] = clock;
        table[pointer][2] = act=='W' ? 1 : 0;
        
        return lruID;
    }
    
    //Checks if table contains given page.
    int contains(int pgNum, int clock, char act)
    {
        for(int i = 0; i <=pointer; i++)
            if(table[i][0]==pgNum)
                return i;
        
        return -1;
    }
}