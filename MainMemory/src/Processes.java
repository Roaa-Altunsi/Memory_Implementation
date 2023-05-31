public class Processes {
    
    private String name;
    private int PSsize;
    
    
    // Constructor
    public Processes(String name, int size){
        this.name = name;
        this.PSsize = size;
    }
    
    // Getters & Setters
    public String getName() {
        return name;
    }

    public int getPSsize() {
        return PSsize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPSsize(int PSsize) {
        this.PSsize = PSsize;
    }
   
}
