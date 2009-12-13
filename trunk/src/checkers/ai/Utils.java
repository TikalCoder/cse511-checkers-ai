package checkers.ai;

import java.util.ArrayList;
import java.util.List;

import checkers.model.Board;
import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.Board.PositionState;

public class Utils {
	
	public static Double utilityOf(GameState state) {
		Board gameBoard = state.getBoard();
		
		Double util = 0.0;
		for (int pos = 1; pos <= 32; pos++) {
			PositionState ps = gameBoard.stateAt(pos);
			switch (ps) {
				case EMPTY: break;
				case WHITE_MAN: util++; break;
				case WHITE_KING: util += 2; break;
				case BLACK_MAN: util--; break;
				case BLACK_KING: util -= 2; break;
			}
		}
		
		return util;
	}
	
	
}
