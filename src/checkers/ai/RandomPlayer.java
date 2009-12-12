package checkers.ai;

import java.util.ArrayList;
import java.util.Random;

import checkers.Player;
import checkers.Utils;
import checkers.model.GameState;
import checkers.model.Move;

/**
 * A simple AI player that makes completely random moves.
 *
 * @author Kurt Glastetter
 */
public class RandomPlayer extends Player {
	static Random r = new Random(Utils.SEED);
	
	public Move chooseMove(GameState state) {
//		try {
//			Thread.sleep(3);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		ArrayList<Move> possibleMoves = state.possibleMoves();
		return possibleMoves.get((int) (r.nextDouble() * possibleMoves.size()));
	}
}
