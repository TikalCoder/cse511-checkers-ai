package checkers;

import checkers.model.GameState;
import checkers.model.Move;

public abstract class Player {
	public abstract Move chooseMove(GameState state);
}
