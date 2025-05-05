// RecordCollection.java
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecordCollection implements Iterable<Record> {
    private List<Record> collection = new ArrayList<>();

    /** Load a CSV with header and 11 columns (last is wellnessScore). */
    public static RecordCollection loadFromCSV(String path) throws Exception {
        RecordCollection rc = new RecordCollection();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",");
                Record r = new Record();
                r.setGender(f[0]);
                r.setAge(Integer.parseInt(f[1]));
                r.setAcademicPressure(Integer.parseInt(f[2]));
                r.setStudySatisfaction(Integer.parseInt(f[3]));
                r.setSleepDuration(f[4]);
                r.setDietaryHabits(f[5]);
                r.setSuicidalThoughts(Boolean.parseBoolean(f[6]));  
                r.setStudyHours(Integer.parseInt(f[7]));
                r.setFinancialStress(Integer.parseInt(f[8]));
                r.setFamilyHistory(Boolean.parseBoolean(f[9]));
                r.setWellnessScore(Integer.parseInt(f[10]));
                rc.add(r);
            }
        }
        return rc;
    }

    public void add(Record record) {
        collection.add(record);
    }

    public int size() {
        return collection.size();
    }

    public double percentageOfZeros() {
        if (collection.isEmpty()) return 0.0;
        int zeroCount = 0;
        for (Record r : collection) {
            if (r.getWellnessScore() == 0) zeroCount++;
        }
        return (double) zeroCount / collection.size();
    }

    @Override
    public Iterator<Record> iterator() {
        return collection.iterator();
    }
}
