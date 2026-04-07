package Trees;

public abstract class TreeNode<N extends TreeNode<N>>{
    protected N parent;
    protected N left;
    protected N right;
    private final int value;
    TreeNode(int value) {
        this.value = value;
    }
    int getValue(){
        return this.value;
    }
}
