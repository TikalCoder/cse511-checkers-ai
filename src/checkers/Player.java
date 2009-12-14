package checkers;

import checkers.model.GameState;
import checkers.model.Move;

/**
 * An abstract base class for any kind of human or AI checkers player.
 *
 * @author Kurt Glastetter
 */
public abstract class Player implements Cloneable {
	/**
	 * Called when the checkers game engine requests this Player to make a
	 * move, from the given game state.
	 */
	public abstract Move chooseMove(GameState state);

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
