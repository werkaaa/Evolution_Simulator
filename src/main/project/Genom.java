package project;

import java.util.*;

public class Genom {
    private Random random;
    public List<Integer> genom;
    private List<Integer> stats;
    private boolean empty = false;

    public Genom(boolean empty){
        this.empty = true;
        this.random = new Random();
        this.genom = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        this.stats = new ArrayList<>(Arrays.asList(32, 0, 0, 0, 0, 0, 0, 0));
    }

    public Genom(){
        this.random = new Random();
        this.genom = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        this.stats = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1));
        for(int i = 8; i<32; i++) {
            int n = random.nextInt(8);
            this.genom.add(n); //random.nextInt(n) from [0, n)
            this.stats.set(n, stats.get(n)+1);
        }
        Collections.shuffle(this.genom);
    }

    public Genom(Animal mother, Animal father){
        this.random = new Random();
        List<Animal> parents = new ArrayList<>(Arrays.asList(mother, father));

        int s1 = this.random.nextInt(30)+1;
        int s2 = this.random.nextInt(31-s1)+s1+1;
        List<Vector2D> segments= new ArrayList<>(Arrays.asList(new Vector2D(0, s1), new Vector2D(s1, s2), new Vector2D(s2, 32)));

        int parentIndex = this.random.nextInt(2);
        int segmentIndex = this.random.nextInt(3);
        this.genom = new ArrayList<>(parents.get(parentIndex).getGenom().genom);
        parentIndex = (parentIndex+1)%2;
        for(int i = segments.get(segmentIndex).x; i<segments.get(segmentIndex).y; i++) this.genom.set(i, parents.get(parentIndex).getGenom().genom.get(i));

        this.stats = new ArrayList<>();
        for(int i = 0; i<8; i++) this.stats.add(Collections.frequency(this.genom, i));

        repairGenom();
    }

    public String toString(){
        if(this.empty)
            return "-";
        String representation = "";
        for(int i = 0; i<8; i++){
            for(int j = 0; j< this.stats.get(i); j++){
                representation+=i;
            }
        }
        return representation;
    }

    public int hashCode(){
        int hash = 0;
        for(int i = 0; i<8; i++){
            hash = hash*32+this.stats.get(i);
        }
        return hash;
    }

    public void repairGenom(){
        List<Integer> presentInGenom = new ArrayList<>();
        for(int i = 0; i<8; i++){
            if(this.stats.get(i)>=2){
                presentInGenom.add(i);
            }
        }
        for(int i = 0; i<8; i++){
            if(this.stats.get(i)==0){
                int valueToChange = presentInGenom.get(this.random.nextInt(presentInGenom.size())); //random gen that can be changed
                this.genom.set(this.genom.indexOf(valueToChange), i);
                this.stats.set(i, 1);
                this.stats.set(valueToChange, this.stats.get(valueToChange)-1);
                if(this.stats.get(valueToChange)<=1){
                    presentInGenom.remove((Object) valueToChange);
                }
            }
        }
    }
}
