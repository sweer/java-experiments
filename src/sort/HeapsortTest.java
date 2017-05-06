package sort;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class HeapsortTest {

    @Test(expected = NullPointerException.class)
    public void nullArray() {
        Heapsort.heapsort(null);
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
    public void threeItemsTopGreatest() {
        runTest(new int[] {3, 2, 1});
    }

    @Test
    public void threeItemsLeftGreatest() {
        runTest(new int[] {1, 3, 2});
    }

    @Test
    public void threeItemsRightGreatest() {
        runTest(new int[] {1, 2, 3});
    }

    /*
     *       2
     *    12     14
     *  8    4  3   5
     *
     */
    @Test
    public void sink() {
        runTest(new int[] {2, 12, 14, 8, 4, 3, 5});
    }


    private void runTest(int a[]) {
        Heapsort.heapsort(a);
        int[] expected = Arrays.copyOf(a, a.length);
        assertArrayEquals(expected, a);
    }

}