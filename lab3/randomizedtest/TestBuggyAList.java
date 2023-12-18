package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        BuggyAList<Integer> buggy = new BuggyAList<>();
        AListNoResizing<Integer> correct = new AListNoResizing<>();

        buggy.addLast(4);
        correct.addLast(4);
        buggy.addLast(5);
        correct.addLast(5);
        buggy.addLast(6);
        correct.addLast(6);

        assertEquals(correct.size(), buggy.size());

        assertEquals(correct.removeLast(), buggy.removeLast());
        assertEquals(correct.removeLast(), buggy.removeLast());
        assertEquals(correct.removeLast(), buggy.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> buggy = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                buggy.addLast(randVal);
                // System.out.println("addLast(" + randVal + ")");
            } else if ((operationNumber == 1) && L.size() > 0) {
                // getLast
                int lastOfCorrect = L.getLast();
                int lastOfBuggy = buggy.getLast();
                assertEquals(lastOfCorrect, lastOfBuggy);
                // System.out.println("getLast() -> " + "Correct: " + lastOfCorrect + "; Buggy: " + lastOfBuggy);
            } else if ((operationNumber == 2) && L.size() > 0) {
                // removeLast
                int lastOfCorrect = L.removeLast();
                int lastOfBuggy = buggy.removeLast();
                assertEquals(lastOfCorrect, lastOfBuggy);
                // System.out.println("removeLast() -> " + "Correct: " + lastOfCorrect + "; Buggy: " + lastOfBuggy);
            }
        }
    }
}
