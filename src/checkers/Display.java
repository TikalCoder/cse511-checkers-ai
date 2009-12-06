package checkers;

import checkers.model.GameState;
import checkers.model.Move;

public abstract class Display {
	public abstract void init(GameState newState);
	public abstract void update(Move move, GameState newState);
}
