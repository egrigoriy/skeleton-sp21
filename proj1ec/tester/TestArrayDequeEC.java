package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void enqueueBothSidesThenDequeBothSides() {
//        Your test should randomly call StudentArrayDeque and ArrayDequeSolution methods
//        until they disagree on an output.

        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> reference = new ArrayDequeSolution<>();

        for (int i = 0; i < 1000; i++) {
            double n = StdRandom.uniform();
            if (n < 0.5) {
                student.addFirst(i);
                reference.addFirst(i);
            } else {
                student.addLast(i);
                reference.addLast(i);
            }
        }
        int expected = reference.size();
        int actual = student.size();
        assertEquals(expected, actual);


        for (int i = 0; i < 1000; i++) {
            double n = StdRandom.uniform();
            if (n < 0.5) {
                actual = student.removeFirst();
                expected = reference.removeFirst();
                assertEquals(expected, actual);
            } else {
                actual = student.removeLast();
                expected = reference.removeLast();
                assertEquals(expected, actual);
            }
        }
    }

    // generate random number
    // if result 0 test addFirst
    // if result 1 test removeFirst
    // if result 2 test addLast
    // if result 3 test removeLast
}
