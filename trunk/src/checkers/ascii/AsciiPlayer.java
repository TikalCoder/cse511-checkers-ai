package checkers.ascii;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.Scanner;

import checkers.Player;
import checkers.model.Board;
import checkers.model.GameState;
import checkers.model.Jump;
import checkers.model.Move;
import checkers.model.PlayerId;
import checkers.model.Walk;

public class AsciiPlayer extends Player {
	public Move chooseMove(GameState state) {
		ArrayList<Move> possibleMoves = state.possibleMoves();

		// prompt for the player's move
		printPlayerSymbol(state.playerToMove());
		System.out.print("  to move.  Possible moves are:\n\n   ");
		for (int i = 0; i < possibleMoves.size(); ++i) {
			if (i != 0) System.out.print(", ");
			System.out.print(possibleMoves.get(i));
		}
		System.out.println();

		// get a valid move sequence
		for (;;) {
			System.out.println();
			System.out.print(" Input move: ");
			System.out.flush();

			// get user's input
			Scanner input = new Scanner(System.in);
			String moveString = input.nextLine().trim();

			System.out.println();

			try {
				// extract the input into a sequence of position integers
				ArrayList<Integer> positionSequence = new ArrayList<Integer>();
				for (String s : moveString.split("[x-]")) {
					positionSequence.add(Integer.valueOf(s));
				}

				// turn the position sequence into a Move
				Move move = positionSequenceToMove(positionSequence);

				// make sure the move matches a possible move
				if (possibleMoves.contains(move))
					return move;

			} catch (Exception e) { } // if exception, just repeat loop

			System.out.println(
					" Invalid move input `" + moveString + "'.  Try again.");
		}
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

	private static Move positionSequenceToMove(ArrayList<Integer> sequence) {
		if (sequence.size() < 2)
			throw new IllegalArgumentException(
					"position sequence must have at least 2 positions to be"
					+ " considered a move");

		int pos0 = sequence.get(0);
		int pos1 = sequence.get(1);

		if (Board.areWalkable(pos0, pos1) && sequence.size() == 2) {
			// we have a walk move
			return new Walk(pos0, pos1);
		} else {
			// we have a jump move or an invalid move; here we just assume it
			// is a valid jump and return it
			Jump jump = new Jump(pos0, pos1);
			for (int i = 2; i < sequence.size(); ++i)
				jump.jumpAgain(sequence.get(i));
			return jump;
		}
	}
}
