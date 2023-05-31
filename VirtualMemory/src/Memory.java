
import java.util.ArrayList;

public class Memory {

    ArrayList<Frame> memory;

    // Constructors
    public Memory(int memSize , int numberOfFrames , int frameSize) {
        memory = new ArrayList<>(numberOfFrames);

        // create frames
        int base = 0;
        int endAddress = frameSize;
        for (int i = 0; i < numberOfFrames; i++) {
            memory.add(new Frame(i, base, endAddress, frameSize));
            base += frameSize;
            endAddress += frameSize;
        }
    }

    // Other methods
    // Get memory size (number of partitions)
    public int getMemorySize() {
        return memory.size();
    }

    // Get frame by frame number
    public Frame findFrame(int frameNo) {
        for (Frame frame : memory) { //loop through the memory
            if (frame.getFrameNo() == frameNo) {
                return frame;
            }
        }
        return null;
    }

    public boolean isFull() {
        for (int i = 0; i < memory.size(); i++) {
            if (!memory.get(i).getUsed()) {
                //there is at least one frame free
                return false;
            }
        }
        // memory is full
        return true;
    }
}
