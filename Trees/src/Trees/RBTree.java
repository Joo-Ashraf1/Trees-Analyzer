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
        return false;
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
}
