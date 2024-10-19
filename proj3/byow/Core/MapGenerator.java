package byow.Core;

import java.util.*;

public class MapGenerator {
    private final Map map;

    public MapGenerator(int width, int height) {
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
            DIRECTION[] dirs = DIRECTION.values();
            RandomUtils.shuffle(RANDOM, dirs);
            for (DIRECTION dir: dirs) {
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

    private Room getRandomRoom(Random random) {
        int width = RandomUtils.uniform(random, 4, 9);
        int height = RandomUtils.uniform(random, 4, 9);
        int posnX = RandomUtils.uniform(random, 30);
        int posnY = RandomUtils.uniform(random, 30);
        int choice = RandomUtils.uniform(random, 5);
        Posn posn = new Posn(posnX, posnY);
        switch (choice) {
            case 0: {
                return new RoomH(width, posn);
            }
            case 1: {
                return new RoomV(height,  posn);
            }
            case 2: {
                return new Room(width, height, posn);
            }
            default:
                return new Room(width, height, posn);
        }
    }

    public static void main(String[] args) {
        MapGenerator mapGenerator = new MapGenerator(80, 30);
        Map map = mapGenerator.generate(123456789L);
        System.out.println("================== MAP ===================");
        System.out.println(map);
    }
}