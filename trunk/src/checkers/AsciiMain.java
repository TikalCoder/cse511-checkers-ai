package checkers;

import java.util.ArrayList;
import java.util.Scanner;

import checkers.model.Board;
import checkers.model.Move;
import checkers.model.PlayerId;
import checkers.print.PrettyBoardPrinter;

public class AsciiMain {
	public static void main(String[] args) {
		Board board = new Board();
		PrettyBoardPrinter printer = new PrettyBoardPrinter();
		PlayerId playerToMove = PlayerId.BLACK;
		Scanner input = new Scanner(System.in);

		// main loop
		for (;;) {
			ArrayList<Move> possibleMoves = board.possibleMoves(playerToMove);

			if (possibleMoves.size() == 0)
				break;

			// make sequence of moves until the end of player's turn
			TURN_LOOP: for (;;) {

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
						String moveString = input.nextLine();

						for (String s : moveString.trim().split("\\s*[x-]\\s*")) {
							moveSequence.add(Integer.valueOf(s));
						}

						if (moveSequence.size() >= 2 &&
								isStartOfMoveInList(moveSequence, possibleMoves)) {
							break;
						}
					} catch (Exception e) { }

					System.out.println(" Invalid move input.  Try again.");
					moveSequence.clear();
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

				possibleMoves = board.possibleMoves(playerToMove);
			}

			// we actually made a complete move (yay!)
			playerToMove = playerToMove.opponent();
		}

		System.out.println("\n");
		printer.print(board);
		printPlayerSymbol(playerToMove.opponent());
		System.out.println("  wins!!!");
		System.out.println();
		System.out.println(" Game Over.");
	}

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

	private static boolean isStartOfMoveInList(
			ArrayList<Integer> moveSequence, ArrayList<Move> completeMoves) {
		for (Move m : completeMoves)
			if (isStartOfMove(moveSequence, m))
				return true;
		return false;
	}

	private static boolean isStartOfMove(
			ArrayList<Integer> moveSequence, Move completeMove) {
		if (completeMove.getSequence().size() < moveSequence.size())
			return false;

		if (moveSequence.size() < 2)
			return false;

		for (int i = 0; i < moveSequence.size(); ++i) {
			if (completeMove.getSequence().get(i) != moveSequence.get(i))
				return false;
		}

		return true;
	}
}
