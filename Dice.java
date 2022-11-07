import java.util.Random;

public class Dice{

    public static int roll() {
        Random rand = new Random();
        int randNum;
        randNum = rand.nextInt(6) + 1;
        return randNum;
    }
}