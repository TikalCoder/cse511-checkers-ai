package checkers;

import java.io.FileNotFoundException;

import checkers.ai.NegamaxPlayer;
import checkers.ai.RandomPlayer;
import checkers.ascii.AsciiDisplay;
import checkers.ascii.AsciiPlayer;
import checkers.gui.Frame;
import checkers.gui.GUIDisplay;
import checkers.gui.GUIPlayer;
import checkers.io.FenDisplay;
import checkers.io.FenIO;
import checkers.model.GameState;
import checkers.print.PrettyBoardPrinter;

/**
 * Meant to provide a unified main program, to execute a checkers game for any
 * kind of AI or human player.
 *
 * @author Kurt Glastetter
 */
class NewMain {
	public static void main(String args[]) throws FileNotFoundException {
		GameState startingState = new GameState();

		// If filename is provided via command-line argument, parse the file as
		// FEN input to set up the initial game state.
		if (args.length > 0)
			startingState = FenIO.parseFen(args[0]);
		
		Frame gui = new Frame();
		
//		Player playerForWhite = new RandomPlayer();
		Player playerForWhite = new NegamaxPlayer(5);
//		Player playerForWhite = new GUIPlayer(gui.getBoardUI());
//		Player playerForWhite = new AsciiPlayer();
		Player playerForBlack = new GUIPlayer(gui.getBoardUI());
//		Player playerForBlack = new RandomPlayer();
		
//		Display display = new AsciiDisplay();

		Game game = new Game(playerForBlack, playerForWhite, startingState);

		gui.setPlayerTypes();
		
//		game.registerDisplay(display);
		game.registerDisplay(new GUIDisplay(gui.getBoardUI()));
		game.registerDisplay(new FenDisplay());

		while (!game.isOver()) {
			game.makeMove(game.getPlayerToMove().chooseMove(game.getState()));
		}

		// Print end-game messages
		printEndGameMessages(game);
	}

	private static void printEndGameMessages(Game game) {
		System.out.println("\n");
		new PrettyBoardPrinter().print(game.getState().getBoard());
		System.out.println("\n" + game.getState().playerToMove().opponent() + " wins!!!\n");
		System.out.println(" Game Over.");
	}
}
