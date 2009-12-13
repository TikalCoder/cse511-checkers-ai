package checkers.io;

import checkers.Display;
import checkers.io.FenIO;
import checkers.model.GameState;
import checkers.model.Move;

public class FenDisplay extends Display {
	public void init(GameState newState) {
		System.out.println(FenIO.outputFen(newState));
	}

	public void update(Move move, GameState newState) {
		init(newState);
	}
}
