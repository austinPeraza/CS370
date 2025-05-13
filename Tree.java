import java.util.ArrayList;

public class Tree {
    private Node head;

    public void buildTree(RecordCollection data, ArrayList<String> features) {
        head = new Node(data);
        head.split(data, features);
        grow(head, features);
    }

    private void grow(Node node, ArrayList<String> features) {
        if (!node.isTerminal()) {
            Node left  = node.getLeftChild();
            Node right = node.getRightChild();
            left.split(left.subset(), features);
            grow(left, features);
            right.split(right.subset(), features);
            grow(right, features);
        }
    }

    
    public int predict(Record input) {
        if (head == null) throw new IllegalStateException("Tree not built");
        return traverse(head, input);
    }

    private int traverse(Node node, Record rec) {
        if (node.isTerminal()) {
            
            double p0 = node.subset().percentageOfZeros();
            return p0 > 0.5 ? 0 : 1;
        } else {
            Node next = node.evaluate(rec);
            return traverse(next, rec);
        }
    }

    protected Node getHead() {
        return head;
    }

    protected void setHead(Node head) {
        this.head = head;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Tree other = (Tree) obj;
            return (this.head.equals(other.getHead())) ? true : false;
        }
    }

    @Override
    public String toString() {
        return toStringHelper(head);
    }

    private String toStringHelper(Node node) {
        if (node.isTerminal()) {
            return node.toString();
        } else {
            return node.toString() + "\n" + toStringHelper(node.getLeftChild()) + "\n" + (node.getRightChild());
        }
    }
}
