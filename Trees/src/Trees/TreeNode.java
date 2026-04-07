package Trees;

public abstract class TreeNode<N extends TreeNode<N>>{
    protected N parent;
    private N left;
    private N right;
    private int value;
    TreeNode(int value) {
        this.value = value;
    }
}
