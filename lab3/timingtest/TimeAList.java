package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
//        Timing table for addLast
//        N     time (s)        # ops  microsec/op
//                ------------------------------------------------------------
//        1000         0.00         1000         0.20
//        2000         0.01         2000         0.20
//        4000         0.01         4000         1.20
//        8000         0.04         8000         4.30
//        16000         0.10        16000        10.00
//        32000         0.50        32000        49.70
//        64000         1.15        64000       114.80
//        128000         3.74       128000       374.30

        AList<Integer> Ns = getSizes();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        for (int i = 0; i < Ns.size(); i++) {
            int N = Ns.get(i);
            double timeInSeconds = getTimeToAddLastOfAList(N);
            times.addLast(timeInSeconds);
            opCounts.addLast(N);
        }
        System.out.println("Timing table for addLast");
        printTimingTable(Ns, times, opCounts);
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

    /**
     * Returns the time needed to call addLast method of AList N given times
     * */
    private static double getTimeToAddLastOfAList(int N) {
        AList<Integer> aList = new AList<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            aList.addLast(i);
        }
        return sw.elapsedTime();
    }

}
