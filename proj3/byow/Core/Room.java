package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;

public class Room implements Figure {
    private final int width;
    private final int height;
    private Posn posn;
    private TETile[][] tiles;

    public Room(int width, int height, Posn posn) {
        this.width = width;
        this.height = height;
        this.posn = posn;
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

    public TETile[][] getTiles() {
        if (tiles != null) {
            return tiles;
        }

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
        tiles = newTiles;
        return tiles;
    }


    private boolean isFloor(int x, int y) {
        return (0 < x) && (x < width - 1) && (0 < y) && (y < height - 1);
    }

    public boolean overlaps(Figure other) {
        return !isOneUpper(other) && !isOneLefter(other);
    }

    public boolean isOneUpper(Figure other) {
        int thisTop = posn.getY() + height - 1;
        int thisBottom = posn.getY();
        int otherTop = other.getPosn().getY() + other.getHeight() - 1;
        int otherBottom = other.getPosn().getY();
        return thisBottom > otherTop || otherBottom > thisTop;
    }

    public boolean isOneLefter(Figure other) {
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

    @Override
    public boolean contains(Posn p) {
        int x = p.getX();
        int y = p.getY();
        int posnX = posn.getX();
        int posnY = posn.getY();
        return (posnX <= x && x <= posnX + width - 1) && (posnY <= y && y <= posnY + height - 1);
    }

    public void setPosn(Posn posn) {
        this.posn = posn;
    }

    public void makeNeighbor(Room nextRoom, DIRECTION dir) {
        Posn newPosn;
        int alignV = this.posn.getY() + (this.height - nextRoom.height) / 2;
        int alignH = this.posn.getX() + (this.width - nextRoom.width) / 2;
        switch (dir) {
            case RIGHT:
                newPosn = new Posn(this.posn.getX() + this.width, alignV);
                nextRoom.setPosn(newPosn);
                break;
            case LEFT:
                newPosn = new Posn(this.posn.getX() - nextRoom.getWidth(), alignV);
                nextRoom.setPosn(newPosn);
                break;
            case UP:
                newPosn = new Posn(alignH, this.posn.getY() + this.height);
                nextRoom.setPosn(newPosn);
                break;
            case DOWN:
                newPosn = new Posn(alignH, this.posn.getY() - nextRoom.getHeight());
                nextRoom.setPosn(newPosn);
                break;
        }
    }


    public void punchDoorTo(Room nextRoom, DIRECTION dir) {
        if (tiles == null) {
            getTiles();
        }
        punchDoor(dir);
        nextRoom.punchDoor(dir.getOpposite());
        int shift = (this.height - nextRoom.height) / 2;
//        switch (dir) {
//            case RIGHT: {
//                punchDoor(dir);
//                nextRoom.punchDoor(DIRECTION.LEFT);
//                break;
//            }
//        }
//        if (onRightSide(nextRoom)) {
//            tiles[width - 1][height/2] = Tileset.MOUNTAIN;
//            nextRoom.getTiles()[0][nextRoom.getHeight()/2] = Tileset.MOUNTAIN;
//        }
//        if (onLeftSide(nextRoom)) {
//            tiles[0][height/2] = Tileset.WATER;
//            nextRoom.getTiles()[nextRoom.getHeight() - 1][nextRoom.getHeight()/2] = Tileset.WATER;
//        }
    }

    private void punchDoor(DIRECTION dir) {
        int x = 0;
        int y = 0;
        switch (dir) {
            case RIGHT:
                x = width - 1;
                if (height % 2 == 0) {
                    y = height/2 -1;
                } else {
                    y = height/2;
                }
                break;
            case LEFT:
                x = 0;
                if (height % 2 == 0) {
                    y = height/2 -1;
                } else {
                    y = height/2;
                }
                break;

        }
        getTiles()[x][y] = Tileset.MOUNTAIN;
    }



    private boolean onRightSide(Room nextRoom) {
        return nextRoom.getPosn().getX() == this.posn.getX() + width;
    }
    private boolean onLeftSide(Room nextRoom) {
        return this.posn.getX() == nextRoom.getPosn().getX() + nextRoom.getWidth();
    }
}
