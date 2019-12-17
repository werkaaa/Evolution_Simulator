package project;

import java.util.Random;

public class Plant implements IMapElement{
    private Vector2D position;
    private Map map;
    // TODO: czy nie lepiej żeby była klasa abstrakcyjna zamiast interfaceu

    public Plant(Map map){
        this.map = map;
        this.position = this.map.getFreePosition();
    }

    public Plant(Vector2D position, Map map){
        this.map = map;
        this.position = position;
    }


    @Override
    public Vector2D getPosition(){
        return this.position;
    }

    @Override
    public String toString(){
        return "p ";
    }
}
