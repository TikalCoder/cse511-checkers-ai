package checkers;

import java.lang.Math;
import java.util.ArrayList;

import checkers.Player;
import checkers.model.GameState;
import checkers.model.Move;

public class RandomPlayer extends Player {
	public Move chooseMove(GameState state) {
		ArrayList<Move> possibleMoves = state.possibleMoves();
		return possibleMoves.get((int) (Math.random() * possibleMoves.size()));
	}
}
