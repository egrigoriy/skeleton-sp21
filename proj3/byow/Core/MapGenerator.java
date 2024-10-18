package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class MapGenerator {
    private final int width;
    private final int height;
    private final Map map;

    public MapGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        map = new Map(width, height);
    }

    public Map generate(long seed) {
        List<Figure> rooms = generateRandomRooms(seed);
        map.addFigures(rooms);
        return map;
    }

    private List<Figure> generateRandomRooms(long seed) {
        Random RANDOM = new Random(seed);
        List<Figure> rooms = new ArrayList<>();
        Room startingRoom = getRandomRoom(RANDOM);
        Queue<Room> queue = new LinkedList<>();
        rooms.add(startingRoom);
        queue.add(startingRoom);
        while (!queue.isEmpty()) {
            Room currentRoom = queue.poll();
            for (DIRECTION dir: DIRECTION.values()) {
//                System.out.println(dir);
//                currentRoom.print();
                Room nextRoom = getRandomRoom(RANDOM);
                currentRoom.makeNeighbor(nextRoom, dir);
                if (map.canContain(nextRoom) && !nextRoom.overlaps(rooms)) {
                    currentRoom.punchDoorTo(nextRoom, dir);
                    rooms.add(nextRoom);
                    queue.add(nextRoom);
                }
            }
        }
        return rooms;
    }

    private DIRECTION getRandomDirection(Random random) {
        int num = RandomUtils.uniform(random, 5);
        switch (num) {
            case 0: return DIRECTION.UP;
            case 1: return DIRECTION.DOWN;
            case 2: return DIRECTION.LEFT;
            case 3: return DIRECTION.RIGHT;
            default:
                return DIRECTION.RIGHT;
        }
    }

    private Room getRandomRoom(Random random) {
        int width = RandomUtils.uniform(random, 3, 9);
        int height = RandomUtils.uniform(random, 3, 9);
        int posnX = RandomUtils.uniform(random, width);
        int posnY = RandomUtils.uniform(random, height);
        int choice = RandomUtils.uniform(random, 3);
        Posn posn = new Posn(posnX, posnY);
        switch (choice) {
            case 0: {
                return new Room(width, 3,  posn);
            }
            case 1: {
                return new Room(3, height,  posn);
            }
            case 2: {
                return new Room(width, height, posn);
            }
            default:
                return new Room(width, 3,  posn);
        }
    }

    public static void main(String[] args) {
        MapGenerator mapGenerator = new MapGenerator(80, 30);
        Map map = mapGenerator.generate(123456789L);
        System.out.println("================== MAP ===================");
        System.out.println(map);
    }
}
