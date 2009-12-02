package checkers;

import java.util.ArrayList;
import java.util.Scanner;

import checkers.model.Board;
import checkers.model.Move;
import checkers.model.PlayerId;
import checkers.print.PrettyBoardPrinter;

/**
 * An ASCII-based 2-player checkers game interface, playable using stdin.
 */
public class AsciiMain {
	public static void main(String[] args) {
		Board board = new Board();
		PrettyBoardPrinter printer = new PrettyBoardPrinter();
		PlayerId playerToMove = PlayerId.BLACK; // Black moves first.
		Scanner input = new Scanner(System.in);

		// main loop
		for (;;) {
			ArrayList<Move> possibleMoves = board.possibleMoves(playerToMove);

			// game is over when the current player can't move
			if (possibleMoves.size() == 0)
				break;

			// make sequence of moves until the end of the player's turn
			TURN_LOOP: for (;;) {

				// print the board
				System.out.println("\n");
				printer.print(board);

				// prompt for the player's move
				printPlayerSymbol(playerToMove);
				System.out.print("  to move.  Possible moves are: ");
				for (int i = 0; i < possibleMoves.size(); ++i) {
					if (i != 0) System.out.print(", ");
					System.out.print(possibleMoves.get(i));
				}
				System.out.println(".");

				// get a (possibly partial) valid sequence of moves
				ArrayList<Integer> moveSequence = new ArrayList<Integer>();
				for (;;) {
					System.out.println();
					System.out.print(" Input move: ");

					try {
						// get user's input
						String moveString = input.nextLine();

						// extract the input into a sequence of integers
						for (String s : moveString.trim().split("\\s*[x-]\\s*")) {
							moveSequence.add(Integer.valueOf(s));
						}

						// make sure the sequence matches a possible move
						if (moveSequence.size() >= 2 &&
								isStartOfMoveInList(moveSequence, possibleMoves)) {
							break;
						}
					} catch (Exception e) { } // if exception, just repeat loop

					System.out.println(" Invalid move input.  Try again.");

					moveSequence.clear(); // need to reuse this
				}

				try {
					// make the (possibly partial) sequence of moves
					for (int i = 0; i < moveSequence.size() - 1; ++i) {
						if (board.makeSingleMove(
								moveSequence.get(i), moveSequence.get(i + 1))) {
							break TURN_LOOP;
						}
					}
				} catch (Exception e) {
					System.out.println(" Invalid move.  Try again.");
				}

				// at this point, we probably made part of a move

				// get the new set of possible moves from the new position
				possibleMoves = board.possibleMoves(playerToMove);

			} // end turn_loop

			// we actually made a complete move (yay!)
			playerToMove = playerToMove.opponent();

		} // end main loop

		// print the final results
		System.out.println("\n");
		printer.print(board);
		printPlayerSymbol(playerToMove.opponent());
		System.out.println("  wins!!!");
		System.out.println();
		System.out.println(" Game Over.");
	}

	/**
	 * Prints an ASCII checker symbol for the given player.
	 */
	private static void printPlayerSymbol(PlayerId player) {
		if (player == PlayerId.BLACK) {
			System.out.print(
				"  ___\n" +
				" (   )\n" +
				"  \"\"\"");
		} else {
			System.out.print(
				"  ___\n" +
				" (###)\n" +
				"  \"\"\"");
		}
	}

	/**
	 * Returns whether the given moveSequence constitutes at least part
	 * of one of the given completeMoves (could match more than one)
	 */
	private static boolean isStartOfMoveInList(
			ArrayList<Integer> moveSequence, ArrayList<Move> completeMoves) {
		for (Move m : completeMoves)
			if (isStartOfMove(moveSequence, m))
				return true;
		return false;
	}

	/**
	 * Returns whether the given moveSequence constitutes at least part
	 * of the given completeMove.
	 */
	private static boolean isStartOfMove(
			ArrayList<Integer> moveSequence, Move completeMove) {
		// moveSequence can't be longer than completeMove
		if (completeMove.getSequence().size() < moveSequence.size())
			return false;

		// moveSequence has to have at least one move (2 positions)
		if (moveSequence.size() < 2)
			return false;

		// moveSequence needs to match at least the first part of completeMove
		for (int i = 0; i < moveSequence.size(); ++i) {
			if (completeMove.getSequence().get(i) != moveSequence.get(i))
				return false;
		}

		return true;
	}
}
