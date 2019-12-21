package project;

import java.util.*;

public class Genome {
    private Random random;
    private List<Integer> genome;
    private List<Integer> stats;
    private boolean empty = false;


    public Genome(String basic){
        this.genome = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7));
        this.stats = new ArrayList<>(Arrays.asList(4, 4, 4, 4, 4, 4, 4, 4));
    }

    public Genome(boolean empty){
        this.empty = true;
        this.random = new Random();
        this.genome = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        this.stats = new ArrayList<>(Arrays.asList(32, 0, 0, 0, 0, 0, 0, 0));
    }

    public Genome(){
        this.random = new Random();
        this.genome = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        this.stats = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1));
        for(int i = 8; i<32; i++) {
            int n = random.nextInt(8);
            this.genome.add(n); //random.nextInt(n) from [0, n)
            this.stats.set(n, stats.get(n)+1);
        }
        //Collections.shuffle(this.genom); //old version with randomly shuffled genomes
        Collections.sort(this.genome);
    }

    public Genome(Animal mother, Animal father){
        this.random = new Random();
        List<Animal> parents = new ArrayList<>(Arrays.asList(mother, father));

        int s1 = this.random.nextInt(30)+1;
        int s2 = this.random.nextInt(31-s1)+s1+1;
        List<Vector2D> segments= new ArrayList<>(Arrays.asList(new Vector2D(0, s1), new Vector2D(s1, s2), new Vector2D(s2, 32)));

        int parentIndex = this.random.nextInt(2);
        int segmentIndex = this.random.nextInt(3);
        this.genome = new ArrayList<>(parents.get(parentIndex).getGenome().genome);
        parentIndex = (parentIndex+1)%2;
        for(int i = segments.get(segmentIndex).getX(); i<segments.get(segmentIndex).getY(); i++) this.genome.set(i, parents.get(parentIndex).getGenome().genome.get(i));

        this.stats = new ArrayList<>();
        for(int i = 0; i<8; i++) this.stats.add(Collections.frequency(this.genome, i));

        repairGenome();
        Collections.sort(this.genome);
    }

    public List<Integer> getGenome(){
        return this.genome;
    }

    public boolean isEmpty(){
        return this.empty;
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

    public boolean equals(Object other){
        for(int i = 0; i<8; i++){
            if(this.stats.get(i)!=((Genome)other).stats.get(i))
                return false;
        }
        return true;
    }

    public void repairGenome(){
        List<Integer> presentInGenom = new ArrayList<>();
        for(int i = 0; i<8; i++){
            if(this.stats.get(i)>=2){
                presentInGenom.add(i);
            }
        }
        for(int i = 0; i<8; i++){
            if(this.stats.get(i)==0){
                int valueToChange = presentInGenom.get(this.random.nextInt(presentInGenom.size())); //random gen that can be changed
                this.genome.set(this.genome.indexOf(valueToChange), i);
                this.stats.set(i, 1);
                this.stats.set(valueToChange, this.stats.get(valueToChange)-1);
                if(this.stats.get(valueToChange)<=1){
                    presentInGenom.remove((Object) valueToChange);
                }
            }
        }
    }
}
