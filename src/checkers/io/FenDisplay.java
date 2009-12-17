package checkers.io;

import checkers.Display;
import checkers.io.FenIO;
import checkers.model.GameState;
import checkers.model.Move;

/**
 * Display class for printing the FEN (Forsyth Edwards Notation) string after
 * every move.
 *
 * @author Kurt Glastetter
 */
public class FenDisplay extends Display {
	public void init(GameState newState) {
		System.out.println(FenIO.outputFen(newState));
	}

	public void update(Move move, GameState newState) {
		init(newState);
	}
}
