package checkers;

import java.util.ArrayList;

import checkers.model.Board;
import checkers.model.Move;
import checkers.model.PlayerId;
import checkers.print.PrettyBoardPrinter;

public class AsciiMain {
	public static void main(String[] args) {
		Board board = new Board();

		System.out.println("\n");
		new PrettyBoardPrinter().print(board);

		System.out.print(
			"  ___\n" +
			" (   )\n" +
			"  \"\"\"  to move.  Possible moves are: ");

		ArrayList<Move> possibleMoves = board.possibleMoves(PlayerId.BLACK);
		for (int i = 0; i < possibleMoves.size(); ++i) {
		    if (i != 0) System.out.print(", ");
		    System.out.print(possibleMoves.get(i));
		}
		System.out.println(".");

		board.makeSingleMove(10, 14); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(23, 18); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(14, 23); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(27, 18); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(12, 16); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(24, 19); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(16, 23); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(26, 19); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(11, 15); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(19, 10); new PrettyBoardPrinter().print(board);
		board.makeSingleMove( 6, 15); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(18, 11); new PrettyBoardPrinter().print(board);
		board.makeSingleMove( 7, 16); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(32, 27); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(16, 20); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(31, 26); new PrettyBoardPrinter().print(board);
		board.makeSingleMove( 9, 14); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(28, 24); new PrettyBoardPrinter().print(board);
		board.makeSingleMove( 3,  7); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(27, 23); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(20, 27); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(22, 18); new PrettyBoardPrinter().print(board);
		board.makeSingleMove(27, 32); new PrettyBoardPrinter().print(board);
	}
}
