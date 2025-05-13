import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import java.util.Random;
import java.util.List;

public class SystemTests {

    private RecordCollection artificialDataset;
    private void initializeArtificialDataset() {
        try {
            artificialDataset = RecordCollection.loadFromCSV("data/sample_dataset_1000.csv");
        } catch (Exception e) {
            return;
        }
    }
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

    // WELLNESS SCORE CALCULATION TESTS
    @Test
    public void lowRiskStudent() {
        // initialize test
        initializeArtificialDataset();
        Forest forest = new Forest();
        forest.trainForest(artificialDataset, 100, null);

        // get prediction
        Record record = new Record();
        record.setGender("Male");
        record.setAge(22);
        record.setAcademicPressure(1);
        record.setStudySatisfaction(5);
        record.setSleepDuration("More than 8 hours");
        record.setDietaryHabits("Healthy");
        record.setSuicidalThoughts(false);
        record.setStudyHours(6);
        record.setFinancialStress(1);
        record.setFamilyHistory(false);
        float score = forest.predictScore(record);

        // check if the score is less than or equal to 0.5
        assertTrue(score <= 0.5);
    }

    @Test
    public void highRiskStudent() {
        // initialize test
        initializeArtificialDataset();
        Forest forest = new Forest();
        forest.trainForest(artificialDataset, 100, null);

        // get prediction
        Record record = new Record();
        record.setGender("Male");
        record.setAge(22);
        record.setAcademicPressure(5);
        record.setStudySatisfaction(1);
        record.setSleepDuration("Less than 5 hours");
        record.setDietaryHabits("Unhealthy");
        record.setSuicidalThoughts(true);
        record.setStudyHours(6);
        record.setFinancialStress(5);
        record.setFamilyHistory(true);
        float score = forest.predictScore(record);

        // check if the score is greater than or equal to 0.5
        assertTrue(score >= 0.5);
    }

    // WELLNESS SCORE FACTORS TEST
    @Test
    public void wellnessScoreFactors() {
        // initialize artifical dataset
        artificialDataset = new RecordCollection();
        artificialDataset.add(new Record("Female", 33, 1, 1, "More than 8 hours", "Healthy", true, 5, 2, false, 0));
        artificialDataset.add(new Record("Female", 25, 2, 4, "5-6 hours", "Healthy", false, 8, 4, true, 0));
        artificialDataset.add(new Record("Male", 28, 4, 2, "More than 8 hours", "Healthy", true, 6, 5, true, 1));
        artificialDataset.add(new Record("Female", 18, 3, 4, "More than 8 hours", "Healthy", true, 9, 2, false, 1));
        artificialDataset.add(new Record("Male", 19, 3, 5, "Less than 5 hours", "Moderate", true, 8, 3, true, 1));
        artificialDataset.add(new Record("Male", 30, 1, 3, "7-8 hours", "Unhealthy", false, 7, 2, true, 0));
        artificialDataset.add(new Record("Female", 31, 3, 3, "5-6 hours", "Unhealthy", false, 12, 2, false, 0));
        artificialDataset.add(new Record("Male", 32, 5, 1, "Less than 5 hours", "Moderate", false, 4, 1, false, 1));

        // train forest
        Forest forest = new Forest();
        forest.trainForest(artificialDataset, 10, null);

        // set up record
        Record record = new Record();
        record.setGender("Male");
        record.setAge(22);
        record.setAcademicPressure(5);
        record.setStudySatisfaction(1);
        record.setSleepDuration("Less than 5 hours");
        record.setDietaryHabits("Unhealthy");
        record.setSuicidalThoughts(true);
        record.setStudyHours(6);
        record.setFinancialStress(5);
        record.setFamilyHistory(true);

        // test wellness factors - should be academic pressure and sleep duration
        WellnessFeedback feedback = new WellnessFeedback();
        List<String> factors = feedback.obtainFactors(record, forest);

        // assert that factors contains academicPressure and sleepDuration
        assertTrue(factors.contains("academicPressure"));
        assertTrue(factors.contains("sleepDuration"));
    }

    // CUSTOM MODEL TRAINING TESTS
    @Test
    public void train10Trees() {
        // initialize tests
        initializeArtificialDataset(10000);
        Forest testForest = new Forest();
        testForest.trainForest(artificialDataset, 10, null);
    }

    @Test
    public void train50Trees() {
        // initialize tests
        initializeArtificialDataset(10000);
        Forest testForest = new Forest();
        testForest.trainForest(artificialDataset, 50, null);
    }

    @Test
    public void train100Trees() {
        // initialize tests
        initializeArtificialDataset(10000);
        Forest testForest = new Forest();
        testForest.trainForest(artificialDataset, 100, null);
    }
}
