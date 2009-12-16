package checkers;

import java.io.IOException;
import java.util.ArrayList;

import checkers.ai.NegamaxPlayer;
import checkers.ai.NegamaxOrderingPlayer;
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
	private static Frame gui = null;
	private static Display asciiDisplay = null;
	private static Display guiDisplay = null;
	private static Display fenDisplay = null;

	public static void main(String args[]) throws IOException {
		// default options:
		GameState startingState = new GameState(); // start of new game
		String playerForBlackString = "GUI";
		String playerForWhiteString = "NEGAMAX";

		ArrayList<String> displayStrings = new ArrayList<String>();

		for (int i = 0; i < args.length; ++i) {
			if (args[i].equals("-h") || args[i].equals("-help")
					|| args[i].equals("--help"))
				exitWithHelp();
			else if (args[i].equals("-f"))
				startingState = FenIO.parseFen(args[++i]);
			else if (args[i].equals("-F"))
				startingState = FenIO.parseFenFile(args[++i]);
			else if (args[i].equals("-b"))
				playerForBlackString = args[++i];
			else if (args[i].equals("-w"))
				playerForWhiteString = args[++i];
			else if (args[i].equals("-d"))
				displayStrings.add(args[++i]);
		}

		Player playerForBlack = parsePlayerString(playerForBlackString);
		Player playerForWhite = parsePlayerString(playerForWhiteString);
		parseDisplayStrings(displayStrings);

		Game game = new Game(playerForBlack, playerForWhite, startingState);

		// if the GUI was created by parsePlayerString or parseDisplayStrings,
		// then we need to initialize it.  (Note: must do this after creation
		// of game, but before registering the displays.)
		if (gui != null)
			gui.init();

		registerDisplays(game);

		// the game's main loop:
		while (!game.isOver()) {
			game.makeMove(game.getPlayerToMove().chooseMove(game.getState()));
		}

		// Print end-game messages
		printEndGameMessages(game);
	}

	private static String getFileExtension(String filename) {
		String ext = (filename.lastIndexOf(".")==-1)?"":filename.substring(filename.lastIndexOf(".")+1,filename.length()).toLowerCase();
		return ext;
	}

	private static void printEndGameMessages(Game game) {
		System.out.println("\n");
		new PrettyBoardPrinter().print(game.getState().getBoard());
		System.out.println("\n" + game.getState().playerToMove().opponent() + " wins!!!\n");
		System.out.println(" Game Over.");
	}

	private static Player parsePlayerString(String playerString) {
		if (playerString.toUpperCase().equals("GUI")) {
			ensureGuiDisplayExists();
			return new GUIPlayer(gui.getBoardUI());
		}
		if (playerString.toUpperCase().equals("ASCII")) {
			ensureAsciiDisplayExists();
			return new AsciiPlayer();
		}
		if (playerString.toUpperCase().equals("RANDOM")) {
			return new RandomPlayer();
		}
		if (playerString.toUpperCase().equals("NEGAMAX")) {
			return new NegamaxPlayer(5); // TODO: make the depth an option
		}
		if (playerString.toUpperCase().equals("NEGAMAXORDERING")) {
			return new NegamaxOrderingPlayer(5, 4); // TODO: make the depths an option
		}

		throw new IllegalArgumentException(
				"could not parse player string `" + playerString + "'");
	}

	private static void parseDisplayStrings(ArrayList<String> displayStrings) {
		for (String displayString : displayStrings) {
			if (displayString.toUpperCase().equals("GUI"))
				ensureGuiDisplayExists();
			else if (displayString.toUpperCase().equals("ASCII"))
				ensureAsciiDisplayExists();
			else if (displayString.toUpperCase().equals("FEN"))
				ensureFenDisplayExists();
			else if (displayString.toUpperCase().equals("ALL")) {
				ensureGuiDisplayExists();
				ensureAsciiDisplayExists();
				ensureFenDisplayExists();
			}
			else
				throw new IllegalArgumentException(
						"could not parse display string `" + displayString + "'");
		}
	}

	private static void ensureGuiDisplayExists() {
		if (gui == null)
			gui = new Frame();

		if (guiDisplay == null)
			guiDisplay = new GUIDisplay(gui.getBoardUI());
	}

	private static void ensureAsciiDisplayExists() {
		if (asciiDisplay == null)
			asciiDisplay = new AsciiDisplay();
	}

	private static void ensureFenDisplayExists() {
		if (fenDisplay == null)
			fenDisplay = new FenDisplay();
	}

	private static void registerDisplays(Game game) {
		if (guiDisplay   != null) game.registerDisplay(guiDisplay);
		if (asciiDisplay != null) game.registerDisplay(asciiDisplay);
		if (fenDisplay   != null) game.registerDisplay(fenDisplay);
	}

	private static void exitWithHelp() {
		System.out.println("TODO: Help message goes here.");
		System.exit(0);
	}
}
