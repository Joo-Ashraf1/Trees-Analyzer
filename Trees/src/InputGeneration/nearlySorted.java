package InputGeneration;

import java.util.Random;

public class nearlySorted extends abstractGenerator{
    @Override
    public int[] generate(int percentage) {
        if(percentage<0||percentage>100) throw new IllegalArgumentException("Percentage must be in [0, 100], got: " + percentage);
        ;
        int[]result=new int[n];
        Random rand=new Random();
        rand.setSeed(seed);

        for(int i=0;i<n;i++){
            result[i]=i;
        }
        for(int i=0;i<(int)(n * (percentage / 100.0));i++){
            int a=rand.nextInt(n);
            int b=rand.nextInt(n);
            int tmp = result[a];
            result[a] = result[b];
            result[b] = tmp;

        }
        return result;

    }
}
