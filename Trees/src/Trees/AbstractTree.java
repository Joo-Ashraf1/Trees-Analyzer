package Trees;

public abstract class AbstractTree<N extends TreeNode<N>> implements TreeStructure {
   protected int size;
    protected N root;
    @Override
    public int size(){
        return this.size;
    }

    @Override
    public int height(){
        return heightRecursive(root);
    }

    @Override
    public int[] inOrder(){
        int[] inOrder = new int[size];
        int [] index={0}; //this is just to make it global as if its pointer
        recurseInOrder(root,index,inOrder);

        return inOrder;
    }



    private void recurseInOrder(N node,int[] i,int[] result){
        if(isNil(node)){
            return;
        }
        recurseInOrder(node.left,i,result);
        result[i[0]++]=node.getValue();
        recurseInOrder(node.right,i,result);


    }
    private int heightRecursive(N node){
        if(isNil(node)){
            return 0;
        }
        return Math.max(
                heightRecursive(node.left)
                ,heightRecursive(node.right))
                + 1;
        }
    //we take u replace it with this handles connection between daddy and node
    protected void transplant(N u, N v) {
        if(isNil(u.parent)) root=v;
        else if(u==u.parent.left) u.parent.left=v; //this two lines for parent connect
        else u.parent.right=v;
        if(!isNil(v))  v.parent = u.parent;

    }

    protected N minimum(N node) {
        while (!isNil(node.left)) {
            node = node.left;
        }
        return node;
    }
    abstract protected boolean isNil(N node);

}
