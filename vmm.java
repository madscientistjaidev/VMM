import static java.lang.System.in;
import static java.lang.System.out;
import java.util.Scanner;

public class vmm
{
    public static void main(String args[])
    {
        Scanner ui = new Scanner(in);
        int opt;
        int stats[] = null; //To hold statistics
        
        out.println("Select simulation type");
        out.println("1. Physical memory size = 256");
        out.println("2. Physical memory size = 128");
        out.println("3. Read/Write bits in address file");
        out.println("4. Exit");
        
        opt = ui.nextInt();
        while(opt != 1 && opt !=2 && opt !=3 && opt!=4)
        {
            out.println("Invalid input. Try again.");
            opt = ui.nextInt();
        }
        
        vmmRW obj;
        
        switch(opt)
        {
            case 1: obj = new vmmRW(args[0], 256, false);
                    stats = obj.run();
                    break;
                
            case 2: obj = new vmmRW(args[0], 128, false);
                    stats = obj.run();
                    break;
                            
            case 3: obj = new vmmRW(args[0], 256, true);
                    stats = obj.run();
                    break;
            
            case 4: return;
                    
            default:
                    break;
        }
        
        //Calculating statistics
        double tlbHitRate = (double)stats[1]/(double)stats[0]*100;
        double pageFaultRate = (double)stats[2]/(double)stats[0]*100;
        double writeBackRate = (double)stats[3]/(double)stats[0]*100;
        
        System.out.println("\nStatistics");
        System.out.println("Total operations = " + stats[0]);
        System.out.println("TLB Hits = " + stats[1] + " >> " + Double.toString(tlbHitRate)+"%");
        System.out.println("Page Faults = " + stats[2] + " >> " + Double.toString(pageFaultRate)+"%");
        if(stats[3]!=0)
            System.out.println("WriteBacks = " + stats[3] + " >> " + Double.toString(writeBackRate)+"%");
    }
}