
public class Address {

    private int addressNo;
    private int value;
    private int frameNo;

    // Constructor
    public Address(int addressNo) {
        this.addressNo = addressNo;
        this.value = -1;
    }
    
    public Address(int addressNo , int value) {
        this.addressNo = addressNo;
        this.value = value;
    }

    // Getters & Setters
    public int getAddressNo() {
        return addressNo;
    }

    public int getValue() {
        return value;
    }
    
    public int getFrameNo() {
        return frameNo;
    }

    public void setFrameNo(int frameNo) {
        this.frameNo = frameNo;
    }

    public void setAddressNo(int addressNo) {
        this.addressNo = addressNo;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
