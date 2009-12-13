package checkers;

import java.io.FileNotFoundException;

import checkers.ai.RandomPlayer;
import checkers.ascii.AsciiDisplay;
import checkers.ascii.AsciiPlayer;
import checkers.gui.Frame;
import checkers.gui.GUIDisplay;
import checkers.gui.GUIPlayer;
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
		Player playerForWhite = new GUIPlayer(gui.getBoardUI());
//		Player playerForWhite = new AsciiPlayer();
		Player playerForBlack = new GUIPlayer(gui.getBoardUI());
//		Player playerForBlack = new RandomPlayer();
		
//		Display display = new AsciiDisplay();
		Display guiDisplay = new GUIDisplay(gui.getBoardUI());

		Game game = new Game(playerForBlack, playerForWhite, startingState);

//		game.registerDisplay(display);
		game.registerDisplay(guiDisplay);

		while (!game.isOver()) {
			game.makeMove(game.getPlayerToMove().chooseMove(game.getState()));
		}


		// Print end-game messages
		System.out.println("\n");
		new PrettyBoardPrinter().print(game.getState().getBoard());
		System.out.println("\n" + game.getState().playerToMove().opponent() + " wins!!!\n");
		System.out.println(" Game Over.");
	}
}
