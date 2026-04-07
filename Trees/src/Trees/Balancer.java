package Trees;

public interface Balancer<T>  {
    public void insertFixUp(RBNode node);
    public void deleteFixUp(RBNode node);
}
