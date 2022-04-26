package snakeladder.game;

import java.util.ArrayList;
import java.util.List;

public class DieController {

    private NavigationPane np;
    private List<Die> dice;
    private int totalDie;

    public DieController(NavigationPane np) {
        this.np = np;
        this.dice = new ArrayList<>();
        this.totalDie = 0;
    }

    public void roll(int nb){
        int sizeOfDie = dice.size();
        Die die = new Die(nb, this, sizeOfDie+1);
        dice.add(die);
        totalDie += nb;
    }

    private boolean isFinished(int currentRoll){
        if (currentRoll == np.getNumberOfDice()){
            return true;
        } else {
            return false;
        }
    }

    public void finishedRoll(int index){
        if (isFinished(index)) {
            np.startMoving(totalDie);
            // reset list
            this.totalDie = 0;
            this.dice.clear();
        }
    }

    private List<Die> getAllDice(){
        return this.dice;
    }

    public Die LastDie(){
        dice = getAllDice();
        return dice.get(dice.size()-1);
    }
}
