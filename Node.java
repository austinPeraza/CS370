import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Node {
    
    // FIELDS
    private String attribute;  // the attribute on which the node is split
    private double numericalValue;  // the value on which the node is split (if the node is split on numerical data)
    private String categoricalValue;  // the value on which the node is split (if the node is split on categorical data)
    private boolean terminal;  // whether the node is terminal or not
    private Node left;  // the left child of the current node
    private Node right;  // the right child of the current node
    private RecordCollection subset;  // the subset of the RecordCollection that this node is splitting

    // CONSTRUCTORS
    public Node() {
        subset = new RecordCollection();
    }

    public Node(RecordCollection subset) {
        this.subset = subset;
    }

    // METHODS
    // get attribute
    public String getAttribute() {
        return attribute;
    }

    // get the value (if the value is numerical)
    public double getNumericalValue() {
        return numericalValue;
    }

    // get the value (if the value is categorical)
    public String getCategoricalValue() {
        return categoricalValue;
    }

    // get the left child
    public Node getLeftChild() {
        return left;
    }

    // get the right child
    public Node getRightChild() {
        return right;
    }

    // get the subset
    public RecordCollection subset() {
        return subset;
    }

    public boolean isTerminal() {
        return terminal;
    }

    // take a Record object and evaluate it based on the node's decision condition
    public Node evaluate(Record record) {
        switch (attribute) {
            case "gender":
                if (record.getGender() == categoricalValue) return left;
                else return right;
                break;
            case "age":
                if (record.getAge() < numericalValue) return left;
                else return right;
                break;
            case "academicPressure":
                if (record.getAcademicPressure() < numericalValue) return left;
                else return right;
                break;
            case "studySatisfaction":
                if (record.getStudySatisfaction() < numericalValue) return left;
                else return right;
                break;
            case "sleepDuration":
                if (record.getSleepDuration() == categoricalValue) return left;
                else return right;
                break;
            case "dietaryHabits":
                if (record.getDietaryHabits() == categoricalValue) return left;
                else return right;
                break;
            case "suicidalThoughts":
                if (record.getSuicidalThoughts()) return left;
                else return right;
                break;
            case "studyHours":
                if (record.getStudyHours() < numericalValue) return left;
                else return right;
                break;
            case "financialStress":
                if (record.getFinancialStress() < numericalValue) return left;
                else return right;
                break;
            case "familyHistory":
                if (record.getFamilyHistory()) return left;
                else return right;
                break;
            default:
                throw new Exception("attribute type not found");
        }
    }

    // takes a RecordCollection and splits it into two groups, maximizing accuracy
    public void split(RecordCollection data) {
        // set subset to data
        subset = data;

        // checks if the node should be terminal. if not, split the node on each potential attribute
        if (data.percentageOfZeros() == 0 || data.percentageOfZeros() == 1) {
            terminal = true;
        } else {
            terminal = false;

            String bestAttribute;  // stores the current best attribute
            double bestNumericalValue;  // stores the current best value to split on (assuming attribute is numerical)
            String bestCategoricalValue;  // stores the current best value to split on (assuming attribute is categorical)
            double bestScore = 0;  // stores the score associated with the current best attribute
            String[] attributeList = {"gender", "age", "academicPressure", "studySatisfaction", "sleepDuration", "dietaryHabits",
                                      "suicidalThoughts", "studyHours", "financialStress", "familyHistory"};  // stores the attributes to attempt to split on
            
            // attempt a split on each attribute
            for (String a : attributeList) {
                double currentNumericalValue = 0;
                String currentCategoricalValue = null;

                // get the value to split on
                if (a == "age" || a == "academicPressure" || a == "studySatisfaction" || a == "studyHours" || a == "financialStress") {
                    currentNumericalValue = getValueForNumericalSplit(a);
                } else if (a == "gender" || a == "sleepDuration" || a == "dietaryHabits") {
                    currentCategoricalValue = getValueForCategoricalSplit(a);
                }

                // get the score from the split
                double currentScore = getScoreFromSplit(a, currentNumericalValue, currentCategoricalValue);

                // check if the current split > the current best stored split
                if (currentScore > bestScore) {
                    bestScore = currentScore;
                    bestAttribute = a;
                    bestNumericalValue = currentNumericalValue;
                    bestCategoricalValue = currentCategoricalValue;
                }
            }

            // set the attribute & value to the output of the best split
            attribute = bestAttribute;
            numericalValue = bestNumericalValue;
            categoricalValue = bestCategoricalValue;

            // create the leaf nodes
            setLeafNodes();
        }
    }

    // PRIVATE HELPER METHODS
    // get the value to split on for a numerical value
    private double getValueForNumericalSplit(String attribute) {
        double bestValue;
        double bestScore = 0;
        for (Record record : subset) {
            // get a value to attempt to split on
            double currentValue;
            switch (attribute) {
                case "age":
                    currentValue = record.getAge();
                    break;
                case "academicPressure":
                    currentValue = record.getAcademicPressure();
                    break;
                case "studySatisfaction":
                    currentValue = record.getStudySatisfaction();
                    break;
                case "studyHours":
                    currentValue = record.getStudyHours();
                    break;
                case "financialStress":
                    currentValue = record.getFinancialStress();
                    break;
                default:
                    throw new Exception("Attribute type not found");
            }

            // attempt a split
            RecordCollection leftSubset = new RecordCollection();
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
                        throw new Exception("Attribute type not found");
                }
            }

            // check the output of the split and calculate a score
            double currentScore = leftSubset.percentageOfZeros() + rightSubset.percentageOfZeros();
            currentScore = currentScore > 0.5 ? currentScore : 1 - currentScore;

            // if the current score > best score, set the new value to split on
            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestValue = currentValue;
            }
        }
        return bestValue;
    }

    // get the value to split on for a categorical value
    private String getValueForCategoricalSplit(String attribute) {
        String bestValue;
        double bestScore = 0;
        
        // get a value to attempt to split on
        ArrayList<String> valuesToCheck;
        switch (attribute) {
            case "gender":
                valuesToCheck = new ArrayList<>(Arrays.asList("Male", "Female", "Other"));
                break;
            case "sleepDuration":
                valuesToCheck = new ArrayList<>(Arrays.asList("Less than 5 hours", "5-6 hours", "7-8 hours", "More than 8 hours", "Other"));
                break;
            case "dietaryHabits":
                valuesToCheck = new ArrayList<>(Arrays.asList("Unhealthy", "Moderate", "Healthy", "Other"));
                break;
            default:
                throw new Exception("Attribute type not found");
        }

        // attempt a split
        for (String v : valuesToCheck) {
            RecordCollection leftSubset = new RecordCollection();
            RecordCollection rightSubset = new RecordCollection();
            for (Record r : subset) {
                switch (attribute) {
                    case "gender":
                        if (r.getGender() == v) leftSubset.add(r);
                        else rightSubset.add(r);
                        break;
                    case "sleepDuration":
                        if (r.getSleepDuration() == v) leftSubset.add(r);
                        else rightSubset.add(r);
                        break;
                    case "dietaryHabits":
                        if (r.getDietaryHabits() == v) leftSubset.add(r);
                        else rightSubset.add(r);
                        break;
                    default:
                        throw new Exception("Attribute type not found");
                }
            }

            // check the output of the split and calculate a score
            double currentScore = leftSubset.percentageOfZeros() + rightSubset.percentageOfZeros();
            currentScore = currentScore > 0.5 ? currentScore : 1 - currentScore;

            // if the current score > best score, set the new value to split on
            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestValue = v;
            }
        }
        return bestValue;
    }

    // get score from a split
    private double getScoreFromSplit(String attribute, double numericalValue, String categoricalValue) {
        RecordCollection leftSubset = new RecordCollection();
        RecordCollection rightSubset = new RecordCollection();

        for (Record record : subset) {
            switch (attribute) {
                case "gender":
                    if (record.getGender() == categoricalValue) leftSubset.add(record);
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
                    if (record.getSleepDuration() == categoricalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "dietaryHabits":
                    if (record.getDietaryHabits() == categoricalValue) leftSubset.add(record);
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
                    throw new Exception("attribute type not found");
            }
        }

        double score = leftSubset.percentageOfZeros() + rightSubset.percentageOfZeros();
        return (score > 0.5 ? score : 1 - score);
    }

    // set the leaf nodes
    private void setLeafNodes() {
        RecordCollection leftSubset = new RecordCollection();
        RecordCollection rightSubset = new RecordCollection();

        for (Record record : subset) {
            switch (attribute) {
                case "gender":
                    if (record.getGender() == categoricalValue) leftSubset.add(record);
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
                    if (record.getSleepDuration() == categoricalValue) leftSubset.add(record);
                    else rightSubset.add(record);
                    break;
                case "dietaryHabits":
                    if (record.getDietaryHabits() == categoricalValue) leftSubset.add(record);
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
                    throw new Exception("attribute type not found");
            }
        }

        left = new Node(leftSubset);
        right = new Node(rightSubset);
    }
}