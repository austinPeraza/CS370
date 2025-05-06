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

    /** Train up to 10 trees, reporting progress */
    public void trainForest(RecordCollection data, ProgressListener listener) {
        cancelRequested = false;
        trees.clear();
        final int TOTAL = 10;
        for (int i = 0; i < TOTAL; i++) {
            if (cancelRequested) break;
            RecordCollection sample = bootstrap(data);
            Tree t = new Tree();
            t.buildTree(sample);
            trees.add(t);
            if (listener != null) listener.onProgress(i + 1, TOTAL);
        }
    }

    /** Average tree predictions */
    public float predictScore(Record input) {
        if (trees.isEmpty()) throw new IllegalStateException("Forest not trained");
        float sum = 0;
        for (Tree t : trees) sum += t.predict(input);
        return sum / trees.size();
    }

    public List<Tree> getTrees() { return trees; }

    /** Progress callback interface */
    public interface ProgressListener {
        void onProgress(int current, int total);
    }
}