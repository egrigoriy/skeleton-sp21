package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        System.out.println("Timing table for getLast");
        AList<Integer> sizes = getSizes();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        // 1. Create an SLList.
        SLList<Integer> aSLList = new SLList<>();

        for (int i = 0; i < sizes.size(); i++) {
            int N = sizes.get(i);
        // 2. Add N items to the SLList.
            addNItems(aSLList, N);
        // 3. Start the timer.
            Stopwatch sw = new Stopwatch();
        // 4. Perform M getLast operations on the SLList.
            int M = 10000;
            getLastMTimes(aSLList, M);
        // 5. Check the timer. This gives the total time to complete all M operations.
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            opCounts.addLast(M);
        }
        printTimingTable(sizes, times, opCounts);
    }

    /** Performs getLast operation on a given SLList given M times */
    private static void getLastMTimes(SLList<Integer> aSLList, int M) {
        for (int j = 0; j < M; j++) {
            aSLList.getLast();
        }
    }

    /** Performs addLast operation on a given SLList given N times */
    private static void addNItems(SLList<Integer> aSLList, int N) {
        for (int i = 0; i < N; i++) {
            aSLList.addLast(i);
        }
    }


    /**
     * Returns an AList with sizes of data structures
     * */
    private static AList<Integer> getSizes() {
        AList<Integer> sizes = new AList<>();
        sizes.addLast(1000);
        sizes.addLast(2000);
        sizes.addLast(4000);
        sizes.addLast(8000);
        sizes.addLast(16000);
        sizes.addLast(32000);
        sizes.addLast(64000);
        return sizes;
    }
}
