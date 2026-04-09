package org.example.BenchMarking;


import org.example.InputGeneration.fullyRandom;
import org.example.InputGeneration.nearlySorted;
import org.example.Trees.BST;
import org.example.Trees.RBTree;
import org.example.Trees.TreeStructure;
import org.example.Trees.AbstractTree;
import org.example.Trees.Validator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Main entry point for the full benchmark suite.
 *
 * Benchmarks:
 *   1. INSERT        — build tree from empty
 *   2. CONTAINS      — 50k present + 50k absent lookups
 *   3. DELETE        — remove 20% of elements
 *   4. SORT (tree)   — build tree + inOrder traversal
 *   5. SORT (merge)  — MergeSort on same input (for comparison)
 *
 * Outputs:
 *   - Console table  → copy into report
 *   - benchmark_results.csv  → feed into Jupyter notebook
 *   - mergesort_results.csv  → feed into Jupyter notebook
 */
public class BenchmarkMain {

    private static final int RUNS = 6;   // run 6, discard first (warmup), keep 5
    private static final int KEEP = 5;

    public static void main(String[] args) {

        // ── Safety check ───────────────────────────────────────
        if (Validator.VALIDATE) {
            System.err.println("ERROR: Validator.VALIDATE = true");
            System.err.println("Flip to false before benchmarking.");
            System.exit(1);
        }

        List<BenchmarkResult> allBstResults   = new ArrayList<>();
        List<BenchmarkResult> allRbtResults   = new ArrayList<>();
        List<BenchmarkResult> allMergeResults = new ArrayList<>();

        // ── Input distributions ────────────────────────────────
        int[][] inputs = {
                new fullyRandom().generate(1),
                new nearlySorted().generate(1),
                new nearlySorted().generate(5),
                new nearlySorted().generate(10)
        };

        String[] distNames = {
                "Fully Random",
                "Nearly Sorted 1%",
                "Nearly Sorted 5%",
                "Nearly Sorted 10%"
        };

        // ── Run all benchmarks ─────────────────────────────────
        for (int d = 0; d < inputs.length; d++) {
            int[]  input    = inputs[d];
            String distName = distNames[d];

            System.out.println("\n=== " + distName + " ===");

            // BST
            TreeStructure bst = new BST();
            List<BenchmarkResult> bstResults =
                    BenchmarkRunner.runAll(bst, "BST", input, distName);

            // RBT — same input array
            TreeStructure rbt = new RBTree();
            List<BenchmarkResult> rbtResults =
                    BenchmarkRunner.runAll(rbt, "RBT", input, distName);

            // MergeSort — SORT operation only, same input
            BenchmarkResult mergeResult = benchmarkMergeSort(input, distName);

            // log results
            for (BenchmarkResult r : bstResults) BenchmarkLogger.logResult(r);
            for (BenchmarkResult r : rbtResults) BenchmarkLogger.logResult(r);
            BenchmarkLogger.logResult(mergeResult);

            // log speed-ups
            for (int i = 0; i < bstResults.size(); i++) {
                BenchmarkLogger.logSpeedUp(
                        distName,
                        bstResults.get(i).getOperation(),
                        bstResults.get(i).getMedian(),
                        rbtResults.get(i).getMedian()
                );
            }

            // tree sort vs merge sort
            BenchmarkResult bstSort = bstResults.get(3);
            BenchmarkResult rbtSort = rbtResults.get(3);
            System.out.printf("  BST sort vs MergeSort: %.2fx%n",
                    Stats.speedUp(bstSort.getMedian(), mergeResult.getMedian()));
            System.out.printf("  RBT sort vs MergeSort: %.2fx%n",
                    Stats.speedUp(rbtSort.getMedian(), mergeResult.getMedian()));

            allBstResults.addAll(bstResults);
            allRbtResults.addAll(rbtResults);
            allMergeResults.add(mergeResult);
        }

        // ── Print console tables ───────────────────────────────
        System.out.println("\n\n══ FULL RESULTS TABLE ══");
        BenchmarkLogger.printTable(new ArrayList<>(), allBstResults, allRbtResults);
        printMergeSortComparison(allBstResults, allRbtResults, allMergeResults);

        // ── Export CSVs ────────────────────────────────────────
        CSVExporter.export(allBstResults, allRbtResults, "benchmark_results.csv");
        exportMergeSortCSV(allMergeResults, "mergesort_results.csv");

        BenchmarkLogger.logBenchmarkComplete();
    }

    // ── MergeSort benchmark ────────────────────────────────────

    private static BenchmarkResult benchmarkMergeSort(int[] input, String distName) {
        long[] times = new long[RUNS];
        for (int run = 0; run < RUNS; run++) {
            long start = System.nanoTime();
            MergeSort.sort(input);          // clones internally — input stays intact
            times[run] = System.nanoTime() - start;
        }
        long[] kept = new long[KEEP];
        System.arraycopy(times, 1, kept, 0, KEEP);
        return new BenchmarkResult("MergeSort", distName, "SORT", kept);
    }

    // ── Sort comparison table ──────────────────────────────────

    private static void printMergeSortComparison(List<BenchmarkResult> bstResults,
                                                 List<BenchmarkResult> rbtResults,
                                                 List<BenchmarkResult> mergeResults) {
        System.out.println("\n\n══ SORT COMPARISON: Tree Sort vs MergeSort ══");
        System.out.println("| Distribution       | BST (ns)      | RBT (ns)      | MergeSort (ns) | BST/Merge | RBT/Merge |");
        System.out.println("|--------------------|---------------|---------------|----------------|-----------|-----------|");

        for (int d = 0; d < mergeResults.size(); d++) {
            BenchmarkResult bstSort   = bstResults.get(d * 4 + 3);   // every 4th = SORT
            BenchmarkResult rbtSort   = rbtResults.get(d * 4 + 3);
            BenchmarkResult mergeSort = mergeResults.get(d);

            System.out.printf("| %-18s | %,13d | %,13d | %,14d | %9.2fx | %9.2fx |%n",
                    mergeSort.getDistribution(),
                    bstSort.getMedian(),
                    rbtSort.getMedian(),
                    mergeSort.getMedian(),
                    Stats.speedUp(bstSort.getMedian(), mergeSort.getMedian()),
                    Stats.speedUp(rbtSort.getMedian(), mergeSort.getMedian())
            );
        }
    }

    // ── MergeSort CSV ──────────────────────────────────────────

    private static void exportMergeSortCSV(List<BenchmarkResult> results, String path) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("tree,distribution,operation,median_ns,mean_ns,std_ns");
            for (BenchmarkResult r : results) {
                pw.printf("%s,%s,%s,%d,%.2f,%.2f%n",
                        r.getTreeName(),
                        r.getDistribution(),
                        r.getOperation(),
                        r.getMedian(),
                        r.getMean(),
                        r.getStdDev()
                );
            }
            System.out.println("[CSV] MergeSort written to: " + path);
        } catch (IOException e) {
            System.err.println("[CSV] Failed: " + e.getMessage());
        }
    }
}
