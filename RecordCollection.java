import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecordCollection implements Iterable<Record> {
    // Field: stores a list of Record objects
    private List<Record> collection;

    // Constructor: initializes an empty RecordCollection
    public RecordCollection() {
        collection = new ArrayList<>();
    }

    // Method: add a Record to the collection
    public void add(Record record) {
        collection.add(record);
    }

    // Method: returns the percentage of records with wellnessScore == 0
    public double percentageOfZeros() {
        if (collection.isEmpty()) {
            return 0.0; // Avoid division by zero
        }

        int zeroCount = 0;
        for (Record record : collection) {
            if (record.getWellnessScore() == 0) {
                zeroCount++;
            }
        }

        return (double) zeroCount / collection.size();
    }

    // Method: provide an iterator over the records
    @Override
    public Iterator<Record> iterator() {
        return collection.iterator();
    }

    // Optional getter if you need access to the collection directly
    public List<Record> getCollection() {
        return collection;
    }
}
