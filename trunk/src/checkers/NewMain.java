package checkers;

import checkers.Display;
import checkers.Game;
import checkers.Player;
import checkers.ai.RandomPlayer;
import checkers.ascii.AsciiDisplay;
import checkers.ascii.AsciiPlayer;
import checkers.gui.Frame;
import checkers.gui.GUIDisplay;
import checkers.gui.GUIPlayer;

/**
 * Meant to provide a unified main program, to execute a checkers game for any
 * kind of AI or human player.
 *
 * @author Kurt Glastetter
 */
class NewMain {
	public static void main(String args[]) {
		Frame gui = new Frame();
		
		Player playerForBlack = new RandomPlayer();
//		Player playerForWhite = new AsciiPlayer();
		Player playerForWhite = new GUIPlayer(gui);
		
		Display display = new AsciiDisplay();
		Display guiDisplay = new GUIDisplay(gui);
		

		Game game = new Game(playerForBlack, playerForWhite);

		game.registerDisplay(display);
		game.registerDisplay(guiDisplay);

		while (!game.isOver()) {
			game.makeMove(game.getPlayerToMove().chooseMove(game.getState()));
		}
	}
}
