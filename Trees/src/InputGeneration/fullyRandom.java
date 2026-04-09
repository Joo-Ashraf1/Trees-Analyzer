package InputGeneration;

import java.util.stream.IntStream;
import java.util.Random;


public class fullyRandom extends abstractGenerator {
    @Override
    public int[] generate(int percentage) {
        Random rand=new Random();
        rand.setSeed(seed);
        int[]result=new int[n];
        for(int i=0;i<n;i++){
            result[i]=rand.nextInt(range+1);
        }
        return result;
    }
}
