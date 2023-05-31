// CPCS361 Group Project - Winter 2023 
// Virtual Memory Class (Main Class)

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class VirtualMemory {

    // Declare variables for page size & number of frames
    static int pageSize = 256;
    static int numberOfFrames;
    public static void main(String[] args) throws IndexOutOfBoundsException , FileNotFoundException {
        
        // ============================== Specifications ======================================
        // initialize number of frames
        numberOfFrames = 256;
        
        // Implement the Page Table as an Array of 2^8 entries
        int[] pageTable = new int[256];
        int logicalAddress, memValue, savedMemValue, maskedAddress,
                offset, pageNo, frameNo=-1, translatedAddress;
        
        // Create new random object to generate random numbers within a range
        Random randNo = new Random();
        
        // initialize the page table values with -1 (empty)
        initArr(pageTable);

        // Create the physical memory (Page Replacement -> new size = 128 page*256 page size)
        int memSize = pageSize * numberOfFrames;    // PageSize (256) X frames (256) = 65,536
        Memory physicalMem = new Memory(memSize , numberOfFrames , pageSize);

        // Create two arrays for test cases
        int[][] testCase1 = new int[30][2]; //[logical addresses][value]
        int[][] testCase2 = new int[80][2]; //[logical addresses][value]
        
        // Create an address string array to store the 80 address (used later for printing)
        String[] addressString = new String[80];
        

        // ============================== Files Section =======================================
        // Input & Output Files
        File memAddressesFile = new File("addresses.txt");
        File memValuesFile = new File("correct.txt");
        File memAddr4PRFile = new File("addresses for Page Replacements.txt");
        File outputFile = new File("output.txt");
        File Test1 = new File("Test1.txt");
        File Test2 = new File("Test2.txt");
        File Test3 = new File("Test3.txt");
        

        // Files existance check
        fileCheck(memAddressesFile);
        fileCheck(memValuesFile);
        fileCheck(memAddr4PRFile);
        
        // Input Scanner objects to read from files
        Scanner inputAddress = new Scanner(memAddressesFile);
        Scanner inputMemValue = new Scanner(memValuesFile);
        Scanner memAddr4PR = new Scanner(memAddr4PRFile);

        // Output PrintWriter object to write on files
        PrintWriter output = new PrintWriter(outputFile);
        PrintWriter pr1 = new PrintWriter(Test1);
        PrintWriter pr2 = new PrintWriter(Test2);
        PrintWriter pr3 = new PrintWriter(Test3);

        // =========================== Address Translation ====================================
        // Read 100 logical addresses from addresses.txt file one-by-one
        for (int i = 0; i < 100; i++) {

            // Read the logical address and mask the rightmost 16-bits
            logicalAddress = (inputAddress.nextInt()) & 65535;
            // Read memory value
            memValue = inputMemValue.nextInt();

            // Extract the page number & The offset from the logical address
            pageNo = findPageNo(logicalAddress);    // 1st 8-bits for pageNo
            offset = findOffset(logicalAddress);    // Last 8-bits for offset

            // 1- Make sure that the page number is not in the Page Table
            // 2- if it is, give a random number as a frame number
            if (pageTable[pageNo] == -1) {
                frameNo = randNo.nextInt(numberOfFrames);
                pageTable[pageNo] = frameNo;
            }
            
            // Compute the physical address ((frame number * frame size) + offset)
            translatedAddress = transLA(pageTable[pageNo] , pageSize , offset);

            // Access the frame from physical memory using the frameNo
            Frame frame = physicalMem.findFrame(pageTable[pageNo]);
            // Access the frame address from physical memory using the offset
            Address physAddress = frame.getAddress(offset);
            
            // If Phys.Address. is previously allocated find a new random number as a frame number
            while(physAddress.getValue()!=-1){
                frameNo = randNo.nextInt(numberOfFrames);
                pageTable[pageNo] = frameNo;
                frame = physicalMem.findFrame(frameNo);
                physAddress = frame.getAddress(offset);
            }
            
            // Add the value from correct file to the physical address
            physAddress.setValue(memValue);
            
            // Print the Signed Byte in the output file
            output.println(physAddress.getValue());

            if (i < 30) {
                testCase1[i][0] = logicalAddress;
                testCase1[i][1] = memValue;
            } else if (i < 80) {
                testCase2[i - 30][0] = logicalAddress;
                testCase2[i - 30][1] = memValue;
            }

        }
        
        
        // ============================== Five Test Cases =====================================
        // =============================== The First Test =====================================
        
        String s = "\n---------------- The Five Test Cases -------------------\n" + 
               String.format("%-20s%-10s%-10s%-10s%-10s%-15s\n\n" ,
                "Logical Address", "Page #", "Offset", "Frame #", "Value", "Same as model answer") ;
        System.out.print(s);
        pr1.print(s);

        for (int i = 0; i < 5; i++) {
            // Test random address
            int index = randNo.nextInt(30);

            // Get the logical address
            logicalAddress = (testCase1[index][0]) & 65535;
            // Save the memory value from file correct
            memValue = testCase1[index][1];

            // Find the page number
            pageNo = findPageNo(logicalAddress);
            // Find the offset
            offset = findOffset(logicalAddress);

            // Find the frame number from the page table
            frameNo = pageTable[pageNo];

            // Translate the logical address to the physical address
            translatedAddress = transLA(frameNo, pageSize, offset);

            // Find the frame & offset using frameNo
            Frame frame = physicalMem.findFrame(frameNo);
            Address physAddress = frame.getAddress(offset);

            // Find the saved memory value from frame
            savedMemValue = physAddress.getValue();
            
            // Check if the stored Signed Byte in phys. Mem is equal to the test Signed Byte
            String validation = (memValue == savedMemValue) ? "Yes" : "No";

            s = String.format("%-20d%-10d%-10d%-10d%-10d%-15s\n" ,
                    logicalAddress, pageNo, offset, frameNo, savedMemValue, validation);
            System.out.print(s);
            pr1.print(s);
        }
        
        // ================================ Statistics =======================================
        // ============================= The Second Test =====================================
        
        // Skip 800 readings just to make sure to read new logical addresses in the next test case
        int endIndex = randNo.nextInt(800);
        for (int i = 0; i < endIndex; i++) {
            logicalAddress = inputAddress.nextInt();//skip
        }
        
        // Read another 30 addresses different from the 100 adresses populated before
        for (int i = 50; i < 80; i++) {
            // Read address
            logicalAddress = inputAddress.nextInt();
            // Read memory value
            memValue = inputMemValue.nextInt();

            testCase2[i][0] = logicalAddress;
            testCase2[i][1] = memValue;
        }
        
        // Both types of addresses should be intermixed
        List<int[]> pair = new ArrayList<>();
        pair.addAll(Arrays.asList(testCase2));
        Collections.shuffle(pair);

        // Store logical addresses in the Adresses string (for printing)
        for (int i = 0; i < pair.size(); i++) {
            addressString[i] = "" + pair.get(i)[0];
        }

        // Print Results
        s="\n--------------------- Statistics -----------------------";
        System.out.println(s);
        pr2.println(s);
        // Create counters for page faults & hits
        int pageFault = 0;
        int pageHit = 0;

        for (int i = 0; i < 80; i++) {
            pageNo = findPageNo(pair.get(i)[0]);
            if (pageTable[pageNo] == -1) {
                //System.out.println("Output page not found :<");
                pageFault++;
            } else {
                //System.out.println("Output page is found :)");
                pageHit++;
            }
        }

        //output the length of the addresses string and the Page-fault rate—The percentage of
        //address references that resulted in page faults.
        s = "\nLength of address string is 80\n";
        System.out.println(s);
        pr2.println(s);
        for (int i = 0; i < 80; i++) {
            if (i % 10 == 0 && i != 0) { // Print 10 addresses per line
                System.out.println();
                pr2.println();
            }
            s = String.format("%-5s ", addressString[i]);
            System.out.print(s);
            pr2.print(s);

        }
        System.out.println();
        pr2.println();
        s = "\n> The Number of page faults: " + pageFault
                + "\n> The percentage of address references that resulted in page faults is %"
                + (pageFault / 80.0) * 100.0 + "\n> The Number of page hits: " + pageHit;
        System.out.println(s);
        pr2.println(s);


        // ============================== Page Replacement =====================================
        // Change the size of the physical memory to 128. generate 133 logical addresses
        Memory pageReplacementMemory = new Memory(128*256 , 128, 256); // New memory size is 32768

        // Create a new MyTestData2 (key-value pair): store addresses in random order
        // (i.e. 133 entry in the vector), for each address assign 
        // A random signed byte value
        int[][] MyTestData2 = new int[133][2];
        int[] pageTableTestData2 = new int[256];
        // Initialize pageTable with -1s
        initArr(pageTableTestData2);
        ArrayList<Integer> FIFO = new ArrayList<>();
        int victim;

        s = String.format("\n---------- The testing of the page replacement -----------\n" 
                + "\n%-20s%-15s%-15s%-15s\n\n" ,
                "Logical Address" , "New Page #" , "Victim Page #" , "Reused Frame #");
        System.out.print(s);
        pr3.print(s);

        List<Integer> frameNoArr = generateRand(128);

        for (int i = 0; i < 133; i++) {
            // Read logical address & signed byte
            MyTestData2[i][0] = memAddr4PR.nextInt();
            MyTestData2[i][1] = memAddr4PR.nextInt();
            // Extract Page number & offset
            maskedAddress = MyTestData2[i][0] & 65535;
            pageNo = findPageNo(maskedAddress);
            offset = findOffset(maskedAddress);

            // If memory is full, perform page replacement
            if (isFull(pageReplacementMemory)) {
                victim = FIFO.remove(0);                    // Choose random page to replace
                frameNo = pageTableTestData2[victim];       // Store the frameNo of the victim
                pageTableTestData2[victim] = -1;            // Set the frame# of that page to -1
                s = String.format("%-20d%-15d%-15d%-15d\n" ,
                        maskedAddress , pageNo , victim , frameNo);
                System.out.print(s);
                pr3.print(s);
                pageTableTestData2[pageNo] = frameNo;       // Add the frameNo to the page table
                FIFO.add(pageNo);
            } else {
                frameNo = frameNoArr.get(i);                // Get the randomized frameNo
                pageTableTestData2[pageNo] = frameNo;       // Store that frameNo in the page table
                pageReplacementMemory.findFrame(frameNo).setUsed(true);     //Set the frame status to 'used' in the phys.mem.
                FIFO.add(pageNo);                           // Add the pageNo to the Queue for future replacement
            }

            translatedAddress = transLA(pageTableTestData2[pageNo], 256, offset);    
            Frame frame = pageReplacementMemory.findFrame(pageTableTestData2[pageNo]);
            Address physAddress = frame.getAddress(offset);
            physAddress.setValue(MyTestData2[i][1]);        // Store the signed byte value in the PhysMem
        }

        // ================================= Print Results =======================================    
        s = String.format("\n--------- The testing of the page replacement #2 ----------\n" 
                +"\n%-20s%-15s%-15s%-15s%-15s%-20s\n" ,
                "Logical Address" , "Page #" , "Offset" , "Frame #" , "Value" , "Same as model answer");
        System.out.print(s);
        pr3.print(s);

        for (int i = 128; i < 133; i++) {
            logicalAddress = MyTestData2[i][0];
            memValue = MyTestData2[i][1];
            
            maskedAddress = logicalAddress & 65535;
            pageNo = findPageNo(maskedAddress);
            offset = findOffset(maskedAddress);
            
            frameNo = pageTableTestData2[pageNo];
            savedMemValue = pageReplacementMemory.findFrame(frameNo).getAddress(offset).getValue();
            String validation = (memValue == savedMemValue) ? "Yes" : "No";

            s = String.format("\n%-20d%-15d%-15d%-15d%-15d%-20s" ,
                maskedAddress , pageNo , offset , frameNo , savedMemValue , validation);
            System.out.print(s);
            pr3.print(s);
        }
        
        System.out.println();
        pr1.println();
        pr2.println();
        pr3.println();
        inputAddress.close();
        output.close();
        pr1.close();
        pr2.close();
        pr3.close();
    }

    // Initialize an array elements with -1
    public static void initArr(int[] arr) {
        //initialize page taple values with -1
        for (int i = 0; i < arr.length; i++) {
            arr[i] = -1;
        }
    }

    // Extract page number from 16-bit
    public static int findPageNo(int address) {
        return address / pageSize;
    }

    // Extract offset from 16-bit
    public static int findOffset(int address) {
        return address % pageSize;
    }

    // Translate Logical address to Physical address.
    public static int transLA(int frameNo , int pageSize , int offset) {
        return ((frameNo * pageSize) + offset);
    }

    // Generate a shuffled array with numbers ranged between (0-limit)
    public static List<Integer> generateRand(int limit) {
        
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers;
    }

    // Check if the memory is full or not.
    public static boolean isFull(Memory memory) {
        for (int i = 0; i < memory.getMemorySize(); i++) {
            if (!memory.findFrame(i).getUsed()) {
                //there is at least one frame free
                return false;
            }
        }
        // memory is full
        return true;
    }
    
    // A method to check whether a specific file exists or not.
    public static void fileCheck(File f){
        if (!f.exists()) {
            System.out.println("File " + f.getName() + " doesn't exist");
            System.exit(0);
        }
    }

}
