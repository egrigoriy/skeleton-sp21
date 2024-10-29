package byow.Core;

import byow.TileEngine.TETile;

public class World {
    private final Map map;
    private final int width;
    private final int height;

    private final Avatar avatar;

    public World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.map = new MapGenerator(width, height).generate(seed);
        avatar = new Avatar(new Posn(0, 0));
        this.map.placeAtRandomPosn(avatar);
    }


    public TETile[][] getState() {
        return map.getContent();
    }

    @Override
    public String toString() {
        return map.toString();
    }

    public void moveUp() {
        map.moveFigure(avatar, DIRECTION.UP);
    }

    public void moveLeft() {
        map.moveFigure(avatar, DIRECTION.LEFT);
    }

    public void moveRight() {
        map.moveFigure(avatar, DIRECTION.RIGHT);
    }

    public void moveDown() {
        map.moveFigure(avatar, DIRECTION.DOWN);
    }

    public void toggleFocus() {
        map.toggleFocus(avatar);
    }
}
