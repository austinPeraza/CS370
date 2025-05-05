// Forest.java
import java.util.ArrayList;
import java.util.List;

public class Forest {
    private List<Tree> trees = new ArrayList<>();

    /** (Optional) sample with replacement—stub returns same data. */
    public RecordCollection bootstrap(RecordCollection data) {
        return data;
    }

    public void trainForest(RecordCollection data) {
        trees.clear();
        for (int i = 0; i < 10; i++) {
            RecordCollection sample = bootstrap(data);
            Tree t = new Tree();
            t.buildTree(sample);
            trees.add(t);
        }
    }

    public float predictScore(Record input) {
        if (trees.isEmpty()) throw new IllegalStateException("Forest not trained");
        float sum = 0;
        for (Tree t : trees) sum += t.predict(input);
        return sum / trees.size();
    }

    public List<Tree> getTrees() {
        return trees;
    }
}
