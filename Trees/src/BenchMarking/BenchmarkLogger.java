package org.example.BenchMarking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class BenchmarkLogger {
    private static final Logger log = LoggerFactory.getLogger(BenchmarkLogger.class);

    // ── Result logging ─────────────────────────────────────────

    public static void logResult(BenchmarkResult result) {
        log.info("RESULT | {} | {} | {} | median={}ns | mean={:.0f}ns | std={:.0f}ns",
                result.getTreeName(),
                result.getDistribution(),
                result.getOperation(),
                result.getMedian(),
                result.getMean(),
                result.getStdDev()
        );
    }

    public static void logSpeedUp(String distribution,
                                  String operation,
                                  long bstMedian,
                                  long rbtMedian) {
        double speedUp = Stats.speedUp(bstMedian, rbtMedian);
        log.info("SPEEDUP | {} | {} | {:.2f}x (RBT {} BST)",
                distribution,
                operation,
                speedUp,
                speedUp >= 1.0 ? "faster than" : "slower than"
        );
    }

    public static void logHeight(String treeName,
                                 String distribution,
                                 int height,
                                 int size) {
        log.info("HEIGHT | {} | {} | height={} | size={}",
                treeName, distribution, height, size);
    }

    // ── Section separators (makes console output readable) ─────

    public static void logSectionStart(String treeName, String distribution) {
        log.info("══════════════════════════════════════════════════");
        log.info("  {} × {}", treeName, distribution);
        log.info("══════════════════════════════════════════════════");
    }

    public static void logBenchmarkComplete() {
        log.warn("══ Benchmark complete ══");   // WARN so it shows even in benchmark mode
    }

    // ── Table printer (goes to System.out for report copy-paste) ──

    /**
     * Prints a formatted Markdown table of all results to stdout.
     * Called ONCE after all benchmarks finish.
     * This is the only place System.out is allowed in benchmark code.
     */
    public static void printTable(List<BenchmarkResult> allResults,
                                  List<BenchmarkResult> bstResults,
                                  List<BenchmarkResult> rbtResults) {

        System.out.println("\n| Tree | Distribution | Operation | Median (ns) | Mean (ns) | StdDev (ns) | Speed-up |");
        System.out.println("|------|-------------|-----------|-------------|-----------|-------------|----------|");

        for (int i = 0; i < bstResults.size(); i++) {
            BenchmarkResult bst = bstResults.get(i);
            BenchmarkResult rbt = rbtResults.get(i);
            double speedUp = Stats.speedUp(bst.getMedian(), rbt.getMedian());

            // BST row
            System.out.printf("| %-6s | %-20s | %-10s | %,11d | %,9.0f | %,11.0f | %8s |%n",
                    bst.getTreeName(),
                    bst.getDistribution(),
                    bst.getOperation(),
                    bst.getMedian(),
                    bst.getMean(),
                    bst.getStdDev(),
                    "—"
            );

            // RBT row with speed-up
            System.out.printf("| %-6s | %-20s | %-10s | %,11d | %,9.0f | %,11.0f | %7.2fx |%n",
                    rbt.getTreeName(),
                    rbt.getDistribution(),
                    rbt.getOperation(),
                    rbt.getMedian(),
                    rbt.getMean(),
                    rbt.getStdDev(),
                    speedUp
            );
        }
    }
}
