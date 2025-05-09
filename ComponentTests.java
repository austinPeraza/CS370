import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import java.util.Random;
import java.util.ArrayList;

public class ComponentTests {
    // create the artificial datasets
    private RecordCollection artificialDataset;

    // helper method to initialize an artificial dataset
    private void initializeArtificialDataset(int size) {
        artificialDataset = new RecordCollection();
        String[] genders = {"Male", "Female", "Other"};
        String[] sleep = {"Less than 5 hours", "5-6 hours", "7-8 hours", "More than 8 hours"};
        String[] diet = {"Healthy", "Moderate", "Unhealthy"};
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            artificialDataset.add(new Record(genders[random.nextInt(3)], random.nextInt(35) + 15, random.nextInt(5) + 1,
                                      random.nextInt(5) + 1, sleep[random.nextInt(4)], diet[random.nextInt(3)],
                                      random.nextBoolean(), random.nextInt(13), random.nextInt(5) + 1,
                                      random.nextBoolean(), random.nextInt(2)));
        }
    }

    // COMPONENT TESTS FOR TRAIN FOREST
    @Test
    public void trainForestTestSize10() {
        // initialize tests
        initializeArtificialDataset(10);
        Forest testForest = new Forest();
        testForest.trainForest(artificialDataset, 10, null);
    }

    @Test
    public void trainForestTest100() {
        // initialize tests
        initializeArtificialDataset(100);
        Forest testForest = new Forest();
        testForest.trainForest(artificialDataset, 10, null);
    }

    @Test
    public void trainForestTest1000() {
        // initialize tests
        initializeArtificialDataset(1000);
        Forest testForest = new Forest();
        testForest.trainForest(artificialDataset, 10, null);
    }

    // COMPONENT TESTS FOR BOOTSTRAPPING
    @Test
    public void bootstrap() {
        // generate an artificial dataset
        initializeArtificialDataset(1000);
        Forest dummy = new Forest();
        
        // bootstrap the base collection
        RecordCollection collectionA = dummy.bootstrap(artificialDataset);
        RecordCollection collectionB = dummy.bootstrap(artificialDataset);

        // compare differences
        ArrayList<Record> differences = new ArrayList<>(collectionA.collection());
        differences.removeAll(collectionB.collection());

        assertTrue(differences.size() >= 200);
    }
}
