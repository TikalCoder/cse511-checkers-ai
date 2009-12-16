package checkers;

import java.util.ArrayList;
import java.util.List;

import checkers.ai.AIPlayer;
import checkers.ai.NegamaxOrderingPlayer;
import checkers.ai.NegamaxPlayer;
import checkers.ai.RandomPlayer;
import checkers.model.GameState;

public class AIStats {

	/**
	 * This class is essentially a script to benchmark the various AIPlayers.
	 * They are set up against a RandomPlayer, and the number of searches and 
	 * node evaluations they do is printed.
	 * 
	 * @param args Command line args. Currently none supported.
	 */
	public static void main(String[] args) {
		List<AIPlayer> aiPlayers = new ArrayList<AIPlayer>();
		aiPlayers.add(new NegamaxOrderingPlayer(6, 2));
		
		int steps = 0;
		int evals = 0;
		int evalsMax = 0;
		int searches = 0;
		int searchesMax = 0;

		int trials = 25;

		for (AIPlayer aip : aiPlayers) {
			for (int i = 0; i < trials; i++) {
				Game game = new Game(new RandomPlayer(), aip, new GameState());
				
				System.out.println("Playing game " + (i + 1) + " of " + trials);

				while (!game.isOver()) {
					game.makeMove(game.getPlayerToMove().chooseMove((GameState)game.getState().clone()));
					Player p = game.getPlayerToMove();
					if (p == aip) {
						steps++;

						evals += aip.getEvals();
						if (aip.getEvals() > evalsMax) {
							evalsMax = aip.getEvals();
						}

						searches += aip.getSearches();
						if (aip.getSearches() > searchesMax) {
							searchesMax = aip.getSearches();
						}
					}
				}
			}

			System.out.println("For player: " + aip);
			System.out.println("Average searches: " + searches / (double)steps);
			System.out.println("Average evals: " + evals / (double)steps);
			System.out.println("Maximum searches: " + searchesMax);
			System.out.println("Maximum evals: " + evalsMax);
			System.out.println();
		}

	}

}
