package checkers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import checkers.ai.AIPlayer;
import checkers.ai.NegamaxExtensionPlayer;
import checkers.ai.NegamaxOrderingPlayer;
import checkers.ai.NegamaxPlayer;
import checkers.ai.NegascoutOrderingPlayer;
import checkers.ai.RandomPlayer;
import checkers.ascii.AsciiPlayer;
import checkers.model.GameState;
import checkers.model.Move;

/**
 * AIStats
 *
 * @author Andrew Duffey
 * @author Scott Bressler
 */
public class AIStats {

	/**
	 * This class is essentially a script to benchmark the various AIPlayers.
	 * They are set up against a RandomPlayer, and the number of searches and
	 * node evaluations they do is printed.
	 *
	 * @param args Command line args. Currently none supported.
	 */
	public static void main(String[] args) {
		aiOpeningMoves();
//		aiVsAI();
	}

	private static void aiOpeningMoves() {
		List<AIPlayer> aiPlayers = new ArrayList<AIPlayer>();
		for (int i = 9; i <= 10; i++) {
//			aiPlayers.add(new NegamaxPlayer(i));
//			aiPlayers.add(new NegamaxExtensionPlayer(i));
//			aiPlayers.add(new NegamaxOrderingPlayer(i, i));
//			aiPlayers.add(new NegamaxOrderingPlayer(i, 4));
			aiPlayers.add(new NegascoutOrderingPlayer(i, i));
			aiPlayers.add(new NegascoutOrderingPlayer(i, 4));
		}
		for (AIPlayer aip : aiPlayers) {
			System.out.println(aip);
			Game game = new Game(new AsciiPlayer(), aip, new GameState());
			ArrayList<? extends Move> possibleMoves = game.getState().possibleMoves();
			Collections.reverse(possibleMoves);
			for(Move move : possibleMoves) {
				game = new Game(new AsciiPlayer(), aip, new GameState());
//				System.out.println("Move " + move);
				game.makeMove(move);
				game.makeMove(game.getPlayerToMove().chooseMove(game.getState()));
				System.out.println(aip.getSearches() + "\t" + aip.getEvals() + "\t" + (aip.getSearches()-aip.getEvals()));
			}
		}
	}

	private static void aiVsAI() {
		List<AIPlayer> aiPlayers = new ArrayList<AIPlayer>();
//		aiPlayers.add(new NegamaxOrderingPlayer(9, 5));
//		aiPlayers.add(new NegamaxOrderingPlayer(9, 7));
//		aiPlayers.add(new NegamaxOrderingPlayer(9, 9));
		aiPlayers.add(new NegascoutOrderingPlayer(9, 5));
//		aiPlayers.add(new NegascoutOrderingPlayer(9, 7));
//		aiPlayers.add(new NegascoutOrderingPlayer(9, 9));

		int steps = 0;
		int evals = 0;
		int evalsMax = 0;
		int searches = 0;
		int searchesMax = 0;

		int trials = 25;

		for (AIPlayer aip : aiPlayers) {
			System.out.println("Running statistics for player: " + aip);
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
