package org.example.BenchMarking;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class CSVExporter {

    private static final String DEFAULT_PATH = "benchmark_results.csv";

    public static void export(List<BenchmarkResult> bstResults,
                              List<BenchmarkResult> rbtResults,
                              String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            // header
            pw.println("tree,distribution,operation,median_ns,mean_ns,std_ns,speedup");

            for (int i = 0; i < bstResults.size(); i++) {
                BenchmarkResult bst = bstResults.get(i);
                BenchmarkResult rbt = rbtResults.get(i);
                double speedUp = Stats.speedUp(bst.getMedian(), rbt.getMedian());

                // BST row — speedup column is blank (it IS the baseline)
                pw.printf("%s,%s,%s,%d,%.2f,%.2f,%s%n",
                        bst.getTreeName(),
                        bst.getDistribution(),
                        bst.getOperation(),
                        bst.getMedian(),
                        bst.getMean(),
                        bst.getStdDev(),
                        ""
                );

                // RBT row — speedup relative to BST
                pw.printf("%s,%s,%s,%d,%.2f,%.2f,%.3f%n",
                        rbt.getTreeName(),
                        rbt.getDistribution(),
                        rbt.getOperation(),
                        rbt.getMedian(),
                        rbt.getMean(),
                        rbt.getStdDev(),
                        speedUp
                );
            }

            System.out.println("[CSVExporter] Written to: " + filePath);

        } catch (IOException e) {
            System.err.println("[CSVExporter] Failed to write CSV: " + e.getMessage());
        }
    }

    public static void export(List<BenchmarkResult> bstResults,
                              List<BenchmarkResult> rbtResults) {
        export(bstResults, rbtResults, DEFAULT_PATH);
    }
}
