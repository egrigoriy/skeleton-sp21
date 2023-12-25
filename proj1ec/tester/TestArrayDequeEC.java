package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void testRandomAddRemove() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> reference = new ArrayDequeSolution<>();
        StringBuilder message = new StringBuilder();

        int numberOfOperations = 10;

        randomlyAddFirstLast(numberOfOperations, reference, student, message);
        assertEqualWhenRandomlyRemoveFirstLast(numberOfOperations, reference, student, message);
    }

    private void randomlyAddFirstLast(int numberOfOperations, ArrayDequeSolution<Integer> reference, StudentArrayDeque<Integer> student, StringBuilder message) {
        for (int i = 0; i < numberOfOperations; i++) {
            double n = StdRandom.uniform();
            if (n < 0.5) {
                student.addFirst(i);
                reference.addFirst(i);
                message.append("addFirst(" + i + ")\n");
            } else {
                student.addLast(i);
                reference.addLast(i);
                message.append("addLast(" + i + ")\n");
            }
        }
    }

    private void assertEqualWhenRandomlyRemoveFirstLast(int numberOfOperations,
                                                        ArrayDequeSolution<Integer> reference,
                                                        StudentArrayDeque<Integer> student,
                                                        StringBuilder message) {
        for (int i = 0; i < numberOfOperations; i++) {
            Integer actual;
            Integer expected;
            double n = StdRandom.uniform();
            if (n < 0.5) {
                actual = student.removeFirst();
                expected = reference.removeFirst();
                message.append("removeFirst()");
                assertEqualsOrAppendMessage(message, expected, actual);
            } else {
                actual = student.removeLast();
                expected = reference.removeLast();
                message.append("removeLast()");
                assertEqualsOrAppendMessage(message, expected, actual);
            }
        }
    }

    public void assertEqualsOrAppendMessage(StringBuilder message, Integer expected, Integer actual) {
        if (!expected.equals(actual)) {
            assertEquals(message.toString(), expected, actual);
        } else {
            message.append("\n");
        }
    }
}
