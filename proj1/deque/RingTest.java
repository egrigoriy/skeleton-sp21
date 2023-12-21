package deque;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RingTest {
    @Test
    public void canCreateARing() {
        Ring r = new Ring(12);
    }

    @Test
    public void nextNoEdge() {
        Ring r = new Ring(13);
        assertEquals(1, r.getNext(0));
        assertEquals(10, r.getNext(9));
    }

    @Test
    public void nextEdge() {
        Ring r = new Ring(13);
        assertEquals(0, r.getNext(12));
    }

    @Test
    public void prevNoEdge() {
        Ring r = new Ring(13);
        assertEquals(0, r.getPrev(1));
        assertEquals(11, r.getPrev(12));
    }

    @Test
    public void prevEdge() {
        Ring r = new Ring(13);
        assertEquals(12, r.getPrev(0));
    }

    @Test
    public void getLinearIndexWithShiftNoEdgeTest() {
        Ring r = new Ring(13);
        assertEquals(0, r.getLinearIndexWithShift(0, 0));
        assertEquals(1, r.getLinearIndexWithShift(1, 0));

        assertEquals(0, r.getLinearIndexWithShift(5, 5));
        assertEquals(1, r.getLinearIndexWithShift(6, 5));
        assertEquals(2, r.getLinearIndexWithShift(7, 5));
        assertEquals(3, r.getLinearIndexWithShift(8, 5));
    }
    @Test
    public void getLinearIndexWithShiftEdgeTest() {
        Ring r = new Ring(13);
        assertEquals(7, r.getLinearIndexWithShift(12, 5));
        assertEquals(8, r.getLinearIndexWithShift(0, 5));
        assertEquals(9, r.getLinearIndexWithShift(1, 5));
        assertEquals(10, r.getLinearIndexWithShift(2, 5));
    }

    @Test
    public void getRingIndexWithShiftNoEdgeTest() {
        Ring r = new Ring(13);
        assertEquals(0, r.getRingIndexWithShift(0, 0));
        assertEquals(1, r.getRingIndexWithShift(1, 0));

        assertEquals(5, r.getRingIndexWithShift(3, 2));
        assertEquals(6, r.getRingIndexWithShift(1, 5));
        assertEquals(7, r.getRingIndexWithShift(2, 5));
        assertEquals(8, r.getRingIndexWithShift(3, 5));
        assertEquals(9, r.getRingIndexWithShift(4, 5));
    }

    @Test
    public void getRingIndexWithShiftEdgeTest() {
        Ring r = new Ring(13);

        assertEquals(12, r.getRingIndexWithShift(7, 5));
        assertEquals(0, r.getRingIndexWithShift(8, 5));
        assertEquals(1, r.getRingIndexWithShift(9, 5));
    }


    @Test
    public void composition() {
        Ring r = new Ring(13);

        int shift = 5;
        int ringIndex = 7;
        int linearIndex = r.getLinearIndexWithShift(ringIndex, shift);
        assertEquals(ringIndex, r.getRingIndexWithShift(linearIndex, shift));
    }
}
