package byow.Core.world;

import byow.Core.world.figures.*;

import java.util.*;

/**
 * Represents a map generator
 */
public class WorldMapGenerator {
    private final WorldMap worldMap;

    public WorldMapGenerator(int width, int height) {
        worldMap = new WorldMap(width, height);
    }

    /**
     * Returns a randomly generated world map from given seed
     * @param seed
     * @return world map
     */
    public WorldMap generate(long seed) {
        List<Figure> rooms = generateRandomRooms(seed);
        worldMap.addFigures(rooms);
        return worldMap;
    }

    /**
     * Returns a list of randomly generated rooms
     * @param seed
     * @return a list of rooms
     */
    private List<Figure> generateRandomRooms(long seed) {
        Random randomProvider = new Random(seed);
        List<Figure> rooms = new ArrayList<>();
        Room startingRoom = getRandomRoom(randomProvider);
        Queue<Room> queue = new LinkedList<>();
        rooms.add(startingRoom);
        queue.add(startingRoom);
        while (!queue.isEmpty()) {
            Room currentRoom = queue.poll();
            DIRECTION[] dirs = DIRECTION.values();
            RandomUtils.shuffle(randomProvider, dirs);
            for (DIRECTION dir: dirs) {
                Room nextRoom = getRandomRoom(randomProvider);
                currentRoom.makeNeighbor(nextRoom, dir);
                if (worldMap.canContain(nextRoom) && !nextRoom.overlaps(rooms)) {
                    currentRoom.punchDoorTo(nextRoom, dir);
                    rooms.add(nextRoom);
                    queue.add(nextRoom);
                }
            }
        }
        return rooms;
    }

    /**
     * Returns a room with random width, height and position or
     * horizontal hallway with random width and position or
     * vertical hallway with random height and position
     * @param random
     * @return
     */
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
        WorldMapGenerator mapGenerator = new WorldMapGenerator(80, 30);
        WorldMap worldMap = mapGenerator.generate(123456789L);
        System.out.println("================== MAP ===================");
        System.out.println(worldMap);
    }
}
