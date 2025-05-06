import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {
    
    // FIELDS
    private String attribute;            // the attribute on which the node is split
    private double numericalValue;       // the value on which the node is split (if numerical)
    private String categoricalValue;     // the value on which the node is split (if categorical)
    private boolean terminal;            // whether the node is terminal or not
    private Node left;                   // the left child of the current node
    private Node right;                  // the right child of the current node
    private RecordCollection subset;     // the subset this node is splitting

    // CONSTRUCTORS
    public Node() {
        subset = new RecordCollection();
    }

    public Node(RecordCollection subset) {
        this.subset = subset;
    }

    // ACCESSORS
    public String getAttribute() { return attribute; }
    public double getNumericalValue() { return numericalValue; }
    public String getCategoricalValue() { return categoricalValue; }
    public Node getLeftChild() { return left; }
    public Node getRightChild() { return right; }
    public RecordCollection subset() { return subset; }
    public boolean isTerminal() { return terminal; }

    // Compare two Nodes to see if they are equal
    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Node other = (Node) obj;
            if (this.left == null && this.right == null) {
                return (this.left == other.getLeftChild() && this.right == getRightChild() && this.subset.equals(other.subset())) ? true : false;
            } else if (this.left == null) {
                return (this.left == other.getLeftChild() && this.right.equals(other.getRightChild()) && this.subset.equals(other.subset())) ? true : false;
            } else if (this.right == null) {
                return (this.left.equals(other.getLeftChild()) && this.right == getRightChild() && this.subset.equals(other.subset())) ? true : false;
            } else {
                return (this.left.equals(other.getLeftChild()) && this.right.equals(other.getRightChild()) && this.subset.equals(other.subset())) ? true : false;
            }
        }
    }

    // Print out the contents of the Node
    @Override
    public String toString() {
        return subset.toString();
    }

    // Evaluate a record by following the split condition
    public Node evaluate(Record record) {
        switch (attribute) {
            case "gender":
                return record.getGender().equals(categoricalValue) ? left : right;  // Changed: removed unreachable breaks & use equals
            case "age":
                return record.getAge() < numericalValue ? left : right;              // Changed: removed unreachable breaks
            case "academicPressure":
                return record.getAcademicPressure() < numericalValue ? left : right; // Changed: removed unreachable breaks
            case "studySatisfaction":
                return record.getStudySatisfaction() < numericalValue ? left : right;// Changed: removed unreachable breaks
            case "sleepDuration":
                return record.getSleepDuration().equals(categoricalValue) ? left : right; // Changed: removed breaks & use equals
            case "dietaryHabits":
                return record.getDietaryHabits().equals(categoricalValue) ? left : right; // Changed: removed breaks & use equals
            case "suicidalThoughts":
                return record.getSuicidalThoughts() ? left : right;                  // Changed: removed breaks
            case "studyHours":
                return record.getStudyHours() < numericalValue ? left : right;       // Changed: removed breaks
            case "financialStress":
                return record.getFinancialStress() < numericalValue ? left : right;  // Changed: removed breaks
            case "familyHistory":
                return record.getFamilyHistory() ? left : right;                     // Changed: removed breaks
            default:
                throw new RuntimeException("attribute type not found: " + attribute); // Changed: unchecked exception
        }
    }

    // Split this node’s subset into two child nodes
    public void split(RecordCollection data) {
        subset = data;

        if (data.percentageOfZeros() == 0 || data.percentageOfZeros() == 1) {
            terminal = true;
        } else {
            terminal = false;

            // Changed: initialize these to avoid “might not have been initialized”
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
                } else if (a.equals("gender") || a.equals("sleepDuration") || a.equals("dietaryHabits")) {
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

            // set the chosen split
            attribute = bestAttribute;
            numericalValue = bestNumericalValue;
            categoricalValue = bestCategoricalValue;

            setLeafNodes();
        }
    }

    // PRIVATE HELPERS

    private double getValueForNumericalSplit(String attribute) {
        // Changed: initialize to avoid “might not have been initialized”
        double bestValue = 0;
        double bestScore = -1;

        for (Record record : subset) {
            double currentValue;
            switch (attribute) {
                case "age":               currentValue = record.getAge();              break;
                case "academicPressure":  currentValue = record.getAcademicPressure(); break;
                case "studySatisfaction": currentValue = record.getStudySatisfaction();break;
                case "studyHours":        currentValue = record.getStudyHours();       break;
                case "financialStress":   currentValue = record.getFinancialStress();  break;
                default:
                    throw new RuntimeException("Attribute type not found: " + attribute); // Changed
            }

            RecordCollection leftSubset  = new RecordCollection();
            RecordCollection rightSubset = new RecordCollection();
            for (Record r : subset) {
                switch (attribute) {
                    case "age":
                        if (r.getAge() < currentValue) leftSubset.add(r);
                        else rightSubset.add(r);
                        break;
                    case "academicPressure":
                        if (r.getAcademicPressure() < currentValue) leftSubset.add(r);
                        else rightSubset.add(r);
                        break;
                    case "studySatisfaction":
                        if (r.getStudySatisfaction() < currentValue) leftSubset.add(r);
                        else rightSubset.add(r);
                        break;
                    case "studyHours":
                        if (r.getStudyHours() < currentValue) leftSubset.add(r);
                        else rightSubset.add(r);
                        break;
                    case "financialStress":
                        if (r.getFinancialStress() < currentValue) leftSubset.add(r);
                        else rightSubset.add(r);
                        break;
                    default:
                        throw new RuntimeException("Attribute type not found: " + attribute); // Changed
                }
            }

            double score = leftSubset.percentageOfZeros() + rightSubset.percentageOfZeros();
            score = score > 0.5 ? score : 1 - score;

            if (score > bestScore) {
                bestScore = score;
                bestValue = currentValue;
            }
        }
        return bestValue;
    }

    private String getValueForCategoricalSplit(String attribute) {
        // Changed: initialize to avoid “might not have been initialized”
        String bestValue = null;
        double bestScore = -1;

        List<String> valuesToCheck;
        switch (attribute) {
            case "gender":
                valuesToCheck = Arrays.asList("Male", "Female", "Other");
                break;
            case "sleepDuration":
                valuesToCheck = Arrays.asList("< 5 hours", "5-6 hours", "6-7 hours", "7-8 hours", "> 8 hours");
                break;
            case "dietaryHabits":
                valuesToCheck = Arrays.asList("Unhealthy", "Moderate", "Healthy", "Very Healthy");
                break;
            default:
                throw new RuntimeException("Attribute type not found: " + attribute); // Changed
        }

        for (String v : valuesToCheck) {
            RecordCollection leftSubset  = new RecordCollection();
            RecordCollection rightSubset = new RecordCollection();
            for (Record r : subset) {
                switch (attribute) {
                    case "gender":
                        if (r.getGender().equals(v)) leftSubset.add(r);
                        else rightSubset.add(r);
                        break;
                    case "sleepDuration":
                        if (r.getSleepDuration().equals(v)) leftSubset.add(r);
                        else rightSubset.add(r);
                        break;
                    case "dietaryHabits":
                        if (r.getDietaryHabits().equals(v)) leftSubset.add(r);
                        else rightSubset.add(r);
                        break;
                    default:
                        throw new RuntimeException("Attribute type not found: " + attribute); // Changed
                }
            }

            double score = leftSubset.percentageOfZeros() + rightSubset.percentageOfZeros();
            score = score > 0.5 ? score : 1 - score;
            if (score > bestScore) {
                bestScore = score;
                bestValue = v;
            }
        }
        return bestValue;
    }

    private double getScoreFromSplit(String attribute, double numericalValue, String categoricalValue) {
        RecordCollection leftSubset  = new RecordCollection();
        RecordCollection rightSubset = new RecordCollection();

        for (Record record : subset) {
            switch (attribute) {
                case "gender":
                    if (record.getGender().equals(categoricalValue)) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "age":
                    if (record.getAge() < numericalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "academicPressure":
                    if (record.getAcademicPressure() < numericalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "studySatisfaction":
                    if (record.getStudySatisfaction() < numericalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "sleepDuration":
                    if (record.getSleepDuration().equals(categoricalValue)) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "dietaryHabits":
                    if (record.getDietaryHabits().equals(categoricalValue)) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "suicidalThoughts":
                    if (record.getSuicidalThoughts()) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "studyHours":
                    if (record.getStudyHours() < numericalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "financialStress":
                    if (record.getFinancialStress() < numericalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "familyHistory":
                    if (record.getFamilyHistory()) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                default:
                    throw new RuntimeException("attribute type not found: " + attribute); // Changed
            }
        }

        double score = leftSubset.percentageOfZeros() + rightSubset.percentageOfZeros();
        return score > 0.5 ? score : 1 - score;
    }

    private void setLeafNodes() {
        RecordCollection leftSubset  = new RecordCollection();
        RecordCollection rightSubset = new RecordCollection();

        for (Record record : subset) {
            switch (attribute) {
                case "gender":
                    if (record.getGender().equals(categoricalValue)) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "age":
                    if (record.getAge() < numericalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "academicPressure":
                    if (record.getAcademicPressure() < numericalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "studySatisfaction":
                    if (record.getStudySatisfaction() < numericalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "sleepDuration":
                    if (record.getSleepDuration().equals(categoricalValue)) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "dietaryHabits":
                    if (record.getDietaryHabits().equals(categoricalValue)) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "suicidalThoughts":
                    if (record.getSuicidalThoughts()) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "studyHours":
                    if (record.getStudyHours() < numericalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "financialStress":
                    if (record.getFinancialStress() < numericalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "familyHistory":
                    if (record.getFamilyHistory()) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                default:
                    throw new RuntimeException("attribute type not found: " + attribute); // Changed
            }
        }

        left  = new Node(leftSubset);
        right = new Node(rightSubset);
    }
}
