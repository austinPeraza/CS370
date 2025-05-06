import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class UnitTests {
    // create the artificial datasets
    private RecordCollection artificalDataset1 = new RecordCollection();
    private RecordCollection artificalDataset2 = new RecordCollection();

    // helper method to initialize the artifical datasets
    private void initializeArtificalDatasets() {
        artificalDataset1 = new RecordCollection();
        artificalDataset2 = new RecordCollection();

        artificalDataset1.add(new Record("Female", 22, 0, 3, "5-6 hours", "Unhealthy", false, 6, 4, false, 0));
        artificalDataset1.add(new Record("Male", 34, 3, 3, "Less than 5 hours", "Healthy", false, 7, 3, true, 0));
        artificalDataset1.add(new Record("Male", 19, 3, 2, "7-8 hours", "Unhealthy", true, 4, 2, false, 1));
        artificalDataset1.add(new Record("Female", 27, 2, 5, "Less than 5 hours", "Moderate", false, 8, 5, false, 0));
        artificalDataset1.add(new Record("Male", 36, 4, 2, "Less than 5 hours", "Unhealthy", false, 12, 4, true, 0));
        artificalDataset1.add(new Record("Other", 23, 2, 1, "5-6 hours", "Moderate", true, 5, 4, true, 1));
        artificalDataset1.add(new Record("Female", 31, 1, 3, "7-8 hours", "Moderate", true, 2, 1, false, 1));
        artificalDataset1.add(new Record("Male", 41, 5, 4, "More than 8 hours", "Healthy", false, 10, 1, true, 0));

        artificalDataset2.add(new Record("Female", 33, 1, 1, "More than 8 hours", "Healthy", true, 5, 2, false, 0));
        artificalDataset2.add(new Record("Female", 25, 2, 4, "5-6 hours", "Healthy", false, 8, 4, true, 0));
        artificalDataset2.add(new Record("Male", 28, 4, 2, "More than 8 hours", "Healthy", true, 6, 5, true, 1));
        artificalDataset2.add(new Record("Female", 18, 3, 4, "More than 8 hours", "Healthy", true, 9, 2, false, 1));
        artificalDataset2.add(new Record("Male", 19, 3, 5, "Less than 5 hours", "Moderate", true, 8, 3, true, 1));
        artificalDataset2.add(new Record("Male", 30, 1, 3, "7-8 hours", "Unhealthy", false, 7, 2, true, 0));
        artificalDataset2.add(new Record("Female", 31, 3, 3, "5-6 hours", "Unhealthy", false, 12, 2, false, 0));
        artificalDataset2.add(new Record("Male", 32, 5, 1, "Less than 5 hours", "Moderate", false, 4, 1, false, 1));
    }

    // UNIT TESTS FOR NODE
    @Test
    public void splitBooleanAttribute() {
        // create expected outcome
        RecordCollection expectedLeftSubset = new RecordCollection();
        expectedLeftSubset.add(new Record("Male", 19, 3, 2, "7-8 hours", "Unhealthy", true, 4, 2, false, 1));
        expectedLeftSubset.add(new Record("Other", 23, 2, 1, "5-6 hours", "Moderate", true, 5, 4, true, 1));
        expectedLeftSubset.add(new Record("Female", 31, 1, 3, "7-8 hours", "Moderate", true, 2, 1, false, 1));
        Node expectedLeftChild = new Node(expectedLeftSubset);

        RecordCollection expectedRightSubset = new RecordCollection();
        expectedRightSubset.add(new Record("Female", 22, 0, 3, "5-6 hours", "Unhealthy", false, 6, 4, false, 0));
        expectedRightSubset.add(new Record("Male", 34, 3, 3, "Less than 5 hours", "Healthy", false, 7, 3, true, 0));
        expectedRightSubset.add(new Record("Female", 27, 2, 5, "Less than 5 hours", "Moderate", false, 8, 5, false, 0));
        expectedRightSubset.add(new Record("Male", 36, 4, 2, "Less than 5 hours", "Unhealthy", false, 12, 4, true, 0));
        expectedRightSubset.add(new Record("Male", 41, 5, 4, "More than 8 hours", "Healthy", false, 10, 1, true, 0));
        Node expectedRightChild = new Node(expectedRightSubset);

        // create actual outcome
        Node actualNode = new Node();
        initializeArtificalDatasets();
        actualNode.split(artificalDataset1);

        // assert that the children are equal
        assertEquals(expectedLeftChild, actualNode.getLeftChild());
        assertEquals(expectedRightChild, actualNode.getRightChild());
    }
}