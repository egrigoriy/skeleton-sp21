package deque;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CyclicCursorTest {
    @Test
    public void canCreate() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.FORTH, 5);
        assertEquals(5, c.getPosition());
    }

    @Test
    public void getNextPositionForthNoEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.FORTH, 5);
        assertEquals(6, c.getNextPosition());
    }

    @Test
    public void getNextPositionBackNoEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.BACK, 5);
        assertEquals(4, c.getNextPosition());
    }

    @Test
    public void getNextPositionForthEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.FORTH, 9);
        assertEquals(0, c.getNextPosition());
    }

    @Test
    public void getNextPositionBackEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.BACK, 0);
        assertEquals(9, c.getNextPosition());
    }

    @Test
    public void advanceForthNoEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.FORTH, 5);
        c.advance();
        assertEquals(6, c.getPosition());
        c.advance();
        assertEquals(7, c.getPosition());
        c.advance();
        assertEquals(8, c.getPosition());
    }

    @Test
    public void advanceBackNoEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.BACK, 5);
        c.advance();
        assertEquals(4, c.getPosition());
        c.advance();
        assertEquals(3, c.getPosition());
        c.advance();
        assertEquals(2, c.getPosition());
    }

    @Test
    public void advanceForthEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.FORTH, 8);
        c.advance();
        assertEquals(9, c.getPosition());
        c.advance();
        assertEquals(0, c.getPosition());
        c.advance();
        assertEquals(1, c.getPosition());
    }

    @Test
    public void advanceBackEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.BACK, 1);
        c.advance();
        assertEquals(0, c.getPosition());
        c.advance();
        assertEquals(9, c.getPosition());
        c.advance();
        assertEquals(8, c.getPosition());
    }

    @Test
    public void backForthNoEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.FORTH, 5);
        c.back();
        assertEquals(4, c.getPosition());
        c.back();
        assertEquals(3, c.getPosition());
        c.back();
        assertEquals(2, c.getPosition());
    }

    @Test
    public void backBackNoEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.BACK, 5);
        c.back();
        assertEquals(6, c.getPosition());
        c.back();
        assertEquals(7, c.getPosition());
        c.back();
        assertEquals(8, c.getPosition());
    }

    @Test
    public void backForthEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.FORTH, 1);
        c.back();
        assertEquals(0, c.getPosition());
        c.back();
        assertEquals(9, c.getPosition());
        c.back();
        assertEquals(8, c.getPosition());
        c.advance();
        assertEquals(9, c.getPosition());
    }

    @Test
    public void backBackEdge() {
        CyclicCursor c = new CyclicCursor(10, CyclicCursor.DIR.BACK, 8);
        c.back();
        assertEquals(9, c.getPosition());
        c.back();
        assertEquals(0, c.getPosition());
        c.back();
        assertEquals(1, c.getPosition());
        c.advance();
        assertEquals(0, c.getPosition());
    }
}
