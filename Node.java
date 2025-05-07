import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {

    private String attribute;
    private double numericalValue;
    private String categoricalValue;
    private boolean terminal;
    private Node left;
    private Node right;
    private RecordCollection subset;

    public Node() {
        subset = new RecordCollection();
    }

    public Node(RecordCollection subset) {
        this.subset = subset;
    }

    public String getAttribute() { return attribute; }
    public double getNumericalValue() { return numericalValue; }
    public String getCategoricalValue() { return categoricalValue; }
    public Node getLeftChild() { return left; }
    public Node getRightChild() { return right; }
    public RecordCollection subset() { return subset; }
    public boolean isTerminal() { return terminal; }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) return false;
        Node other = (Node) obj;
        if (this.left == null && this.right == null) {
            return this.subset.equals(other.subset());
        } else if (this.left == null) {
            return this.right.equals(other.getRightChild()) && this.subset.equals(other.subset());
        } else if (this.right == null) {
            return this.left.equals(other.getLeftChild()) && this.subset.equals(other.subset());
        } else {
            return this.left.equals(other.getLeftChild()) && this.right.equals(other.getRightChild()) && this.subset.equals(other.subset());
        }
    }

    @Override
    public String toString() {
        return subset.toString();
    }

    public Node evaluate(Record record) {
        switch (attribute) {
            case "gender": return record.getGender().equals(categoricalValue) ? left : right;
            case "age": return record.getAge() < numericalValue ? left : right;
            case "academicPressure": return record.getAcademicPressure() < numericalValue ? left : right;
            case "studySatisfaction": return record.getStudySatisfaction() < numericalValue ? left : right;
            case "sleepDuration": return record.getSleepDuration().equals(categoricalValue) ? left : right;
            case "dietaryHabits": return record.getDietaryHabits().equals(categoricalValue) ? left : right;
            case "suicidalThoughts": return record.getSuicidalThoughts() ? left : right;
            case "studyHours": return record.getStudyHours() < numericalValue ? left : right;
            case "financialStress": return record.getFinancialStress() < numericalValue ? left : right;
            case "familyHistory": return record.getFamilyHistory() ? left : right;
            default: throw new RuntimeException("attribute type not found: " + attribute);
        }
    }

    public void split(RecordCollection data) {
        subset = data;

        // ✅ stronger stopping condition
        if (data == null || data.size() == 0 || data.size() == 1 || data.percentageOfZeros() == 0 || data.percentageOfZeros() == 1) {
            terminal = true;
            return;
        }

        terminal = false;

        String bestAttribute = null;
        double bestNumericalValue = 0;
        String bestCategoricalValue = null;
        double bestScore = -1;

        String[] attributeList = {
            "gender", "age", "academicPressure", "studySatisfaction",
            "sleepDuration", "dietaryHabits", "suicidalThoughts",
            "studyHours", "financialStress", "familyHistory"
        };

        for (String a : attributeList) {
            double currentNumericalValue = 0;
            String currentCategoricalValue = null;

            if (a.equals("age") || a.equals("academicPressure") ||
                a.equals("studySatisfaction") || a.equals("studyHours") ||
                a.equals("financialStress")) {
                currentNumericalValue = getValueForNumericalSplit(a);
            } else {
                currentCategoricalValue = getValueForCategoricalSplit(a);
            }

            double currentScore = getScoreFromSplit(a, currentNumericalValue, currentCategoricalValue);
            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestAttribute = a;
                bestNumericalValue = currentNumericalValue;
                bestCategoricalValue = currentCategoricalValue;
            }
        }

        if (bestAttribute == null || bestScore <= 0) {
            terminal = true;
            return;
        }

        attribute = bestAttribute;
        numericalValue = bestNumericalValue;
        categoricalValue = bestCategoricalValue;

        setLeafNodes();
    }

    private double getValueForNumericalSplit(String attribute) {
        double bestValue = 0;
        double bestScore = -1;

        for (Record record : subset) {
            double currentValue;
            switch (attribute) {
                case "age": currentValue = record.getAge(); break;
                case "academicPressure": currentValue = record.getAcademicPressure(); break;
                case "studySatisfaction": currentValue = record.getStudySatisfaction(); break;
                case "studyHours": currentValue = record.getStudyHours(); break;
                case "financialStress": currentValue = record.getFinancialStress(); break;
                default: throw new RuntimeException("Attribute type not found: " + attribute);
            }

            RecordCollection leftSubset = new RecordCollection();
            RecordCollection rightSubset = new RecordCollection();
            for (Record r : subset) {
                switch (attribute) {
                    case "age": if (r.getAge() < currentValue) leftSubset.add(r); else rightSubset.add(r); break;
                    case "academicPressure": if (r.getAcademicPressure() < currentValue) leftSubset.add(r); else rightSubset.add(r); break;
                    case "studySatisfaction": if (r.getStudySatisfaction() < currentValue) leftSubset.add(r); else rightSubset.add(r); break;
                    case "studyHours": if (r.getStudyHours() < currentValue) leftSubset.add(r); else rightSubset.add(r); break;
                    case "financialStress": if (r.getFinancialStress() < currentValue) leftSubset.add(r); else rightSubset.add(r); break;
                }
            }

            double score = leftSubset.percentageOfZeros() + (1 - rightSubset.percentageOfZeros());
            score = score > 1 ? score : 2 - score;

            if (score > bestScore) {
                bestScore = score;
                bestValue = currentValue;
            }
        }
        return bestValue;
    }

    private String getValueForCategoricalSplit(String attribute) {
        List<String> valuesToCheck;
        switch (attribute) {
            case "gender": valuesToCheck = Arrays.asList("Male", "Female", "Other"); break;
            case "sleepDuration": valuesToCheck = Arrays.asList("< 5 hours", "5-6 hours", "6-7 hours", "7-8 hours", "> 8 hours"); break;
            case "dietaryHabits": valuesToCheck = Arrays.asList("Unhealthy", "Moderate", "Healthy", "Very Healthy"); break;
            case "suicidalThoughts":
            case "familyHistory": valuesToCheck = Arrays.asList("true", "false"); break;
            default: throw new RuntimeException("Attribute type not found: " + attribute);
        }

        String bestValue = null;
        double bestScore = -1;
        for (String v : valuesToCheck) {
            RecordCollection leftSubset = new RecordCollection();
            RecordCollection rightSubset = new RecordCollection();
            for (Record r : subset) {
                switch (attribute) {
                    case "gender": if (r.getGender().equals(v)) leftSubset.add(r); else rightSubset.add(r); break;
                    case "sleepDuration": if (r.getSleepDuration().equals(v)) leftSubset.add(r); else rightSubset.add(r); break;
                    case "dietaryHabits": if (r.getDietaryHabits().equals(v)) leftSubset.add(r); else rightSubset.add(r); break;
                    case "suicidalThoughts": if (String.valueOf(r.getSuicidalThoughts()).equals(v)) leftSubset.add(r); else rightSubset.add(r); break;
                    case "familyHistory": if (String.valueOf(r.getFamilyHistory()).equals(v)) leftSubset.add(r); else rightSubset.add(r); break;
                }
            }

            double score = leftSubset.percentageOfZeros() + (1 - rightSubset.percentageOfZeros());
            score = score > 1 ? score : 2 - score;

            if (score > bestScore) {
                bestScore = score;
                bestValue = v;
            }
        }
        return bestValue;
    }

    private double getScoreFromSplit(String attribute, double numericalValue, String categoricalValue) {
        RecordCollection leftSubset = new RecordCollection();
        RecordCollection rightSubset = new RecordCollection();

        for (Record record : subset) {
            switch (attribute) {
                case "gender": if (record.getGender().equals(categoricalValue)) leftSubset.add(record); else rightSubset.add(record); break;
                case "age": if (record.getAge() < numericalValue) leftSubset.add(record); else rightSubset.add(record); break;
                case "academicPressure": if (record.getAcademicPressure() < numericalValue) leftSubset.add(record); else rightSubset.add(record); break;
                case "studySatisfaction": if (record.getStudySatisfaction() < numericalValue) leftSubset.add(record); else rightSubset.add(record); break;
                case "sleepDuration": if (record.getSleepDuration().equals(categoricalValue)) leftSubset.add(record); else rightSubset.add(record); break;
                case "dietaryHabits": if (record.getDietaryHabits().equals(categoricalValue)) leftSubset.add(record); else rightSubset.add(record); break;
                case "suicidalThoughts": if (record.getSuicidalThoughts()) leftSubset.add(record); else rightSubset.add(record); break;
                case "studyHours": if (record.getStudyHours() < numericalValue) leftSubset.add(record); else rightSubset.add(record); break;
                case "financialStress": if (record.getFinancialStress() < numericalValue) leftSubset.add(record); else rightSubset.add(record); break;
                case "familyHistory": if (record.getFamilyHistory()) leftSubset.add(record); else rightSubset.add(record); break;
            }
        }

        double score = leftSubset.percentageOfZeros() + (1 - rightSubset.percentageOfZeros());
        return score > 1 ? score : 2 - score;
    }

    private void setLeafNodes() {
        RecordCollection leftSubset = new RecordCollection();
        RecordCollection rightSubset = new RecordCollection();

        for (Record record : subset) {
            switch (attribute) {
                case "gender": if (record.getGender().equals(categoricalValue)) leftSubset.add(record); else rightSubset.add(record); break;
                case "age": if (record.getAge() < numericalValue) leftSubset.add(record); else rightSubset.add(record); break;
                case "academicPressure": if (record.getAcademicPressure() < numericalValue) leftSubset.add(record); else rightSubset.add(record); break;
                case "studySatisfaction": if (record.getStudySatisfaction() < numericalValue) leftSubset.add(record); else rightSubset.add(record); break;
                case "sleepDuration": if (record.getSleepDuration().equals(categoricalValue)) leftSubset.add(record); else rightSubset.add(record); break;
                case "dietaryHabits": if (record.getDietaryHabits().equals(categoricalValue)) leftSubset.add(record); else rightSubset.add(record); break;
                case "suicidalThoughts": if (record.getSuicidalThoughts()) leftSubset.add(record); else rightSubset.add(record); break;
                case "studyHours": if (record.getStudyHours() < numericalValue) leftSubset.add(record); else rightSubset.add(record); break;
                case "financialStress": if (record.getFinancialStress() < numericalValue) leftSubset.add(record); else rightSubset.add(record); break;
                case "familyHistory": if (record.getFamilyHistory()) leftSubset.add(record); else rightSubset.add(record); break;
            }
        }

        // ✅ extra guard against empty subsets
        if (leftSubset.size() == 0 || rightSubset.size() == 0) {
            terminal = true;
            return;
        }

        left = new Node(leftSubset);
        right = new Node(rightSubset);
    }
}
