package sort;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/*
 * Still unclear, if it is really that important to limit number of tasks we keep in an Executor at any given moment.
 *
 */
public class QuicksortParallel {
    public static final int PARALLEL_MIN_SIZE = 100000;
    private final int[] a;
    private final int parallelThreads = Runtime.getRuntime().availableProcessors() - 1;
    private final ExecutorService executor = Executors.newFixedThreadPool(parallelThreads);
    private final Set<Future<Void>> parallels = ConcurrentHashMap.newKeySet();
    private final Set<Future<Void>> doneParallels = ConcurrentHashMap.newKeySet();

    public QuicksortParallel(int[] a) {
        this.a = a;
    }

    // Assuming 128 bytes per recursive call,
    // 8k-deep recursion may well overflow 1MB standard thread stack
    public static void quicksort(int a[]) {
        new QuicksortParallel(a).quicksortStart();
    }

    private void quicksortStart() {
        quicksortRecursive(0, a.length - 1, 0);
        // Future.get gives a happens-before guarantee to the thread executing the get()
        waitGetParallels(parallels);
        waitGetParallels(doneParallels);

        // Stop threads. No submitted or running tasks shall remain
        List<Runnable> remainingTasks = executor.shutdownNow();
        if (remainingTasks.size() > 0) {
                throw new RuntimeException(this.getClass().getName() + ": not all tasks finished");
        }
    }

    private void waitGetParallels(Set<Future<Void>> whom) {
        // They are spawning children, so iterate until none left
        while (whom.size() > 0) {
            for (Future<Void> f : whom) {
                try {
                    f.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                whom.remove(f);
            }
        }
    }


    private void swap(int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private void quicksortRecursive(int aLow, int aHigh, int depth) {
        while (aLow < aHigh) {
            if (aHigh - aLow == 1) {
                if (a[aLow] > a[aHigh]) {
                    swap(aLow, aHigh);
                }
                return;
            }
            // so now there are at least 3 elements to sort
            int medianPosition = medianPosition(aLow, aLow / 2 + aHigh / 2, aHigh);
            if (medianPosition != aHigh) {
                swap(medianPosition, aHigh);
            }

            int newPivotPosition = findNewPivotPosition(aLow, aHigh);
            if (newPivotPosition != aHigh) {
                swap(newPivotPosition, aHigh);
            }

            // optimization - exclude the sequence of repeated pivot value - helps a lot
            // if there are many repeated values in the array
            int lowRight = newPivotPosition + 1;
            int highLeft = newPivotPosition - 1;
            while (lowRight < aHigh && a[lowRight] == a[newPivotPosition]) {
                lowRight++;
            }

            while (aLow < highLeft && a[highLeft] == a[newPivotPosition]) {
                highLeft--;
            }

            // optimization: call recursive for the smaller part, repeat myself with changed arguments for the bigger
            int lenLeft = highLeft - aLow + 1;
            int lenRight = aHigh - lowRight + 1;

            if (lenLeft > lenRight) {
                invokeParallel(lowRight, aHigh, depth + 1);
                aHigh = highLeft;
            } else {
                invokeParallel(aLow, highLeft, depth + 1);
                aLow = lowRight;
            }
        }

    }

    private void invokeParallel(final int aLow, final int aHigh, final int depth) {

        if (aHigh - aLow < PARALLEL_MIN_SIZE) {
            quicksortRecursive(aLow, aHigh, depth);
            return;
        }

        // Neither synchronized, nor atomic. Thus we may end up with more tasks than threads.

        cleanParallels();

        if (parallels.size() >= parallelThreads) {
            quicksortRecursive(aLow, aHigh, depth);
            return;
        }

        Callable<Void> newTask = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                quicksortRecursive(aLow, aHigh, depth);
                return null;
            }
        };

        parallels.add(executor.submit(newTask));

        // System.out.println("Parallel tasks: " + parallels.size());

    }

    private void cleanParallels() {
        parallels.stream()
                .filter(f -> f.isDone())
                .forEach(f -> {
                    doneParallels.add(f);
                    parallels.remove(f);
                });
    }

    public int findNewPivotPosition(int aLow, int aHigh) {
        int pivot = a[aHigh];
        // System.out.println("aLow = " + aLow + ", aHigh = " + aHigh + ", pivot = " + pivot);
        int low = aLow;
        int high = aHigh - 1;
        while (low < high) {
            while (a[low] < pivot) { // it'll reach pivot in the worst case and then stop
                low++;
            }
            while (a[high] >= pivot && high > low) {
                high--;
            }
            if (low < high) {
                swap(low, high);
                low++;
                high--;
            }
        }

        // so now low & high have either met or glided past another
        // (low == high) <==> a[low] >= pivot, a[low+1] >= pivot (or is the pivot itself) ==> swap low and pivot
        // (low == aHigh) ==> low has reached pivot, entire region is < pivot, so it's in the right place , do nothing
        // else (low > high) <==> low = high + 1, we've swapped two adjancent items, swap low and pivot

        return low;
    }

    public int medianPosition(int i, int j, int k) {
        int medianPosition;
        int ai = a[i];
        int aj = a[j];
        int ak = a[k];
        if ((ai >= aj && aj >= ak) || (ai <= aj && aj <= ak)) {
            medianPosition = j;
        } else if ((aj >= ai && ai >= ak) || (aj <= ai && ai <= ak)) {
            medianPosition = i;
        } else {
            medianPosition = k;
        }
        // System.out.println("ai = " + ai + ", aj = " + aj + ", ak = " + ak + " ==> median position = " + medianPosition) ;
        return medianPosition;
    }

}
