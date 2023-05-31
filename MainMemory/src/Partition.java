public class Partition {
    
    private boolean hole = true;        // Default case
    private int base;
    private int endAddress;
    private int PTsize;
    private Processes process;
    
    
    // Constructor
    public Partition(int base, int endAddress, int size){    // Defaulf constructor
        this.base = base;
        this.endAddress = endAddress;
        this.PTsize = size;
    }
    
    public Partition(Processes proc, int base, int endAddress){
        hole = false;
        process = proc;
        this.base = base;
        this.endAddress = endAddress;
        this.PTsize = endAddress - base + 1;
    }
    
    public Partition(int base, int endAddress){     // Constructor for compact method
        this.base = base;
        this.endAddress = endAddress;
        this.PTsize = endAddress - base + 1;
    }
    
    
    // Getters & Setters
    public boolean isHole() {
        return hole;
    }

    public int getBase() {
        return base;
    }

    public int getEndAddress() {
        return endAddress;
    }

    public int getPTsize() {
        return PTsize;
    }
    
    public Processes getProcess() {
        return process;
    }
    
    public void setBase(int base) {
        this.base = base;
    }

    public void setHole(boolean hole) {
        this.hole = hole;
    }

    public void setEndAddress(int endAddress) {
        this.endAddress = endAddress;
    }

    public void setProcess(Processes process) {
        this.process = process;
    }

    public void setPTsize(int PRsize) {
        this.PTsize = PRsize;
    }
    
}
