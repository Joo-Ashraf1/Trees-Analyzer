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
        z.parent=y;
        if(y==Nil) root=z;
        else if(z.getValue()<y.getValue()) y.left=z;
        else y.right=z;
        z.color=Red;
        InsertFixUp(z);
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
    private void InsertFixUp(RBNode z) {
        while(!isNil(z.parent)&&z.parent.color==Red) {
            if(z.parent==z.parent.parent.left) {
                RBNode uncle = z.parent.parent.right;
                if(!isNil(uncle)&&uncle.color==Red) {
                    uncle.color=Black;
                    z.parent.color=Black;
                    z.parent.parent.color=Red;
                    z=z.parent.parent;
                }
                else if(z.parent.right==z) {
                    z=z.parent;
                    LeftRotation(z);
                }
                else{
                    z.parent.color=Black;
                    z.parent.parent.color=Red;
                    RightRotation(z.parent.parent);
                    break;
                }

            }
            else {
                RBNode uncle = z.parent.parent.left;
                if (uncle.color == Red) {
                    z.parent.color = Black;
                    uncle.color = Black;
                    z.parent.parent.color = Red;
                    z = z.parent.parent;
                }
                else if (z == z.parent.left) {
                    z = z.parent;
                    RightRotation(z);
                }
                else {
                    z.parent.color = Black;
                    z.parent.parent.color = Red;
                    LeftRotation(z.parent.parent);
                }
            }

        }
        root.color=Black;
    }
}
