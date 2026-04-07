package Trees;

public interface TreeStructure {
    boolean insert(int val);
    boolean delete(int val);
    boolean contains(int val);
    int[] inOrder();
    int height();
    int size();
}
