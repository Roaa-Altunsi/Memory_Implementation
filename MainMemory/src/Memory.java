import java.util.ArrayList;
public class Memory {
    
    private final int MAX;
    ArrayList<Partition> memory;
    
    // Constructors
    public Memory(int memotySize){
        MAX = memotySize;
        memory = new ArrayList<>(MAX);
        memory.add(new Partition(0, MAX - 1, MAX));
    }

    // Other methods
    // Get the size of the largest hole
    public int getMaxHoleSize(){
        int maxHoleSize = 0;
            for (Partition part : memory)
                if(part.isHole() && part.getPTsize() > maxHoleSize)
                    maxHoleSize = part.getPTsize();
        return maxHoleSize;   
    } 
    
    // Get the total number of holes
    public int getNumberOfHoles(){
        int total = 0;
        for(Partition part : memory)
            if(part.isHole())
                total++;
        return total;
    }
    
    // Get first fit hole
    public Partition getFirstFitHole(Processes proc){
        for (Partition part : memory)
            if(part.isHole() && part.getPTsize() >= proc.getPSsize())
                return part;
        return null;
    }
    
    // Get best fit 
    public Partition getBestFitHole(Processes proc){
        int procSize = proc.getPSsize();
        Partition bestFit = getFirstFitHole(proc);
        
        for(Partition part : memory)
            if(part.isHole() && part.getPTsize() >= procSize &&
                    part.getPTsize() < bestFit.getPTsize())
                bestFit = part;
        return bestFit;       
    }
    
    // Get Worst fit (hole with the largest size)
    public Partition getWorstFitHole(Processes proc){
        int procSize = proc.getPSsize();
        Partition worstFit = getFirstFitHole(proc);
        
        for(Partition part : memory)
            if(part.isHole() && part.getPTsize() >= procSize && part.getPTsize() > worstFit.getPTsize())
                worstFit = part;
        return worstFit;
    }
    
    // Allocate memory to a specific process
    public void allocate(Partition part, Processes process){
        int index = memory.indexOf(part);
        int endAddress;
        
        if(part.getPTsize() == process.getPSsize()){  // holeSize = ProcessSize
            part.setHole(false);
            part.setProcess(process);
        } else {
            endAddress = part.getBase() + (process.getPSsize()-1);
            memory.add(index, new Partition(process, part.getBase(), endAddress));
            part.setBase(endAddress+1);
            part.setPTsize(part.getEndAddress()-(part.getBase()-1));  
        }
        
    }

    // Deallocate a specific process from memory 
    public void deallocate(Partition allocatedPart){
        int index = memory.indexOf(allocatedPart);
        Partition p = allocatedPart; // p is a partition pointer
        p.setHole(true);
        
        while(((--index) > -1) && (memory.get(index).isHole())){  // The second index is already decremented
            p = memory.get(index);
        }

        int sumHolesSize = 0;
        int endAddress = p.getEndAddress();
        
        int nextIndex = memory.indexOf(p)+1;
        while((nextIndex)<memory.size() && memory.get(nextIndex).isHole()){
            sumHolesSize += memory.get(nextIndex).getPTsize();
            endAddress = memory.get(nextIndex).getEndAddress();
            memory.remove(nextIndex);
        }
        
        // Combine the holes size into one element in the array & update values
        p.setPTsize(p.getPTsize() + sumHolesSize);
        p.setEndAddress(endAddress);
        p.setHole(true);
        p.setProcess(null);

    }
    
    // Search for a process in memory
    public Partition findProcess(String name){
        Partition p=null; //the partition which has the process 
        for (Partition temp : memory){ //loop through the memory
            if(!temp.isHole() && temp.getProcess().getName().equalsIgnoreCase(name)){
                p = temp;
            }
        }
        return p;
    }
    
    // Compact unused holes of memory into one region
    public void compaction() {
        // Calculate the holes sizes.
        int total = 0;
        Partition part; 

        //Loop for every partition in the memory
        for (int k=0; k < memory.size(); k++) {
            part = memory. get(k); 
            if (part.isHole()) {
                total += part.getPTsize();
                // The index after the first hole. 
                int nextIndex = memory.indexOf(part) + 1;
                // Loop from the next partition.
                for (int i = nextIndex; i < memory.size(); i++) {
                    Partition nextPart = memory.get(i);
                    // Make the processes go back one step.
                    nextPart.setBase(nextPart.getBase() - part.getPTsize());
                    nextPart.setEndAddress(nextPart.getEndAddress() - part.getPTsize());
                }
                memory.remove(part);
            }
        }
        // Get the last partition by getting the memory size.
        Partition lastPartition = memory.get(memory.size()-1);
        // Get the last base: which is the last partition end address + 1
        int lastBase = lastPartition.getEndAddress() + 1;
        memory. add( new Partition(lastBase, MAX - 1) ); 
    }
    
    // Print type of partition (Hole or Process)
    public void printMemory(){
        for(Partition part: memory){
            if(part.isHole()){
                System.out.print("Hole, ");
            } else {
                System.out.print(part.getProcess().getName() + ", ");
            }
        }
    }

    // Print a memory status report
    public void statusReport() {
        memory.forEach((part) -> {
            System.out.println("Addresses [" + part.getBase() + ":" + part.getEndAddress() + "] "
                + (part.isHole()
                ? "Unused" : ("Process " + part.getProcess().getName())));
        });
    }

}
