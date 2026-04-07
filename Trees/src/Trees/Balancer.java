package Trees;

public interface Balancer<T>  {
    public void insertFixUp(RB node);
    public void deleteFixUp(RB node);
}
