package checkers;

import static checkers.Constants.DEFAULT_NEGAMAX_SEARCH_DEPTH;
import static checkers.Constants.DEFAULT_NEGAMAX_EXTENSION_SEARCH_DEPTH;

import java.io.IOException;
import java.util.ArrayList;

import checkers.ai.NegamaxExtensionPlayer;
import checkers.ai.NegamaxOrderingPlayer;
import checkers.ai.NegamaxPlayer;
import checkers.ai.NegascoutOrderingPlayer;
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
			game.makeMove(game.getPlayerToMove().chooseMove(
						(GameState) game.getState().clone()));
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

	private static Player parsePlayerString(String playerString) {
		if (playerString.equalsIgnoreCase("GUI")) {
			ensureGuiDisplayExists();
			return new GUIPlayer(gui.getBoardUI());
		}
		if (playerString.equalsIgnoreCase("ASCII")) {
			ensureAsciiDisplayExists();
			return new AsciiPlayer();
		}
		if (playerString.equalsIgnoreCase("RANDOM")) {
			return new RandomPlayer();
		}
		if (playerString.toUpperCase().matches("^NEGAMAX(:\\d+)?")) {
			String[] parts = playerString.split(":");
			if (parts.length == 2)
				return new NegamaxPlayer(Integer.parseInt(parts[1]));
			else
				return new NegamaxPlayer(DEFAULT_NEGAMAX_SEARCH_DEPTH);
		}
		if (playerString.toUpperCase().matches("^NEGAMAXEXTENSION(:\\d+)?")) {
			String[] parts = playerString.split(":");
			if (parts.length == 2)
				return new NegamaxExtensionPlayer(Integer.parseInt(parts[1]));
			else
				return new NegamaxExtensionPlayer(DEFAULT_NEGAMAX_EXTENSION_SEARCH_DEPTH);
		}
		if (playerString.toUpperCase().matches("^NEGAMAXORDERING(:\\d+,\\d+)?")) {
			String[] parts = playerString.split(":");
			if (parts.length == 2) {
				String[] depths = parts[1].split(",");
				return new NegamaxOrderingPlayer(
						Integer.parseInt(depths[0]),
						Integer.parseInt(depths[1]));
			}
			else
				return new NegamaxOrderingPlayer(5, 4); // default
		}
		if (playerString.toUpperCase().matches("^NEGASCOUT(:\\d+,\\d+)?")) {
			String[] parts = playerString.split(":");
			if (parts.length == 2) {
				String[] depths = parts[1].split(",");
				return new NegascoutOrderingPlayer(
						Integer.parseInt(depths[0]),
						Integer.parseInt(depths[1]));
			}
			else
				return new NegascoutOrderingPlayer(5, 4); // default
		}

		throw new IllegalArgumentException(
				"could not parse player string `" + playerString + "'");
	}

	private static void parseDisplayStrings(ArrayList<String> displayStrings) {
		for (String displayString : displayStrings) {
			if (displayString.equalsIgnoreCase("GUI"))
				ensureGuiDisplayExists();
			else if (displayString.equalsIgnoreCase("ASCII"))
				ensureAsciiDisplayExists();
			else if (displayString.equalsIgnoreCase("FEN"))
				ensureFenDisplayExists();
			else if (displayString.equalsIgnoreCase("ALL")) {
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
		System.out.println(
			"CSE 511A Checkers: an American Checkers (English Draughts) game with AI\n"
			// ---------1---------2---------3---------4---------5---------6---------7---------
			+ "\n"
			+ "Usage: java -cp src checkers/NewMain [OPTIONS]...\n"
			+ "\n"
			+ "Options:\n"
			+ "  -b PLAYER   Specifies the player type for black (default: gui)\n"
			+ "  -w PLAYER   Specifies the player type for white (default: negamax:" + DEFAULT_NEGAMAX_SEARCH_DEPTH + ")\n"
			+ "  -d DISPLAY  Specifies (additional) display type (repeat for more)\n"
			+ "  -f FEN      Specifies initial game state in a FEN notation string\n"
			+ "  -F FILE     Same as above, but reads FEN notation from FILE instead\n"
			+ "  -h, --help  Output this incredibly helpful message\n"
			+ "\n"
			+ "PLAYER may be one of:\n"
			+ "  gui     human player, using a graphical user interface\n"
			+ "  ascii   human player, using a text-based user interface\n"
			+ "  random  AI player that makes random moves\n"
			+ "  negamax[:N]\n"
			+ "          AI player, similar to Minimax with alpha-beta pruning, with search\n"
			+ "          depth specified by integer N\n"
			+ "  negamaxextension[:N]\n"
			+ "          AI player, similar to Negamax above, but with extended searching\n"
			+ "          through jump sequences\n"
			+ "  negamaxordering[:N,M]\n"
			+ "          AI player, similar to Negamax above, but with a move-ordering stage\n"
			+ "          that can make alpha-beta pruning more efficient; has search depth N,\n"
			+ "          and move-ordering search depth M (which should be less than N)\n"
			+ "  negascout[:N,M]\n"
			+ "          TODO: fill in description of NegaScout...\n"
			+ "\n"
			+ "DISPLAY(s) may be any of:  gui  ascii  fen  all\n"
			+ "\n"
			+ "Examples:\n"
			+ "  java -cp src checkers/NewMain -b random -w negamax:7 -d gui -d fen\n"
			+ "    will launch a checkers match with a random player for black and a negamax\n"
			+ "    AI with search depth 7 for white, with a GUI, and with FEN notation output\n"
			+ "  java -cp src checkers/NewMain -f B:WK5:BK21,K18,K31,K32.\n"
			+ "    will start toward the end of a game, with black in a material advantage\n"
		);
		System.exit(0);
	}
}
