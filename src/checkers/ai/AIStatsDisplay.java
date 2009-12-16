package checkers.ai;

import checkers.Display;
import checkers.model.GameState;
import checkers.model.Move;

public class AIStatsDisplay extends Display {
	AIPlayer aiPlayer;
	
	public AIStatsDisplay(AIPlayer aip) {
		aiPlayer = aip;
	}

	@Override
	public void init(GameState newState) {
		// do nothing
	}

	@Override
	public void update(Move move, GameState newState) {
		System.out.println("Searches\tEvals");
		System.out.println(aiPlayer.getSearches() + "\t" + aiPlayer.getEvals());
	}

}
