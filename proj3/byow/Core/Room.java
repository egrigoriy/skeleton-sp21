package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;

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
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Posn getPosn() {
        return posn;
    }

    public TETile[][] fillTiles() {
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

    @Override
    public TETile[][] getTiles() {
        return tiles;
    }

    @Override
    public void setTile(int x, int y) {
        tiles[x][y] = Tileset.FLOOR;
    }

    private boolean isFloor(int x, int y) {
        return (0 < x) && (x < width - 1) && (0 < y) && (y < height - 1);
    }

    public boolean overlaps(Figure other) {
        return !isOneUpper(other) && !isOneLefter(other);
    }

    private boolean isOneUpper(Figure other) {
        int thisTop = posn.getY() + height - 1;
        int thisBottom = posn.getY();
        int otherTop = other.getPosn().getY() + other.getHeight() - 1;
        int otherBottom = other.getPosn().getY();
        return thisBottom > otherTop || otherBottom > thisTop;
    }

    private boolean isOneLefter(Figure other) {
        int thisLeft = posn.getX();
        int thisRight = posn.getX() + width - 1;
        int otherLeft = other.getPosn().getX();
        int otherRight = other.getPosn().getX() + other.getWidth() - 1;
        return thisRight < otherLeft || otherRight < thisLeft;
    }


    public boolean overlaps(List<Figure> figures) {
        for (Figure figure : figures) {
            if (overlaps(figure)) {
                return true;
            }
        }
        return false;
    }

    public void setPosn(Posn posn) {
        this.posn = posn;
    }

    public void makeNeighbor(Room nextRoom, DIRECTION dir) {
        Posn newNextPosn = getAlignedNextPosn(nextRoom, dir);
        nextRoom.setPosn(newNextPosn);
    }

    private Posn getAlignedNextPosn(Room nextRoom, DIRECTION dir) {
        Posn newNextPosn = null;
        int shiftV = (this.height - nextRoom.getHeight()) / 2;
        int shiftH = (this.width - nextRoom.getWidth()) / 2;
        switch (dir) {
            case RIGHT:
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

    public void punchDoorTo(Room nextRoom, DIRECTION dir) {
        int shiftH = (this.height - nextRoom.height);
        int shiftW = (this.width - nextRoom.width);
        switch (dir) {
            case RIGHT: {
                if (shiftH % 2 == 0 || height < nextRoom.getHeight()) {
                    this.setTile(width - 1, height / 2);
                    nextRoom.setTile(0, height / 2 - shiftH / 2);
                } else if (height > nextRoom.getHeight()) {
                    this.setTile(width - 1, height / 2 - 1);
                    nextRoom.setTile(0, height/2 - shiftH / 2 - 1);
                }
                break;
            }
            case LEFT: {
                if (shiftH % 2 == 0 || height < nextRoom.getHeight()) {
                    this.setTile(0,height / 2);
                    nextRoom.setTile(nextRoom.getWidth() - 1,height / 2 - shiftH / 2);
               } else if (height > nextRoom.getHeight()) {
                    this.setTile(0, height / 2 - 1);
                    nextRoom.setTile(nextRoom.getWidth() - 1, height / 2 - shiftH / 2 - 1);
                }
                break;
            }
            case UP: {
                if (shiftW % 2 == 0 || width < nextRoom.getWidth()) {
                    this.setTile(width / 2, height - 1);
                    nextRoom.setTile(width/2 - shiftW/2, 0);
                } else if (width > nextRoom.getWidth()) {
                    this.setTile(width / 2 - 1, height - 1);
                    nextRoom.setTile(width / 2 - shiftW/2 - 1, 0);
                }
                break;
            }
            case DOWN: {
                if (shiftW % 2 == 0 || width < nextRoom.getWidth()) {
                    this.setTile(width/2, 0);
                    nextRoom.setTile(width / 2 - shiftW / 2, nextRoom.getHeight() - 1);
                } else if (width > nextRoom.getWidth()) {
                    this.setTile(width / 2 - 1, 0);
                    nextRoom.setTile(width / 2 - shiftW / 2 - 1, nextRoom.getHeight() -1);
                }
                break;
            }
        }
    }
}

