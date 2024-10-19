package byow.Core.tests;

import byow.Core.Figure;
import byow.Core.Posn;
import byow.Core.Room;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RoomTest {

    @Test
    public void shouldSetPosn() {
        Posn posn0 = new Posn(12, 23);
        Room room = new Room(10, 20, posn0);
        Posn posn1 = new Posn(12, 23);
        assertEquals(posn1, room.getPosn());
        posn0 = new Posn(56, 78);
        posn1 = new Posn(56, 78);
        room.setPosn(posn0);
        assertEquals(posn1, room.getPosn());
    }

    @Test
    public void overlapsTrueLeftBottomCornerInside() {
        Room room = new Room(5, 4, new Posn(10, 10));
        Room otherRoom = new Room(4, 5, new Posn(13, 12));
        assertTrue(room.overlaps(otherRoom));
    }

    @Test
    public void overlapsTrueLeftTopCornerInside() {
        Room room = new Room(5, 4, new Posn(10, 10));
        Room otherRoom = new Room(4, 5, new Posn(13, 8));
        assertTrue(room.overlaps(otherRoom));
    }

    @Test
    public void overlapsTrueRightBottomCornerInside() {
        Room room = new Room(5, 4, new Posn(10, 10));
        Room otherRoom = new Room(4, 5, new Posn(8, 12));
        assertTrue(room.overlaps(otherRoom));
    }

    @Test
    public void overlapsTrueRightTopCornerInside() {
        Room room = new Room(5, 4, new Posn(10, 10));
        Room otherRoom = new Room(4, 5, new Posn(8, 8));
        assertTrue(room.overlaps(otherRoom));
    }

    @Test
    public void overlapsFalseWhenOutside() {
        Room room = new Room(5, 4, new Posn(10, 10));
        Room otherRoom = new Room(4, 5, new Posn(15, 16));
        assertFalse(room.overlaps(otherRoom));
    }

    @Test
    public void overlapsRoomsTrue() {
        Room room = new Room(5, 4, new Posn(10, 10));
        Room notOver = new Room(4, 5, new Posn(15, 16));
        Room over1 = new Room(4, 5, new Posn(8, 8));
        Room over2 = new Room(4, 5, new Posn(13, 8));
        List<Figure> rooms = new ArrayList<>();
        rooms.add(over1);
        rooms.add(notOver);
        rooms.add(over2);
        assertTrue(room.overlaps(rooms));
    }

    @Test
    public void overlapsRoomsFalse() {
        Room room = new Room(5, 4, new Posn(10, 10));
        Room notOver1 = new Room(4, 5, new Posn(15, 16));
        Room notOver2 = new Room(4, 5, new Posn(0, 0));
        List<Figure> rooms = new ArrayList<>();
        rooms.add(notOver1);
        rooms.add(notOver2);
        assertFalse(room.overlaps(rooms));
    }
}
