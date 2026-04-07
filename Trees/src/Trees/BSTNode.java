package Trees;

public class BSTNode extends TreeNode<BSTNode> {
    @Override
    public int compareTo(Object o) {
        return 0;
    }
    BSTNode(int value) {
        super(value);

    }
}
