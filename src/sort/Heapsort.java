package sort;

public class Heapsort {

    public static void heapsort(int a[]) {
        if (a.length < 2) {
            return;
        }

        // maxheapify the array, top at 0, children of n at 2n+1 and 2n+2.
        maxHeapify(a);

        // repeatedly swap first and last of the heap, decrease heap size by 1, sink the value
        int heapSize = a.length;
        while (heapSize > 1) {
            swap(a, 0, heapSize - 1);
            heapSize--;
            sink(a, 0, heapSize);
        }
    }

    private static void swap(int a[], int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static void maxHeapify(int[] a) {
        int size = a.length;
        for (int i = (size - 1) / 2; i >= 0; i--) {
            sink(a, i, size);
        }

    }

    private static void sink(int a[], int n, int size) {
        if ((n*2+1) >= size) {
            return; // nothing to sort
        }

        int parent = a[n];
        int left = a[n*2+1];

        if ((n*2+2) == size) {
            // just a single child
            if (parent < left) {
                swap(a, n, n*2+1);
                sink(a, n*2+1, size);
            }
            return;
        }

        int right = a[n*2+2];

        if (left > right) {
            if (parent < left) {
                swap(a, n, n*2+1);
                sink(a, n*2+1, size);
            }
        } else {
            if (parent < right) {
                swap(a, n, n*2+2);
                sink(a, n*2+2, size);
            }
        }
    }
}
