package checkers.ai;

import java.util.ArrayList;
import java.util.Random;

import checkers.Utils;
import checkers.model.GameState;
import checkers.model.Move;

/**
 * A simple AI player that makes completely random moves.
 *
 * @author Kurt Glastetter
 */
public class RandomPlayer extends AIPlayer {
	static Random r = new Random(Utils.SEED);

	public Move chooseMove(GameState state) {
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		ArrayList<? extends Move> possibleMoves = state.possibleMoves();
		Move moveToMake = possibleMoves.get((int) (r.nextDouble() * possibleMoves.size()));
		System.out.println("Random player making move: " + moveToMake);
		return moveToMake;
	}
}
