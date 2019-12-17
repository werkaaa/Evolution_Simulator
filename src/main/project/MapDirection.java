package project;

import java.util.Arrays;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public String toString() {
        switch (this) {
            case NORTH:
                return "North";
            case NORTHEAST:
                return "North-East";
            case EAST:
                return "East";
            case SOUTHEAST:
                return "South-East";
            case SOUTH:
                return "South";
            case SOUTHWEST:
                return "South-West";
            case WEST:
                return "West";
            case NORTHWEST:
                return "North-West";
            default:
                return "Wrong direction";
        }
    }

    public MapDirection next(){
        switch (this) {
            case NORTH:
                return NORTHEAST;
            case NORTHEAST:
                return EAST;
            case EAST:
                return SOUTHEAST;
            case SOUTHEAST:
                return SOUTH;
            case SOUTH:
                return SOUTHWEST;
            case SOUTHWEST:
                return WEST;
            case WEST:
                return NORTHWEST;
            case NORTHWEST:
                return NORTH;
            default:
                return null;
        }
    }

    public MapDirection previous(){
        switch (this) {
            case NORTH:
                return NORTHWEST;
            case NORTHWEST:
                return WEST;
            case WEST:
                return SOUTHWEST;
            case SOUTHWEST:
                return SOUTH;
            case SOUTH:
                return SOUTHEAST;
            case SOUTHEAST:
                return EAST;
            case EAST:
                return NORTHEAST;
            case NORTHEAST:
                return NORTH;
            default:
                return null;
        }
    }

    public Vector2D toPseudoUnitVector(){
        switch (this) {
            case NORTH:
                return new Vector2D(0,1);
            case NORTHEAST:
                return new Vector2D(1, 1);
            case EAST:
                return new Vector2D(1,0);
            case SOUTHEAST:
                return new Vector2D(1, -1);
            case SOUTH:
                return new Vector2D(0,-1);
            case SOUTHWEST:
                return new Vector2D(-1, -1);
            case WEST:
                return new Vector2D(-1,0);
            case NORTHWEST:
                return new Vector2D(-1, 1);

            default:
                return null;
        }
    }

    public static MapDirection intToDirection(int i){
        return MapDirection.values()[i];
    }
// TODO: sprawdzić czy metoda statyczna niczego nie zepsuje
    public static int directionToInt(MapDirection direction){
        return Arrays.asList(MapDirection.values()).indexOf(direction);
    }
}