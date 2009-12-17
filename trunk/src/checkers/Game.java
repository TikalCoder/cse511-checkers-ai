package checkers;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Stack;

import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.PlayerId;

/**
 * Represents a single checkers game, and allows it to be played by any kind of
 * Player (human or AI) and displayed by any kind of Display.
 *
 * @author Kurt Glastetter
 * @author Scott Bressler
 */
public class Game {
	/**
	 * Singleton maintaining a reference to the current game.
	 */
	private static Game CURRENT_GAME;

	/**
	 * Stores the current state of this Game.
	 */
	private GameState state;

	/**
	 * Maps each PlayerId onto an actual Player object, which may be any human
	 * or AI player
	 */
	private EnumMap<PlayerId, Player> players;

	/**
	 * List of all the Display objects that are registered to receive updates
	 * from this Game
	 */
	private ArrayList<Display> displays;

	/**
	 * Stack holding all the game states that the game has gone through, used
	 * for allowing undo operations.
	 */
	private Stack<GameState> stateHistory;

	/**
	 * Constructor initializes to a specified game state, and remembers who the
	 * players are.
	 */
	public Game(Player playerForBlack, Player playerForWhite, GameState state) {
		this.state = state;

		players = new EnumMap<PlayerId, Player>(PlayerId.class);
		players.put(PlayerId.BLACK, playerForBlack);
		players.put(PlayerId.WHITE, playerForWhite);

		displays = new ArrayList<Display>();

		stateHistory = new Stack<GameState>();

		// Add first state to state history for undo support.
		stateHistory.push((GameState) state.clone());

		if (CURRENT_GAME == null)
			CURRENT_GAME = this;
	}

	/**
	 * Constructor initializes to a specified game state, and remembers who the
	 * players are.
	 */
	public Game(Player playerForBlack, Player playerForWhite) {
		this(playerForBlack, playerForWhite, new GameState());
	}

	/**
	 * Constructor initializes game provided a list of players and displays.
	 * @throws CloneNotSupportedException
	 */
	public Game(EnumMap<PlayerId, Player> players, ArrayList<Display> displays) throws CloneNotSupportedException {
		this.state = new GameState();

		this.players = new EnumMap<PlayerId, Player>(PlayerId.class);
		this.players.put(PlayerId.WHITE, (Player) players.get(PlayerId.WHITE).clone());
		this.players.put(PlayerId.BLACK, (Player) players.get(PlayerId.BLACK).clone());
		this.displays = displays;

		stateHistory = new Stack<GameState>();

		// Add first state to state history for undo support.
		stateHistory.push((GameState) state.clone());

		updateDisplays();

		if (CURRENT_GAME == null)
			CURRENT_GAME = this;
	}

	/**
	 * Registers the given Display object to receive updates everytime a move is
	 * made, and also initializes it to the current state of the game.
	 */
	public void registerDisplay(Display display) {
		displays.add(display);
		display.init(state);
	}

	/**
	 * Updates all the registered displays with a new {@link GameState}.
	 */
	public void updateDisplays() {
		for (Display d : displays)
			d.init(stateHistory.peek());
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

		stateHistory.add((GameState) state.clone());

		// Update the displays with the move and new state
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

	/**
	 * Returns a reference to the singleton current game.
	 */
	public static Game currentGame() {
		return CURRENT_GAME;
	}

	/**
	 * Starts a new Game
	 * @throws CloneNotSupportedException
	 */
	public static void newGame() throws CloneNotSupportedException {
		Game oldGame = currentGame();
		Game newGame = new Game(oldGame.players, oldGame.displays);
		CURRENT_GAME = newGame;
	}

	public void setPlayer(PlayerId playerId, Player player) {
		players.put(playerId, player);
	}

	public void setBlackPlayer(Player blackPlayer) {
		setPlayer(PlayerId.BLACK, blackPlayer);
	}

	public void setWhitePlayer(Player whitePlayer) {
		setPlayer(PlayerId.WHITE, whitePlayer);
	}

	public Player getPlayer(PlayerId playerId) {
		return players.get(playerId);
	}

	public Player getBlackPlayer() {
		return getPlayer(PlayerId.BLACK);
	}

	public Player getWhitePlayer() {
		return getPlayer(PlayerId.WHITE);
	}

	/**
	 * Undo a single move in this Game.
	 */
	public void undo() {
		if (stateHistory.size() > 1) {
			stateHistory.pop();
			state.setState(stateHistory.peek());
			for (Display display : displays)
				display.init(state);
		}
	}

	public boolean stateHistoryEmpty() {
		return stateHistory.size() == 1;
	}

	public PlayerId getWinner() {
		if (!isOver())
			throw new IllegalStateException("Game is not over yet. There is no winner.");
		return state.playerToMove().opponent();
	}
}
