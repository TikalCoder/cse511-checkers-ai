package checkers.ai;

import checkers.Display;
import checkers.model.GameState;
import checkers.model.Move;

/**
 * Displays statistics for a given {@link AIPlayer} in the number of
 * searches and evaluations.
 *
 * @author Scott Bressler
 */
public class AIStatsDisplay extends Display {
	AIPlayer aiPlayer;
	private int sumSearches;
	private int sumEvals;

	public AIStatsDisplay(AIPlayer aip) {
		aiPlayer = aip;
	}

	@Override
	public void init(GameState newState) {
		// do nothing
	}

	@Override
	public void update(Move move, GameState newState) {
		sumSearches += aiPlayer.getSearches();
		sumEvals += aiPlayer.getEvals();

		System.out.println("---------------------------------------");
		System.out.println("AI Stats for AIPlayer Type: " + aiPlayer);
		System.out.println("Searches\tEvals");
		System.out.println(aiPlayer.getSearches() + "\t" + aiPlayer.getEvals());
		System.out.println("Sums:");
		System.out.println(sumSearches + "\t" + sumEvals);
	}

}
