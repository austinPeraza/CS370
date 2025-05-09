import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class UnitTests {
    // create the artificial datasets
    private RecordCollection artificialDataset1 = new RecordCollection();
    private RecordCollection artificialDataset2 = new RecordCollection();
    private RecordCollection artificialDataset3 = new RecordCollection();

    // helper method to initialize the artificial datasets
    private void initializeArtificialDatasets() {
        artificialDataset1 = new RecordCollection();
        artificialDataset2 = new RecordCollection();
        artificialDataset3 = new RecordCollection();

        artificialDataset1.add(new Record("Female", 22, 0, 3, "5-6 hours", "Unhealthy", false, 6, 4, false, 0));
        artificialDataset1.add(new Record("Male", 34, 3, 3, "Less than 5 hours", "Healthy", false, 7, 3, true, 0));
        artificialDataset1.add(new Record("Male", 19, 3, 2, "7-8 hours", "Unhealthy", true, 4, 2, false, 1));
        artificialDataset1.add(new Record("Female", 27, 2, 5, "Less than 5 hours", "Moderate", false, 8, 5, false, 0));
        artificialDataset1.add(new Record("Male", 36, 4, 2, "Less than 5 hours", "Unhealthy", false, 12, 4, true, 0));
        artificialDataset1.add(new Record("Other", 23, 2, 1, "5-6 hours", "Moderate", true, 5, 4, true, 1));
        artificialDataset1.add(new Record("Female", 31, 1, 3, "7-8 hours", "Moderate", true, 2, 1, false, 1));
        artificialDataset1.add(new Record("Male", 41, 5, 4, "More than 8 hours", "Healthy", false, 10, 1, true, 0));

        artificialDataset2.add(new Record("Female", 33, 1, 1, "More than 8 hours", "Healthy", true, 5, 2, false, 0));
        artificialDataset2.add(new Record("Female", 25, 2, 4, "5-6 hours", "Healthy", false, 8, 4, true, 0));
        artificialDataset2.add(new Record("Male", 28, 4, 2, "More than 8 hours", "Healthy", true, 6, 5, true, 1));
        artificialDataset2.add(new Record("Female", 18, 3, 4, "More than 8 hours", "Healthy", true, 9, 2, false, 1));
        artificialDataset2.add(new Record("Male", 19, 3, 5, "Less than 5 hours", "Moderate", true, 8, 3, true, 1));
        artificialDataset2.add(new Record("Male", 30, 1, 3, "7-8 hours", "Unhealthy", false, 7, 2, true, 0));
        artificialDataset2.add(new Record("Female", 31, 3, 3, "5-6 hours", "Unhealthy", false, 12, 2, false, 0));
        artificialDataset2.add(new Record("Male", 32, 5, 1, "Less than 5 hours", "Moderate", false, 4, 1, false, 1));

        artificialDataset3.add(new Record("Female", 15, 4, 2, "More than 8 hours", "Healthy", false, 8, 3, true, 0));
        artificialDataset3.add(new Record("Male", 39, 2, 5, "5-6 hours", "Healthy", true, 6, 4, false, 0));
        artificialDataset3.add(new Record("Female", 23, 1, 3, "More than 8 hours", "Healthy", false, 2, 1, true, 0));
        artificialDataset3.add(new Record("Male", 32, 5, 4, "Less than 5 hours", "Healthy", true, 4, 5, false, 0));
        artificialDataset3.add(new Record("Female", 19, 3, 1, "7-8 hours", "Moderate", false, 1, 3, true, 1));
        artificialDataset3.add(new Record("Male", 50, 1, 5, "7-8 hours", "Unhealthy", true, 5, 4, false, 1));
        artificialDataset3.add(new Record("Female", 31, 2, 3, "5-6 hours", "Unhealthy", false, 7, 2, true, 1));
        artificialDataset3.add(new Record("Male", 28, 5, 2, "Less than 5 hours", "Moderate", true, 3, 1, false, 1));
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
        initializeArtificialDatasets();
        actualNode.split(artificialDataset1);

        // assert that the children are equal & that the node split on suicidal thoughts
        assertEquals("suicidalThoughts", actualNode.getAttribute());
        assertEquals(expectedLeftChild, actualNode.getLeftChild());
        assertEquals(expectedRightChild, actualNode.getRightChild());
    }

    @Test
    public void splitNumericalAttribute() {
        // create expected outcome
        RecordCollection expectedLeftSubset = new RecordCollection();
        expectedLeftSubset.add(new Record("Female", 33, 1, 1, "More than 8 hours", "Healthy", true, 5, 2, false, 0));
        expectedLeftSubset.add(new Record("Female", 25, 2, 4, "5-6 hours", "Healthy", false, 8, 4, true, 0));
        expectedLeftSubset.add(new Record("Male", 30, 1, 3, "7-8 hours", "Unhealthy", false, 7, 2, true, 0));
        Node expectedLeftChild = new Node(expectedLeftSubset);

        RecordCollection expectedRightSubset = new RecordCollection();
        expectedRightSubset.add(new Record("Male", 28, 4, 2, "More than 8 hours", "Healthy", true, 6, 5, true, 1));
        expectedRightSubset.add(new Record("Female", 18, 3, 4, "More than 8 hours", "Healthy", true, 9, 2, false, 1));
        expectedRightSubset.add(new Record("Male", 19, 3, 5, "Less than 5 hours", "Moderate", true, 8, 3, true, 1));
        expectedRightSubset.add(new Record("Female", 31, 3, 3, "5-6 hours", "Unhealthy", false, 12, 2, false, 0));
        expectedRightSubset.add(new Record("Male", 32, 5, 1, "Less than 5 hours", "Moderate", false, 4, 1, false, 1));
        Node expectedRightChild = new Node(expectedRightSubset);

        // create actual outcome
        Node actualNode = new Node();
        initializeArtificialDatasets();
        actualNode.split(artificialDataset2);

        // assert that the children are equal & that the node split on academic pressure
        // with the value 3
        assertEquals("academicPressure", actualNode.getAttribute());
        assertEquals(3, actualNode.getNumericalValue());
        assertEquals(expectedLeftChild, actualNode.getLeftChild());
        assertEquals(expectedRightChild, actualNode.getRightChild());
    }

    @Test
    public void splitCategoricalAttribute() {
        // create expected outcome
        RecordCollection expectedLeftSubset = new RecordCollection();
        expectedLeftSubset.add(new Record("Female", 15, 4, 2, "More than 8 hours", "Healthy", false, 8, 3, true, 0));
        expectedLeftSubset.add(new Record("Male", 39, 2, 5, "5-6 hours", "Healthy", true, 6, 4, false, 0));
        expectedLeftSubset.add(new Record("Female", 23, 1, 3, "More than 8 hours", "Healthy", false, 2, 1, true, 0));
        expectedLeftSubset.add(new Record("Male", 32, 5, 4, "Less than 5 hours", "Healthy", true, 4, 5, false, 0));
        Node expectedLeftChild = new Node(expectedLeftSubset);

        RecordCollection expectedRightSubset = new RecordCollection();
        expectedRightSubset.add(new Record("Female", 19, 3, 1, "7-8 hours", "Moderate", false, 1, 3, true, 1));
        expectedRightSubset.add(new Record("Male", 50, 1, 5, "7-8 hours", "Unhealthy", true, 5, 4, false, 1));
        expectedRightSubset.add(new Record("Female", 31, 2, 3, "5-6 hours", "Unhealthy", false, 7, 2, true, 1));
        expectedRightSubset.add(new Record("Male", 28, 5, 2, "Less than 5 hours", "Moderate", true, 3, 1, false, 1));
        Node expectedRightChild = new Node(expectedRightSubset);

        // create actual outcome
        Node actualNode = new Node();
        initializeArtificialDatasets();
        actualNode.split(artificialDataset3);

        // assert that the children are equal & that the node split on dietary habits
        // with the value "healthy"
        assertEquals("dietaryHabits", actualNode.getAttribute());
        assertEquals("Healthy", actualNode.getCategoricalValue());
        assertEquals(expectedLeftChild, actualNode.getLeftChild());
        assertEquals(expectedRightChild, actualNode.getRightChild());
    }

    // UNIT TESTS FOR TREE
    @Test
    public void buildTreeFromSingleAttribute() {
        // initialize artificial datasets
        initializeArtificialDatasets();

        // create expected outcome
        Tree expectedTree = new Tree();
        expectedTree.setHead(new Node(artificialDataset1));
        expectedTree.getHead().setAttribute("suicidalThoughts");

        RecordCollection expectedLeftSubset = new RecordCollection();
        expectedLeftSubset.add(new Record("Male", 19, 3, 2, "7-8 hours", "Unhealthy", true, 4, 2, false, 1));
        expectedLeftSubset.add(new Record("Other", 23, 2, 1, "5-6 hours", "Moderate", true, 5, 4, true, 1));
        expectedLeftSubset.add(new Record("Female", 31, 1, 3, "7-8 hours", "Moderate", true, 2, 1, false, 1));

        RecordCollection expectedRightSubset = new RecordCollection();
        expectedRightSubset.add(new Record("Female", 22, 0, 3, "5-6 hours", "Unhealthy", false, 6, 4, false, 0));
        expectedRightSubset.add(new Record("Male", 34, 3, 3, "Less than 5 hours", "Healthy", false, 7, 3, true, 0));
        expectedRightSubset.add(new Record("Female", 27, 2, 5, "Less than 5 hours", "Moderate", false, 8, 5, false, 0));
        expectedRightSubset.add(new Record("Male", 36, 4, 2, "Less than 5 hours", "Unhealthy", false, 12, 4, true, 0));
        expectedRightSubset.add(new Record("Male", 41, 5, 4, "More than 8 hours", "Healthy", false, 10, 1, true, 0));

        expectedTree.getHead().setLeftChild(new Node(expectedLeftSubset));
        expectedTree.getHead().setRightChild(new Node(expectedRightSubset));

        // create actual outcome
        Tree actualTree = new Tree();
        actualTree.buildTree(artificialDataset1);

        // assert that the two trees are equivalent
        assertEquals(expectedTree, actualTree);
    }

    @Test
    public void buildTreeFromMultipleAttributes() {
        // initialize artificial datasets
        initializeArtificialDatasets();

        // create expected outcome
        Tree expectedTree = new Tree();
        expectedTree.setHead(new Node(artificialDataset2));
        expectedTree.getHead().setAttribute("academicPressure");

        RecordCollection expectedLeftSubset = new RecordCollection();
        expectedLeftSubset.add(new Record("Female", 33, 1, 1, "More than 8 hours", "Healthy", true, 5, 2, false, 0));
        expectedLeftSubset.add(new Record("Female", 25, 2, 4, "5-6 hours", "Healthy", false, 8, 4, true, 0));
        expectedLeftSubset.add(new Record("Male", 30, 1, 3, "7-8 hours", "Unhealthy", false, 7, 2, true, 0));

        RecordCollection expectedRightSubset = new RecordCollection();
        expectedRightSubset.add(new Record("Male", 28, 4, 2, "More than 8 hours", "Healthy", true, 6, 5, true, 1));
        expectedRightSubset.add(new Record("Female", 18, 3, 4, "More than 8 hours", "Healthy", true, 9, 2, false, 1));
        expectedRightSubset.add(new Record("Male", 19, 3, 5, "Less than 5 hours", "Moderate", true, 8, 3, true, 1));
        expectedRightSubset.add(new Record("Female", 31, 3, 3, "5-6 hours", "Unhealthy", false, 12, 2, false, 0));
        expectedRightSubset.add(new Record("Male", 32, 5, 1, "Less than 5 hours", "Moderate", false, 4, 1, false, 1));

        RecordCollection expectedRightLeftSubset = new RecordCollection();
        expectedRightLeftSubset.add(new Record("Female", 31, 3, 3, "5-6 hours", "Unhealthy", false, 12, 2, false, 0));

        RecordCollection expectedRightRightSubset = new RecordCollection();
        expectedRightRightSubset.add(new Record("Male", 28, 4, 2, "More than 8 hours", "Healthy", true, 6, 5, true, 1));
        expectedRightRightSubset.add(new Record("Female", 18, 3, 4, "More than 8 hours", "Healthy", true, 9, 2, false, 1));
        expectedRightRightSubset.add(new Record("Male", 19, 3, 5, "Less than 5 hours", "Moderate", true, 8, 3, true, 1));
        expectedRightRightSubset.add(new Record("Male", 32, 5, 1, "Less than 5 hours", "Moderate", false, 4, 1, false, 1));

        expectedTree.getHead().setLeftChild(new Node(expectedLeftSubset));
        expectedTree.getHead().setRightChild(new Node(expectedRightSubset));
        expectedTree.getHead().getRightChild().setAttribute("sleepDuration");
        expectedTree.getHead().getRightChild().setLeftChild(new Node(expectedRightLeftSubset));
        expectedTree.getHead().getRightChild().setRightChild(new Node(expectedRightRightSubset));

        // create actual outcome
        Tree actualTree = new Tree();
        actualTree.buildTree(artificialDataset2);

        // assert that the two trees are equivalent
        assertEquals(expectedTree, actualTree);
    }
}