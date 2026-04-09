package Trees;

import static Enums.Color.Black;
import static Enums.Color.Red;

public class Validator {
    public static final boolean VALIDATE = true;

    public static void checkBST(BSTNode root) {
        checkBSTProperties(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
        checkParentPointers(root, null);
    }


    public static void checkRBT(RBNode root, RBNode nil) {
        if (root == nil) return;

        // Rule 1 — root must be black
        if (root.color != Black) {
            throw new RuntimeException("RBT violated — root is not BLACK");
        }

        // Rule 2 — no red node has a red child
        checkNoDoubleRed(root, nil);

        // Rule 3 — all paths to nil have same black height
        checkBlackHeight(root, nil);

        // Rule 4 — parent pointers consistent
        checkRBParentPointers(root, nil, nil);

        // Rule 5 — BST ordering still holds
        checkRBTOrder(root, nil, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static void checkParentPointers(BSTNode node, BSTNode expectedParent) {
        if (node == null) return;

        if (node.parent != expectedParent) {
            throw new RuntimeException(
                    "Parent pointer broken at node " + node.getValue()
            );
        }

        checkParentPointers(node.left,  node);
        checkParentPointers(node.right, node);

    }


    private static void checkBSTProperties(BSTNode node, int min, int max) {
        if (node == null) return;
        if (node.getValue() <= min && node.getValue() >= max) {
            throw new RuntimeException(
                    "BST property violated at node " + node.getValue() +
                            " — expected in range (" + min + ", " + max + ")"
            );
        }
        checkBSTProperties(node.left, min, node.getValue());
        checkBSTProperties(node.right, node.getValue(), max);
    }
    //*********************************************************************************************
    private static void checkNoDoubleRed(RBNode node, RBNode nil) {
        if (node == nil) return;

        if (node.color == Red) {
            if (node.left != nil && node.left.color == Red) {
                throw new RuntimeException(
                        "Double red violation — node " + node.getValue() +
                                " (RED) has red left child " + node.left.getValue()
                );
            }
            if (node.right != nil && node.right.color == Red) {
                throw new RuntimeException(
                        "Double red violation — node " + node.getValue() +
                                " (RED) has red right child " + node.right.getValue()
                );
            }
        }

        checkNoDoubleRed(node.left,  nil);
        checkNoDoubleRed(node.right, nil);
    }

    // returns black height of subtree, throws if paths are inconsistent
    private static int checkBlackHeight(RBNode node, RBNode nil) {
        if (node == nil) return 1;  // nil counts as black

        int leftHeight  = checkBlackHeight(node.left,  nil);
        int rightHeight = checkBlackHeight(node.right, nil);

        if (leftHeight != rightHeight) {
            throw new RuntimeException(
                    "Black height violation at node " + node.getValue() +
                            " — left black height=" + leftHeight +
                            ", right black height=" + rightHeight
            );
        }

        // add 1 if this node is black
        return leftHeight + (node.color == Black ? 1 : 0);
    }

    // every node's parent pointer must be consistent
    private static void checkRBParentPointers(RBNode node, RBNode nil, RBNode expectedParent) {
        if (node == nil) return;

        if (node.parent != expectedParent) {
            throw new RuntimeException(
                    "Parent pointer broken at RBT node " + node.getValue()
            );
        }

        checkRBParentPointers(node.left,  nil, node);
        checkRBParentPointers(node.right, nil, node);
    }

    // BST ordering must still hold in RBT
    private static void checkRBTOrder(RBNode node, RBNode nil, int min, int max) {
        if (node == nil) return;

        if (node.getValue() <= min || node.getValue() >= max) {
            throw new RuntimeException(
                    "BST order violated in RBT at node " + node.getValue() +
                            " — expected in range (" + min + ", " + max + ")"
            );
        }

        checkRBTOrder(node.left,  nil, min,            node.getValue());
        checkRBTOrder(node.right, nil, node.getValue(), max);
    }
}
