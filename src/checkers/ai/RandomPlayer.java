package checkers.ai;

import java.util.ArrayList;
import java.util.Random;

import checkers.model.GameState;
import checkers.model.Move;

/**
 * A simple AI player that makes completely random moves.
 *
 * @author Kurt Glastetter
 */
public class RandomPlayer extends AIPlayer {
	Random r;
	int seed;
	
	public RandomPlayer() {
		r = new Random();
		seed = r.nextInt();
		r = new Random(seed);
	}
	
	public RandomPlayer(int seed) {
		this.seed = seed;
		r = new Random(seed);
	}
	
	public int getSeed() {
		return seed;
	}

	public Move chooseMove(GameState state) {
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		ArrayList<? extends Move> possibleMoves = state.possibleMoves();
		Move moveToMake = possibleMoves.get((int) (r.nextDouble() * possibleMoves.size()));
		return moveToMake;
	}
}
