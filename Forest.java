import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Forest {
    private List<Tree> trees = new ArrayList<>();
    private volatile boolean cancelRequested = false;

    /** Request cancellation from UI */
    public void cancelTraining() {
        cancelRequested = true;
    }

    /** Expose cancellation status */
    public boolean isCancelRequested() {
        return cancelRequested;
    }

    /** Bootstrap sample with replacement */
    public RecordCollection bootstrap(RecordCollection data) {
        List<Record> all = new ArrayList<>();
        for (Record r : data) all.add(r);
        RecordCollection sample = new RecordCollection();
        Random rand = new Random();
        int n = all.size();
        for (int i = 0; i < n; i++) {
            sample.add(all.get(rand.nextInt(n)));
        }
        return sample;
    }

    /** Train specified number of trees, reporting progress */
    public void trainForest(RecordCollection data, int numTrees, ProgressListener listener) {
        cancelRequested = false;
        trees.clear();
        for (int i = 0; i < numTrees; i++) {
            if (cancelRequested) break;
            RecordCollection sample = bootstrap(data);
            Tree t = new Tree();
            ArrayList<String> features = getRandomFeatures(5);
            t.buildTree(sample, features);
            trees.add(t);
            if (listener != null) listener.onProgress(i + 1, numTrees);
        }
    }

    /** Average tree predictions */
    public float predictScore(Record input) {
        if (trees.isEmpty()) throw new IllegalStateException("Forest not trained");
        float sum = 0;
        for (Tree t : trees) sum += t.predict(input);
        return sum / trees.size();
    }

    /** Choose random features for each tree. */
    public ArrayList<String> getRandomFeatures(int n) {
        String[] features = {"gender", "age", "academicPressure", "studySatisfaction", "sleepDuration", "dietaryHabits",
                             "suicidalThoughts", "studyHours", "financialStress", "familyHistory"};
        ArrayList<String> returnList = new ArrayList<String>();
        for (int i = 0; i < features.length; i++) {
            returnList.add(features[i]);
        }

        Random rand = new Random();
        for (int i = 0; i < features.length - n; i++) {
            returnList.remove(rand.nextInt(features.length - i));
        }

        return returnList;
    }

    public List<Tree> getTrees() { return trees; }

    /** Progress callback interface */
    public interface ProgressListener {
        void onProgress(int current, int total);
    }
}
