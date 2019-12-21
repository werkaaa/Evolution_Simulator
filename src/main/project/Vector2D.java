package project;

import java.util.ArrayList;
import java.util.List;

public class Vector2D {
    private final int x;
    private final int y;

    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String toString(){
        return "("+this.x+","+this.y+")";
    }

    public int hashCode(){
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    public boolean precedes(Vector2D other){
        return (this.x <= other.x) && (this.y <= other.y);
    }

    public boolean follows(Vector2D other){
        return (this.x >= other.x) && (this.y >= other.y);
    }

    public Vector2D upperRight(Vector2D other){
        return new Vector2D(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    public Vector2D lowerLeft(Vector2D other) {
        return new Vector2D(Math.min(this.x, other.x), Math.min(this.y, other.y));

    }

    public Vector2D add(Vector2D other){
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D subtract(Vector2D other){
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other){
        if (this == other)
            return true;
        if(!(other instanceof Vector2D))
            return false;
        Vector2D that = (Vector2D) other;
        return this.x == that.x && this.y == that.y;
    }

    public Vector2D opposite(){
        return new Vector2D(-1*this.x, -1*this.y);
    }

    public Vector2D fold(Vector2D more){
        return new Vector2D(Math.floorMod(this.x, more.x+1), Math.floorMod(this.y, more.y+1));
    }

    public List<Vector2D> allPointsBetween(Vector2D greater){
        List<Vector2D> positions = new ArrayList<>();
        for(int i = this.x; i<=greater.x; i++){
            for(int j = this.y; j<=greater.y; j++){
                positions.add(new Vector2D(i, j));
            }
        }
        return positions;
    }

}
