import static java.lang.System.exit;
import java.util.Scanner;

public class MemoryManagement {
    
    static Memory mainMemory;
    
    public static void main(String[] args) throws IndexOutOfBoundsException{
        
        Scanner input; String command; String[] commandInfo; 
        System.out.println("|| Welcome to our Memory Management Program ||");
        input = new Scanner(System.in);
        
        System.out.println("Please enter the memory size");
        System.out.print("./allocator ");
        int memorySize = Integer.parseInt(input.nextLine());
        mainMemory = new Memory(memorySize);
            
        while(true){
            
            // Read command from user
            System.out.print("\nallocator>");
            command = input.nextLine().toUpperCase();
            commandInfo = command.split(" ");
            switch(commandInfo[0]){
                       
                case "X": exit(0);  // Terminate the program
                
                case "RQ":  // Call RQ method
                    if(commandInfo.length != 4){
                        System.out.println("Error! Please enter a complete request (e.g., RQ P0 400 F)");
                        continue;
                    }  else if(mainMemory.findProcess(commandInfo[1]) != null){
                        System.out.println("Error! The process " + commandInfo[1] + " is already allocated in memory");
                        continue;
                    } else if (!commandInfo[2].matches("[0-9]+")){
                        System.out.println("Error! The process size should be a number!");
                        continue;
                    }
                    RequestMemLocation(commandInfo[1], Integer.parseInt(commandInfo[2]), commandInfo[3]);
                    break;
                    
                case "RL":  // Call RL Method
                    if (commandInfo.length != 2) {
                        System.out.println("Error! Please enter a complete request (e.g., RL P0)");
                        continue;
                    }
                    ReleaseMemLocation(commandInfo[1]);
                    break;
                    
                case "C":  // Call C Method
                    compact();
                    break;
                    
                case "STAT":  // Call STAT Method
                    printStatusReport();
                    break;   
                    
                default: // The user enters invalid command
                    System.out.println("\nYou are allowed only to enter:");
                    System.out.printf("%-20s\n%-20s\n%-20s\n%-20s\n%-20s\n",
                            "X >> Exit", "RQ >> Request", "RL >> Release", "C >> Compact",
                            "STAT >> Status Report");      
            }
        }
    }
    
    //------------------------ Request Memory Space ----------------------------
    public static void RequestMemLocation(String name, int size, String policy){
        
        Partition hole = null;
        if(mainMemory.getMaxHoleSize() < size){  // Not enough space 
            System.out.println("Error! Memory is full now");
            
        } else {  // Allocate the process
            Processes proc = new Processes(name, size);
            switch (policy) {
                case "F":    // First Fit
                    hole = mainMemory.getFirstFitHole(proc);
                    break;
                case "B":    // Best Fit
                    hole = mainMemory.getBestFitHole(proc);
                    break;
                case "W":    // Worst Fit
                    hole = mainMemory.getWorstFitHole(proc);
                    break;
                default:
                    System.out.println("Error! Please enter one of these Policies: F B W");
                    return;
            }
            mainMemory.allocate(hole ,proc);
        } 
        
    }
    
    //------------------------ Release Memory Space ----------------------------
    public static void ReleaseMemLocation(String name){
        
        // Check if the process exists in the memory
        Partition processPart = mainMemory.findProcess(name);
        if(processPart==null){
            // If process isn't exist, print error message
            System.out.println("Error, process is not found.");
        } else {
            // If process exists, release it
            mainMemory.deallocate(processPart);
        }
        
    }
    
    //----------------------------- Compaction ---------------------------------
    public static void compact(){
        if(mainMemory.getNumberOfHoles() == 1)
            System.out.println("No need to comapct, there is only one hole");
        else
            mainMemory.compaction();
    }
    

    //----------------------------- Status Report ------------------------------
    public static void printStatusReport() {
        mainMemory.statusReport();
    }
  
}
