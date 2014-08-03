import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class ExternalSortTest {

    @org.junit.Test
    public void firstRunTest() throws Exception {
        File fileA = new File(Util.TEST_FILEA_PATH);
        File fileB = new File(Util.TEST_FILEB_PATH);
        int[] fileAContents = {2, 1, 3, 4};
        int[] fileBContents = {0, 0, 0, 0};
        int[] expectedContents = {1, 2, 3, 4};

        DoExternalSortTest(fileA, fileB, fileAContents, fileBContents, fileB, expectedContents);
    }


    @org.junit.Test
    public void duplicateValues() throws Exception {
        File fileA = new File(Util.TEST_FILEA_PATH);
        File fileB = new File(Util.TEST_FILEB_PATH);
        int[] fileAContents = {8, 7, 6, 5, 4, 3, 2, 1};
        int[] fileBContents = {0, 0, 0, 0, 0, 0, 0, 0};
        int[] resultContents = {1, 2, 3, 4, 5, 6, 7, 8};

        DoExternalSortTest(fileA, fileB, fileAContents, fileBContents, fileB, resultContents);
    }

    @org.junit.Test
    public void largerSource() throws Exception {
        File fileA = new File(Util.TEST_FILEA_PATH);
        File fileB = new File(Util.TEST_FILEB_PATH);
        int[] fileAContents = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] fileBContents = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] resultContents = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        DoExternalSortTest(fileA, fileB, fileAContents, fileBContents, fileB, resultContents);
    }

    @org.junit.Test
    public void nonPowerOfTwo() throws Exception {
        File fileA = new File(Util.TEST_FILEA_PATH);
        File fileB = new File(Util.TEST_FILEB_PATH);
        int[] fileAContents = {3, 2, 1};
        int[] fileBContents = {0, 0, 0};
        int[] resultContents = {1, 2, 3};

        DoExternalSortTest(fileA, fileB, fileAContents, fileBContents, fileB, resultContents);
    }

    private void DoExternalSortTest(File fileA, File fileB, int[] fileAContents, int[] fileBContents, File resultFile, int[] resultContents) throws IOException {
        Util.writeIntsToTestFile(fileA, fileAContents);
        Util.writeIntsToTestFile(fileB, fileBContents);
        DoTestSort(fileA, fileB);
        assertArrayEquals(resultContents, Util.getIntsFromTestFile(resultFile));
        fileA.delete();
        fileB.delete();
    }

    private void DoTestSort(File fileA, File fileB) throws IOException {
        ExternalSort.sort(fileA.getPath(), fileB.getPath());
    }
}