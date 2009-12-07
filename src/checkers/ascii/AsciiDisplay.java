package checkers.ascii;

import checkers.Display;
import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.PlayerId;
import checkers.print.BoardPrinter;
import checkers.print.PrettyBoardPrinter;

/**
 * A pretty-printed, ASCII-based Display class.
 *
 * @author Kurt Glastetter
 */
public class AsciiDisplay extends Display {
	private BoardPrinter printer = new PrettyBoardPrinter();

	public void init(GameState newState) {
		System.out.println();
		printer.print(newState.getBoard());
	}

	public void update(Move move, GameState newState) {
		PlayerId playerThatMoved = newState.playerToMove().opponent();

		printPlayerSymbol(playerThatMoved);
		System.out.println(
				"  has " + (move.isJump() ? "jumped" : "moved")
				+ " " + move + ".");

		System.out.println();
		printer.print(newState.getBoard());

		if (newState.gameIsOver()) {
			printPlayerSymbol(playerThatMoved);
			System.out.println("  wins!!!");
			System.out.println();
			System.out.println(" Game Over.");
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

}
