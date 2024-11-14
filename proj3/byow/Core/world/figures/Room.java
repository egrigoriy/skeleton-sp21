package byow.Core.world.figures;

import byow.Core.world.DIRECTION;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;

/**
 * Represents a room at position (tile 0, 0) with width and height
 */
public class Room implements Figure {
    private final int width;
    private final int height;
    private Posn posn;
    private final TETile[][] tiles;

    public Room(int width, int height, Posn posn) {
        this.width = width;
        this.height = height;
        this.posn = posn;
        this.tiles = fillTiles();
    }

    /**
     * Returns the width of this room
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of this room
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns this room position
     * @return position
     */
    public Posn getPosn() {
        return posn;
    }

    /**
     * Returns an array of floor tiles surrounded by wall tiles
     * @return array of tiles
     */
    private TETile[][] fillTiles() {
        TETile[][] newTiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isFloor(x, y)) {
                    newTiles[x][y] = Tileset.FLOOR;
                } else {
                    newTiles[x][y] = Tileset.WALL;
                }
            }
        }
        return newTiles;
    }

    /**
     * Returns this room's arrays of tiles
     * @return array of tiles
     */
    @Override
    public TETile[][] getTiles() {
        return tiles;
    }

    /**
     * Sets the floor tile at this room (x, y)
     * @param x
     * @param y
     */
    private void setTile(int x, int y) {
        tiles[x][y] = Tileset.FLOOR;
    }

    /**
     * Returns true if given internal position is floor, otherwise false
     * @param x
     * @param y
     * @return boolean
     */
    private boolean isFloor(int x, int y) {
        return (0 < x) && (x < width - 1) && (0 < y) && (y < height - 1);
    }

    /**
     * Returns true if given room overlaps with this one
     * @param other
     * @return boolean
     */
    private boolean overlaps(Room other) {
        return !isOneUpper(other) && !isOneLefter(other);
    }

    /**
     * Returns true if one of this or given room is upper, otherwise false
     * @param other
     * @return boolean
     */
    private boolean isOneUpper(Room other) {
        int thisTop = posn.getY() + height - 1;
        int thisBottom = posn.getY();
        int otherTop = other.getPosn().getY() + other.getHeight() - 1;
        int otherBottom = other.getPosn().getY();
        return thisBottom > otherTop || otherBottom > thisTop;
    }

    /**
     * Returns true if one of this or given room is lefter, otherwise false
     * @param other
     * @return boolean
     */
    private boolean isOneLefter(Figure other) {
        int thisLeft = posn.getX();
        int thisRight = posn.getX() + width - 1;
        int otherLeft = other.getPosn().getX();
        int otherRight = other.getPosn().getX() + other.getWidth() - 1;
        return thisRight < otherLeft || otherRight < thisLeft;
    }


    /**
     * Returns true if this room overlaps with given figures, otherwise false
     * @param figures
     * @return boolean
     */
    public boolean overlaps(List<Figure> figures) {
        for (Figure figure : figures) {
            if (overlaps((Room) figure)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the position of this room
     * @param posn
     */
    @Override
    public void setPosn(Posn posn) {
        this.posn = posn;
    }

    /**
     * Makes the given room neighbor to this one in given direction
     * @param nextRoom
     * @param dir
     */
    public void makeNeighbor(Room nextRoom, DIRECTION dir) {
        Posn newNextPosn = getAlignedNextPosn(nextRoom, dir);
        nextRoom.setPosn(newNextPosn);
    }

    /**
     * Returns the position of given next room aligned to this room in given direction
     * @param nextRoom
     * @param dir
     * @return position
     */
    private Posn getAlignedNextPosn(Room nextRoom, DIRECTION dir) {
        Posn newNextPosn = null;
        int shiftV = (this.height - nextRoom.getHeight()) / 2;
        int shiftH = (this.width - nextRoom.getWidth()) / 2;
        switch (dir) {
            case RIGHT:
            default:
                newNextPosn = this.posn.translate(this.width, shiftV);
                break;
            case LEFT:
                newNextPosn = this.posn.translate(-nextRoom.getWidth(), shiftV);
                break;
            case UP:
                newNextPosn = this.posn.translate(shiftH, this.height);
                break;
            case DOWN:
                newNextPosn = this.posn.translate(shiftH, -nextRoom.getHeight());
                break;
        }
        return newNextPosn;
    }

    /**
     * Punches a door from this room to given one aligned in given direction
     * @param nextRoom
     * @param dir
     */
    public void punchDoorTo(Room nextRoom, DIRECTION dir) {
        int shiftH = (this.height - nextRoom.height);
        int shiftW = (this.width - nextRoom.width);
        switch (dir) {
            case RIGHT:
            default: {
                if (shiftH % 2 == 0 || height < nextRoom.getHeight()) {
                    this.setTile(width - 1, height / 2);
                    nextRoom.setTile(0, height / 2 - shiftH / 2);
                } else if (height > nextRoom.getHeight()) {
                    this.setTile(width - 1, height / 2 - 1);
                    nextRoom.setTile(0, height / 2 - shiftH / 2 - 1);
                }
                break;
            }
            case LEFT: {
                if (shiftH % 2 == 0 || height < nextRoom.getHeight()) {
                    this.setTile(0, height / 2);
                    nextRoom.setTile(nextRoom.getWidth() - 1, height / 2 - shiftH / 2);
                } else if (height > nextRoom.getHeight()) {
                    this.setTile(0, height / 2 - 1);
                    nextRoom.setTile(nextRoom.getWidth() - 1, height / 2 - shiftH / 2 - 1);
                }
                break;
            }
            case UP: {
                if (shiftW % 2 == 0 || width < nextRoom.getWidth()) {
                    this.setTile(width / 2, height - 1);
                    nextRoom.setTile(width / 2 - shiftW / 2, 0);
                } else if (width > nextRoom.getWidth()) {
                    this.setTile(width / 2 - 1, height - 1);
                    nextRoom.setTile(width / 2 - shiftW / 2 - 1, 0);
                }
                break;
            }
            case DOWN: {
                if (shiftW % 2 == 0 || width < nextRoom.getWidth()) {
                    this.setTile(width / 2, 0);
                    nextRoom.setTile(width / 2 - shiftW / 2, nextRoom.getHeight() - 1);
                } else if (width > nextRoom.getWidth()) {
                    this.setTile(width / 2 - 1, 0);
                    nextRoom.setTile(width / 2 - shiftW / 2 - 1, nextRoom.getHeight() - 1);
                }
                break;
            }
        }
    }
}

