// Tree.java
public class Tree {
    private Node head;

    /** Build the tree by invoking your Node.split(...) recursively. */
    public void buildTree(RecordCollection data) {
        head = new Node(data);
        head.split(data);
        grow(head);
    }

    private void grow(Node node) {
        if (!node.isTerminal()) {
            Node left  = node.getLeftChild();
            Node right = node.getRightChild();
            left.split(left.subset());
            grow(left);
            right.split(right.subset());
            grow(right);
        }
    }

    /** Traverse using Node.evaluate and at leaf compute majority decision. */
    public int predict(Record input) {
        if (head == null) throw new IllegalStateException("Tree not built");
        return traverse(head, input);
    }

    private int traverse(Node node, Record rec) {
        if (node.isTerminal()) {
            // majority vote: more zeros → 0, else 1
            double p0 = node.subset().percentageOfZeros();
            return p0 > 0.5 ? 0 : 1;
        } else {
            Node next = node.evaluate(rec);
            return traverse(next, rec);
        }
    }
}
