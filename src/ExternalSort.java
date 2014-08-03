import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ExternalSort {

    private static int NUM_BYTES_IN_INT = 4;
    private static int DIV_SIZE_IN_BYTES = 24;

    public static void main(String[] args) throws Exception {

    }

    public static void sort(String fileAPath, String fileBPath) throws FileNotFoundException, IOException {

        //TODO: Allocate based on available memory.
        byte[] divA = new byte[DIV_SIZE_IN_BYTES];
        byte[] divB = new byte[DIV_SIZE_IN_BYTES];

        //TODO: Put on Github/Backup.
        //TODO: Need to actually restrict chunk size / availible size!

        File fileA = new File(fileAPath);
        File fileB = new File(fileBPath);

        File sourceFile = fileA;
        File destinationFile = fileB;
        File tempFile;

        int numberOfInts = (int) fileA.length();

        //TODO: Remove extra runs.
        for (int runNumber = 0; runNumber < numberOfRunsRequired(numberOfInts); runNumber++) {
            doRun(sourceFile, destinationFile, runNumber, divA, divB);
            //Swap files
            tempFile = destinationFile;
            destinationFile = sourceFile;
            sourceFile = tempFile;
            //
            System.out.println(Util.intsFromBothFiles());
        }
    }

    private static void doRun(File sourceFile, File desinationFile, int runNumber, byte[] divA, byte[] divB) throws IOException {

        DataInputStream sourceInputStreamLeft = new DataInputStream(new BufferedInputStream(new FileInputStream(new RandomAccessFile(sourceFile, "rw").getFD())));
        DataInputStream sourceInputStreamRight = new DataInputStream(new BufferedInputStream(new FileInputStream(new RandomAccessFile(sourceFile, "rw").getFD())));
        DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new RandomAccessFile(desinationFile, "rw").getFD())));

        int runSizeInBytes = (int) Math.pow(2, runNumber) * NUM_BYTES_IN_INT;
        //TODO Make smaller than run size
        int chunkSizeInBytes = runSizeInBytes;
        int numberOfChunksForRun = (int)Math.ceil((double) runSizeInBytes / (double) chunkSizeInBytes);
        int chunksLeftForDivA = numberOfChunksForRun;
        int chunksLeftForDivB = numberOfChunksForRun;

        int numIntsInDivA = -1;
        int numIntsInDivB = -1;
        IntBuffer divAIntBuffer = ByteBuffer.wrap(divA).asIntBuffer();
        IntBuffer divBIntBuffer = ByteBuffer.wrap(divB).asIntBuffer();

        System.out.println("Run size in bytes: " + runSizeInBytes);

        while (chunksLeftForDivA > 0 || chunksLeftForDivB > 0) {

            //Load next chunk if needed
            if (divAIntBuffer.position() > numIntsInDivA) {
                if (chunksLeftForDivA > 0) {
                    numIntsInDivA = readChunkIntoDivA(divA, sourceInputStreamLeft, chunkSizeInBytes);
                    chunksLeftForDivA -= 1;
                    divAIntBuffer.position(0);
                }
            }

            if (divBIntBuffer.position() > numIntsInDivB) {
                if (chunksLeftForDivB > 0) {
                    numIntsInDivB = readChunkIntoDivB(divB, sourceInputStreamRight, chunkSizeInBytes);
                    chunksLeftForDivB -= 1;
                    divBIntBuffer.position(0);
                }
            }

            if(numIntsInDivA == 0 && numIntsInDivB == 0) {
                break;
            }

            if(numIntsInDivA == 0) {
                while(numIntsInDivB > 0) {
                    outputStream.writeInt(divBIntBuffer.get());
                    numIntsInDivB -= 1;
                }
                break;
            }

            if(numIntsInDivB == 0) {
                while(numIntsInDivA > 0) {
                    outputStream.writeInt(divAIntBuffer.get());
                    numIntsInDivA -= 1;
                }
                break;
            }

            int AValue = divAIntBuffer.get();
            numIntsInDivA -= 1;
            int BValue = divBIntBuffer.get();
            numIntsInDivB -= 1;

            do {
                if(AValue < BValue)
                {
                    outputStream.writeInt(AValue);
                    if(numIntsInDivA > 0) {
                        AValue = divAIntBuffer.get();
                        numIntsInDivA -= 1;
                    }
                }
                else
                {
                    outputStream.writeInt(BValue);
                    if(numIntsInDivB > 0) {
                        BValue = divBIntBuffer.get();
                        numIntsInDivB -= 1;
                    }
                }
            } while (!(numIntsInDivA == 0 || numIntsInDivB == 0));

            outputStream.flush();
        }

        sourceInputStreamLeft.close();
        sourceInputStreamRight.close();
        outputStream.close();
    }

    private static int readChunkIntoDivB(byte[] divB, DataInputStream sourceInputStreamRight, int chunkSizeInBytes) throws IOException {
        int divBBytesRead;
        sourceInputStreamRight.skipBytes(chunkSizeInBytes);
        divBBytesRead = readChunk(sourceInputStreamRight, divB, chunkSizeInBytes);
        return (divBBytesRead / NUM_BYTES_IN_INT);
    }

    private static int readChunkIntoDivA(byte[] divA, DataInputStream sourceInputStreamLeft, int chunkSizeInBytes) throws IOException {
        int divABytesRead;
        divABytesRead = readChunk(sourceInputStreamLeft, divA, chunkSizeInBytes);
        sourceInputStreamLeft.skipBytes(chunkSizeInBytes);
        return (divABytesRead / NUM_BYTES_IN_INT);
    }

    private static int readChunk(DataInputStream inputStream, byte[] targetByteArray, int chunkSize) throws IOException {
        return inputStream.read(targetByteArray, 0, chunkSize);
    }

    private static int numberOfRunsRequired(int numberOfInts) {
        return (int) (Math.log(numberOfInts) / Math.log(2));
    }

    //TODO: Try these - should be more optimal?
    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    //Provided Checksum Implementation
    public static String checkSum(String f) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestInputStream ds = new DigestInputStream(new FileInputStream(f), md);
            byte[] b = new byte[512];
            while (ds.read(b) != -1) ;
            String computed = "";
            for (byte v : md.digest())
                computed += byteToHex(v);

            return computed;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "<error computing checksum>";
    }

    private static String byteToHex(byte b) {
        String r = Integer.toHexString(b);
        if (r.length() == 8) {
            return r.substring(6);
        }
        return r;
    }
}
