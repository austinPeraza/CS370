import java.util.ArrayList;
import java.util.List;

public class WellnessFeedback {

    public List<String> obtainFactors(Record input, Forest forest) {
        // list of all the attributes we find as we traverse trees
        List<String> allAttributes = new ArrayList<>();

        // go through each tree in the forest
        for (Tree tree : forest.getTrees()) {
            // Start at the top (root) of the tree
            Node currentNode = tree.getHead();

            // Keep moving down the tree
            while (!currentNode.isTerminal()) {
                // Grab the attribute we're splitting on
                String attribute = currentNode.getAttribute();
                // Add it to the list of all attributes we found
                allAttributes.add(attribute);
                // Move to the next node based on the user's input
                currentNode = currentNode.evaluate(input);
            }
        }
        // how often each attribute showed up

        // - uniqueAttributes will store the names of the attributes
        // - frequencies will store how many times we saw each one
        List<String> uniqueAttributes = new ArrayList<>();
        List<Integer> frequencies = new ArrayList<>();

        // loop through everything
        for (String attribute : allAttributes) {
            if (uniqueAttributes.contains(attribute)) {
                // just bump up count if seen again
                int index = uniqueAttributes.indexOf(attribute);
                frequencies.set(index, frequencies.get(index) + 1);
            } else {
                // if new, add it and set its count to 1
                uniqueAttributes.add(attribute);
                frequencies.add(1);
            }
        }

        // simple bubble sort by how often they showed up
        for (int i = 0; i < frequencies.size() - 1; i++) {
            for (int j = i + 1; j < frequencies.size(); j++) {
                // if the next one is more frequent, swap them
                if (frequencies.get(j) > frequencies.get(i)) {
                    // swap the counts
                    int tempFreq = frequencies.get(i);
                    frequencies.set(i, frequencies.get(j));
                    frequencies.set(j, tempFreq);

                    // swap the attributes too, gotta keep them in sync
                    String tempAttr = uniqueAttributes.get(i);
                    uniqueAttributes.set(i, uniqueAttributes.get(j));
                    uniqueAttributes.set(j, tempAttr);
                }
            }
        }

        // limit it to just the top 5 (if we have that many)
        if (uniqueAttributes.size() > 5) {
            uniqueAttributes = uniqueAttributes.subList(0, 5);
        }

        // send it back to whoever asked for it
        return uniqueAttributes;
    }
}
