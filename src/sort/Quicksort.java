package sort;

/*
 * sorts an array in an ascending order
 *
 */
public class Quicksort {
        public static void quicksort(int a[]) {
            quicksortRecursive(a, 0, a.length - 1);
        }

        private static void swap (int a[], int i, int j) {
            int temp = a[i]; a[i] = a[j]; a[j] = temp;
        }

        private static void quicksortRecursive(int a[], int aLow, int aHigh) {
            if (aLow >= aHigh) {
                return;
            }
            if (aHigh - aLow == 1) {
                if (a[aLow] > a[aHigh]) {
                    swap(a, aLow, aHigh);
                }
                return;
            }
            // so now there are at least 3 elements to sort
            int pivot = a[aHigh];
            int high = aHigh - 1;
            int low = aLow;

            while (low < high) {
                while (a[low] < pivot) { // it'll reach pivot in the worst case and then stop
                    low++;
                }
                while (a[high] >= pivot && high > low) {
                    high--;
                }
                if (low < high) {
                    swap(a, low, high);
                    low++;
                    high--;
                }
            }

            // so now they've either met or glided past another
            // (low == high) <==> a[low] >= pivot, a[low+1] >= pivot (or is the pivot itself) ==> swap low and pivot
            // (low == aHigh) ==> low has reached pivot, entire region is < pivot, so it's in the right place , do nothing
            // else (low > high) <==> low = high + 1, we've swapped two adjancent items, swap low and pivot

            int newPivotPosition = low;
            if (newPivotPosition != aHigh) {
                swap(a, newPivotPosition, aHigh);
            }

            quicksortRecursive(a, aLow, low - 1);
            quicksortRecursive(a, low + 1, aHigh);

            // optimization: call recursive for the smaller part, repeat myself with changed arguments for the bigger

        }
}
