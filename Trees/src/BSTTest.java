package org.example.Tests;

import org.example.Trees.BST;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BST Tests")
public class BSTTest {

    private BST tree;

    @BeforeEach
    void setUp() {
        tree = new BST();
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
    @DisplayName("insert multiple maintains size")
    void insertMultiple() {
        for (int i = 1; i <= 10; i++) tree.insert(i);
        assertEquals(10, tree.size());
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
    @DisplayName("delete leaf node")
    void deleteLeaf() {
        tree.insert(10);
        tree.insert(5);
        assertTrue(tree.delete(5));
        assertFalse(tree.contains(5));
        assertEquals(1, tree.size());
    }

    @Test
    @DisplayName("delete node with one child")
    void deleteOneChild() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(3);
        assertTrue(tree.delete(5));
        assertFalse(tree.contains(5));
        assertTrue(tree.contains(3));
        assertEquals(2, tree.size());
    }

    @Test
    @DisplayName("delete node with two children")
    void deleteTwoChildren() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        assertTrue(tree.delete(10));
        assertFalse(tree.contains(10));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(15));
        assertEquals(2, tree.size());
    }

    @Test
    @DisplayName("delete root — single node")
    void deleteRoot() {
        tree.insert(10);
        assertTrue(tree.delete(10));
        assertEquals(0, tree.size());
        assertFalse(tree.contains(10));
    }

    // ── inOrder ────────────────────────────────────────────────

    @Test
    @DisplayName("inOrder on empty tree returns empty array")
    void inOrderEmpty() {
        assertArrayEquals(new int[]{}, tree.inOrder());
    }

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

    // ── height ─────────────────────────────────────────────────

    @Test
    @DisplayName("height of empty tree is 0")
    void heightEmpty() {
        assertEquals(0, tree.height());
    }

    @Test
    @DisplayName("height of single node is 1")
    void heightSingle() {
        tree.insert(10);
        assertEquals(1, tree.height());
    }

    @Test
    @DisplayName("height of balanced tree")
    void heightBalanced() {
        // insert 4, 2, 6, 1, 3, 5, 7 → perfect binary tree height 3
        for (int v : new int[]{4, 2, 6, 1, 3, 5, 7}) tree.insert(v);
        assertEquals(3, tree.height());
    }

    // ── edge cases ─────────────────────────────────────────────

    @Test
    @DisplayName("insert and delete all elements leaves empty tree")
    void insertDeleteAll() {
        int[] vals = {5, 3, 8, 1, 4, 7, 9};
        for (int v : vals) tree.insert(v);
        for (int v : vals) tree.delete(v);
        assertEquals(0, tree.size());
        assertEquals(0, tree.height());
        assertArrayEquals(new int[]{}, tree.inOrder());
    }

    @Test
    @DisplayName("large sequential insert does not throw StackOverflow")
    void largeSequentialInsert() {
        // nearly-sorted is the dangerous case for BST recursion
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100_000; i++) tree.insert(i);
        });
    }
}