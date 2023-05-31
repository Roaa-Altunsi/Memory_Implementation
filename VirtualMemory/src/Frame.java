
public class Frame {
    
    private int frameNo;
    private int base;
    private int endAddress;
    private int frameSize;
    private Address[] addresses;
    private boolean used = false;
    
    
    // Constructor
    public Frame(int frameNo, int base, int endAddress, int frameSize){
        this.frameNo = frameNo;
        this.base = base;
        this.endAddress = endAddress;
        this.frameSize = frameSize;
        
        // set addresses value
        addresses = new Address[this.frameSize];
        int address = base;
        for (int i = 0; i < frameSize; i++) {
            addresses[i] = new Address(address++);
        }
    }
    
    
    // Getters & Setters
    public int getBase() {
        return base;
    }

    public int getEndAddress() {
        return endAddress;
    }

    public int getSize() {
        return frameSize;
    }
    
    public Address getAddress(int index) {
        return addresses[index];
    }
    
    public int getFrameNo() {
        return this.frameNo;
    }
    
    public boolean getUsed() {
        return this.used;
    }
    
    public void setUsed(boolean used) {
        this.used = used;
    }
    
    public void setFrameNo(int frameNo) {
        this.frameNo = frameNo;
    }
    
    public void setBase(int base) {
        this.base = base;
    }

    public void setEndAddress(int endAddress) {
        this.endAddress = endAddress;
    }

    public void setAddress(int index, Address newAddress) {
        this.addresses[index] = newAddress;
    }

    public void setSize(int size) {
        this.frameSize = size;
    }
   
}
