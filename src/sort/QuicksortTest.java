package sort;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class QuicksortTest {

    @Test(expected = NullPointerException.class)
    public void nullArray() {
        Quicksort.quicksort(null);
    }


    @Test
    public void emptyArray() {
        runTest(new int[] {});
    }

    @Test
    public void oneItem() {
        runTest(new int[] { 1 });
    }

    @Test
    public void twoItemsRightOrder() {
        runTest(new int[] {1, 2});
    }

    @Test
    public void twoItemsWrongOrder() {
        runTest(new int[] {2, 1});
    }

    @Test
    public void lowReachesPivot() {
        runTest(new int[] {1, 1, 3});
    }

    @Test
    public void highReachesFirst() {
        runTest(new int[] {1, 1, 1});
    }

    @Test
    public void lowReachesHigh() {
        runTest(new int[] {1, 4, 3});
    }

    @Test
    public void highReachedLow() {
        runTest(new int[] {1, 4, 5, 3});
    }

    @Test
    public void sixEquals() {
        runTest(new int[] {1, 1, 1, 1, 1, 1});
    }

    @Test
    public void sevenReverseSorted() {
        runTest(new int[] {9, 8, 7, 6, 5, 4, 3});
    }

    @Test
    public void eightSorted() {
        runTest(new int[] {1, 2, 3, 4, 5, 6, 7, 8});
    }

    @Test
    public void repeated10Mio() {
        int[] a = new int[10000000];
        for (int i = 0; i < a.length; i++) {
            a[i] = i % 256;
        }
        runTest(a);
    }


    private void runTest(int a[]) {
        Quicksort.quicksort(a);
        int[] expected = Arrays.copyOf(a, a.length);
        Assert.assertArrayEquals(expected, a);
    }

}