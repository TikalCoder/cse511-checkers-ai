package checkers;

import java.util.ArrayList;
import java.util.EnumMap;

import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.PlayerId;

/**
 * Represents a single checkers game, and allows it to be played by any kind of
 * Player (human or AI) and displayed by any kind of Display.
 *
 * @author Kurt Glastetter
 */
public class Game {
	// stores the current state of this Game
	private GameState state;

	// maps each PlayerId onto an actual Player object, which may be any human
	// or AI player
	private EnumMap<PlayerId, Player> players;

	// list of all the Display objects that are registered to receive updates
	// from this Game
	private ArrayList<Display> displays;

	/**
	 * Constructor initializes to the start of a new game, and remembers who
	 * the players are.
	 */
	public Game(Player playerForBlack, Player playerForWhite) {
		state = new GameState();

		players = new EnumMap<PlayerId, Player>(PlayerId.class);
		players.put(PlayerId.BLACK, playerForBlack);
		players.put(PlayerId.WHITE, playerForWhite);

		displays = new ArrayList<Display>();
	}

	/**
	 * Registers the given Display object to receive updates everytime a move
	 * is made, and also initializes it to the current state of the game.
	 */
	public void registerDisplay(Display display) {
		displays.add(display);
		display.init(state);
	}

	/**
	 * Returns true if and only if the game is over.
	 */
	public boolean isOver() {
		return state.gameIsOver();
	}

	/**
	 * Updates the state based on the provided Move, and informs all of the
	 * registered displays of the update.
	 */
	public void makeMove(Move move) {
		state.makeMove(move);

		// update the displays with the move and new state
		for (Display display : displays)
			display.update(move, state);
	}

	/**
	 * Returns this Game's GameState object.
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * Returns the Player object that is supposed to make the next move.
	 */
	public Player getPlayerToMove() {
		return players.get(state.playerToMove());
	}
}
