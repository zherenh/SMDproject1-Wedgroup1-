package snakeladder.game;

import ch.aplu.jgamegrid.Actor;

public class Die extends Actor
{

  private int nb;

  private DieController dieController;
  private int index;

  Die(int nb, DieController dieController, int index)
  {
    super("sprites/pips" + nb + ".gif", 7);
    this.nb = nb;

    this.dieController = dieController;
    this.index = index;
  }

  public void act()
  {
    showNextSprite();
    if (getIdVisible() == 6)
    {
      setActEnabled(false);

      // roll next die
      dieController.finishedRoll(index);
    }
  }

}
