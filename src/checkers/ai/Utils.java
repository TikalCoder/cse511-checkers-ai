package checkers.ai;

import checkers.model.Board;
import checkers.model.GameState;
import checkers.model.PlayerId;
import checkers.model.Board.PositionState;

public class Utils {
	
	public static double utilityOf(GameState state) {
		if (state.gameIsOver()) {
			if (state.playerToMove() == PlayerId.WHITE) { // white lost!
				return -10000;
			} else {
				return 10000;
			}
		}
		
		Board gameBoard = state.getBoard();
		
		Integer white = 0;
		Integer black = 0;
		for (int pos = 1; pos <= 32; pos++) {
			PositionState ps = gameBoard.stateAt(pos);
			switch (ps) {
				case EMPTY: break;
				case WHITE_MAN: white += 100; break;
				case WHITE_KING: white += 130; break;
				case BLACK_MAN: black += 100; break;
				case BLACK_KING: black += 130; break;
			}
		}
		
		int util = white - black;
		util += (250 * (white - black)) / (white + black);
		
		return util;
	}
	
	
}
