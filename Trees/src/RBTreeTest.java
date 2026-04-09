package org.example.Tests;

import org.example.Trees.RBTree;
import org.example.Trees.Validator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RBTree Tests")
public class RBTreeTest {

    private RBTree tree;

    @BeforeEach
    void setUp() {
        tree = new RBTree();
        // Validator should be ON during tests — catches structural violations immediately
    }

    // ── insert ─────────────────────────────────────────────────

    @Test
    @DisplayName("insert into empty tree")
    void insertEmpty() {
        assertTrue(tree.insert(10));
        assertEquals(1, tree.size());
    }

    @Test
    @DisplayName("insert duplicate returns false")
    void insertDuplicate() {
        tree.insert(10);
        assertFalse(tree.insert(10));
        assertEquals(1, tree.size());
    }

    @Test
    @DisplayName("root is always black after insert")
    void rootAlwaysBlack() {
        // inserting triggers recoloring — root must stay black
        for (int v : new int[]{10, 20, 30, 15, 25}) {
            tree.insert(v);
            // Validator.checkRBT will throw if root is red
            assertDoesNotThrow(() -> Validator.checkRBT(tree.getRoot(), tree.getNil()));
        }
    }

    @Test
    @DisplayName("RBT invariants hold after sequential inserts")
    void invariantsAfterSequentialInsert() {
        for (int i = 1; i <= 1000; i++) {
            tree.insert(i);
        }
        assertDoesNotThrow(() -> Validator.checkRBT(tree.getRoot(), tree.getNil()));
    }

    @Test
    @DisplayName("RBT invariants hold after random inserts")
    void invariantsAfterRandomInsert() {
        int[] vals = {41, 38, 31, 12, 19, 8, 7, 5, 2, 4};
        for (int v : vals) tree.insert(v);
        assertDoesNotThrow(() -> Validator.checkRBT(tree.getRoot(), tree.getNil()));
    }

    @Test
    @DisplayName("height is bounded by 2*log2(n+1)")
    void heightBounded() {
        for (int i = 1; i <= 1000; i++) tree.insert(i);
        int maxHeight = (int)(2 * Math.log(tree.size() + 1) / Math.log(2)) + 1;
        assertTrue(tree.height() <= maxHeight,
                "Height " + tree.height() + " exceeds bound " + maxHeight);
    }

    // ── contains ───────────────────────────────────────────────

    @Test
    @DisplayName("contains on empty tree returns false")
    void containsEmpty() {
        assertFalse(tree.contains(5));
    }

    @Test
    @DisplayName("contains finds inserted value")
    void containsPresent() {
        tree.insert(5);
        assertTrue(tree.contains(5));
    }

    @Test
    @DisplayName("contains returns false for absent value")
    void containsAbsent() {
        tree.insert(5);
        assertFalse(tree.contains(99));
    }

    // ── delete ─────────────────────────────────────────────────

    @Test
    @DisplayName("delete from empty tree returns false")
    void deleteEmpty() {
        assertFalse(tree.delete(5));
    }

    @Test
    @DisplayName("delete absent value returns false")
    void deleteAbsent() {
        tree.insert(5);
        assertFalse(tree.delete(99));
    }

    @Test
    @DisplayName("delete leaf — invariants hold")
    void deleteLeaf() {
        tree.insert(10);
        tree.insert(5);
        assertTrue(tree.delete(5));
        assertFalse(tree.contains(5));
        assertEquals(1, tree.size());
        assertDoesNotThrow(() -> Validator.checkRBT(tree.getRoot(), tree.getNil()));
    }

    @Test
    @DisplayName("delete root — single node")
    void deleteRoot() {
        tree.insert(10);
        assertTrue(tree.delete(10));
        assertEquals(0, tree.size());
    }

    @Test
    @DisplayName("RBT invariants hold after mixed insert+delete")
    void invariantsAfterMixedOps() {
        for (int i = 1; i <= 100; i++) tree.insert(i);
        for (int i = 1; i <= 100; i += 2) tree.delete(i);   // delete odd numbers
        assertDoesNotThrow(() -> Validator.checkRBT(tree.getRoot(), tree.getNil()));
        assertEquals(50, tree.size());
    }

    @Test
    @DisplayName("delete all elements leaves empty tree")
    void deleteAll() {
        int[] vals = {5, 3, 8, 1, 4, 7, 9};
        for (int v : vals) tree.insert(v);
        for (int v : vals) tree.delete(v);
        assertEquals(0, tree.size());
        assertEquals(0, tree.height());
    }

    // ── inOrder ────────────────────────────────────────────────

    @Test
    @DisplayName("inOrder returns sorted values")
    void inOrderSorted() {
        int[] insertOrder = {5, 3, 8, 1, 4, 7, 9};
        for (int v : insertOrder) tree.insert(v);
        assertArrayEquals(new int[]{1, 3, 4, 5, 7, 8, 9}, tree.inOrder());
    }

    @Test
    @DisplayName("inOrder after delete still sorted")
    void inOrderAfterDelete() {
        for (int v : new int[]{5, 3, 8, 1, 4}) tree.insert(v);
        tree.delete(3);
        assertArrayEquals(new int[]{1, 4, 5, 8}, tree.inOrder());
    }

    // ── stress test ────────────────────────────────────────────

    @Test
    @DisplayName("stress — 10k inserts and deletes maintain invariants")
    void stressTest() {
        for (int i = 0; i < 10_000; i++) tree.insert(i);
        for (int i = 0; i < 5_000; i++) tree.delete(i);
        assertDoesNotThrow(() -> Validator.checkRBT(tree.getRoot(), tree.getNil()));
        assertEquals(5_000, tree.size());
    }
}