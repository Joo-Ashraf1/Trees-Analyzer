package Trees;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BST extends AbstractTree<BSTNode>{



    @Override
    public boolean insert(int val) {
        if(isNil(root)){
            root = new BSTNode(val);
            size++;
            return true;
        }
        BSTNode current = root;
        while(true){
            if(val<current.getValue()){
                if(isNil(current.left)){
                    current.left = new BSTNode(val);
                    current.left.parent = current;
                    size++;
                    return true;
                }
                else{
                    current = current.left;
                }
            }
            else if(val>current.getValue()){
                if(isNil(current.right)){
                    current.right = new BSTNode(val);
                    current.right.parent = current;
                    size++;
                    return true;
                }
                else{
                    current = current.right;
                }
            }
            else return false;

        }
    }

    @Override
    public boolean delete(int val) {
        if(isNil(root)) return false;
        BSTNode current = root;
        while(!isNil(current)){
            if(val<current.getValue()) current = current.left;
            else if(val>current.getValue()) current = current.right;
            else{
                vanish(current);
                return  true;
            }
        }
        return false;
    }
    private void vanish(BSTNode node) {
        if (isNil(node.left) && isNil(node.right)) {
            transplant(node, null);
        }

        else if (isNil(node.left)) {
            transplant(node, node.right);
        }

        else if (isNil(node.right)) {
            transplant(node, node.left);
        }

        else {
            BSTNode successor = minimum(node.right);

            if (successor.parent != node) {
                transplant(successor, successor.right);
                successor.right = node.right;
                successor.right.parent = successor;
            }
            transplant(node, successor);
            successor.left = node.left;
            successor.left.parent = successor;
        }

        size--;
    }
    //we take u replace it with this handles connection between daddy and node
    private void transplant(BSTNode u, BSTNode v) {
        if(isNil(u.parent)) root=v;
        else if(u==u.parent.left) u.parent.left=v; //this two lines for parent connect
        else u.parent.right=v;
        if(!isNil(v))  v.parent = u.parent;

    }

    private BSTNode minimum(BSTNode node) {
        while (!isNil(node.left)) {
            node = node.left;
        }
        return node;
    }

    @Override
    public boolean contains(int val) {
        BSTNode current = root;
        while(!isNil(current)){
            if(val==current.getValue()) return true;
            else if(val<current.getValue()) current = current.left;
            else current = current.right;
        }
        return false;
    }
    @Override
    public int[] inOrder() {
        int i=0;
        int[] result = new int[size];
        Stack<BSTNode> stack = new Stack<>();
        BSTNode current = root;
        while(!stack.isEmpty()||!isNil(current)){
            while(!isNil(current)){
                stack.push(current);
                current = current.left;
            }
            current = stack.pop();
            result[i++]=current.getValue();
            current = current.right;
        }
        return result;
    }
    @Override
    public int height() {
        if (isNil(root)) {
            return 0;
        }

        Queue<BSTNode> queue = new LinkedList<>();
        queue.add(root);
        int height = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                BSTNode current = queue.poll();

                if (!isNil(current.left)) {
                    queue.add(current.left);
                }
                if (!isNil(current.right)) {
                    queue.add(current.right);
                }
            }
            height++;
        }

        return height;    }

    @Override
    protected boolean isNil(BSTNode node) {
        return node == null;
    }

}
