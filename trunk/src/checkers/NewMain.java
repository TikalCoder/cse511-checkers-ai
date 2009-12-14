package checkers;

import java.io.IOException;

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
 * Usage:
 * 1) Without any arguments, a new checkers game will be created.
 * 1) Provide the filename of a file containing FEN.
 * 2) Provide FEN input directly.
 * 
 * Using either options 2 or 3, the initial game state will be set to match the provided FEN.
 *
 * @author Kurt Glastetter
 * @author Scott Bressler
 */
class NewMain {
	public static void main(String args[]) throws IOException {
		GameState startingState = new GameState();

		// If filename is provided via command-line argument, parse the file as
		// FEN input to set up the initial game state.
		if (args.length > 0) {
			String filename = args[0];
			String ext = (filename.lastIndexOf(".")==-1)?"":filename.substring(filename.lastIndexOf(".")+1,filename.length());
			if (ext.equals("fen") || ext.equals("txt"))
				startingState = FenIO.parseFenFile(filename);
			else
				startingState = FenIO.parseFen(filename);
			
		}
		
		Frame gui = new Frame();
		
//		Player playerForWhite = new RandomPlayer();
		Player playerForWhite = new NegamaxPlayer(6);
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
