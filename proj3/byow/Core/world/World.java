package byow.Core.world;

import byow.Core.world.figures.Hero;
import byow.Core.world.figures.Posn;
import byow.TileEngine.TETile;

/**
 * Represents a world
 */
public class World {
    private final WorldMap worldMap;
    private final Hero hero;

    public World(int width, int height, long seed) {
        this.worldMap = new WorldMapGenerator(width, height).generate(seed);
        hero = new Hero(new Posn(0, 0));
        this.worldMap.placeAtRandomPosn(hero);
    }

    /**
     * Returns the content of the world
     * @return array of tiles
     */
    public TETile[][] getContent() {
        return worldMap.getContent();
    }

    @Override
    public String toString() {
        return worldMap.toString();
    }

    /**
     * Moves the hero on the map up
     */
    public void moveUp() {
        worldMap.moveFigure(hero, DIRECTION.UP);
    }

    /**
     * Moves the hero on the map left
     */
    public void moveLeft() {
        worldMap.moveFigure(hero, DIRECTION.LEFT);
    }

    /**
     * Moves the hero on the map right
     */
    public void moveRight() {
        worldMap.moveFigure(hero, DIRECTION.RIGHT);
    }

    /**
     * Moves the hero on the map down
     */
    public void moveDown() {
        worldMap.moveFigure(hero, DIRECTION.DOWN);
    }

    /**
     * Toggles the focus around the hero
     */
    public void toggleFocus() {
        worldMap.toggleFocus(hero);
    }

    /**
     * Returns the description of the tile placed at given x and y
     * @param x
     * @param y
     * @return tile description
     */
    public String getTileDescription(double x, double y) {
        return worldMap.getTileDescription(x, y);
    }
}
