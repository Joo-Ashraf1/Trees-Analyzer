package Trees;

import Enums.Color;

import static Enums.Color.Black;
import static Enums.Color.Red;

public class RBNode extends TreeNode<RBNode> {
    protected Color color; //will change it in its tree
    public RBNode(Color color) {
        super(0);
        this.color = color;
    }
    RBNode(int value) {
        super(value);
        color=Black;
    }
}
