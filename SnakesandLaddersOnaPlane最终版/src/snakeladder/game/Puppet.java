package snakeladder.game;

import ch.aplu.jgamegrid.*;
import java.awt.Point;

public class Puppet extends Actor
{
  private GamePane gamePane;
  private NavigationPane navigationPane;
  private int cellIndex = 0;
  private int nbSteps;
  private Connection currentCon = null;
  private int y;
  private int dy;
  private boolean isAuto;
  private String puppetName;

  private boolean isLowestValue;

  private ToggleState toggleState = new ToggleState();

  private boolean isOverlap = false;

  public void setOverlap(boolean isOverlap){
    this.isOverlap = isOverlap;
  }

  public boolean getIsOverlap(){
    return this.isOverlap;
  }
  private Record record;
  public Record getRecord(){
    return this.record;
  }

  Puppet(GamePane gp, NavigationPane np, String puppetImage,String puppetName)
  {
    super(puppetImage);
    this.gamePane = gp;
    this.navigationPane = np;
    this.puppetName = puppetName;
    this.record =new Record(puppetName, np);

  }

  public boolean isAuto() {
    return isAuto;
  }

  public void setAuto(boolean auto) {
    isAuto = auto;
  }

  public String getPuppetName() {
    return puppetName;
  }

  public void setPuppetName(String puppetName) {
    this.puppetName = puppetName;
  }

  void go(int nbSteps)
  {
    if (cellIndex == 100)  // after game over
    {
      cellIndex = 0;
      setLocation(gamePane.startLocation);
    }
    this.nbSteps = nbSteps;
    if (nbSteps > 0){
      record.recordRolled(nbSteps);
    }

    // check is lowest step
    isLowestValue = nbSteps / navigationPane.getNumberOfDice() == 1;

    setActEnabled(true);
  }

  void resetToStartingPoint() {
    cellIndex = 0;
    setLocation(gamePane.startLocation);
    setActEnabled(true);
  }

  int getCellIndex() {
    return cellIndex;
  }

//  private void movePuppet()
//  {
//    int tens = cellIndex / 10;
//    int ones = cellIndex - tens * 10;
//    if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
//    {
//      if (ones == 0 && cellIndex > 0)
//        setLocation(new Location(getX(), getY() - 1));
//      else
//        setLocation(new Location(getX() + 1, getY()));
//    }
//    else     // Cells starting left 20, 40, .. 100
//    {
//      if (ones == 0)
//        setLocation(new Location(getX(), getY() - 1));
//      else
//        setLocation(new Location(getX() - 1, getY()));
//    }
//    cellIndex++;
//  }

  private void movePuppet(int nbSteps){
    if (nbSteps > 0){
      cellIndex ++;
    } else if (nbSteps < 0){
      cellIndex --;
    }
    setLocation(GamePane.cellToLocation(cellIndex));
  }

  public void act()
  {
    if ((cellIndex / 10) % 2 == 0)
    {
      if (isHorzMirror())
        setHorzMirror(false);
    }
    else
    {
      if (!isHorzMirror())
        setHorzMirror(true);
    }

    // Animation: Move on connection
    if (currentCon != null && !(isLowestValue && currentCon.cellStart - currentCon.cellEnd > 0))
    {
      int x = gamePane.x(y, currentCon);
      setPixelLocation(new Point(x, y));
      y += dy;

      // Check end of connection
      if ((dy > 0 && (y - gamePane.toPoint(currentCon.locEnd).y) > 0)
        || (dy < 0 && (y - gamePane.toPoint(currentCon.locEnd).y) < 0))
      {
        gamePane.setSimulationPeriod(100);
        setActEnabled(false);
        setLocation(currentCon.locEnd);
        cellIndex = currentCon.cellEnd;
        setLocationOffset(new Point(0, 0));
        currentCon = null;
        navigationPane.prepareRoll(cellIndex);
      }
      return;
    }

    // When the puppet go back, dont change 
    if (nbSteps < 0) {
      movePuppet(nbSteps);
      nbSteps++;
      if (nbSteps == 0)
      {
        // Check if on connection start
        if ((currentCon = gamePane.getConnectionAt(getLocation())) != null)
        {
          gamePane.setSimulationPeriod(50);
          y = gamePane.toPoint(currentCon.locStart).y;
          if (currentCon.locEnd.y > currentCon.locStart.y)
            dy = gamePane.animationStep;
          else
            dy = -gamePane.animationStep;
          navigationPane.prepareRoll(cellIndex);
          if (currentCon instanceof Snake)
          {
            navigationPane.showStatus("Digesting...");
            navigationPane.playSound(GGSound.MMM);
          }
          else
          {
            navigationPane.showStatus("Climbing...");
            navigationPane.playSound(GGSound.BOING);
          }
          if (currentCon.cellEnd > currentCon.cellStart){
            record.up();
          }else {
            record.down();
          }
        }
        else
        {
          setActEnabled(false);
//          isOverlap = false;
//          navigationPane.prepareRoll(cellIndex);
        }



      }
    }

    // Normal movement
    if (nbSteps > 0)
    {
      isOverlap = false;
      movePuppet(nbSteps);
      if (cellIndex == 100)  // Game over
      {
        setActEnabled(false);
        navigationPane.prepareRoll(cellIndex);
        return;
      }

      nbSteps--;
      if (nbSteps == 0)
      {
        // Check if on connection start
        if (gamePane.getConnectionAt(getLocation()) != null
                && !(isLowestValue && gamePane.getConnectionAt(getLocation()).cellStart
                - gamePane.getConnectionAt(getLocation()).cellEnd > 0))
        {
          currentCon = gamePane.getConnectionAt(getLocation());
          gamePane.setSimulationPeriod(50);
          y = gamePane.toPoint(currentCon.locStart).y;
          if (currentCon.locEnd.y > currentCon.locStart.y)
            dy = gamePane.animationStep;
          else
            dy = -gamePane.animationStep;
          if (currentCon instanceof Snake)
          {
            navigationPane.showStatus("Digesting...");
            navigationPane.playSound(GGSound.MMM);
          }
          else
          {
            navigationPane.showStatus("Climbing...");
            navigationPane.playSound(GGSound.BOING);
          }
          if (currentCon.cellEnd > currentCon.cellStart){
            record.up();
          }else {
            record.down();
          }
        }
        else
        {
          setActEnabled(false);
          navigationPane.prepareRoll(cellIndex);
        }

        // 需要测试，need more tests about whether the auto toggle algorithm is correct
        if(isAuto) {
          boolean toToggle = toggleState.onclickToggle(navigationPane, gamePane);
          if (toToggle) {
            navigationPane.onclickToggleButton(true);
          } else {
            navigationPane.onclickToggleButton(false);
          }
        }
      }
    }
  }

}
