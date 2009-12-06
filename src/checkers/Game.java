package checkers;

import java.util.ArrayList;
import java.util.EnumMap;

import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.PlayerId;

public class Game {
	private GameState state;
	private EnumMap<PlayerId, Player> players;
	private ArrayList<Display> displays;

	public Game(Player playerForBlack, Player playerForWhite) {
		state = new GameState();

		players = new EnumMap<PlayerId, Player>(PlayerId.class);
		players.put(PlayerId.BLACK, playerForBlack);
		players.put(PlayerId.WHITE, playerForWhite);

		displays = new ArrayList<Display>();
	}

	public void registerDisplay(Display display) {
		displays.add(display);
		display.init(state);
	}

	public boolean isOver() {
		return state.gameIsOver();
	}

	public void makeMove(Move move) {
		state.makeMove(move);

		// update the displays with the move and new state
		for (Display display : displays)
			display.update(move, state);
	}

	public GameState getState() {
		return state;
	}

	public Player getPlayerToMove() {
		return players.get(state.playerToMove());
	}
}
