package InputGeneration;

import java.util.stream.IntStream;

public abstract class abstractGenerator implements inputGenerator {
    protected static final int n=100000;
    protected static final int range=n*10;
    protected static final long seed=42L;
    abstract public int[] generate(int seed);
}
