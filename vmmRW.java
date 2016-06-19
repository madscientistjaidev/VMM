import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.System.out;
import java.util.Scanner;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;

public class vmmRW
{
    //Structures
    PageTable PT = new PageTable();
    TLB tlb = new TLB(16);
    PhyMem PM;
    boolean RW;
    
    //Path of address file
    String path;
    
    vmmRW(String path, int memSize, boolean RW)
    {
        this.path = path;
        PM = new PhyMem(memSize);
        this.RW = RW;
    }
    
    int []run()
    {
        //Loading addresses.txt
        Scanner sc = null;
        try {sc = new Scanner(new File(path));}
        catch (FileNotFoundException ex) {Logger.getLogger(vmm.class.getName()).log(SEVERE, null, ex);}
        
        //Loading BACKING_STORE.bin
        RandomAccessFile backStore = null;
        try {backStore = new RandomAccessFile(new File("BACKING_STORE.bin"), "r");}
        catch (FileNotFoundException ex) {Logger.getLogger(vmm.class.getName()).log(SEVERE, null, ex);}

        //Address variables
        int virAddr;
        int phyAddr = -1;
        int pgNum;
        int offset;
        int frameNo;
        
        //Used to parse input
        String line;
        char act;
        
        //To measure statistics
        int tlbHit = 0;
        int pgFault = 0;
        int writeBackCount = 0;
        
        int clock = 0;  //Clock for LRU
        
        int writeBack = -1;
        
        byte newFrame[] = new byte[256];
        
        while(sc.hasNextLine())
        {
            //Parsing input
            line = sc.nextLine();
            act='R';
            if(RW)
            {
                act = line.toCharArray()[line.length()-1];
            }
            virAddr = new Scanner(line).nextInt();
            
            //Getting addresses
            pgNum = virAddr >> 8;
            offset = virAddr & 0x00FF;
            
            //Check in TLB
            if((frameNo=tlb.contains(pgNum, clock, act))!=-1)
            {
                writeBack = pgNum;
                tlbHit++;
            }
            
            //Check in Page Table
            else if(PT.contains(pgNum, clock, act) != -1)
            {
                frameNo = PM.contains(pgNum, clock, act);
                tlb.add(pgNum, frameNo, clock, act);
                
            }
            
            //Fetch from Backing Store
            else
            {
                pgFault++;
                
                PT.add(pgNum, clock, act);
                
                try {backStore.seek(virAddr);}
                catch (IOException ex) {Logger.getLogger(vmm.class.getName()).log(SEVERE, null, ex);}
                
                frameNo = PM.add(pgNum, clock, act);
                
                tlb.add(pgNum, frameNo, clock, act);
                
                phyAddr = offset + (frameNo*256) - 256;
            }
            
            //Print output
            try
            {
                backStore.seek(pgNum*256);
                                
                out.print("Virtual address: " + virAddr + "\tPhysical address: " + phyAddr +  "\tValue: " + backStore.readByte());
                
            }
            catch (IOException ex) {Logger.getLogger(vmm.class.getName()).log(SEVERE, null, ex);}
            
            //Read-Write indication
            if(RW)
            {
                out.print("\tAction: " + act);
                if(writeBack !=-1)
                {
                    writeBackCount++;
                    out.print("\tWriteback to: " + writeBack);
                }
            }
            
            System.out.println();
            
            writeBack = -1;
            clock++;
        }
        
        //Closing BACKING_STORE.bin
        try {backStore.close();}
        catch (IOException ex) {Logger.getLogger(vmm.class.getName()).log(SEVERE, null, ex);}
        sc.close();
        
        //Returning statistics
        int stats[] = {clock, tlbHit, pgFault, writeBackCount};
        return stats;
    }
}