import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

public class AcceptanceTests {
    
    private RecordCollection trainingDataset;
    private Forest forest;
    private RecordCollection testDataset;

    private void initializeArtificialDatasets() {
        try {
            trainingDataset = RecordCollection.loadFromCSV("data/sample_dataset_5000.csv");
        } catch (Exception e) {
            return;
        }
        forest = new Forest();
        forest.trainForest(trainingDataset, 200, null);
        try {
            testDataset = RecordCollection.loadFromCSV("data/test_dataset_100.csv");
        } catch (Exception e) {
            return;
        }
    }

    @Test
    public void accuracyTest() {
        // initialize datasets
        initializeArtificialDatasets();

        // check number of mistakes
        int mistakes = 0;
        for (Record record : testDataset) {
            float score = (forest.predictScore(record) >= 0.5) ? 1 : 0;
            if (score != record.getWellnessScore()) {
                mistakes++;
            }
        }
        
        // check for at least 98% accuracy
        assertTrue(mistakes <= 2);
    }

    @Test
    public void speedTest() {
        // initialize datasets
        initializeArtificialDatasets();

        // get total amount of time to predict score
        long totalTime = 0;
        for (Record record : testDataset) {
            long startTime = System.currentTimeMillis();
            forest.predictScore(record);
            long endTime = System.currentTimeMillis();
            totalTime += endTime - startTime;
        }
        double averageTime = totalTime / 100000;  // get in seconds

        // check that the average time is less than 5 seconds
        assertTrue(averageTime < 5);
    }
}