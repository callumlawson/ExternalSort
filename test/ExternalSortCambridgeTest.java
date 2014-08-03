import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static org.junit.Assert.assertEquals;

public class ExternalSortCambridgeTest {

    private String TEST_RESOURCE_PATH = "E:\\ExternalSort\\testResources\\";

    @org.junit.Test
    public void emptySort() throws Exception {
        runTestWithChecksum(1, "d41d8cd98f0b24e980998ecf8427e");
    }

    @org.junit.Test
    public void negitiveNumber() throws Exception {
        runTestWithChecksum(2, "a54f041a9e15b5f25c463f1db7449");
    }

    @org.junit.Test
    public void test3() throws Exception {
        runTestWithChecksum(3, "c2cb56f4c5bf656faca0986e7eba38");
    }

    @org.junit.Test
    public void test4() throws Exception {
        runTestWithChecksum(4, "c1fa1f22fa36d331be4027e683baad6");
    }

    @org.junit.Test
    public void test5() throws Exception {
        runTestWithChecksum(5, "8d79cbc9a4ecdde112fc91ba625b13c2");
    }

    @org.junit.Test
    public void test6() throws Exception {
        runTestWithChecksum(6, "1e52ef3b2acef1f831f728dc2d16174d");
    }

    @org.junit.Test
    public void test7() throws Exception {
        runTestWithChecksum(7, "6b15b255d36ae9c85ccd3475ec11c3");
    }

    @org.junit.Test
    public void test8() throws Exception {
        runTestWithChecksum(8, "1484c15a27e48931297fb6682ff625");
    }

    @org.junit.Test
    public void test9() throws Exception {
        runTestWithChecksum(9, "ad4f60f065174cf4f8b15cbb1b17a1bd");
    }

    @org.junit.Test
    public void test10() throws Exception {
        runTestWithChecksum(10, "32446e5dd58ed5a5d7df2522f0240");
    }

    @org.junit.Test
    public void test11() throws Exception {
        runTestWithChecksum(11, "435fe88036417d686ad8772c86622ab");
    }

    @org.junit.Test
    public void test12() throws Exception {
        runTestWithChecksum(12, "c4dacdbc3c2e8ddbb94aac3115e25aa2");
    }

    @org.junit.Test
    public void test13() throws Exception {
        runTestWithChecksum(13, "3d5293e89244d513abdf94be643c630");
    }

    @org.junit.Test
    public void test14() throws Exception {
        runTestWithChecksum(14, "468c1c2b4c1b74ddd44ce2ce775fb35c");
    }

    @org.junit.Test
    public void test15() throws Exception {
        runTestWithChecksum(15, "79d830e4c0efa93801b5d89437f9f3e");
    }

    @org.junit.Test
    public void test16() throws Exception {
        runTestWithChecksum(16, "c7477d400c36fca5414e0674863ba91");
    }

    @org.junit.Test
    public void test17() throws Exception {
        runTestWithChecksum(16, "cc80f01b7d2d26042f3286bdeff0d9");
    }

    private void runTestWithChecksum(int testNumber, String checksum) throws IOException {
        copyFile(fileAPath(testNumber), Util.TEST_FILEA_PATH);
        copyFile(fileBPath(testNumber), Util.TEST_FILEB_PATH);
        ExternalSort.sort(Util.TEST_FILEA_PATH, Util.TEST_FILEB_PATH);
        assertEquals(checksum, ExternalSort.checkSum(Util.TEST_FILEA_PATH));
    }

    private void copyFile(String sourceFile, String destinationFile) throws IOException {
        new File(destinationFile).delete();
        FileChannel src = new FileInputStream(sourceFile).getChannel();
        FileChannel dest = new FileOutputStream(destinationFile).getChannel();
        dest.transferFrom(src, 0, src.size());
        src.close();
        dest.close();
    }

    private String fileAPath(int testNumber) {
        return TEST_RESOURCE_PATH + "test" + testNumber + "a.dat";
    }

    private String fileBPath(int testNumber) {
        return TEST_RESOURCE_PATH + "test" +  testNumber + "b.dat";
    }
}