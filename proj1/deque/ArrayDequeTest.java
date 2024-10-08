package deque;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class ArrayDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        // System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        ArrayDeque<String> lld1 = new ArrayDeque<>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        // System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    public void getTest() {

        // System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(10);
        lld1.addFirst(20);
        lld1.addFirst(30);

        assertEquals(Integer.valueOf(30), lld1.get(0));
        assertEquals(Integer.valueOf(10), lld1.get(2));

    }


    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        // System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        ArrayDeque<String> lld1 = new ArrayDeque<>();
        ArrayDeque<Double> lld2 = new ArrayDeque<>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        // System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void resizeADequeTest() {

        // System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        int halfSize = 10;
        for (int i = 0; i < 2 * halfSize; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < halfSize; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = halfSize * 2 - 1; i > halfSize; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
}
    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigADequeTest() {

        // System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
        int i = 0;
    }

    @Test
    public void iteratorTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        Iterator<Integer> iterator = lld1.iterator();
        assertEquals(false, iterator.hasNext());

        for (int i = 0; i < 7; i++) {
            lld1.addLast(i);
        }

        for (int i = 0; i < 7; i++) {
            Integer actual = iterator.next();
            assertEquals(Integer.valueOf(i), actual);
        }

    }

    @Test
    public void basicGet() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addFirst(0);
        assertEquals(Integer.valueOf(0), ad.removeFirst());
        ad.addFirst(2);
        assertEquals(Integer.valueOf(2), ad.get(0));
        assertEquals(Integer.valueOf(2), ad.get(0));
        ad.addLast(5);
        ad.addLast(6);
        assertEquals(Integer.valueOf(6), ad.removeLast());
        ad.addLast(8);
        assertEquals(Integer.valueOf(2), ad.removeFirst());
        assertEquals(Integer.valueOf(5), ad.removeFirst());
        assertEquals(Integer.valueOf(8), ad.removeFirst());
        ad.addLast(12);
    }
}
