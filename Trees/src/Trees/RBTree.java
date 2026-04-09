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
        z.left=Nil;
        z.right=Nil;
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
            RBNode z = root;
            while (!isNil(z)) {
                if (val < z.getValue()) z = z.left;
                else if (val > z.getValue()) z = z.right;
                else break;
            }
            if (isNil(z)) return false;
            deleteNode(z);
            size--;
            return true;

    }
    private void deleteNode(RBNode z) {
            RBNode y = z;
            Color yOriginalColor = y.color;
            RBNode x;
            if (isNil(z.left)) {
                x = z.right;
                transplant(z, z.right);
            }
            else if (isNil(z.right)) {
                x = z.left;
                transplant(z, z.left);
            }
            else {
                y = minimum(z.right);
                yOriginalColor = y.color;
                x = y.right;
                if (y.parent == z) {
                    x.parent = y;
                } else {
                    transplant(y, y.right);
                    y.right = z.right;
                    y.right.parent = y;
                }
                transplant(z, y);
                y.left = z.left;
                y.left.parent = y;
                y.color = z.color;
            }

            if (yOriginalColor == Black) {
                deleteFixUp(x);
            }
    }
    private void deleteFixUp(RBNode x) {
        while(x!=root&&x.color==Black) {
            if(x==x.parent.left) {
                RBNode w=x.parent.right;
                if(w.color==Red) { //case when sibling is red
                    x.parent.color=Red;
                    w.color=Black;
                    LeftRotation(x.parent);
                    w=x.parent.right;
                }
                else if(w.color==Black&&w.left.color==Black&&w.right.color==Black) {//case 2 DB is sibling
                    w.color=Red;
                    x=x.parent;
                }
                else{ //case 3 the far nephew is black
                    if(w.right.color==Black&&w.left.color==Red) {
                        w.left.color=Black;
                        w.color=Red;
                        RightRotation(w);
                        w=x.parent.right;
                    }
                    w.color=x.parent.color;
                    x.parent.color=Black;
                    w.right.color=Black;
                    LeftRotation(x.parent);
                    x=root;

                }


            }
            else{
                // symmetric cases
                RBNode w = x.parent.left;

                if (w.color == Red) {
                    w.color = Black;
                    x.parent.color = Red;
                    RightRotation(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color == Black && w.left.color == Black) {
                    w.color = Red;
                    x = x.parent;
                }
                else {
                    if (w.left.color == Black) {
                        w.right.color = Black;
                        w.color = Red;
                        LeftRotation(w);
                        w = x.parent.left;
                    }

                    w.color = x.parent.color;
                    x.parent.color = Black;
                    w.left.color = Black;
                    RightRotation(x.parent);
                    x = root;
                }
            }
        }
        x.color=Black;
    }

    @Override
    public boolean contains(int val) {
        RBNode x=root;
        while (x!=Nil) {
            if(x.getValue()==val) return true;
            else if(x.getValue()<val) x=x.right;
            else x=x.left;
        }
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
