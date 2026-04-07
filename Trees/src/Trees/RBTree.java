package Trees;

import Enums.Color;

import static Enums.Color.Black;
import static Enums.Color.Red;

public class RBTree extends AbstractTree<RBNode> {
    protected final RBNode Nil=new RBNode(0);
    public RBTree() {
        Nil.color= Black;
        Nil.left=Nil;
        Nil.right=Nil;
        Nil.parent=Nil;
        root=Nil;
    }


    @Override
    public boolean insert(int val) {
        RBNode x=root;
        RBNode y=x;
        while (x!=Nil) {
            y=x;
            if(x.getValue()<val) x=x.right;
            else if(x.getValue()>val) x=x.left;
            else return false;
        }
        RBNode z=new RBNode(val);
        z.parent=x;
        if(y==Nil) root=z;
        else if(z.getValue()<y.getValue()) y.left=z;
        else y.right=z;
        z.color=Red;
        insertFixUp(z);
        size++;
        return true;
    }

    @Override
    public boolean delete(int val) {
        return false;
    }

    @Override
    public boolean contains(int val) {
        return false;
    }
    @Override
    protected boolean isNil(RBNode node) {
        return node == Nil;
    }
    private void LeftRotation(RBNode node) {
        if(isNil(node)||isNil(node.right)) return;
        RBNode res=node.right;
        node.right=res.left;
        if(!isNil(res.left)) res.left.parent=node;
        res.parent=node.parent;
        if(isNil(node.parent)) root=res;
        else if(node.parent.left==node) node.parent.left=res;
        else node.parent.right=res;
        res.left=node;
        node.parent=res;
    }
    private void RightRotation(RBNode node) {
            if (isNil(node) || isNil(node.left)) return;
            RBNode res = node.left;
            node.left = res.right;
            if (!isNil(res.right)) {
                res.right.parent = node;
            }
            res.parent = node.parent;
            if (isNil(node.parent)) {
                root = res;
            } else if (node == node.parent.right) {
                node.parent.right = res;
            } else {
                node.parent.left = res;
            }
            res.right = node;
            node.parent = res;

    }
    private void InsertFixUp(RBNode node) {
        System.out.println("I wanna sleep");
    }
}
