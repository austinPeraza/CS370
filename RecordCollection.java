import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecordCollection implements Iterable<Record> {
    private List<Record> collection = new ArrayList<>();

    
    public static RecordCollection loadFromCSV(String path) throws Exception {
        RecordCollection rc = new RecordCollection();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                // include empty fields
                String[] f = line.split(",", -1);
                if (f.length < 11) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }
                // trim whitespace on each field
                for (int i = 0; i < 11; i++) {
                    f[i] = f[i].trim();
                }
                // skip rows with any empty field
                boolean anyEmpty = false;
                for (int i = 0; i < 11; i++) {
                    if (f[i].isEmpty()) {
                        anyEmpty = true;
                        break;
                    }
                }
                if (anyEmpty) {
                    System.err.println("Skipping line with missing values: " + line);
                    continue;
                }

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