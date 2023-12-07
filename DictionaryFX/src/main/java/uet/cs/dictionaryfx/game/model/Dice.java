package uet.cs.dictionaryfx.game.model;

import java.util.Random;

public class Dice {
    //Data Fields
    private int value;
    private Random random;

    //Constructor
    public Dice() {
        this.value = 1;
        random = new Random();
    }

    //Getter
    public int getValue() {
        return value;
    }

    //Setter
    public void setValue(int value) {
        this.value = value;
    }

    //Method
    public void roll(int limit) {
        value = 1 + random.nextInt(limit);
    }

    public static void main(String[] args) {
        Dice d = new Dice();
        d.roll(6);
    }
}

