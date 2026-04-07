package Trees;

import Enums.Color;

public class RBNode extends TreeNode<RBNode> {
    Color color;
    RBNode(int value) {
        super(value);
        color=Color.black;
    }
}
