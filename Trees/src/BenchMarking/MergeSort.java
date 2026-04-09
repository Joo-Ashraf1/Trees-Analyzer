package org.example.BenchMarking;

public class MergeSort {
    public static int[] sort(int[] input) {
        int[] copy = input.clone();
        mergeSort(copy);
        return copy;
    }

    private static void mergeSort(int[] array) {
        int len = array.length;
        if (len <= 1) return;

        int middle = len / 2;

        int[] leftArray  = new int[middle];
        int[] rightArray = new int[len - middle];

        // split
        for (int i = 0; i < len; i++) {
            if (i < middle) leftArray[i]         = array[i];
            else            rightArray[i - middle] = array[i];
        }

        mergeSort(leftArray);
        mergeSort(rightArray);
        merge(leftArray, rightArray, array);
    }

    private static void merge(int[] left, int[] right, int[] array) {
        int l = 0, r = 0, i = 0;

        while (l < left.length && r < right.length) {
            if (left[l] <= right[r]) array[i++] = left[l++];
            else                     array[i++] = right[r++];
        }

        while (l < left.length)  array[i++] = left[l++];
        while (r < right.length) array[i++] = right[r++];
    }
}
