import java.io.*;
import java.util.Arrays;

public class Util {

    public static String TEST_FILEA_PATH = "E:\\ExternalSort\\testResources\\testFileA.dat";
    public static String TEST_FILEB_PATH = "E:\\ExternalSort\\testResources\\testFileB.dat";

    public static String intsFromBothFiles() throws IOException {
        return "FileA: " + Arrays.toString(getIntsFromTestFile(new File(TEST_FILEA_PATH))) + "  FileB: " + Arrays.toString(getIntsFromTestFile(new File(TEST_FILEB_PATH)));
    }

    public static void writeIntsToTestFile(File file, int[] testNumbers) throws IOException {
        DataOutputStream data = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new RandomAccessFile(file,"rw").getFD())));

        for (int number : testNumbers) {
            data.writeInt(number);
        }

        data.flush();
        data.close();
    }

    public static int[] getIntsFromTestFile(File testFile) throws IOException {
        RandomAccessFile file = new RandomAccessFile(testFile,"rw");
        DataInputStream data = new DataInputStream(new BufferedInputStream(new FileInputStream(file.getFD())));

        int numInts = (int)file.length()/4;
        int[] ints = new int[numInts];
        for(int index = 0; index < numInts; index ++) {
            ints[index] = data.readInt();
        }

        data.close();
        return ints;
    }
}
