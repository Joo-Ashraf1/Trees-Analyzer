package org.example.BenchMarking;

public class BenchmarkResult {
    private final String treeName;
    private final String distribution;
    private final String operation;
    private final long[] timesNs;

    // computed once on first access
    private final long   median;
    private final double mean;
    private final double stdDev;

    public BenchmarkResult(String treeName,
                           String distribution,
                           String operation,
                           long[] timesNs) {
        this.treeName     = treeName;
        this.distribution = distribution;
        this.operation    = operation;
        this.timesNs      = timesNs.clone();

        this.median = Stats.median(timesNs);
        this.mean   = Stats.mean(timesNs);
        this.stdDev = Stats.stdDev(timesNs);
    }

    public String getTreeName()     { return treeName; }
    public String getDistribution() { return distribution; }
    public String getOperation()    { return operation; }
    public long   getMedian()       { return median; }
    public double getMean()         { return mean; }
    public double getStdDev()       { return stdDev; }
    public long[] getTimesNs()      { return timesNs.clone(); }

    @Override
    public String toString() {
        return String.format(
                "%-10s | %-20s | %-12s | median=%,8dns | mean=%,8.0fns | std=%,8.0fns",
                treeName, distribution, operation, median, mean, stdDev
        );
    }
}
