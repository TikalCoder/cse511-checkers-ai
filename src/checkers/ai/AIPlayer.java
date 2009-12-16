package checkers.ai;

import checkers.Player;

/**
 * Abstract base class representing a Player that is controlled by the computer.
 * 
 * @author Scott Bressler
 */
public abstract class AIPlayer extends Player {
	protected int searches;
	protected int evals;

	public int getSearches() {
		return searches;
	}

	public int getEvals() {
		return evals;
	}
	
}
