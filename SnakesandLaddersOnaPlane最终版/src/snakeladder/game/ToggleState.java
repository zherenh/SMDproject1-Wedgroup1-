package snakeladder.game;

import java.util.List;

public class ToggleState implements Toggle{

    @Override
    public boolean onclickToggle(NavigationPane np, GamePane gp){

        int numberOfDice = np.getNumberOfDice();
        int maxSteps = numberOfDice * 6;
        boolean isToggleOpen = false;

        List<Connection> connections = gp.getConnections();

        int nextPlayerIndex = gp.getCurrentPuppetIndex() == 1 ? 0 : 1;
        Puppet nextPlayer = gp.getAllPuppets().get(nextPlayerIndex);

//        if (gp.getCurrentPuppetIndex() == 0) {
//            nextPlayer = playerList.get(1);
//        } else {
//            nextPlayer = playerList.get(0);
//        }
//
        int nextPlayerCell = nextPlayer.getCellIndex();
        int maxRangeOfMovement = nextPlayerCell + maxSteps;
        int minRangeOfMovement = nextPlayerCell + numberOfDice;

        int totalUpStep = 0;
        int totalDownStep = 0;

        for (Connection connection : connections) {
            if (connection.cellStart < connection.cellEnd) {
                if (connection.cellStart >= nextPlayerCell && connection.cellStart <= maxRangeOfMovement
                        && connection.cellStart >= minRangeOfMovement) {
                    totalUpStep += (connection.cellEnd - connection.cellStart);
                }
            } else {
                if (connection.cellStart >= nextPlayerCell && connection.cellStart <= maxRangeOfMovement
                        && connection.cellStart >= minRangeOfMovement) {
                    totalDownStep += (connection.cellStart - connection.cellEnd);
                }
            }
        }
        isToggleOpen = totalUpStep > totalDownStep;
        return isToggleOpen;

    }
}
