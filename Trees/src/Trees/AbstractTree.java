package Trees;

public abstract class AbstractTree<N extends TreeNode<N>> implements TreeStructure {
    int size;
    N root;
    @Override
    public int size(){
        return this.size;
    }

    @Override
    public int height(){
        
    }

    @Override
    public int[] inOrder(){
        int[] inOrder = new int[size];
        recurseInOrder(root,0,inOrder);

        return inOrder;
    }



    private void recurseInOrder(N node,int i,int[] result){
        if(isNil(node)){
            return;
        }
        recurseInOrder(node.left,i,result);
        result[i++]=node.getValue();
        recurseInOrder(node.right,i,result);


    }
    abstract protected boolean isNil(N node);

}
