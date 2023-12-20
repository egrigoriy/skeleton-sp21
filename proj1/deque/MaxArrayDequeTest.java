package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    @Test
    public void genericComparator() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new BiggerInt());
        mad.addLast(12);
        mad.addLast(99);
        mad.addLast(3);
        mad.addLast(25);
        assertEquals(Integer.valueOf(99), mad.max());
    }

    @Test
    public void givenComparator() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new BiggerInt());
        mad.addLast(12);
        mad.addLast(99);
        mad.addLast(3);
        mad.addLast(25);
        assertEquals(Integer.valueOf(3), mad.max(new SmallerInt()));
    }

    @Test
    public void manyMaxValues() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new BiggerInt());
        mad.addLast(12);
        mad.addLast(66);
        mad.addLast(3);
        mad.addLast(66);
        assertEquals(Integer.valueOf(66), mad.max());

    }

    class BiggerInt implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            return (int)o1 - (int)o2;
        }
    }
    class SmallerInt implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            return (int)o2 - (int)o1;
        }
    }
}
