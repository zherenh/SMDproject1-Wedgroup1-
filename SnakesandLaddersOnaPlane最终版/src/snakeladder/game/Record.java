package snakeladder.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Record {

    private NavigationPane np;

    String playerName;
    Map<Integer,Integer> rolledCounting = new HashMap<>();
    boolean needMapInitialise = true;
    int goUp, goDown;

    public Record(String playerName, NavigationPane np){
        this.np = np;
        this.playerName=playerName;
    }

    public void setRecordInitialise(Map<Integer,Integer> rolledCounting){
        if(needMapInitialise) {
            int numberOfDice = np.getNumberOfDice();
            int maxDiceValue = numberOfDice * 6;
            for (int i = numberOfDice; i <= maxDiceValue; i++) {
                rolledCounting.put(i, 0);
            }
            needMapInitialise = false;
        }
    }

    public void recordRolled(int nb){
        if(needMapInitialise)
            setRecordInitialise(rolledCounting);
        rolledCounting.put(nb,rolledCounting.get(nb)+1);
    }

    public void up(){
        goUp++;
    }
    public void down(){
        goDown++;
    }

    @Override
    public String toString(){
        setRecordInitialise(rolledCounting);
        String output = playerName+" Rolled: "+rolledCounting.entrySet().toString()+
                ", Traversed: up="+goUp+" down="+goDown;
        return output;
    }

}
