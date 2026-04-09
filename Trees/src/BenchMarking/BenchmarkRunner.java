package org.example.BenchMarking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.example.Trees.TreeStructure;
public class BenchmarkRunner {
    private static final int RUNS = 6;   // run 6, discard first, keep 5
    private static final int KEEP = 5;
    private static final int N = 100_000;
    private static final long SEED = 42L;
    public static List<BenchmarkResult> runAll(TreeStructure tree,String treeName,int[] input,String distributionName){
        List<BenchmarkResult> results = new ArrayList<>();

        // 1. INSERT — build from empty tree each run
        results.add(benchmarkInsert(tree, treeName, input, distributionName));

        // tree is now populated — all subsequent ops use this populated tree

        // 2. CONTAINS — 50k present + 50k absent
        results.add(benchmarkContains(tree, treeName, input, distributionName));

        // 3. DELETE — remove 20% of inserted elements
        results.add(benchmarkDelete(tree, treeName, input, distributionName));

        // 4. SORT — rebuild tree then inOrder (tree sort benchmark)
        results.add(benchmarkSort(tree, treeName, input, distributionName));

        return results;
    }

    private static BenchmarkResult benchmarkInsert(TreeStructure tree, String treeName, int[] input, String distributionName){
        long [] times=new long[RUNS];

        for(int run=0;run<RUNS;run++){
            clearTree(tree);
            long start=System.nanoTime();
            for(int val:input) tree.insert(val);
            times[run]=System.nanoTime()-start;
        }
        BenchmarkLogger.logHeight(treeName, distributionName, tree.height(), tree.size());

        return new BenchmarkResult(treeName, distributionName, "INSERT", dropFirst(times));
    }

    private static BenchmarkResult benchmarkContains(TreeStructure tree,
                                                     String treeName,
                                                     int[] input,
                                                     String distName) {
        // build lookup arrays once — same across all runs
        int[] present = buildPresentArray(input, N / 2);           // 50k values in tree
        int[] absent  = buildAbsentArray(input, N / 2);            // 50k values NOT in tree
        long[] times  = new long[RUNS];

        for (int run = 0; run < RUNS; run++) {
            long start = System.nanoTime();
            for (int val : present) tree.contains(val);
            for (int val : absent)  tree.contains(val);
            times[run] = System.nanoTime() - start;
        }

        return new BenchmarkResult(treeName, distName, "CONTAINS", dropFirst(times));
    }

    private static BenchmarkResult benchmarkDelete(TreeStructure tree,
                                                   String treeName,
                                                   int[] input,
                                                   String distName) {
        int[] toDelete = buildDeleteArray(input, (int)(N * 0.2));
        long[] times   = new long[RUNS];
        for (int run = 0; run < RUNS; run++) {
            // restore full tree before each delete run
            clearTree(tree);
            for (int val : input) tree.insert(val);

            long start = System.nanoTime();
            for (int val : toDelete) tree.delete(val);
            times[run] = System.nanoTime() - start;
        }
        return new BenchmarkResult(treeName, distName, "DELETE", dropFirst(times));
    }
    private static BenchmarkResult benchmarkSort(TreeStructure tree, String treeName, int[] input, String distName) {
        long[] times=new long[RUNS];
        for (int run = 0; run < RUNS; run++) {
            clearTree(tree);
            long start = System.nanoTime();
            for(int val:input){
                tree.insert(val);
            }
            tree.inOrder();
            times[run] = System.nanoTime() - start;
        }
        return new BenchmarkResult(treeName, distName, "SORT", dropFirst(times));
    }

    public static long timeOnce(Benchmarktask task) {
        long start = System.nanoTime();
        task.run();
        return System.nanoTime() - start;
    }
    private static long[] dropFirst(long[] times){
        long[] kept=new long[KEEP];
        System.arraycopy(times,1,kept,0,KEEP);
        return kept;
    }

    private static int[] buildPresentArray(int[] input,int count){
        Random rand=new Random();
        rand.setSeed(SEED);
        int[] present = new int[count];
        for(int i=0;i<count;i++){
            present[i]=input[rand.nextInt(input.length)];
        }
        return present;
    }

    private static int[] buildAbsentArray(int[] input, int count) {
        int[] result = new int[count];
        // values above RANGE are guaranteed absent since input is in [0, 10*N]
        for (int i = 0; i < count; i++) {
            result[i] = 10 * N + 1 + i;
        }
        return result;
    }

    private static int[] buildDeleteArray(int[] input, int count) {
        Random rand=new Random();
        rand.setSeed(SEED+1);
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i]=input[rand.nextInt(input.length)];

        }
        return result;
    }


    private static void clearTree(TreeStructure tree) {
        int[] all = tree.inOrder();
        for (int val : all) tree.delete(val);
    }










}
