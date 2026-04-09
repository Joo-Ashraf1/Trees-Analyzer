package org.example.BenchMarking;

import java.util.Arrays;

public final class Stats {
    private Stats() {
    }
    public static double mean(long[] times){
        long sum = 0;
        for(long time:times){
            sum+=time;
        }
        return (double) sum/times.length;
    }
    public static long median(long[] times){
        long[] sorted= Arrays.copyOf(times,times.length);
        Arrays.sort(sorted);
        int mid=sorted.length/2;
        if (sorted.length % 2 == 0) {
            return (sorted[mid - 1] + sorted[mid]) / 2;
        }
        return sorted[mid];
    }
    public static double stdDev(long[] times) {
        double m = mean(times);
        double variance = 0;
        for (long t : times) {
            variance += (t - m) * (t - m);
        }
        return Math.sqrt(variance / times.length);
    }

    public static double speedUp(long bstMedian, long rbtMedian) {
        if (rbtMedian == 0) return Double.POSITIVE_INFINITY;
        return (double) bstMedian / rbtMedian;
    }
}
