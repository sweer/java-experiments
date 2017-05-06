package sort;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;


public class QuicksortParallelTest {
    @Test(expected = NullPointerException.class)
    public void nullArray() {
        QuicksortParallel.quicksort(null);
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
    public void repeated1Mio() {
        int[] a = new int[1_000_000];
        for (int i = 0; i < a.length; i++) {
            a[i] = i % 256;
        }
        runTest(a);
    }

    @Test
    public void sorted1Mio() {
        int[] a = new int[1_000_000];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        runTest(a);
    }

    @Test
    public void random1Mio() {
        int[] a = new int[1_000_000];
        for (int i = 0; i < a.length; i++) {
            a[i] = (int) (Math.random()*Integer.MAX_VALUE);
        }
        runTest(a);
    }

    @Test
    public void equal1Mio() {
        int[] a = new int[1000000];
        for (int i = 0; i < a.length; i++) {
            a[i] = 7;
        }
        runTest(a);
    }

    private void runTest(int a[]) {
        QuicksortParallel.quicksort(a);
        int[] expected = Arrays.copyOf(a, a.length);
        Assert.assertArrayEquals(expected, a);
    }

}