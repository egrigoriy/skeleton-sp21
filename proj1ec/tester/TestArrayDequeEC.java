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
        String message = "";

        int numberOfOperations = 1000;
        for (int i = 0; i < numberOfOperations; i++) {
            double n = StdRandom.uniform();
            if (n < 0.5) {
                student.addFirst(i);
                reference.addFirst(i);
                message += "addFirst(" + i + ")" + "\n";
            } else {
                student.addLast(i);
                reference.addLast(i);
                message += "addLast(" + i + ")" + "\n";
            }
        }
        Integer expected = reference.size();
        Integer actual = student.size();
        assertEquals(expected, actual);


        for (int i = 0; i < numberOfOperations; i++) {
            double n = StdRandom.uniform();
            if (n < 0.5) {
                actual = student.removeFirst();
                expected = reference.removeFirst();
                message += "removeFirst()";
                if (expected != actual) {
                    assertEquals(message, expected, actual);
                } else {
                    message += "\n";
                }
            } else {
                actual = student.removeLast();
                expected = reference.removeLast();
                message += "removeLast()";
                if (expected != actual) {
                    assertEquals(message, expected, actual);
                } else {
                    message += "\n";
                }
            }
        }
    }

    // generate random number
    // if result 0 test addFirst
    // if result 1 test removeFirst
    // if result 2 test addLast
    // if result 3 test removeLast
}
