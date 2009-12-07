package checkers;

import checkers.model.GameState;
import checkers.model.Move;

/**
 * An abstract base class for any kind of checkers Display.
 * <p>
 * For example, this could be a simple text output display of the game state,
 * or it could be used to send an update event to a GUI.  It could probably be
 * used to create a game "logging" display, that would save every move for
 * later analysis or output.
 *
 * @author Kurt Glastetter
 */
public abstract class Display {
	/**
	 * Called to initialize the state to a new game state (for example, the
	 * initial state).
	 */
	public abstract void init(GameState newState);

	/**
	 * Called everytime a move is made, by any player, to allow this Display to
	 * update or repaint.
	 * <p>
	 * If necessary, the old state (from before the move) could be added as a
	 * parameter.
	 */
	public abstract void update(Move move, GameState newState);
}
