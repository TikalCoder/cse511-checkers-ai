package checkers.ai;

import java.lang.Math;
import java.util.ArrayList;

import checkers.Player;
import checkers.model.GameState;
import checkers.model.Move;

/**
 * A simple AI player that makes completely random moves.
 *
 * @author Kurt Glastetter
 */
public class RandomPlayer extends Player {
	public Move chooseMove(GameState state) {
		ArrayList<Move> possibleMoves = state.possibleMoves();
		return possibleMoves.get((int) (Math.random() * possibleMoves.size()));
	}
}
